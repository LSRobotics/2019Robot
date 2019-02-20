package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class CargoMechanism {

    WPI_TalonSRX lowCargoMotorController; 
    WPI_TalonSRX highCargoMotorController;

    public UltrasonicSensor ultrasonicSensor;

    public void initialize() {
        ultrasonicSensor = new UltrasonicSensor(Statics.Cargo_Ultrasonic_PingChannel, Statics.Cargo_Ultrasonic_EchoChannel);
        ultrasonicSensor.startAutomaticMode();
        lowCargoMotorController = new WPI_TalonSRX(Statics.Low_Cargo_CAN_ID);
        highCargoMotorController = new WPI_TalonSRX(Statics.High_Cargo_CAN_ID);
    }

    public void runCargo() {
        if (Robot.cargoMode == CargoMode.LOWPICKUP) {
            lowCargoPickup();
        }
        else if (Robot.cargoMode == CargoMode.LOWSHOOT) {
            lowCargoShoot();
        }
        else if (Robot.cargoMode == CargoMode.HIGHPICKUP) {
            highCargoPickup();
        }
        else if (Robot.cargoMode == CargoMode.HIGHSHOOT) {
            highCargoShoot();
        }
        else {
            Robot.cargoMode = null;
            stopCargo();
        }
    }

    public void lowCargoPickup() {
        if(ultrasonicSensor.getRangeInches() > Statics.Cargo_Hold_Distance) {
            lowCargoMotorController.set(Statics.Low_Cargo_Motor_Speed);
        }
        else {
            Robot.cargoMode = null;
        }
    }

    public void highCargoPickup() {
        if(ultrasonicSensor.getRangeInches() > Statics.Cargo_Hold_Distance) {
            highCargoMotorController.set(-Statics.High_Cargo_Motor_Speed);
        }
        else {
            Robot.cargoMode = null;
        }
    }

    public void lowCargoShoot() {
        if(ultrasonicSensor.getRangeInches() < Statics.Cargo_Hold_Distance) {
            lowCargoMotorController.set(Statics.Low_Cargo_Motor_Speed);
            highCargoMotorController.set(-Statics.Low_Cargo_Motor_Speed);
        }
        else {
            Robot.cargoMode = null;
        }
    }

    public void highCargoShoot() {
        if(ultrasonicSensor.getRangeInches() < Statics.Cargo_Hold_Distance) {
            lowCargoMotorController.set(Statics.Low_Cargo_Motor_Speed);
            highCargoMotorController.set(Statics.High_Cargo_Motor_Speed);
        }
        else {
            Robot.cargoMode = null;
        }
    }

    public void stopCargo() {
        lowCargoMotorController.set(0);
        highCargoMotorController.set(0);
    }
}

enum CargoMode {
    LOWPICKUP, HIGHPICKUP, LOWSHOOT, HIGHSHOOT;
}