package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.InClaw;
import org.firstinspires.ftc.teamcode.hardware.InSwivel;
import org.firstinspires.ftc.teamcode.hardware.InWrist;

public class Grabber {
    private final InWrist wrist;
    private final InSwivel swivel;
    private final InClaw claw;

    private enum State {
        UP, // holding up position, claw closed
        DOWN, // holding down position, claw open, swivel could be anywhere
        TRANSFER, // holding transfer position
        IN, // holding in position, claw open
        GRABBING_TO_UP, // in down position, closing claw; about to go into MOVING_UP
        GRABBING_TO_TRANSFER, // in down position, closing claw; about to go into MOVING_TRANSFER
        MOVING_UP, // moving to up
        MOVING_DOWN, // moving to down
        MOVING_TRANSFER, // moving to transfer
        MOVING_IN, // moving to in
    }
    private State state;
    private double swivelPos;

    public Grabber(Hardware hardware, Hardware.InitialConfiguration initial) {
        this(hardware.inWrist, hardware.inSwivel, hardware.inClaw, initial);
    }

    public Grabber(InWrist wrist, InSwivel swivel, InClaw claw, Hardware.InitialConfiguration inital) {
        this.wrist = wrist;
        this.swivel = swivel;
        this.claw = claw;
        this.state = inital.branch(State.IN, State.TRANSFER, State.IN);
    }

    public void toUp() {
        switch (this.state) {
            case UP:
            case GRABBING_TO_UP:
            case MOVING_UP:
                break;

            case DOWN:
                claw.close();
            case GRABBING_TO_TRANSFER:
                this.state = State.GRABBING_TO_UP;
                break;

            case TRANSFER:
            case IN:
            case MOVING_TRANSFER:
            case MOVING_IN:
                swivel.setPosition(InSwivel.MIDDLE);
            case MOVING_DOWN:
                claw.close();
                wrist.setPosition(InWrist.State.UP);
                this.state = State.MOVING_UP;
                break;
        }
    }

    public void toDown() {
        switch (this.state) {
            case DOWN:
            case MOVING_DOWN:
                break;

            case TRANSFER:
            case MOVING_TRANSFER:
                swivel.setPosition(InSwivel.MIDDLE);
            case UP:
            case MOVING_UP:
                claw.open();
                wrist.setPosition(InWrist.State.DOWN);
                this.state = State.MOVING_DOWN;
                break;

            case IN:
            case MOVING_IN:
                swivel.setPosition(InSwivel.MIDDLE);
                wrist.setPosition(InWrist.State.DOWN);
                this.state = State.MOVING_DOWN;
                break;

            case GRABBING_TO_UP:
            case GRABBING_TO_TRANSFER:
                claw.open();
                this.state = State.DOWN;
                break;
        }
    }

    public void toTransfer() {
        switch (this.state) {
            case TRANSFER:
            case GRABBING_TO_TRANSFER:
            case MOVING_TRANSFER:
                break;

            case DOWN:
                claw.close();
            case GRABBING_TO_UP:
                this.state = State.GRABBING_TO_TRANSFER;
                break;

            case MOVING_DOWN:
            case UP:
            case MOVING_UP:
                swivel.setPosition(InSwivel.TRANSFER);
                wrist.setPosition(InWrist.State.TRANSFER);
                this.state = State.MOVING_TRANSFER;
                break;

            case IN:
            case MOVING_IN:
                wrist.setPosition(InWrist.State.TRANSFER);
                break;
        }
    }

    public void toIn() {
        switch (this.state) {
            case IN:
            case MOVING_IN:
                break;

            case GRABBING_TO_UP:
            case GRABBING_TO_TRANSFER:
            case UP:
            case MOVING_UP:
                this.claw.open();
            case DOWN:
            case MOVING_DOWN:
                swivel.setPosition(InSwivel.TRANSFER);
                wrist.setPosition(InWrist.State.IN);
                this.state = State.MOVING_IN;
                break;

            case TRANSFER:
            case MOVING_TRANSFER:
                wrist.setPosition(InWrist.State.IN);
                break;
        }
    }

    private static double mapSwivelPos(double pos) {
        return Range.scale(pos, 0, 1, InSwivel.LEFT, InSwivel.RIGHT);
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

    public void openClawAtTransfer() {
        if (this.state == State.TRANSFER) {
            claw.open();
        }
    }

    public void update(double dt) {
        switch (this.state) {
            case UP:
            case DOWN:
            case TRANSFER:
            case IN:
                break;
            case GRABBING_TO_UP:
                if (!this.claw.isBusy()) {
                    swivel.setPosition(InSwivel.MIDDLE);
                    wrist.setPosition(InWrist.State.UP);
                    this.state = State.MOVING_UP;
                }
                break;
            case GRABBING_TO_TRANSFER:
                if (!this.claw.isBusy()) {
                    swivel.setPosition(InSwivel.TRANSFER);
                    wrist.setPosition(InWrist.State.TRANSFER);
                    this.state = State.MOVING_TRANSFER;
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
            case MOVING_TRANSFER:
                if (!this.wrist.isBusy() && !this.swivel.isBusy()) {
//                    claw.open();
                    this.state = State.TRANSFER;
                }
                break;
            case MOVING_IN:
                if (!this.wrist.isBusy() && !this.swivel.isBusy()) {
                    this.state = State.IN;
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
            case IN:
                return false;
            case TRANSFER:
                return this.claw.isBusy();
            default:
                return true;
        }
    }
}
