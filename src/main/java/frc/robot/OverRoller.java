package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRXPIDSetConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.*;
public class OverRoller {

    WPI_TalonSRX leftOverRollerMotorController;
    WPI_TalonSRX rightOverRollerMotorController;
    TalonSRXPIDSetConfiguration leftpidConfig;
    TalonSRXPIDSetConfiguration rightpidConfig;
    
    public void initialize() {
        leftOverRollerMotorController = new WPI_TalonSRX(Statics.Left_Over_Roller_CAN_ID);
        rightOverRollerMotorController = new WPI_TalonSRX(Statics.Right_Over_Roller_CAN_ID);
        
        leftOverRollerMotorController.configFactoryDefault();
        rightOverRollerMotorController.configFactoryDefault();

        rightpidConfig = new TalonSRXPIDSetConfiguration();
        leftpidConfig = new TalonSRXPIDSetConfiguration();
        leftOverRollerMotorController.configurePID(leftpidConfig, 0, 30, true);
        rightOverRollerMotorController.configurePID(rightpidConfig, 0, 30, true); //TODO tune
    }

    public void lowerArms() {
        rightOverRollerMotorController.set(ControlMode.Position, 4096);
        leftOverRollerMotorController.set(ControlMode.Position, -4096); //TODO figure out encoder value.
    }

    public void raiseArms() {
        leftOverRollerMotorController.set(ControlMode.Position, 0);
        rightOverRollerMotorController.set(ControlMode.Position, 0);
    }

}