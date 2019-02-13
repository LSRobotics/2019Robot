package frc.robot;

import edu.wpi.first.wpilibj.*;

public class UltrasonicPIDController extends PIDController {
    
    public UltrasonicPIDController(double kP, double kI, double kD, double kF, PIDSource pidSource, PIDOutput pidOutput) {
        super(kP, kI, kD, kF, pidSource, pidOutput);
    }

    public void calculate() {
        super.calculate();
    }
}