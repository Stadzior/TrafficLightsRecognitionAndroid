package project.advancedmobileapplications.trafficlightsrecognitionandroid.utils;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import project.advancedmobileapplications.trafficlightsrecognitionandroid.enums.LightColor;

/**
 * Created by Patryk Rutkowski on 14.10.2017.
 */

public class ImageUtils {

    private static Scalar lowerRedHueBottom = new Scalar(0, 64, 200);
    private static Scalar lowerRedHueTop = new Scalar(69, 255, 255);

    private static Scalar lowerLowHueBottom = new Scalar(85, 64, 200);
    private static Scalar lowerLowHueTop = new Scalar(170, 255, 255);

    public static LightColor checkPhoto(Bitmap bmp) {
        Mat originalImage = bitmapToMat(bmp);

        Mat imgWithoutNoise = new Mat();
        Imgproc.medianBlur(originalImage, imgWithoutNoise, 3);
        Imgproc.cvtColor(imgWithoutNoise, imgWithoutNoise, Imgproc.COLOR_BGR2HSV);

        Mat redOutput = createHueMask(imgWithoutNoise.clone(), lowerLowHueBottom, lowerLowHueTop);
        Mat greenOutput = createHueMask(imgWithoutNoise.clone(), lowerRedHueBottom, lowerRedHueTop);

        if (Core.countNonZero(matToGrey(redOutput)) > Core.countNonZero(matToGrey(greenOutput)))
            return LightColor.RED;
        else
            return LightColor.GREEN;

    }

    private static Mat matToGrey(Mat mat) {
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
        return mat;
    }

    private static Mat bitmapToMat(Bitmap bmp) {
        Mat mat = new Mat();
        Bitmap bmp32 = bmp.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, mat);
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

}
