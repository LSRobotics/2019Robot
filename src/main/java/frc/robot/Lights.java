package frc.robot;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.DriverStation;

public class Lights {
    public Spark lightSpark;
    public int port = Statics.Light_PWM_Port;

    public void initialize() {
        lightSpark = new Spark(port);
    }

    public void lightChange(double lightMode) {
        lightSpark.set(lightMode);
    }
}