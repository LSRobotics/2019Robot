package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class CargoMechanism {

    WPI_TalonSRX lowCargoMotorController;
    WPI_TalonSRX highCargoMotorController;

    public void initializeCargoPickup() {
        lowCargoMotorController = new WPI_TalonSRX(8);
        highCargoMotorController = new WPI_TalonSRX(9);
    }

    public void lowPickupOn() {
        lowCargoMotorController.set(-.5);
    }

    public void lowPickupOff() {
        lowCargoMotorController.set(0);
    }

    public void highPickupOn() {
        lowCargoMotorController.set(-.5);
        highCargoMotorController.set(-.5);
    }

    public void highPickupOff() {
        lowCargoMotorController.set(0);
        highCargoMotorController.set(0);
    }

    public void cargoShoot() {
        lowCargoMotorController.set(.7);
        highCargoMotorController.set(.7);
    }
}