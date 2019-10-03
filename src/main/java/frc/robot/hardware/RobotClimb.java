package frc.robot.hardware;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import frc.robot.software.*;

public class RobotClimb {

    static DoubleSolenoid climber;
    static boolean isForward = true;

    static public void initialize() {
        climber = new DoubleSolenoid(Statics.PCM_CLIMB_F, Statics.PCM_CLIMB_R);
    }

    static public void openPenumatics() {
        climber.set(Value.kForward);
    }

    static public void closePenumatics() {
        climber.set(Value.kReverse);
    }

    static public void actuate() {
        isForward = !isForward;

        climber.set(isForward? Value.kForward : Value.kReverse);
    }


}