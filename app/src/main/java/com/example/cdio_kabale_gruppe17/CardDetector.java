package com.example.cdio_kabale_gruppe17;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
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

public class CardDetector {
    public static int height = 0;
    public static int width = 0;

    public static int[] getCard(Bitmap bitmap){

        Mat billedeMat = new Mat();
        Utils.bitmapToMat(bitmap, billedeMat);
        Mat grayScale = new Mat();
        Imgproc.cvtColor(billedeMat,grayScale,Imgproc.COLOR_RGB2GRAY);
        Mat edges = new Mat();
        Imgproc.Canny(grayScale,edges ,100,300);
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges,contours,hierarchy,Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);


        MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
        MatOfPoint2f approxCurve = new MatOfPoint2f();

        int[] pixels = null;

        for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0]) {
            MatOfPoint contour = contours.get(idx);
            Rect rect = Imgproc.boundingRect(contour);
            double contourArea = Imgproc.contourArea(contour);
            System.out.println("CONTOUR AREA FOR " + idx + " IS " + contourArea);
            matOfPoint2f.fromList(contour.toList());
            Imgproc.approxPolyDP(matOfPoint2f, approxCurve, Imgproc.arcLength(matOfPoint2f, true) * 0.02, true);
            long total = approxCurve.total();
            System.out.println("TOTAL IS " + total);
            if (total == 4) {
                List<Double> cos = new ArrayList<>();
                Point[] points = approxCurve.toArray();
                for (int j = 2; j < total + 1; j++) {
                    cos.add(angle(points[(int) (j % total)], points[j - 2], points[j - 1]));
                }
                Collections.sort(cos);
                Double minCos = cos.get(0);
                Double maxCos = cos.get(cos.size() - 1);
                boolean isRect = minCos >= -0.1 && maxCos <= 0.3;
                if (isRect) {
                    double ratio = Math.abs(1 - (double) rect.width / rect.height);
                    //drawText(billedeMat,rect.tl(), ratio <= 0.02 ? "SQU" : "RECT");
                    pixels = getBitmapPixels(matToBitmap(billedeMat), rect.x, rect.y, rect.width, rect.height);
                    height = rect.height;
                    width = rect.width;
                }
            }
        }


        return pixels;
    }


    private static Bitmap matToBitmap(Mat image) {
        Bitmap bmp = null;
        Mat rgb = new Mat();
        Imgproc.cvtColor(image, rgb, Imgproc.COLOR_RGB2RGBA);
        try {
            bmp = Bitmap.createBitmap(rgb.cols(), rgb.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(rgb, bmp);
        } catch (CvException e) {
            Log.d("Exception", e.getMessage());
        }
        return bmp;
    }

    private static double angle(Point pt1, Point pt2, Point pt0) {
        double dx1 = pt1.x - pt0.x;
        double dy1 = pt1.y - pt0.y;
        double dx2 = pt2.x - pt0.x;
        double dy2 = pt2.y - pt0.y;
        return (dx1*dx2 + dy1*dy2)/Math.sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10);
    }

    private static void drawText(Mat colorImage, Point ofs, String text) {
        if (text.equals("Andet"))  Imgproc.putText(colorImage, text, ofs, Core.FONT_HERSHEY_SIMPLEX, 4, new Scalar(255,255,0));
        else Imgproc.putText(colorImage, text, ofs, Core.FONT_HERSHEY_SIMPLEX, 5, new Scalar(255,0,0));
    }

    private static int[] getBitmapPixels(Bitmap bitmap, int x, int y, int width, int height) {
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), x, y,
                width, height);
        final int[] subsetPixels = new int[width * height];
        for (int row = 0; row < height; row++) {
            System.arraycopy(pixels, (row * bitmap.getWidth()),
                    subsetPixels, row * width, width);
        }
        return subsetPixels;
    }


}
