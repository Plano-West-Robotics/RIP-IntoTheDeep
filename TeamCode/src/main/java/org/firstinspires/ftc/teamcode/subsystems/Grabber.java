package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.hardware.Claw;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.Swivel;
import org.firstinspires.ftc.teamcode.hardware.Wrist;

public class Grabber {
    private final Wrist wrist;
    private final Swivel swivel;
    private final Claw claw;

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

    public Grabber(Hardware hardware) {
        this(hardware.wrist, hardware.swivel, hardware.claw);
    }

    public Grabber(Wrist wrist, Swivel swivel, Claw claw) {
        this.wrist = wrist;
        this.swivel = swivel;
        this.claw = claw;
        assert wrist.getPosition() == Wrist.State.BUCKET.pos;
        assert swivel.getPosition() == Swivel.BUCKET;
        claw.open();
        this.state = State.BUCKET;
    }

    public void toUp() {
        switch (this.state) {
            case UP:
            case GRABBING_TO_UP:
            case MOVING_UP:
                break;

            case DOWN:
                claw.close();
            case GRABBING_TO_BUCKET:
                this.state = State.GRABBING_TO_UP;
                break;

            case BUCKET:
            case MOVING_BUCKET:
                swivel.setPosition(Swivel.MIDDLE);
            case MOVING_DOWN:
                claw.close();
                wrist.setPosition(Wrist.State.UP);
                this.state = State.MOVING_UP;
                break;
        }
    }

    public void toDown() {
        switch (this.state) {
            case DOWN:
            case MOVING_DOWN:
                break;

            case BUCKET:
            case MOVING_BUCKET:
                swivel.setPosition(Swivel.MIDDLE);
            case UP:
            case MOVING_UP:
                claw.open();
                wrist.setPosition(Wrist.State.DOWN);
                this.state = State.MOVING_DOWN;
                break;

            case GRABBING_TO_UP:
            case GRABBING_TO_BUCKET:
                claw.open();
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
                claw.close();
            case GRABBING_TO_UP:
                this.state = State.GRABBING_TO_BUCKET;
                break;

            case MOVING_DOWN:
            case UP:
            case MOVING_UP:
                swivel.setPosition(Swivel.BUCKET);
                wrist.setPosition(Wrist.State.BUCKET);
                this.state = State.MOVING_BUCKET;
                break;
        }
    }

    private static double mapSwivelPos(double pos) {
        return Range.scale(pos, 0, 1, Swivel.LEFT, Swivel.RIGHT);
    }

    public void setSwivel(double pos) {
        if (this.state == State.DOWN) {
            this.swivelPos = pos;
            swivel.setPosition(mapSwivelPos(this.swivelPos));
        }
    }

    public void swivelBumpLeft() {
        if (this.state == State.DOWN) {
            if (this.swivelPos <= 0.00) swivelPos = 0.75;
            else if (this.swivelPos <= 0.25) swivelPos = 0.00;
            else if (this.swivelPos <= 0.50) swivelPos = 0.25;
            else if (this.swivelPos <= 0.75) swivelPos = 0.50;
            else if (this.swivelPos <= 1.00) swivelPos = 0.75;
            swivel.setPosition(mapSwivelPos(this.swivelPos));
        }
    }

    public void swivelBumpRight() {
        if (this.state == State.DOWN) {
            if (this.swivelPos >= 1.00) swivelPos = 0.25;
            else if (this.swivelPos >= 0.75) swivelPos = 1.00;
            else if (this.swivelPos >= 0.50) swivelPos = 0.75;
            else if (this.swivelPos >= 0.25) swivelPos = 0.50;
            else if (this.swivelPos >= 0.00) swivelPos = 0.25;
            swivel.setPosition(mapSwivelPos(this.swivelPos));
        }
    }

    public void openClawAtBucket() {
        if (this.state == State.BUCKET) {
            claw.open();
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
                    swivel.setPosition(Swivel.MIDDLE);
                    wrist.setPosition(Wrist.State.UP);
                    this.state = State.MOVING_UP;
                }
                break;
            case GRABBING_TO_BUCKET:
                if (!this.claw.isBusy()) {
                    swivel.setPosition(Swivel.BUCKET);
                    wrist.setPosition(Wrist.State.BUCKET);
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
//                    claw.open();
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
