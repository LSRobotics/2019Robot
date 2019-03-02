package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class RobotClimb {
    
    WPI_TalonSRX scooterMotorController;
    CANSparkMax climbMotorController;
    DoubleSolenoid climbSolenoid;

    public void initialize() {
        scooterMotorController = new WPI_TalonSRX(Statics.Scooter_Climb_CAN_ID);
        climbMotorController = new CANSparkMax(Statics.Climb_Wheels_CAN_ID, MotorType.kBrushless);
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
            scooterMotorController.set(-.2); //TODO check
        }
        else {
            scooterMotorController.set(0);
        }
    }


}