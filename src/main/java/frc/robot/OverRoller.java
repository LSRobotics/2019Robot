package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class OverRoller {

    CANSparkMax leftOverRollerMotorController;
    CANSparkMax rightOverRollerMotorController;
    
    public void initialize() {
        leftOverRollerMotorController = new CANSparkMax(Statics.Left_Over_Roller_CAN_ID, MotorType.kBrushless);
        rightOverRollerMotorController = new CANSparkMax(Statics.Right_Over_Roller_CAN_ID, MotorType.kBrushless);
        
        leftOverRollerMotorController.restoreFactoryDefaults();
        rightOverRollerMotorController.restoreFactoryDefaults();

        leftOverRollerMotorController.getEncoder().setPosition(0);
        rightOverRollerMotorController.getEncoder().setPosition(0);
    }

    public void lowerArms() {
        leftOverRollerMotorController.set(Statics.Left_Over_Roller_Motor_Speed);
        rightOverRollerMotorController.set(-Statics.Right_Over_Roller_Motor_Speed);
    }

    public void raiseArms() {
        leftOverRollerMotorController.set(-Statics.Left_Over_Roller_Motor_Speed);
        rightOverRollerMotorController.set(Statics.Right_Over_Roller_Motor_Speed);
    }

    public void stopArms() {
        leftOverRollerMotorController.set(0);
        rightOverRollerMotorController.set(0);
    }

    public double getLeftEncoder() {
        return leftOverRollerMotorController.getEncoder().getPosition();
    }

    public double getRightEncoder() {
        return rightOverRollerMotorController.getEncoder().getPosition();
    }

}