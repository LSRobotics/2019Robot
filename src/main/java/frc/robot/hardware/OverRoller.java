package frc.robot.hardware;

import frc.robot.hardware.MotorNG.Model;
import frc.robot.software.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class OverRoller {

    final static double OVERROLLER_SPD = .5;

    static MotorNG left;
    static MotorNG right;

    static public void initialize() {

        left = new MotorNG(Statics.Left_Over_Roller_CAN_ID, Model.SPARK_MAX);
        right = new MotorNG(Statics.Right_Over_Roller_CAN_ID, Model.SPARK_MAX);
    }

    static public void lowerArms() {
        left.move(OVERROLLER_SPD);
        right.move(-OVERROLLER_SPD);
    }

    static public void raiseArms() {
        left.move(-OVERROLLER_SPD);
        right.move(OVERROLLER_SPD);
    }

    static public void stopArms() {
        left.move(0);
        right.move(0);
    }

    static public double getLeftEncoder() {
        return left.getEncoderReading();
    }

    static public double getRightEncoder() {
        return right.getEncoderReading();
    }

}