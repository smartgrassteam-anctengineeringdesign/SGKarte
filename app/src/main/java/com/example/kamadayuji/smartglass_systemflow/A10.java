package com.example.kamadayuji.smartglass_systemflow;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.opencv.core.CvType.CV_8U;


public class A10 extends AppCompatActivity /*implements CameraBridgeViewBase.CvCameraViewListener*/ {

    // GALLARYへのアクセスのための定数
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    private final static int RESULT_CAMERA = 1001;
    private final static int REQUEST_PERMISSION = 1002;

    private ImageView imageView;
    private String filePath;
    private Uri cameraUri;

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
        setContentView(R.layout.activity10);

        //カメラを起動する
        startCamera();

        //opencvカメラ起動
        //opencvCameraStart();

    }


    private void startCamera() {

        Log.d("A10", "起動開始");

        //imageView画面のヒモ付
        imageView= findViewById(R.id.imageView);

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
                                    int resultCode, Intent intent) {
        if (requestCode == RESULT_CAMERA) {

            if (cameraUri != null) {
                imageView.setImageURI(cameraUri);

                registerDatabase(filePath);
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




