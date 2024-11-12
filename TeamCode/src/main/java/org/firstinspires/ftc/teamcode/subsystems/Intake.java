package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Hardware;

public class Intake {
    private final Extendo extendo;
    private final Grabber grabber;

    private final double EXTEND_THRESH = 0.3;

    private enum State {
        CONTINUUM, IN_1, IN_2, BUCKET, OUT_2, OUT_1;
    }
    private State state;
    private double k;
    private boolean down;

    private double pow;
    private double target;
    private boolean goingToTarget;

    public Intake(Hardware hardware) {
        this.state = State.BUCKET;
        this.extendo = new Extendo(hardware, 0);
        this.grabber = new Grabber(hardware, SimpleGrabber.State.BUCKET);
        this.goingToTarget = false;
    }

    public void setPower(double pow) {
        this.pow = pow;
    }

    public void toggleDown() {
        if (state == State.CONTINUUM || state == State.IN_1 || state == State.OUT_1) {
            down = !down;
            if (down) this.grabber.goTo(SimpleGrabber.State.DOWN);
            else this.grabber.goTo(SimpleGrabber.State.UP);
        }
    }

    /**
     * @param target 0.0 to 1.0 for a position within the continuum; -1 (really, any negative value) for bucket
     */
    public void setTarget(double target) {
        if (target < 0.0) this.target = -1;
        else this.target = Range.scale(target, 0.0, 1.0, EXTEND_THRESH, 1.0);
        this.goingToTarget = true;
    }

    public void update(double dt) {
        boolean pos, neg;
        if (!this.goingToTarget) {
            pos = pow > 0;
            neg = pow < 0;
        } else {
            pos = this.target > 0;
            neg = this.target < 0;
        }

        switch (this.state) {
            case CONTINUUM:
                if (this.goingToTarget && this.target > 0 && !this.extendo.isBusy()) {
                    this.goingToTarget = false;
                }

                if (!this.goingToTarget) {
                    k += 1.0 * dt * pow;
                    k = Range.clip(k, EXTEND_THRESH, 1.0);
                } else {
                    if (this.target < 0) k = EXTEND_THRESH;
                    else k = this.target;
                }
                this.extendo.goTo(k);

                if (k == EXTEND_THRESH && neg) {
                    this.state = State.IN_1;
                    this.extendo.goTo(0.0);
                }
                break;
            case IN_1:
                if (pos) {
                    this.state = State.OUT_1;
                    this.extendo.goTo(EXTEND_THRESH);
                } else if (!this.extendo.isBusy()) {
                    this.state = State.IN_2;
                    this.grabber.goTo(SimpleGrabber.State.BUCKET);
                }
                break;
            case IN_2:
                if (pos) {
                    this.state = State.OUT_2;
                    this.grabber.goTo(SimpleGrabber.State.UP);
                } else if (!this.grabber.isBusy()) {
                    this.state = State.BUCKET;
                }
                break;
            case BUCKET:
                if (this.goingToTarget && this.target < 0) {
                    this.goingToTarget = false;
                }

                if (pos) {
                    this.state = State.OUT_2;
                    this.grabber.goTo(SimpleGrabber.State.UP);
                }
                break;
            case OUT_2:
                if (neg) {
                    this.state = State.IN_2;
                    this.grabber.goTo(SimpleGrabber.State.BUCKET);
                } else if (!this.grabber.isBusy()) {
                    this.state = State.OUT_1;
                    this.extendo.goTo(EXTEND_THRESH);
                    down = false;
                }
                break;
            case OUT_1:
                if (neg) {
                    this.state = State.IN_1;
                    this.extendo.goTo(0);
                } else if (!this.extendo.isBusy()) {
                    this.state = State.CONTINUUM;
                    if (this.goingToTarget) {
                        k = target;
                        this.extendo.goTo(k);
                    } else {
                        k = EXTEND_THRESH;
                    }
                }
                break;
        }

        this.extendo.update(dt);
        this.grabber.update(dt);
    }
}
