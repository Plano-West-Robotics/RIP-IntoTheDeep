package org.firstinspires.ftc.teamcode.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;

public class SubPipeline extends OpenCvPipeline {
    public WhatToDisplay whatToDisplay = WhatToDisplay.INPUT;

    public static Scalar RED_LO = new Scalar(0.0, 77.9, 86.4);
    public static Scalar RED_HI = new Scalar(25.5, 255.0, 255.0);
    public static Scalar BLUE_LO = new Scalar(0.0, 0.0, 0.0);
    public static Scalar BLUE_HI = new Scalar(255.0, 255.0, 255.0);
    public static Scalar NEUTRAL_LO = new Scalar(0.0, 0.0, 0.0);
    public static Scalar NEUTRAL_HI = new Scalar(255.0, 255.0, 255.0);
    public static double CANNY_HYST_LO = 0.0;
    public static double CANNY_HYST_HI = 255.0;

    public enum WhatToDisplay {
        INPUT, MASKED_RED, EDGES_RED,// MASKED_BLUE, MASKED_NEUTRAL;
    }

    private final Mat hsv = new Mat();
    private final Mat redMask = new Mat();
    private final Mat blueMask = new Mat();
    private final Mat neutralMask = new Mat();
    private final Mat masked = new Mat();
    private final Mat edges = new Mat();

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

        Core.inRange(hsv, RED_LO, RED_HI, redMask);
        Core.inRange(hsv, BLUE_LO, BLUE_HI, blueMask);
        Core.inRange(hsv, NEUTRAL_LO, NEUTRAL_HI, neutralMask);

        Mat hierarchy = new Mat();
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(
                redMask,
                contours,
                hierarchy,
                Imgproc.RETR_LIST,
                Imgproc.CHAIN_APPROX_SIMPLE
        );
        hierarchy.release();

        switch (whatToDisplay) {
            case INPUT:
                return input;
            case MASKED_RED:
                Core.multiply(input, new Scalar(0.25, 0.25, 0.25), masked);
                Core.bitwise_and(input, input, masked, redMask);
                Imgproc.drawContours(masked, contours, -1, new Scalar(0, 255, 0));

                return masked;
            case EDGES_RED:
                masked.setTo(new Scalar(0.0, 0.0, 0.0));
                Core.bitwise_and(input, input, masked, redMask);
                Imgproc.Canny(redMask, edges, CANNY_HYST_LO, CANNY_HYST_HI);

                return edges;
            default:
                return input;
        }
    }
}
