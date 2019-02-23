package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class RobotClimb {
    
    WPI_TalonSRX scooterMotorController;
    WPI_TalonSRX climbMotorController;
    DoubleSolenoid climbSolenoid;

    public void initialize() {
        scooterMotorController = new WPI_TalonSRX(Statics.Scooter_Climb_CAN_ID);
        climbMotorController = new WPI_TalonSRX(Statics.Climb_Wheels_CAN_ID);
        climbSolenoid = new DoubleSolenoid(Statics.Robot_Climb_Solenoid_Forward_Channel, Statics.Robot_Climb_Solenoid_Reverse_Channel);
    }

    public void openPenumatics() {
        climbSolenoid.set(Value.kForward);
    }

    public void closePenumatics() {
        climbSolenoid.set(Value.kReverse);
    }

    public void runClimb(boolean runClimber, boolean switchPressed) {
        if (runClimber && !switchPressed) {
            climbMotorController.set(1);
        }
        else {
            climbMotorController.set(0);
        }
    }

    public void runScooter(boolean runScooter) {
        if (runScooter) {
            scooterMotorController.set(-.2);
        }
        else {
            scooterMotorController.set(0);
        }
    }


}