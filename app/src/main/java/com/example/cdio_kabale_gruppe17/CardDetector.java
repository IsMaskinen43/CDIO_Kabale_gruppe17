package com.example.cdio_kabale_gruppe17;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.osgi.OpenCVNativeLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDetector {
    public static List<Integer> height = new ArrayList<>();
    public static List<Integer> width = new ArrayList<>();
    public static List<int[]> pixels = new ArrayList<>();
    public static Bitmap lastUsedBitmap;
    public static Bitmap grayScale;

    public static void getCard(Bitmap bitmap, float resizeRatio){
        if (bitmap != null) lastUsedBitmap = bitmap;
        else bitmap = lastUsedBitmap;
        height.clear();
        width.clear();
        pixels.clear();
        // convert to mat
        Mat billedeMat = new Mat();
        Mat resizedImage = new Mat();
        Utils.bitmapToMat(bitmap, billedeMat);
        Utils.bitmapToMat(bitmap, resizedImage);

        Size downscale = new Size(bitmap.getWidth()/resizeRatio, bitmap.getHeight()/resizeRatio);
        Imgproc.resize(billedeMat, billedeMat, downscale);
        Imgproc.resize(resizedImage, resizedImage, downscale);

        // make grayscale
        Imgproc.cvtColor(billedeMat, billedeMat, Imgproc.COLOR_RGB2RGBA);
        Imgproc.cvtColor(billedeMat, billedeMat, Imgproc.COLOR_RGBA2GRAY);

        // blur image
        //Imgproc.medianBlur(billedeMat, billedeMat, 9);
        Imgproc.GaussianBlur(billedeMat, billedeMat, new Size(7,7),0);

        // make canny
        Imgproc.Canny(billedeMat, billedeMat, 10, 100);

        // dilate picture
        Imgproc.dilate(billedeMat, billedeMat, new Mat(), new Point(-1, -1), 1);

        // find the contours of the picture
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(billedeMat, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // make a blank mat
        Mat drawing = Mat.zeros(billedeMat.size(),billedeMat.type());

        MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        for (int i = 0; i < contours.size(); i++){
            MatOfPoint contour = contours.get(i);
            Rect rect = Imgproc.boundingRect(contour);
            matOfPoint2f.fromList(contour.toList());
            Imgproc.approxPolyDP(matOfPoint2f, approxCurve, Imgproc.arcLength(matOfPoint2f, true) * 0.02, true);
            long total = approxCurve.total();
            if (total == 4){
                // draw the contour onto the drawing
                Imgproc.drawContours(drawing, contours, i , new Scalar(255,255,255), -1);
                pixels.add(getBitmapPixels(matToBitmap(resizedImage), rect.x, rect.y, rect.width, rect.height));
                width.add(rect.width);
                height.add(rect.height);
            }
        }
        grayScale = matToBitmapGray(drawing);
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

    private static Bitmap matToBitmapGray(Mat image){
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

    private static double angle(Point pt1, Point pt2, Point pt0) {
        double dx1 = pt1.x - pt0.x;
        double dy1 = pt1.y - pt0.y;
        double dx2 = pt2.x - pt0.x;
        double dy2 = pt2.y - pt0.y;
        return (dx1*dx2 + dy1*dy2)/Math.sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10);
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
