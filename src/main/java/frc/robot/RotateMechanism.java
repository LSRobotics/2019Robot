package frc.robot;

import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class RotateMechanism extends PIDSubsystem{
    
    public RotateMechanism() {
        super(1.0, 0, 0);
        setAbsoluteTolerance(1);
        getPIDController().setContinuous(false);
    }
    
    public void initDefaultCommand() {
    }

    public double returnPIDInput() {
        return 1; //return gyrosensor.getangle; pass gyrosensor angle
    }

    public void usePIDOutput(double d) {
        //output to motor
    }
}