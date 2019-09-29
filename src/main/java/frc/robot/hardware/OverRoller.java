package frc.robot.hardware;

import frc.robot.software.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class OverRoller {

    static CANSparkMax left;
    static CANSparkMax right;
    
    static public void initialize() {
        left = new CANSparkMax(Statics.Left_Over_Roller_CAN_ID, MotorType.kBrushless);
        right = new CANSparkMax(Statics.Right_Over_Roller_CAN_ID, MotorType.kBrushless);
        
        left.restoreFactoryDefaults();
        right.restoreFactoryDefaults();

        left.getEncoder().setPosition(0);
        right.getEncoder().setPosition(0);
    }

    static public void lowerArms() {
        left.set(Statics.Left_Over_Roller_Motor_Speed);
        right.set(-Statics.Right_Over_Roller_Motor_Speed);
    }

    static public void raiseArms() {
        left.set(-Statics.Left_Over_Roller_Motor_Speed);
        right.set(Statics.Right_Over_Roller_Motor_Speed);
    }

    static public void stopArms() {
        left.set(0);
        right.set(0);
    }

    static public double getLeftEncoder() {
        return left.getEncoder().getPosition();
    }

    static public double getRightEncoder() {
        return right.getEncoder().getPosition();
    }

}