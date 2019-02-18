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

    public void runClimb(boolean switchPressed) {
        if (!switchPressed) {
            climbMotorController.set(.5);
        }
        else {
            climbMotorController.set(0);
        }
    }

    public void runScooter() {
        if (scooterMotorController.get() == 0) {
            scooterMotorController.set(1);
        }
        else {
            scooterMotorController.set(0);
        }
    }


}