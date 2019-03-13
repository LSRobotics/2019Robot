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

}