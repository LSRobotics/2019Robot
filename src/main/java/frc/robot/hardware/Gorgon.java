package frc.robot.hardware;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.software.*;

public class Gorgon {

    static DoubleSolenoid gorgon;
    static boolean isForward = true;

    static public void initialize() {
        gorgon = new DoubleSolenoid(Statics.PCM_GORGON_F, Statics.PCM_GORGON_R);
        gorgon.set(Value.kOff);
    }

    static public void openGorgon() {
        isForward = true;
        gorgon.set(Value.kForward);
    }

    static public void closeGorgon() {

        isForward = false;
        gorgon.set(Value.kReverse);
    }

    static public boolean isOpen() {
        return !isForward;
    }

    static public void actuate() {
        isForward = !isForward;

        gorgon.set(isForward ? Value.kForward : Value.kReverse);
    }

}