package frc.robot.hardware;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Ultrasonic;

public class UltrasonicSensor extends Ultrasonic {

    public UltrasonicSensor(int pingChannel, int echoChannel) {

        super(pingChannel, echoChannel, Ultrasonic.Unit.kInches);
    }

    public UltrasonicSensor getActualSensor() {
        return this;
    }

    public void destroySensor() {
        this.close();
    }

    public void startAutomaticMode() {
        this.setAutomaticMode(true);
    }
}