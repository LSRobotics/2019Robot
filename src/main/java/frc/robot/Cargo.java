package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Cargo {

    static WPI_TalonSRX lowCargoMotorController; 
    static WPI_TalonSRX highCargoMotorController;

    static int timer = 0;

    static public UltrasonicSensor ultrasonicSensor;

    static public void initialize() {
        ultrasonicSensor = new UltrasonicSensor(Statics.Cargo_Ultrasonic_PingChannel, Statics.Cargo_Ultrasonic_EchoChannel);
        ultrasonicSensor.startAutomaticMode();
        lowCargoMotorController = new WPI_TalonSRX(Statics.Low_Cargo_CAN_ID);
        highCargoMotorController = new WPI_TalonSRX(Statics.High_Cargo_CAN_ID);
    }

    static public boolean ballCaptured() {
        if(ultrasonicSensor.getRangeInches() < 6) {
            return true;
        }
        return false;
    }

    static public void runCargo() {
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

    static public void lowCargoPickup() {
        if(ultrasonicSensor.getRangeInches() > Statics.CARGO_HOLD_DISTANCE) {
            lowCargoMotorController.set(-Statics.Low_Cargo_Motor_Speed);
        }
        else {
            Robot.cargoMode = null;
        }
    }

    static public void highCargoPickup() {
        if(ultrasonicSensor.getRangeInches() > Statics.CARGO_HOLD_DISTANCE) {
            highCargoMotorController.set(-Statics.High_Cargo_Intake_Motor_Speed);
        }
        else {
            Robot.cargoMode = null;
        }
    }

    static public void lowCargoShoot() {
        if(ultrasonicSensor.getRangeInches() < Statics.CARGO_HOLD_DISTANCE) {
            lowCargoMotorController.set(-Statics.Low_Cargo_Shoot_Motor_Speed);
            highCargoMotorController.set(-Statics.Low_Cargo_Shoot_High_Motor_Speed);
        }
        else if (timer < (2 * Statics.SEC_TO_INTERVAL)) {
            lowCargoMotorController.set(-Statics.Low_Cargo_Shoot_Motor_Speed);
            highCargoMotorController.set(Statics.Low_Cargo_Motor_Speed);
            timer++;
        }
        else {
            Robot.cargoMode = null;
            timer = 0;
        }
    }

    static public void highCargoShoot() {
        if(ultrasonicSensor.getRangeInches() < Statics.CARGO_HOLD_DISTANCE) {
            highCargoMotorController.set(Statics.High_Cargo_Motor_Speed);
            lowCargoMotorController.set(-.7);
        }
        else if (timer < (2 * Statics.SEC_TO_INTERVAL)) {
            highCargoMotorController.set(Statics.High_Cargo_Motor_Speed);
            lowCargoMotorController.set(-Statics.Low_Cargo_Motor_Speed);
            timer++;
        }
        else {
            Robot.cargoMode = null;
            timer = 0;
        }
    }

    static public void stopCargo() {
        lowCargoMotorController.set(0);
        highCargoMotorController.set(0);
    }
}

enum CargoMode {
    LOWPICKUP, HIGHPICKUP, LOWSHOOT, HIGHSHOOT;
}