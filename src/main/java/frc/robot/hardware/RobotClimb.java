package frc.robot.hardware;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import frc.robot.software.*;

public class RobotClimb {
    
    // WPI_TalonSRX scooterMotorController;
    // CANSparkMax climbMotorController;
    static DoubleSolenoid climber;

    static public void initialize() {
        // scooterMotorController = new WPI_TalonSRX(Statics.Scooter_Climb_CAN_ID);
        // climbMotorController = new CANSparkMax(Statics.Climb_Wheels_CAN_ID, MotorType.kBrushless);
        climber = new DoubleSolenoid(Statics.Robot_Climb_Solenoid_Forward_Channel, Statics.Robot_Climb_Solenoid_Reverse_Channel);
    }

    static public void openPenumatics() {
        climber.set(Value.kForward);
    }

    static public void closePenumatics() {
        climber.set(Value.kReverse);
    }

}