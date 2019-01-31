package frc.robot;

import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;


public class RotateMechanism extends PIDSubsystem{
    
    GyroSensor gyroSensor = Robot.mGyroSensor;

    public RotateMechanism() {
        super(1.0, 0, 0);
        setAbsoluteTolerance(1);
        getPIDController().setContinuous(false);
    }
    
    public void initDefaultCommand() {
    }

    public double returnPIDInput() {
        return gyroSensor.getAngle(); //return gyrosensor.getangle; pass gyrosensor angle
    }

    public void usePIDOutput(double d) {
        //output to motor
    }
}