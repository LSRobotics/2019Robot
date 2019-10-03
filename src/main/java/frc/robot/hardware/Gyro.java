package frc.robot.hardware;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI;

public class Gyro {

    public static ADXRS450_Gyro gyro;

    public static void initialize() {
        gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
        gyro.reset();
        gyro.calibrate();
    }

    public static double getAngle() {
        return gyro.getAngle();
    }

    public static double getAbsAngle() {
        return gyro.getAngle() % 360;
    }
}