package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
public class OverRoller {

    WPI_TalonSRX leftOverRollerMotorController;
    WPI_TalonSRX rightOverRollerMotorController;
    
    public void initialize() {
        leftOverRollerMotorController = new WPI_TalonSRX(Statics.Left_Over_Roller_CAN_ID);
        rightOverRollerMotorController = new WPI_TalonSRX(Statics.Right_Over_Roller_CAN_ID);
        
        leftOverRollerMotorController.configFactoryDefault();
        rightOverRollerMotorController.configFactoryDefault();
    }

    public void lowerArms() {
        leftOverRollerMotorController.set(.25);
        rightOverRollerMotorController.set(-.25);
    }

    public void raiseArms() {
        leftOverRollerMotorController.set(-.25);
        rightOverRollerMotorController.set(.25);
    }

    public void stopArms() {
        leftOverRollerMotorController.set(0);
        rightOverRollerMotorController.set(0);
    }

}