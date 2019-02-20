package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Ultrasonic;

public class UltrasonicSensor {

    public Ultrasonic ultrasonicSensor;
    public DigitalOutput digitalPingChannel;
    public DigitalInput digitalEchoChannel;

    public UltrasonicSensor(int PingChannel, int EchoChannel) {
        digitalPingChannel = new DigitalOutput(PingChannel);
        digitalEchoChannel = new DigitalInput(EchoChannel);
        ultrasonicSensor = new Ultrasonic(digitalPingChannel, digitalEchoChannel, Ultrasonic.Unit.kInches);
    }

    public Ultrasonic getActualSensor() {
        return ultrasonicSensor;
    }

    public double getRangeInches() {
        return ultrasonicSensor.getRangeInches();
    }
    
    public void destroySensor() {
        ultrasonicSensor.close();
    }
    
    public void ping() {
        ultrasonicSensor.ping();
    }

    public boolean isRangeValid() {
        return ultrasonicSensor.isRangeValid();
    }

    public void startAutomaticMode() {
        ultrasonicSensor.setAutomaticMode(true);
    }
}