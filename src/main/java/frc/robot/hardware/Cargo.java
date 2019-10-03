package frc.robot.hardware;

import frc.robot.software.*;
import frc.robot.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Cargo {

    public enum Mode {
        LOWPICKUP, HIGHPICKUP, LOWSHOOT, HIGHSHOOT, IDLE;
    }

    static WPI_TalonSRX low;
    static WPI_TalonSRX high;

    static int timer = 0;

    static public UltrasonicSensor ultrasonicSensor;

    static public void initialize() {
        ultrasonicSensor = new UltrasonicSensor(Statics.Cargo_Ultrasonic_PingChannel,
                Statics.Cargo_Ultrasonic_EchoChannel);
        ultrasonicSensor.startAutomaticMode();
        low = new WPI_TalonSRX(Statics.LOW_CARGO);
        high = new WPI_TalonSRX(Statics.HIGH_CARGO);
    }

    static public boolean ballCaptured() {
        if (ultrasonicSensor.getRangeInches() < 6) {
            return true;
        }
        return false;
    }

    static public void run() {
        switch (Robot.cargoMode) {
        case LOWPICKUP:
            lowPickup();
            break;
        case LOWSHOOT:
            lowShoot();
            break;
        case HIGHPICKUP:
            highPickup();
            break;
        case HIGHSHOOT:
            highShoot();
            break;
        case IDLE:
        default:
            stopCargo();
        }
    }

    /*
     * static public void runCargo() {
     * 
     * if (Robot.cargoMode == Mode.LOWPICKUP) { lowPickup(); } else if
     * (Robot.cargoMode == Mode.LOWSHOOT) { lowShoot(); } else if (Robot.cargoMode
     * == Mode.HIGHPICKUP) { highPickup(); } else if (Robot.cargoMode ==
     * Mode.HIGHSHOOT) { highShoot(); } else { Robot.cargoMode = Mode.IDLE;
     * stopCargo(); } }
     * 
     */

    static public void lowPickup() {
        if (ultrasonicSensor.getRangeInches() > Statics.CARGO_HOLD_DISTANCE) {
            low.set(-Statics.Low_Cargo_Motor_Speed);
        } else {
            Robot.cargoMode = Mode.IDLE;
        }
    }

    static public void highPickup() {
        if (ultrasonicSensor.getRangeInches() > Statics.CARGO_HOLD_DISTANCE) {
            high.set(-Statics.High_Cargo_Intake_Motor_Speed);
        } else {
            Robot.cargoMode = Mode.IDLE;
        }
    }

    static public void lowShoot() {
        if (ultrasonicSensor.getRangeInches() < Statics.CARGO_HOLD_DISTANCE) {
            low.set(-Statics.Low_Cargo_Shoot_Motor_Speed);
            high.set(-Statics.Low_Cargo_Shoot_High_Motor_Speed);
        } else if (timer < (2 * Statics.SEC_TO_INTERVAL)) {
            low.set(-Statics.Low_Cargo_Shoot_Motor_Speed);
            high.set(Statics.Low_Cargo_Motor_Speed);
            timer++;
        } else {
            Robot.cargoMode = Mode.IDLE;
            timer = 0;
        }
    }

    static public void highShoot() {
        if (ultrasonicSensor.getRangeInches() < Statics.CARGO_HOLD_DISTANCE) {
            high.set(Statics.High_Cargo_Motor_Speed);
            low.set(-.7);
        } else if (timer < (2 * Statics.SEC_TO_INTERVAL)) {
            high.set(Statics.High_Cargo_Motor_Speed);
            low.set(-Statics.Low_Cargo_Motor_Speed);
            timer++;
        } else {
            Robot.cargoMode = Mode.IDLE;
            timer = 0;
        }
    }

    static public void stopCargo() {
        Robot.cargoMode = Mode.IDLE;
        low.set(0);
        high.set(0);
    }
}