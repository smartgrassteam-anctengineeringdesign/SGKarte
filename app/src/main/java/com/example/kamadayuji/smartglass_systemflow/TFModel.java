package com.example.kamadayuji.smartglass_systemflow;

import android.graphics.Bitmap;
import android.graphics.Color;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TFModel {
    private final static String MODELFILE = "/sdcard/deeplab/frozen_inference_graph.pb";
    private final static String INPUTNAME = "ImageTensor";
    private final static String OUTPUTNAME = "SemanticPredictions";
    private static final int MAXWIDTH = 513;
    private static final int MAXHEIGHT = 513;

    private TensorFlowInferenceInterface mTFInterface=null;

    public boolean isInitialized() {return mTFInterface!=null;}

    //初期化
    public boolean initialize() {
        //モデルファイルを開く
        File file=new File(MODELFILE);
        FileInputStream is=null;

        try {
            is=new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(is==null) return false;

        //TensorFlowにモデルファイルを読み込む
        mTFInterface=new TensorFlowInferenceInterface(is);

        //モデルファイルを閉じる
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    //実行
    public Bitmap run(Bitmap src) {
        if(mTFInterface==null || src==null) return null;

        int wOrg=src.getWidth();
        int hOrg=src.getHeight();
        int w=wOrg,h=hOrg;

        //処理可能なサイズに縮小する
        Bitmap shrinked=null;
        if(w>MAXWIDTH || h>MAXHEIGHT) {
            if(w>MAXWIDTH) {h= (int) (((float)MAXWIDTH/w)*h);w=MAXWIDTH;}
            if(h>MAXHEIGHT) {w= (int) (((float)MAXHEIGHT/h)*w);h=MAXHEIGHT;}
            shrinked=Bitmap.createScaledBitmap(src,w,h,true);
            if(shrinked==null) return null;
            src=shrinked;
        }

        //BitmapをTensorFlowに与えるデータ形式に変換する
        //  …今回のモデルではRGBのbyte配列
        int srcInt[]=new int[w*h];
        src.getPixels(srcInt,0,w,0,0,w,h);
        byte srcByte[]=new byte[w*h*3];
        for(int i=0,c=srcInt.length;i<c;i++) {
            int col=srcInt[i];
            srcByte[i*3+0]=(byte)((col&0x00FF0000)>>16);    //R
            srcByte[i*3+1]=(byte)((col&0x0000FF00)>>8);     //G
            srcByte[i*3+2]=(byte)((col&0x000000FF));        //B
        }

        //TensorFlow実行
        //  inputName,outputNameにはモデルで規定された名前を与える
        int resultInt[]=new int[w*h];   //戻り値:推定された物体のIDの配列。0なら背景
        mTFInterface.feed(INPUTNAME,srcByte,1,h,w,3);
        mTFInterface.run(new String[]{OUTPUTNAME});//,true);
        mTFInterface.fetch(OUTPUTNAME,resultInt);

        //結果をビットマップに変換
        Bitmap result=Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        /*for(int y=0;y<h;y++) {
            for(int x=0;x<w;x++) {
                int id=resultInt[y*w+x];
//              result.setPixel(x,y,id==0? Color.TRANSPARENT:0x80FF0000);   //0x80FF0000:=半透明の赤
                result.setPixel(x,y,id!=7? Color.TRANSPARENT:0x80FF0000);   //0x80FF0000:=半透明の赤,7=車のID
            }
        }*/

        if(w!=wOrg || h!=hOrg) result=Bitmap.createScaledBitmap(result,wOrg,hOrg,true);
        if(shrinked!=null) shrinked.recycle();

        return result;
    }
}