package com.example.cdio_kabale_gruppe17;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class CardDetector {
    public static List<Integer> height = new ArrayList<>();
    public static List<Integer> width = new ArrayList<>();
    public static List<int[]> pixels = new ArrayList<>();
    public static List<Integer> xCoords = new ArrayList<>();
    public static List<Integer> yCoords = new ArrayList<>();
    public static Bitmap lastUsedBitmap;
    public static Bitmap grayScale;

    public static void getCard(Bitmap bitmap, float resizeRatio){
        if (bitmap != null) lastUsedBitmap = bitmap;
        else bitmap = lastUsedBitmap;
        height.clear();
        width.clear();
        pixels.clear();
        xCoords.clear();
        yCoords.clear();
        // convert to mat
        Mat billedeMat = new Mat();
        Mat resizedImage = new Mat();
        Utils.bitmapToMat(bitmap, billedeMat);
        Utils.bitmapToMat(bitmap, resizedImage);

        Size downscale = new Size(bitmap.getWidth()/resizeRatio, bitmap.getHeight()/resizeRatio);
        Imgproc.resize(billedeMat, billedeMat, downscale);
        Imgproc.resize(resizedImage, resizedImage, downscale);

        // make grayscale
        Imgproc.cvtColor(billedeMat, billedeMat, Imgproc.COLOR_RGB2GRAY);
        //Imgproc.cvtColor(billedeMat, billedeMat, Imgproc.COLOR_RGBA2GRAY);

        // blur image
        Imgproc.medianBlur(billedeMat, billedeMat, 9);
        //Imgproc.GaussianBlur(billedeMat, billedeMat, new Size(9,9),0);

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
        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint contour = contours.get(i);
            Rect rect = Imgproc.boundingRect(contour);
            matOfPoint2f.fromList(contour.toList());
            Imgproc.approxPolyDP(matOfPoint2f, approxCurve, Imgproc.arcLength(matOfPoint2f, true) * 0.02, true);
            long total = approxCurve.total();
            if (total == 4) {
                // draw the contour onto the drawing
                Imgproc.drawContours(drawing, contours, i, new Scalar(255, 255, 255), -1);
                pixels.add(getBitmapPixels(matToBitmap(resizedImage), rect.x, rect.y, rect.width, rect.height));
                width.add(rect.width);
                height.add(rect.height);
                xCoords.add(rect.x);
                yCoords.add(rect.y);
            }
        }
        /*for (int i = 0; i < xCoords.size(); i++) {
            drawLine(drawing, new Point(xCoords.get(i), yCoords.get(i)), new Point(xCoords.get(i)+300, yCoords.get(i)+300));
        }*/
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

    // TODO fix out of memory error ved resizeratio 1
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


    private static void drawLine(Mat img, Point pt1, Point pt2){
        Imgproc.line(img, pt1, pt2, new Scalar(0,255,0), 10);
    }
}
