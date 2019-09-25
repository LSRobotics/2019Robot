package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Gorgon {

    static DoubleSolenoid gorgonSolenoid;

    static public void initialize() {
        gorgonSolenoid = new DoubleSolenoid(Statics.Gorgon_Solenoid_Forward_Channel, Statics.Gorgon_Solenoid_Reverse_Channel);
    }

    static public void openGorgon() {
        gorgonSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    static public void closeGorgon() {
        gorgonSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

}