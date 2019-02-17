package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class RobotClimb {
    
    WPI_TalonSRX leftClimbMotorController;
    WPI_TalonSRX rightClimbMotorController;
    DoubleSolenoid climbSolenoid;

    public void initialize() {
        leftClimbMotorController = new WPI_TalonSRX(Statics.left_Climb_CAN_ID);
        rightClimbMotorController = new WPI_TalonSRX(Statics.right_Climb_CAN_ID);
        climbSolenoid = new DoubleSolenoid(Statics.Robot_Climb_Solenoid_Forward_Channel, Statics.Robot_Climb_Solenoid_Reverse_Channel);
    }


}