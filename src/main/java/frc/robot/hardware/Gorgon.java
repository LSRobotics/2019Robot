package frc.robot.hardware;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.software.*;

public class Gorgon {

    static DoubleSolenoid gorgon;

    static public void initialize() {
        gorgon = new DoubleSolenoid(Statics.Gorgon_Solenoid_Forward_Channel, Statics.Gorgon_Solenoid_Reverse_Channel);
        gorgon.set(Value.kOff);
    }

    static public void openGorgon() {
        gorgon.set(Value.kForward);
    }

    static public void closeGorgon() {
        gorgon.set(Value.kReverse);
    }

}