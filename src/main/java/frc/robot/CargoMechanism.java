package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class CargoMechanism {

    WPI_TalonSRX lowCargoMotorController; 
    WPI_TalonSRX highCargoMotorController;
    WPI_TalonSRX frontOverRollerMotorController;

    public UltrasonicSensor ultrasonicSensor;

    public void initialize() {
        // ultrasonicSensor = new UltrasonicSensor(Statics.Cargo_Ultrasonic_PingChannel, Statics.Cargo_Ultrasonic_EchoChannel);
        lowCargoMotorController = new WPI_TalonSRX(Statics.Low_Cargo_CAN_ID);
        highCargoMotorController = new WPI_TalonSRX(Statics.High_Cargo_CAN_ID);
        frontOverRollerMotorController = new WPI_TalonSRX(Statics.Front_Over_Roller_CAN_ID);
    }

    public boolean lowCargoPickup() {
        if(ultrasonicSensor.getRangeInches() > Statics.Cargo_Hold_Distance) {
            lowCargoMotorController.set(-Statics.Low_Cargo_Motor_Speed);
            frontOverRollerMotorController.set(-Statics.Front_Over_Rollor_Motor_Speed);
            return true;
        }
        else {
            lowCargoMotorController.set(0);
            frontOverRollerMotorController.set(0);
            return false;
        }
    }

    public boolean highCargoPickup() {
        if(ultrasonicSensor.getRangeInches() > Statics.Cargo_Hold_Distance) {
            highCargoMotorController.set(-Statics.High_Cargo_Motor_Speed);
            frontOverRollerMotorController.set(-Statics.Front_Over_Rollor_Motor_Speed);
            return true;
        }
        else {
            highCargoMotorController.set(0);
            frontOverRollerMotorController.set(0);
            return false;
        }
    }

    public boolean lowCargoShoot() {
        if(ultrasonicSensor.getRangeInches() < Statics.Cargo_Hold_Distance) {
            lowCargoMotorController.set(Statics.Low_Cargo_Motor_Speed);
            frontOverRollerMotorController.set(-Statics.Front_Over_Rollor_Motor_Speed);
            return true;
        }
        else {
            lowCargoMotorController.set(0);
            frontOverRollerMotorController.set(0);
            return false;
        }
    }

    public boolean highCargoShoot() {
        if(ultrasonicSensor.getRangeInches() < Statics.Cargo_Hold_Distance) {
            lowCargoMotorController.set(Statics.Low_Cargo_Motor_Speed);
            highCargoMotorController.set(Statics.High_Cargo_Motor_Speed);
            return true;
        }
        else {
            lowCargoMotorController.set(0);
            highCargoMotorController.set(0);
            frontOverRollerMotorController.set(0);
            return false;
        }
    }
}
