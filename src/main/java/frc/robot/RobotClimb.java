package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class RobotClimb {
    
    WPI_TalonSRX climbWheelsMotorController;
    WPI_TalonSRX scooterMotorController;
    DoubleSolenoid climbSolenoid;

    public void initialize() {
        climbWheelsMotorController = new WPI_TalonSRX(Statics.Climb_Wheels_CAN_ID);
        scooterMotorController = new WPI_TalonSRX(Statics.Scooter_Climb_CAN_ID);
        climbSolenoid = new DoubleSolenoid(Statics.Robot_Climb_Solenoid_Forward_Channel, Statics.Robot_Climb_Solenoid_Reverse_Channel);
    }

    


}