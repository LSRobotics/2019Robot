package frc.robot.hardware;

import edu.wpi.first.wpilibj.AnalogGyro;
import frc.robot.Statics;

final public class GyroSensor {

    public static AnalogGyro sensor;

    public static void initialize() {
        sensor = new AnalogGyro(Statics.GYRO_PORT);
        sensor.initGyro();
        sensor.calibrate();
    }

    public static double getAngle() {
        return sensor.getAngle();
    }
}