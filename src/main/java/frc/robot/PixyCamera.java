package frc.robot;

import edu.wpi.first.wpilibj.I2C;

public class PixyCamera {

    I2C i2c;
    byte[] arduinoData;

    public void initializePixyCamera() {
        i2c = new I2C(I2C.Port.kOnboard, 168);
    }

    public void getDataFromArduino() {
        i2c.readOnly(arduinoData, 72);
    }
}