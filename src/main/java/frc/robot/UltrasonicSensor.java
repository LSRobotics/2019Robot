package frc.robot;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Ultrasonic.Unit;

public class UltrasonicSensor {
    
    public static int mPingChannel;
    public static int mEchoChannel;

    public static Ultrasonic mUltrasonicSensor;

    public void initializeUltrasonicSenor() {
        mUltrasonicSensor = new Ultrasonic(mPingChannel, mEchoChannel, Ultrasonic.Unit.kInches);
        mUltrasonicSensor.setEnabled(true);
        mUltrasonicSensor.setAutomaticMode(true);
    }

    public double getRange() {
        if(mUltrasonicSensor.isRangeValid()) {
            return mUltrasonicSensor.getRangeInches();
        }
        return -1;
    }

    public void destroySensor() {
        mUltrasonicSensor.setEnabled(false);
        mUltrasonicSensor.close();
    }



}