package frc.robot.hardware;

import frc.robot.software.*;

public class Chassis {

    static Motor l1, l2, r1, r2;

    static double speedFactor = 1;
    static boolean isFliped = false;

    static public void initialize() {
        l1 = new Motor(Statics.CHASSIS_L1,false);
        l2 = new Motor(Statics.CHASSIS_L2,false);
        r1 = new Motor(Statics.CHASSIS_R1,true);
        r2 = new Motor(Statics.CHASSIS_R2,true);
    }

    static public void setSpeedFactor(double factor) {
        speedFactor = factor;
    }

    static public void drive(double y, double x) {

        final double left = Utils.clipValue(y + x, -1.0, 1.0) * speedFactor * (isFliped ? -1 : 1);
        final double right = Utils.clipValue(y - x, -1.0, 1.0) * speedFactor * (isFliped ? -1 : 1);

        l1.move(left);
        l2.move(left);
        r1.move(right);
        r2.move(right);

    }

    static public void stop() {
        drive(0, 0);
    }

    static public void flip() {
        isFliped = !isFliped;
    }

    static public void test(boolean isTurn, boolean isLeft) {
        if(isLeft) {
            l1.move(isTurn? 0.5 : 0);
            l2.move(isTurn? 0.5 : 0);
        
        }
        else {
            r1.move(isTurn? 0.5 : 0);
            r2.move(isTurn? 0.5 : 0);
        }
    }
}
