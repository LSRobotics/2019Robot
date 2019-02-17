package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SerialPort;

public class GyroSensor {
    
    public static AHRS GyroSensor;

    public void initializeGyroSensor() {
        GyroSensor = new AHRS(SerialPort.Port.kMXP);
        GyroSensor.reset();
        GyroSensor.zeroYaw();
    }

    public double getAngle() {
        return GyroSensor.getAngle();
    }

    public AHRS getActualGyroSensor() {
        return GyroSensor;
    }
}