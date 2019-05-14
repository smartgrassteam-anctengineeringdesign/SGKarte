package com.example.kamadayuji.smartglass_systemflow;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


// Google Cloud Vison APIの呼び出しに必要なパッケージ
import com.example.kamadayuji.smartglass_systemflow.database.DBAdapterBodyTemp;
import com.example.kamadayuji.smartglass_systemflow.imageProcessing.PackageManagerUtils;
import com.example.kamadayuji.smartglass_systemflow.imageProcessing.PermissionUtils;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.ImageContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class A10 extends AppCompatActivity /*implements CameraBridgeViewBase.CvCameraViewListener*/ {

    // API呼び出し時のヘッダ指定
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";

    // GALLARYへのアクセスのための定数
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    private final static int RESULT_CAMERA = 1001;
    private final static int REQUEST_PERMISSION = 1002;

    private String filePath;
    private Uri cameraUri;

    private TextView ocrTextView;
    private ImageView ocrImageView;

    private Button mButton10MovePatientInspectionResult;

    private static String KEYWORD_PATIENT = "KEY_PATIENT";
    private List<Patient> patientItems;
    private Patient patient;
    private int patientId;


    //[体温を登録]ボタン押下でデータベースに登録
    View.OnClickListener button10BtRegOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // キーボードを非表示
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

            // DBに登録
            saveList();

            //体温及び血圧データ一覧へ遷移
            Intent intent = new Intent(getApplication(), A07PatientInspectionResultActivity.class);
            intent = intent.putExtra("KEY_PATIENT", (Serializable) patientItems);
            startActivity(intent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity10_read_and_check_medical_data);

        //ocrimageView画面のヒモ付
        ocrImageView= findViewById(R.id.ocrImageView);
        ocrTextView = (TextView) findViewById(R.id.ocrTextView);

        mButton10MovePatientInspectionResult = (Button) findViewById(R.id.button10_BodyTempReg);
        mButton10MovePatientInspectionResult.setOnClickListener(button10BtRegOnClickListener);

        //前Activityにて選択した患者のIDを取得
        getSelectedPatientId();

        //カメラを起動する
        startCamera();

    }


    private void startCamera() {

        Log.d("A10", "起動開始");



        if (PermissionUtils.requestPermission(
                this,
                REQUEST_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {

            // 保存先のフォルダーをカメラに指定した場合
            File cameraFolder = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DCIM), "Camera");


            // 保存ファイル名
            String fileName = new SimpleDateFormat(
                    "ddHHmmss", Locale.US).format(new Date());
            filePath = String.format("%s/%s.jpg", cameraFolder.getPath(), fileName);
            Log.d("debug", "filePath:" + filePath);

            // capture画像のファイルパス
            File cameraFile = new File(filePath);
            cameraUri = FileProvider.getUriForFile(
                    A10.this,
                    getApplicationContext().getPackageName() + ".fileprovider",
                    cameraFile);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            startActivityForResult(intent, RESULT_CAMERA);
            Log.d("debug", "startActivityForResult()");

        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_CAMERA) {

            if (cameraUri != null) {

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), cameraUri);

                    // 画像の横、縦サイズを取得
                    int imageWidth = bitmap.getWidth();
                    int imageHeight = bitmap.getHeight();

                    // Matrix インスタンス生成
                    Matrix matrix = new Matrix();

                    // 画像中心を基点に90度回転
                    matrix.setRotate(0, imageWidth/2, imageHeight/2);

                    // 90度回転したBitmap画像を生成
                    Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0,
                            imageWidth, imageHeight, matrix, true);

                    ocrImageView.setImageBitmap(bitmap2);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                registerDatabase(filePath);
                uploadImage(cameraUri);
            } else {
                Log.d("debug", "cameraUri == null");
            }
        }
    }

    // アンドロイドのデータベースへ登録する
    private void registerDatabase(String file) {
        ContentValues contentValues = new ContentValues();
        ContentResolver contentResolver = A10.this.getContentResolver();
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        contentValues.put("_data", file);
        contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    // 画像のOCR処理
    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap ocrBitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                1200);

                // Google Cloud Vision APIの呼び出し
                callCloudVision(ocrBitmap);

            } catch (IOException e) {
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Google Cloud Vision APIの呼び出し
     *
     * @param bitmap 送信する画像ファイル
     * @throws IOException
     *
     **/
    private void callCloudVision(final Bitmap bitmap) throws IOException {

        // 処理中メッセージの表示
        ocrTextView.setText("画像を解析中です");

        // API呼び出しを行うための非同期処理
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    HttpTransport http = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    // Vision API呼び出しのための初期処理
                    VisionRequestInitializer reqInitializer =
                            new VisionRequestInitializer(getString(R.string.vision_api_key)) {

                                @Override
                                protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                                        throws IOException {
                                    super.initializeVisionRequest(visionRequest);

                                    String packageName = getPackageName();
                                    visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                                    String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);
                                    visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                                }
                            };

                    // リクエストの作成
                    Vision.Builder builder = new Vision.Builder(http, jsonFactory, null);
                    builder.setVisionRequestInitializer(reqInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchImgReq =
                            new BatchAnnotateImagesRequest();

                    batchImgReq.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImgReq = new AnnotateImageRequest();

                        // 画像のJPEGへの変換
                        com.google.api.services.vision.v1.model.Image base64Image = new com.google.api.services.vision.v1.model.Image();
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        byte[] imageBytes = out.toByteArray();

                        base64Image.encodeContent(imageBytes);
                        annotateImgReq.setImage(base64Image);

                        // Vision APIのFeatures設定
                        annotateImgReq.setFeatures(new ArrayList<Feature>() {{
                            Feature textDetect = new Feature();

                            // OCR 文字認識’TEXT_DETECTION’を使う
                            textDetect.setType("TEXT_DETECTION");
                            textDetect.setMaxResults(10);
                            add(textDetect);

                        }});

                        // 言語のヒントを設定
                        //final Spinner selectLang = (Spinner) findViewById(R.id.lang);

                        // UIで選択された言語を取得する
                        List<String> langHint = new ArrayList<String>();
                        langHint.add("ja");

                        ImageContext ic= new ImageContext();
                        ic.setLanguageHints(langHint);

                        annotateImgReq.setImageContext(ic);


                        // リクエストにセット
                        add(annotateImgReq);
                    }});

                    // Vison APIの呼び出し
                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchImgReq);

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);

                } catch (IOException e) {
                    // エラー処理

                }
                return getString(R.string.call_api_error) ;
            }

            // 解析結果を表示
            protected void onPostExecute(String result) {
                ocrTextView.setText(result);
            }
        }.execute();
    }


    // 画像のサイズ変更処理
    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDim) {

        int orgWidth = bitmap.getWidth();
        int orgHeight = bitmap.getHeight();
        int resWidth = maxDim;
        int resHeight = maxDim;

        if (orgHeight > orgWidth) {
            resHeight = maxDim;
            resWidth = (int) (resHeight * (float) orgWidth / (float) orgHeight);
        } else if (orgWidth > orgHeight) {
            resWidth = maxDim;
            resHeight = (int) (resWidth * (float) orgHeight / (float) orgWidth);
        } else if (orgHeight == orgWidth) {
            resHeight = maxDim;
            resWidth = maxDim;
        }
        return Bitmap.createScaledBitmap(bitmap, resWidth, resHeight, false);
    }

    // レスポンスからの文字列検出
    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        String message="";

        List<EntityAnnotation> ocrData = response.getResponses().get(0).getTextAnnotations();
        if (ocrData != null) {
            message += ocrData.get(0).getDescription();
        } else {
            message += R.string.text_detection_error;
        }

        return message;
    }

    //前のアクティビティから取得した患者情報を展開
    private void getSelectedPatientId(){

        patientItems = (ArrayList<Patient>)getIntent().getSerializableExtra(KEYWORD_PATIENT);
        patient = patientItems.get(0);
        patientId = patient.getId();
    }

    private void saveList() {

        //読み取られた数値を登録する
        String strBodyTemp = ocrTextView.getText().toString();
        String strTime;
        String strDate;
        String strRemarks = null;

        // 入力された単価と個数は文字列からint型へ変換
        float iBodyTemp = Float.parseFloat(strBodyTemp);

        //現在時刻を取得
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        long intUnixTime = date.getTime()/1000;

        String strpatientId = String.valueOf(patientId);

        // DBへの登録処理
        DBAdapterBodyTemp dbAdapterBodyTemp = new DBAdapterBodyTemp(this,strpatientId);
        dbAdapterBodyTemp.openDB();                                         // DBの読み書き
        dbAdapterBodyTemp.saveDB((int)intUnixTime, iBodyTemp, strRemarks);      // DBに登録
        dbAdapterBodyTemp.closeDB();                                        // DBを閉じる

    }

}




