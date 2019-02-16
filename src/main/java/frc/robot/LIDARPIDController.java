package frc.robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDOutput;

public class LIDARPIDController extends PIDController {

    public LIDARPIDController(double kP, double kI, double kD, double kF, PIDSource source, PIDOutput output) {
        super (kP, kI, kD, kF, source, output);
    }

    public void calculate() {
        super.calculate();
    }
}
