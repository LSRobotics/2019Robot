package frc.robot.hardware;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.software.*;

public class Lights {
    public static Spark lightSpark;
    public static int port = Statics.Light_PWM_Port;

    public static void initialize() {
        lightSpark = new Spark(port);
    }

    public static void lightChange(double lightMode) {
        lightSpark.set(lightMode);
    }
}