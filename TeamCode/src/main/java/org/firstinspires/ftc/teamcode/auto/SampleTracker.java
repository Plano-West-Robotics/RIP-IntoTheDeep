package org.firstinspires.ftc.teamcode.auto;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.teleop.Gamepads;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;
import org.firstinspires.ftc.teamcode.units.Distance2;
import org.firstinspires.ftc.teamcode.units.Pose;

import java.util.ArrayList;

class SampleTracker {
        int xTeeth;
        int yTeeth;
        int yawQuarterturns;

        public SampleTracker() {
            xTeeth = 0;
            yTeeth = 0;
            yawQuarterturns = 0;
        }

        public void update(Gamepads gamepads) {
            if (gamepads.justPressed(Gamepads.Button.GP1_DPAD_LEFT)) xTeeth--;
            if (gamepads.justPressed(Gamepads.Button.GP1_DPAD_RIGHT)) xTeeth++;
            if (gamepads.justPressed(Gamepads.Button.GP1_DPAD_DOWN)) yTeeth--;
            if (gamepads.justPressed(Gamepads.Button.GP1_DPAD_UP)) yTeeth++;
            if (gamepads.justPressed(Gamepads.Button.GP1_LEFT_BUMPER)) yawQuarterturns--;
            if (gamepads.justPressed(Gamepads.Button.GP1_RIGHT_BUMPER)) yawQuarterturns++;
            xTeeth = Math.min(xTeeth, 0);
            yTeeth = Math.max(0, yTeeth);
            yawQuarterturns %= 4; if (yawQuarterturns < 0) yawQuarterturns += 4;
        }

        @SuppressLint("DefaultLocale")
        public void writeToTelemetry(Telemetry telemetry) {
            telemetry.addData("X", String.format("%d teeth left of center", -xTeeth));
            telemetry.addData("Y", String.format("%d teeth away from you", yTeeth));
            telemetry.addData("Angle",
                    yawQuarterturns == 0 ? "U-D ( | )" :
                    yawQuarterturns == 1 ? "UR-DL ( / )" :
                    yawQuarterturns == 2 ? "L-R ( - )" :
                    "UL-DR ( \\ )"
            );

            String s = "X: \\``\\__\\``/__\\``/__\\``/__\\``/__\\`|";
            if (-10 <= xTeeth && xTeeth <= 0) {
                int i = (xTeeth + 10) * 3 + 4;
                s = s.substring(0, i) + "**" + s.substring(i + 2);
            }
            telemetry.addLine();
            telemetry.addLine(s);

            s = "Y: |_\\||/__\\``/__\\``/__\\``/__\\``\\__\\``/";
            if (yTeeth <= 11) {
                int i = (yTeeth) * 3 + 3;
                s = s.substring(0, i) + "**" + s.substring(i + 2);
            }
            telemetry.addLine();
            telemetry.addLine(s);
        }

        private final static Distance TOOTH = Distance.inMM(30.4);
        public Pose getPose() {
            Distance x = Distance.inTiles(0).add(Distance.inMM(-20)).add(TOOTH.mul(xTeeth));
            Distance y = Distance.inTiles(-1).add(Distance.inMM(20)).add(TOOTH.mul(yTeeth));
            Angle yaw = Angle.LEFT.add(Angle.inDegrees(45).mul(-yawQuarterturns));
            return new Pose(new Distance2(x, y), yaw);
        }

        @SuppressLint("DefaultLocale")
        public static ArrayList<Pose> waitForStartWithTracker(LinearOpMode opMode) {
            Telemetry telemetry = opMode.telemetry;

            Gamepads gamepads = new Gamepads(opMode.gamepad1, opMode.gamepad2);
            boolean lastGp1LsUp = false;
            boolean lastGp1LsDown = false;

            ArrayList<SampleTracker> samples = new ArrayList<>();
            int selected = 0;

            while (!opMode.isStarted()) {
                gamepads.update(opMode.gamepad1, opMode.gamepad2);

                if (gamepads.justPressed(Gamepads.Button.GP1_CROSS)) {
                    samples.add(new SampleTracker());
                    selected = samples.size() - 1;
                }
                if (gamepads.justPressed(Gamepads.Button.GP1_CIRCLE)) {
                    if (!samples.isEmpty()) samples.remove(selected);
                }

                double gp1Ls = gamepads.getAnalogValue(Gamepads.AnalogInput.GP1_LEFT_STICK_Y);
                boolean gp1LsUp = gp1Ls > 0;
                boolean gp1LsDown = gp1Ls < 0;
                if (gp1LsUp && !lastGp1LsUp) selected--;
                if (gp1LsDown && !lastGp1LsDown) selected++;
                selected = Math.max(0, Math.min(selected, samples.size() - 1));
                lastGp1LsUp = gp1LsUp;
                lastGp1LsDown = gp1LsDown;

                if (samples.isEmpty()) {
                    telemetry.addLine("No samples.");
                    telemetry.update();
                    continue;
                }

                for (int i = 0; i < samples.size(); i++) {
                    if (i == selected) telemetry.addLine(String.format("- Sample %d <----", i));
                    else telemetry.addLine(String.format("- Sample %d", i));
                }
                telemetry.addLine();

                samples.get(selected).update(gamepads);
                samples.get(selected).writeToTelemetry(telemetry);

                telemetry.update();
            }

            telemetry.clear();
            telemetry.update();

            ArrayList<Pose> poses = new ArrayList<>();
            for (SampleTracker sample : samples) poses.add(sample.getPose());
            return poses;
        }
    }