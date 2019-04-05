package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Winch {

    WPI_TalonSRX winchMotorController;
    

    public void initializeWinch() {
        winchMotorController = new WPI_TalonSRX(Statics.Winch_CAN_ID);
        winchMotorController.configFactoryDefault();
        winchMotorController.setSelectedSensorPosition(0);
    }

    public void lowerGorgon() {
        winchMotorController.set(.2);
    }

    public void raiseGorgon() {
        winchMotorController.set(-.5);
    }

    public void stopGorgon() {
        winchMotorController.set(0);
    }

    public int getWinchEncoderValue() {
        return winchMotorController.getSelectedSensorPosition(0);
    }

}