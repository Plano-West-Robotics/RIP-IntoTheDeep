package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.util.ServoTimer;

public class Grabber2 {
    private final SimpleGrabber2 inner;
    private final ServoTimer wrist;
    private final ServoTimer swivel;
    private final ServoTimer claw;

    private enum State {
        UP, // holding up position, claw closed
        DOWN, // holding down position, claw open, swivel could be anywhere
        BUCKET, // holding bucket position, claw open
        GRABBING_TO_UP, // in down position, closing claw; about to go into MOVING_UP
        GRABBING_TO_BUCKET, // in down position, closing claw; about to go into MOVING_BUCKET
        MOVING_UP, // moving to up
        MOVING_DOWN, // moving to down
        MOVING_BUCKET, // moving to bucket
    }
    private State state;
    private double swivelPos;

    public Grabber2(Hardware hardware) {
        this.inner = new SimpleGrabber2(hardware);
        this.wrist = new ServoTimer(SimpleGrabber2.WristState.BUCKET.pos, 1.1);
        this.swivel = new ServoTimer(0.5, 1.1);
        this.claw = new ServoTimer(1.0, 1.1);
        this.inner.wristTo(SimpleGrabber2.WristState.BUCKET);
        this.inner.swivelTo(0.5);
        this.inner.openClaw();
        this.state = State.BUCKET;
    }

    private void wristTo(SimpleGrabber2.WristState state) {
        this.wrist.goTo(state.pos);
        this.inner.wristTo(state);
    }

    private void swivelTo(double pos) {
        this.swivel.goTo(pos);
        this.inner.swivelTo(pos);
    }

    private void closeClaw() {
        this.claw.goTo(0.0);
        this.inner.closeClaw();
    }

    private void openClaw() {
        this.claw.goTo(1.0);
        this.inner.openClaw();
    }

    public void toUp() {
        switch (this.state) {
            case UP:
            case GRABBING_TO_UP:
            case MOVING_UP:
                break;

            case DOWN:
                this.closeClaw();
            case GRABBING_TO_BUCKET:
                this.state = State.GRABBING_TO_UP;
                break;

            case BUCKET:
            case MOVING_DOWN:
            case MOVING_BUCKET:
                this.closeClaw();
                this.wristTo(SimpleGrabber2.WristState.UP);
                this.state = State.MOVING_UP;
                break;
        }
    }

    public void toDown() {
        switch (this.state) {
            case DOWN:
            case MOVING_DOWN:
                break;

            case UP:
            case MOVING_UP:
            case MOVING_BUCKET:
            case BUCKET:
                this.openClaw();
                this.wristTo(SimpleGrabber2.WristState.DOWN);
                this.state = State.MOVING_DOWN;
                break;

            case GRABBING_TO_UP:
            case GRABBING_TO_BUCKET:
                this.openClaw();
                this.state = State.DOWN;
                break;
        }
    }

    public void toBucket() {
        switch (this.state) {
            case BUCKET:
            case GRABBING_TO_BUCKET:
            case MOVING_BUCKET:
                break;

            case DOWN:
                this.closeClaw();
            case GRABBING_TO_UP:
                this.state = State.GRABBING_TO_BUCKET;
                break;

            case MOVING_DOWN:
            case UP:
            case MOVING_UP:
                this.wristTo(SimpleGrabber2.WristState.BUCKET);
                this.state = State.MOVING_BUCKET;
                break;
        }
    }

    public void setSwivel(double pos) {
        if (this.state == State.DOWN) {
            this.swivelPos = pos;
            this.swivelTo(this.swivelPos);
        }
    }

    public void swivelBumpLeft() {
        if (this.state == State.DOWN) {
            if (this.swivelPos <= 0.00) swivelPos = 0.75;
            else if (this.swivelPos <= 0.25) swivelPos = 0.00;
            else if (this.swivelPos <= 0.50) swivelPos = 0.25;
            else if (this.swivelPos <= 0.75) swivelPos = 0.50;
            else if (this.swivelPos <= 1.00) swivelPos = 0.75;
            this.swivelTo(this.swivelPos);
        }
    }

    public void swivelBumpRight() {
        if (this.state == State.DOWN) {
            if (this.swivelPos >= 1.00) swivelPos = 0.25;
            else if (this.swivelPos >= 0.75) swivelPos = 1.00;
            else if (this.swivelPos >= 0.50) swivelPos = 0.75;
            else if (this.swivelPos >= 0.25) swivelPos = 0.50;
            else if (this.swivelPos >= 0.00) swivelPos = 0.25;
            this.swivelTo(this.swivelPos);
        }
    }

    public void openClawAtBucket() {
        if (this.state == State.BUCKET) {
            this.openClaw();
        }
    }

    public void update(double dt) {
        switch (this.state) {
            case UP:
            case DOWN:
            case BUCKET:
                break;
            case GRABBING_TO_UP:
                if (!this.claw.isBusy()) {
                    this.swivelTo(0.5);
                    this.wristTo(SimpleGrabber2.WristState.UP);
                    this.state = State.MOVING_UP;
                }
                break;
            case GRABBING_TO_BUCKET:
                if (!this.claw.isBusy()) {
                    this.swivelTo(0.5);
                    this.wristTo(SimpleGrabber2.WristState.BUCKET);
                    this.state = State.MOVING_BUCKET;
                }
                break;
            case MOVING_UP:
                if (!this.wrist.isBusy() && !this.swivel.isBusy()) {
                    this.state = State.UP;
                }
                break;
            case MOVING_DOWN:
                if (!this.wrist.isBusy() && !this.claw.isBusy()) {
                    this.swivelPos = 0.5;
                    this.state = State.DOWN;
                }
                break;
            case MOVING_BUCKET:
                if (!this.wrist.isBusy() && !this.swivel.isBusy()) {
//                    this.openClaw();
                    this.state = State.BUCKET;
                }
                break;
        }

        this.wrist.update(dt);
        this.swivel.update(dt);
        this.claw.update(dt);
    }

    public boolean isBusy() {
        switch (this.state) {
            case DOWN:
                return this.swivel.isBusy();
            case UP:
            case BUCKET:
                return false;
            default:
                return true;
        }
    }
}
