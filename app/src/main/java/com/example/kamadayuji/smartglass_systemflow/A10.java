package com.example.kamadayuji.smartglass_systemflow;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


// Google Cloud Vison APIの呼び出しに必要なパッケージ
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;




import static org.opencv.core.CvType.CV_8U;


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

   /*
    //[登録]ボタン押下でデータベースに登録
    View.OnClickListener button08_1BtRegOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // キーボードを非表示
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

            // DBに登録
            saveList();
        }
    };
    */



/*
    //opencv関連の設定
    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean              mIsJavaCamera = true;
    private MenuItem             mItemSwitchCamera = null;



    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public A10(){
        Log.i(TAG, "Instantiated new " + this.getClass());
    }
*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity11_read_and_check_medical_data);

        //ocrimageView画面のヒモ付
        ocrImageView= findViewById(R.id.ocrImageView);
        ocrTextView = (TextView) findViewById(R.id.ocrTextView);



        //カメラを起動する
        startCamera();

        //opencvカメラ起動
        //opencvCameraStart();

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
                    matrix.setRotate(-90, imageWidth/2, imageHeight/2);

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


    private void saveList() {

        /*
            //日付に関しては後で変更が必要
            int iDate = Integer.parseInt(strDate);
            int iTime = Integer.parseInt(strTime);

            //患者Idを取得
            String patientId = String.valueOf(patient.getId());

            // DBへの登録処理
            DBAdapterBodyTemp dbAdapterBodyTemp = new DBAdapterBodyTemp(this,patientId);
            dbAdapterBodyTemp.openDB();                                         // DBの読み書き
            dbAdapterBodyTemp.saveDB(iDate, iTime, iBodyTemp, strRemarks);   // DBに登録
            dbAdapterBodyTemp.closeDB();                                        // DBを閉じる

*/
        //患者の体温データ一覧へ遷移

    }











    /*
    private void opencvCameraStart(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.activity_java_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(Mat inputFrame) {

        Mat src = inputFrame;//入力画像
        Mat dst = Mat.zeros(inputFrame.width(),inputFrame.height(),CV_8U);//初期化
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2HSV);//HSVに変換

        Mat src2 = dst;//HSV画像を代入
        Mat dst2 = Mat.zeros(inputFrame.width(),inputFrame.height(),CV_8U);//初期化
        Scalar low = new Scalar( 0,30,90);//下限(H,S,V)
        Scalar high = new Scalar(35,255,255);//上限(H,S,V)
        Core.inRange( src2,  low,  high , dst2);//肌色抽出 上限と下限を設定


        return inputFrame;
    }
    */
}




