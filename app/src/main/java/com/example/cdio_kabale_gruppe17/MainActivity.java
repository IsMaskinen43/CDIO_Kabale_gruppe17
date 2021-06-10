package com.example.cdio_kabale_gruppe17;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cdio_kabale_gruppe17.ml.Model;

import org.jetbrains.annotations.NotNull;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.opencv.android.OpenCVLoader;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "opencv";
    private ImageView kortBillede;
    Interpreter tflite;
    Button btn_press;
    static {
        if (!OpenCVLoader.initDebug())
            Log.d(TAG, "Unable to load OpenCV");
        else {
            Log.d(TAG, "OpenCV loaded");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_press = findViewById(R.id.button_press);
        kortBillede = findViewById(R.id.kortBillede);
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.kort);
        kortBillede.setImageBitmap(bitmap);

        tensorController tc = new tensorController(this);

        CardDetector.getCard(bitmap, 4);
        Bitmap bitmap2 = BitmapFactory.decodeResource(this.getResources(),R.drawable.h3);
        tc.runmodel(bitmap2);
        //Intent i = new Intent(this, BilledeActivity.class);
       // startActivity(i);
        btn_press.setOnClickListener(this);


    }


    public void onClick(View v){
        if(v == btn_press){

        }
    }



}