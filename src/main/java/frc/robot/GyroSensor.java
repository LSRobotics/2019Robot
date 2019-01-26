package frc.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;

public class GyroSensor {
    
    public static ADXRS450_Gyro GyroSensor;

    public void initializeGyroSensor() {
        GyroSensor = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
        GyroSensor.reset();
        GyroSensor.calibrate();
    }

    public double getAngle() {
        return GyroSensor.getAngle();
    }
}