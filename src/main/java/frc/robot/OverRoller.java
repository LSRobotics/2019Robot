package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRXPIDSetConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.*;
public class OverRoller {

    WPI_TalonSRX leftOverRollerMotorController;
    WPI_TalonSRX rightOverRollerMotorController;
    TalonSRXPIDSetConfiguration pidConfig;
    
    public void initialize() {
        leftOverRollerMotorController = new WPI_TalonSRX(Statics.Left_Over_Roller_CAN_ID);
        rightOverRollerMotorController = new WPI_TalonSRX(Statics.Right_Over_Roller_CAN_ID);
        
        leftOverRollerMotorController.configFactoryDefault();
        rightOverRollerMotorController.configFactoryDefault();
        rightOverRollerMotorController.follow(leftOverRollerMotorController);

        pidConfig = new TalonSRXPIDSetConfiguration();
        leftOverRollerMotorController.configurePID(pidConfig, 0, 30, true);
    }

    public void lowerArms() {
        leftOverRollerMotorController.set(ControlMode.Position, 4096/4); //TODO figure out encoder value.
    }

    public void raiseArms() {
        leftOverRollerMotorController.set(ControlMode.Position, 0);
    }

}