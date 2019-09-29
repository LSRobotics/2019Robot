package frc.robot.hardware;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import frc.robot.software.*;

public class Winch {

    static WPI_TalonSRX winch;

    public static void initialize() {
        winch = new WPI_TalonSRX(Statics.Winch_CAN_ID);
        winch.configFactoryDefault();
        winch.setSelectedSensorPosition(0);
    }

    public static void lowerGorgon() {
        winch.set(.2);
    }

    public static void raiseGorgon() {
        winch.set(-.5);
    }

    public static void stopGorgon() {
        winch.set(0);
    }

    public static int getWinchEncoderValue() {
        return winch.getSelectedSensorPosition(0);
    }

}