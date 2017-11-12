package project.advancedmobileapplications.trafficlightsrecognitionandroid.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.HOGDescriptor;

import project.advancedmobileapplications.trafficlightsrecognitionandroid.enums.LightColor;

/**
 * Created by Patryk Rutkowski on 14.10.2017.
 */

public class ImageUtils {

    private static Scalar lowerGreenHueBottom = new Scalar(40, 100, 50);
    private static Scalar lowerGreenHueTop = new Scalar(80, 255, 255);

    private static Scalar lowerRedHueBottom = new Scalar(85, 64, 200);
    private static Scalar lowerRedHueTop = new Scalar(170, 255, 255);

    public static LightColor checkPhoto(Bitmap bmp) {
        Mat originalImage = bitmapToMat(bmp);

        Mat imgWithoutNoise = new Mat();
        Imgproc.medianBlur(originalImage, imgWithoutNoise, 3);
        Imgproc.cvtColor(imgWithoutNoise, imgWithoutNoise, Imgproc.COLOR_BGR2HSV);
        Mat redOutput = createHueMask(imgWithoutNoise.clone(), lowerRedHueBottom, lowerRedHueTop);
        Mat greenOutput = createHueMask(imgWithoutNoise.clone(), lowerGreenHueBottom, lowerGreenHueTop);

        if (Core.countNonZero(matToGrey(redOutput)) > Core.countNonZero(matToGrey(greenOutput)))
            return LightColor.RED;
        else
            return LightColor.GREEN;
    }

    public static Bitmap addCirclesToBitmap(Bitmap bmp){
        Mat img = bitmapToMat(bmp);
        Mat imgWithoutNoise = new Mat();
        Imgproc.medianBlur(img, imgWithoutNoise, 3);
        Imgproc.cvtColor(imgWithoutNoise, imgWithoutNoise, Imgproc.COLOR_BGR2HSV);
        Mat circles = new Mat();
        Imgproc.HoughCircles(img, circles, Imgproc.CV_HOUGH_GRADIENT, 1d, 0);
        return matToBitmap(circles);
    }

    public static Bitmap getColorBitmap(Bitmap bmp, LightColor desiredColor){
        Mat img = bitmapToMat(bmp);
        Mat imgWithoutNoise = new Mat();
        Imgproc.medianBlur(img, imgWithoutNoise, 3);
        Imgproc.cvtColor(imgWithoutNoise, imgWithoutNoise, Imgproc.COLOR_BGR2HSV);
        switch (desiredColor)
        {
            case RED:
            {
                Mat redOutput = createHueMask(imgWithoutNoise.clone(), lowerRedHueBottom, lowerRedHueTop);
                int count = detectHumans(redOutput);
                Bitmap redBmp = Bitmap.createBitmap(bmp);
                Utils.matToBitmap(redOutput, redBmp);
                return redBmp;
            }
            case GREEN:
            {
                Mat greenOutput = createHueMask(imgWithoutNoise.clone(), lowerGreenHueBottom, lowerGreenHueTop);
                Bitmap greenBmp = Bitmap.createBitmap(bmp);
                Utils.matToBitmap(greenOutput, greenBmp);
                return greenBmp;
            }
            default:
                return null;
        }
    }

    public static int detectHumans(Mat source){
        HOGDescriptor hog = new HOGDescriptor();
        hog.setSVMDetector(HOGDescriptor.getDefaultPeopleDetector());
        MatOfRect found = new MatOfRect();
        hog.detectMultiScale(source, found, new MatOfDouble(0));
        return found.toArray().length;
    }

    public static Bitmap rotateImage(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap ChangeImageColors(Bitmap source,int brightness , float contrastLevel){
        if (contrastLevel > -1 && contrastLevel < 5 && brightness > -256 && brightness < 256) {
            int[] pixels = new int[source.getHeight() * source.getWidth()];
            source.getPixels(pixels, 0, source.getWidth(), 0, 0, source.getWidth(), source.getHeight());
            for (int i = 0; i < pixels.length; i++)
            {
                int[] rgb = IntToRgb(pixels[i]);
                for (int c = 0; c < rgb.length; c++){
                    rgb[c] = (int)(contrastLevel*(float)rgb[c]+brightness);
                    rgb[c] = rgb[c] > 255 ? 255 : rgb[c];
                    rgb[c] = rgb[c] < 0 ? 0 : rgb[c];
                }
                pixels[i] = RgbToInt(rgb);
            }
            source.setPixels(pixels, 0, source.getWidth(), 0, 0, source.getWidth(), source.getHeight());
            return Bitmap.createBitmap(source);
        }
        return null;
    }

    private static int[] IntToRgb(int source){
        //String hexColor = String.format("%06X", (0xFFFFFF & source));
        int[] rgb = new int[3];
        rgb[0] = (source >> 16) & 0xFF;
        rgb[1] = (source >> 8) & 0xFF;
        rgb[2] = source & 0xFF;
        return rgb;
    }

    private static int RgbToInt(int[] rgb){
        int value = rgb[0];
        value = (value << 8) + rgb[1];
        value = (value << 8) + rgb[2];
        return value;
    }

    private static Mat matToGrey(Mat mat) {
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
        return mat;
    }

    private static Mat bitmapToMat(Bitmap bmp) {
        Mat mat = new Mat();
        //Bitmap bmp32 = bmp.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp, mat);
        return mat;
    }

    private static Bitmap matToBitmap(Mat mat) {
        Bitmap bmp;
        bmp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bmp);
        return bmp;
    }

    private static Mat createHueMask(Mat src, Scalar low, Scalar upper) {
        Mat outputImage = new Mat();
        Mat mask = new Mat();
        Core.inRange(src, low, upper, mask);
        Core.bitwise_and(src, src, outputImage, mask);
        return outputImage;
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static byte[] decodeSampledBitmapFromResource(byte[] data, int   reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //options.inPurgeable = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        //return BitmapFactory.decodeByteArray(data, 0, data.length, options);
        return data;
    }

    public static Bitmap byteArrayToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

}
