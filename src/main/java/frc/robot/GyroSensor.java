package frc.robot;

import edu.wpi.first.wpilibj.AnalogGyro;

public class GyroSensor {

    public static int mGyroPortNumber; //TODO map channel number

    public static AnalogGyro mGyroSensor = new AnalogGyro(mGyroPortNumber);

    public void initializeGyroSensor() {
        mGyroSensor.initGyro();
        mGyroSensor.calibrate();
    }

    public double getAngle() {
        return mGyroSensor.getAngle();
    }
}