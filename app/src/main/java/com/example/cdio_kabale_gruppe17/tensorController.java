package com.example.cdio_kabale_gruppe17;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class tensorController {
    Context con;
    public tensorController(Context con){
        this.con = con;
    }

    public String runmodel(Bitmap bitmap){
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
        ArrayList<Bitmap> test = new ArrayList<>();
        test.add( tensorImage.getBitmap());
        PictureHelperClass.getInstance().setPictureList(test);

        // Post-processor which dequantize the result
        TensorBuffer probabilityBuffer =
                TensorBuffer.createFixedSize(new int[]{1, 52}, DataType.FLOAT32);

        // Initialise the model
        try{
            MappedByteBuffer tfliteModel
                    = FileUtil.loadMappedFile(con,
                    "model.tflite");
            Interpreter tflite = new Interpreter(tfliteModel);
            // Running inference

            tflite.run(tensorImage.getBuffer(), probabilityBuffer.getBuffer());

        } catch (IOException e){
            Log.e("tfliteSupport", "Error reading model", e);
        }
        Log.d("TensorFlow", "runmodel: "+ Arrays.toString(probabilityBuffer.getFloatArray()));
        return getStringFromTensorOut(Arrays.asList(probabilityBuffer.getFloatArray()));

    }

    public String getStringFromTensorOut(List<float[]> list){
        float num = 0;
        int index = 0;
        int c = 0;
        for (float a : list.get(0)) {
            if (a > num) {
                num = a;
                index = c;
            }
            c++;
        }

        String[] cat = new String[]{ "H2" , "H3"  , "H4"  ,"H5"  ,"H6"  ,"H7"  ,"H8"  ,"H9"  ,"H10"  ,"HB"  ,"HD"  ,"HK"  ,"HA"  ,
                "K2"  , "K3"  , "K4"  ,"K5"  ,"K6"  ,"K7"  ,"K8"  ,"K9"  ,"K10"  ,"KB"  ,"KD"  ,"KK"  ,"KA"  ,
                "S2"  , "S3"  , "S4"  ,"S5"  ,"S6"  ,"S7"  ,"S8"  ,"S9"  ,"S10"  ,"SB"  ,"SD"  ,"SK"  ,"SA"  ,
                "R2"  , "R3"  , "R4"  ,"R5"  ,"R6"  ,"R7"  ,"R8"  ,"R9"  ,"R10"  ,"RB"  ,"RD"  ,"RK"  ,"RA" };
        return cat[index];

    }
}
