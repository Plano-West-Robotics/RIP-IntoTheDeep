package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Hardware;

public class Intake2 {
    private final Extendo extendo;
    private final Grabber2 grabber;

    private final double EXTEND_THRESH = 0.1;

    private enum State {
        CONTINUUM, IN, BUCKET, OUT;
    }
    private State state;
    private double k;
    private double swivel;
    private boolean down;

    private double pow;
    private double target;
    private boolean goingToTarget;

    public Intake2(Hardware hardware) {
        this.state = State.BUCKET;
        this.extendo = new Extendo(hardware, 0);
        this.grabber = new Grabber2(hardware);
        this.goingToTarget = false;
    }

    public void setPower(double pow) {
        this.pow = pow;
    }

    public void swivelBumpLeft() {
        if (state == State.CONTINUUM) {
            this.grabber.swivelBumpLeft();
        }
    }

    public void swivelBumpRight() {
        if (state == State.CONTINUUM) {
            this.grabber.swivelBumpRight();
        }
    }

    public void toggleDown() {
        if (state == State.CONTINUUM) {
            down = !down;
            if (down) {
                this.grabber.toDown();
            } else {
                this.grabber.toUp();
            }
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
                    this.state = State.IN;
                    this.grabber.toBucket();
                }
                break;
            case IN:
                if (pos) {
                    this.state = State.OUT;
                    this.extendo.goTo(EXTEND_THRESH);
                    this.grabber.toUp();
                } else if (!this.extendo.isBusy()) {
                    this.state = State.BUCKET;
                    this.grabber.openClawAtBucket();
                }
                break;
            case BUCKET:
                if (this.goingToTarget && this.target < 0) {
                    this.goingToTarget = false;
                }

                if (pos) {
                    this.state = State.OUT;
                    this.extendo.goTo(EXTEND_THRESH);
                    this.grabber.toUp();
                }
                break;
            case OUT:
                if (neg) {
                    this.state = State.IN;
                    this.grabber.toBucket();
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

    public State getState() {
        return state;
    }
}
