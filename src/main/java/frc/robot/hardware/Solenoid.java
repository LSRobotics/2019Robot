package frc.robot.hardware;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.software.*;
import edu.wpi.first.wpilibj.Compressor;

public class Solenoid {

    Compressor compressor;
    DoubleSolenoid solenoid;

    public Solenoid(int fPort, int rPort) {
        compressor = new Compressor();
        solenoid = new DoubleSolenoid(0, fPort, rPort);
        solenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void move(boolean forward, boolean reverse) {
        if (forward == reverse) {
            return;
        } // Ignore input if both buttons are held / released
        else if (forward) {
            solenoid.set(DoubleSolenoid.Value.kForward);
            RobotUtil.report("Solenoid Forward");
        } else {
            solenoid.set(DoubleSolenoid.Value.kReverse);
            RobotUtil.report("Soleniod Reverse");
        }
    }

    @Override
    public String toString() {
        return "Pressure Status: " + (this.compressor.getPressureSwitchValue() ? "Good" : "Too High") + "\tCurrent: "
                + this.compressor.getCompressorCurrent() + "A";
    }
}
