import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testtensorflow.R;

//"<内部共有ストレージ>/deeplab/"に以下の2ファイルを置いて実行する
//  ・"frozen_inference_graph.pb" :モデルファイル
//  ・"image.jpg" :車が写った画像
public class MainActivity extends Activity {
    private static final String IMAGEFILE = "/sdcard/deeplab/image.jpg";

    //TensorFlowの実行
    //  ここだけがキモ。これ以外は表示とかの雑務
    //=========================================================================
    private TFModel mModel=new TFModel();
    private Bitmap mSource; //元画像
    private Bitmap mResult; //得られたマスク画像

    private void runTensorFlow() {
        if(!mModel.isInitialized()) return;

        //元画像を渡してマスクを得る
        //  ここが核心
        mResult=mModel.run(mSource);
    }

    //以下雑務
    //=========================================================================
    private static final int MAX_SRCWIDTH = 1920;
    private static final int MAX_SRCHEIGHT = 1920;
    private ImageView mIv;
    private int mImageIndex=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //画面の生成
        //  画面いっぱいにImageViewを配置
        mIv=new ImageView(this);
        mIv.setImageResource(R.drawable.ic_launcher_foreground);
        setContentView(mIv);

        //ImageViewをタップしたら処理を行う
        mIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mImageIndex==-1) runTensorFlow();
                switchImage();
            }
        });

        //TensorFlowの初期化
        //  モデルを読み込む
        if(!mModel.initialize()) {
            Toast.makeText(this,"TensorFlow initialization failed.",Toast.LENGTH_SHORT).show();
            return;
        }

        //元画像を開く
        loadImage(IMAGEFILE);
    }

    //画像をタップしたときに表示を切り替える
    private void switchImage() {
        mImageIndex=(mImageIndex+1)%2;  //0～1をループ
        mIv.setImageBitmap((mImageIndex==0)?mResult:mSource);
    }

    //Uriで指定された画像を読み込む
    //  大きすぎる場合は端末によっては表示できないので縮小する
    private void loadImage(String path) {
        //画像取得
        mSource=BitmapFactory.decodeFile(path);

        //縮小
        Bitmap shrinked=createShrinedkBitmap(mSource);
        if(shrinked!=null) {mSource.recycle();mSource=shrinked;}

        //表示
        mIv.setImageBitmap(mSource);
    }

    //表示に適したサイズに縮小したBitmapを生成する
    private Bitmap createShrinedkBitmap(Bitmap src) {
        int wOrg=src.getWidth();
        int hOrg=src.getHeight();
        int w=wOrg,h=hOrg;
        if(w>MAX_SRCWIDTH) {h=(int) (((float)MAX_SRCWIDTH/w)*h);w=MAX_SRCWIDTH;}
        if(h>MAX_SRCHEIGHT) {w=(int) (((float)MAX_SRCHEIGHT/h)*w);h=MAX_SRCHEIGHT;}
        if(w==wOrg && h==hOrg) return null;

        return Bitmap.createScaledBitmap(src,w,h,true);
    }
}