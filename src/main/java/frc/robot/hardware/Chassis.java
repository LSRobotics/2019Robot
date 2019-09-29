package frc.robot.hardware;

import frc.robot.software.*;


public class Chassis {

    static Motor l1,l2,r1,r2;

    static double speedFactor = 1;
    static boolean isFliped = false;

    static public void init() {
        l1 = new Motor(Statics.CHASSIS_L1);
        l2 = new Motor(Statics.CHASSIS_L2);
        r1 = new Motor(Statics.CHASSIS_R1);
        r2 = new Motor(Statics.CHASSIS_R2);
    }

    static public void setSpeedFactor(double factor) {
        speedFactor = factor; 
    }

    static public void drive(double y, double x) {

        final double left  = Utils.clipValue(y + x, -1.0, 1.0) * speedFactor * (isFliped? -1 : 1);
        final double right = Utils.clipValue(y - x, -1.0, 1.0) * speedFactor * (isFliped? -1 : 1);

        l1.setSpeed(left);
        l2.setSpeed(left);
        r1.setSpeed(right);
        r2.setSpeed(right);
        
    }

    static public void stop() {
        drive(0,0);
    }

    static public void flip() {
        isFliped = !isFliped;
    }
}

