package com.example.cdio_kabale_gruppe17;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "opencv";
    private ImageView kortBillede;

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

        kortBillede = findViewById(R.id.kortBillede);
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.kort);
        Mat billedeMat = new Mat();
        Utils.bitmapToMat(bitmap, billedeMat);
        Mat grayScale = new Mat();
        Imgproc.cvtColor(billedeMat,grayScale,Imgproc.COLOR_RGB2GRAY);
        Mat edges = new Mat();
        Imgproc.Canny(grayScale,edges ,100,300);
        /*
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges,contours,hierarchy,Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);


        MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0]) {
            MatOfPoint contour = contours.get(idx);
            Rect rect = Imgproc.boundingRect(contour);
            double contourArea = Imgproc.contourArea(contour);
            System.out.println("CONTOUR AREA FOR " + idx + " IS " + contourArea);
            matOfPoint2f.fromList(contour.toList());
            Imgproc.approxPolyDP(matOfPoint2f, approxCurve, Imgproc.arcLength(matOfPoint2f, true) * 0.02, true);
            long total = approxCurve.total();
            System.out.println("TOTAL IS " + total);
            if (total == 3) { // is triangle
                // do things for triangle
            }
            if (total >= 4 && total <= 6) {
                List<Double> cos = new ArrayList<>();
                Point[] points = approxCurve.toArray();
                for (int j = 2; j < total + 1; j++) {
                    cos.add(angle(points[(int) (j % total)], points[j - 2], points[j - 1]));
                }
                Collections.sort(cos);
                Double minCos = cos.get(0);
                Double maxCos = cos.get(cos.size() - 1);
                boolean isRect = total == 4 && minCos >= -0.1 && maxCos <= 0.3;
                boolean isPolygon = (total == 5 && minCos >= -0.34 && maxCos <= -0.27) || (total == 6 && minCos >= -0.55 && maxCos <= -0.45);
                if (isRect) {
                    double ratio = Math.abs(1 - (double) rect.width / rect.height);
                    drawText(billedeMat,rect.tl(), ratio <= 0.02 ? "SQU" : "RECT");
                }
                if (isPolygon) {
                    drawText(billedeMat, rect.tl(), "Polygon");
                }
            } else {
                drawText(billedeMat,rect.tl(),total+"");
            }
        }
        kortBillede.setImageBitmap(matToBitmap(billedeMat));
         */
        kortBillede.setImageBitmap(matToBitmap(edges));
    }


    private Bitmap matToBitmap(Mat image) {
        Bitmap bmp = null;
        Mat rgb = new Mat();
        Imgproc.cvtColor(image, rgb, Imgproc.COLOR_GRAY2RGBA);
        try {
            bmp = Bitmap.createBitmap(rgb.cols(), rgb.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(rgb, bmp);
        } catch (CvException e) {
            Log.d("Exception", e.getMessage());
        }
        return bmp;
    }

    private double angle(Point pt1, Point pt2, Point pt0) {
        double dx1 = pt1.x - pt0.x;
        double dy1 = pt1.y - pt0.y;
        double dx2 = pt2.x - pt0.x;
        double dy2 = pt2.y - pt0.y;
        return (dx1*dx2 + dy1*dy2)/Math.sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10);
    }

    private void drawText(Mat colorImage, Point ofs, String text) {
        if (text.equals("Andet"))  Imgproc.putText(colorImage, text, ofs, Core.FONT_HERSHEY_SIMPLEX, 4, new Scalar(255,255,0));
        else Imgproc.putText(colorImage, text, ofs, Core.FONT_HERSHEY_SIMPLEX, 5, new Scalar(255,0,0));
    }

}