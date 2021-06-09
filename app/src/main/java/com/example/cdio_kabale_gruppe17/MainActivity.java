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
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.kort2);
        kortBillede.setImageBitmap(bitmap);



        CardDetector.getCard(bitmap, 4);
        Bitmap bitmap2 = BitmapFactory.decodeResource(this.getResources(),R.drawable.h2);
        runmodel2(bitmap2);
        //Intent i = new Intent(this, BilledeActivity.class);
       // startActivity(i);
        btn_press.setOnClickListener(this);


    }
    public float doInference(String inputString){
        float[] inputVal = new float[1];
        inputVal[0] = Float.parseFloat(inputString);

        float[][] outputVal = new float[1][1];

        tflite.run(inputVal,outputVal);

        float inferredValue = outputVal[0][0];

        return inferredValue;

    }
    private MappedByteBuffer loadModelFile() throws IOException{
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declaredLength);
    }

    public void onClick(View v){
        if(v == btn_press){

        }
    }


    public void runmodel2(Bitmap bitmap){
        // Initialization code
        // Create an ImageProcessor with all ops required. For more ops, please
        // refer to the ImageProcessor Architecture section in this README.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeOp(
                                180, 180, ResizeOp.ResizeMethod.BILINEAR))
                        .build();

        // Create a TensorImage object. This creates the tensor of the corresponding
        // tensor type (uint8 in this case) that the TensorFlow Lite interpreter needs.
        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);

        // Analysis code for every frame
        // Preprocess the image
        tensorImage.load(bitmap);
        tensorImage = imageProcessor.process(tensorImage);

        // Post-processor which dequantize the result
        TensorBuffer probabilityBuffer =
                TensorBuffer.createFixedSize(new int[]{1, 52}, DataType.FLOAT32);

        // Initialise the model
        try{
            MappedByteBuffer tfliteModel
                    = FileUtil.loadMappedFile(this,
                    "model.tflite");
            Interpreter tflite = new Interpreter(tfliteModel);
            // Running inference

            tflite.run(tensorImage.getBuffer(), probabilityBuffer.getBuffer());

        } catch (IOException e){
            Log.e("tfliteSupport", "Error reading model", e);
        }
        Log.d(TAG, "runmodel2: output: "+ Arrays.toString(probabilityBuffer.getFloatArray()));

    }

}