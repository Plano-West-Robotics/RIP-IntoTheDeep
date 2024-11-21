package org.firstinspires.ftc.teamcode.poser;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.units.Pose;

public interface Localizer {
    @NonNull
    Pose getPoseEstimate();

    void update();

    class FromDelta implements Localizer {
        private final DeltaLocalizer inner;
        private Pose poseEstimate;

        public FromDelta(DeltaLocalizer inner, Pose initialPose) {
            this.inner = inner;
            this.poseEstimate = initialPose;
        }

        public void update() {
            this.poseEstimate = this.poseEstimate.then(this.inner.updateWithDelta());
        }

        public Pose getPoseEstimate() {
            return poseEstimate;
        }
    }
}
