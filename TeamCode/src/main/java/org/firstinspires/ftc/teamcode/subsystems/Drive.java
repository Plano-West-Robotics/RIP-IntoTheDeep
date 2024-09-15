package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.units.Vector2;

public class Drive {
    Hardware hardware;

    public Drive(Hardware hw) {
        this.hardware = hw;
    }

    /**
     * Apply power to the drive motors to move in the specified manner.
     *
     * @param x forward is positive
     * @param y left is positive
     * @param turn ccw is positive
     */
    public void drive(double x, double y, double turn) {
        this.drive(new Vector2(x, y), turn);
    }

    /**
     * Apply power to the drive motors to move in the specified manner.
     *
     * @param pow forward is positive x, left is positive y
     * @param turn ccw is positive
     */
    public void drive(Vector2 pow, double turn) {
        hardware.fl.setPower(pow.x - pow.y - turn);
        hardware.fr.setPower(pow.x + pow.y + turn);
        hardware.bl.setPower(pow.x + pow.y - turn);
        hardware.br.setPower(pow.x - pow.y + turn);
    }

    public void stop() {
        drive(0, 0, 0);
    }
}
