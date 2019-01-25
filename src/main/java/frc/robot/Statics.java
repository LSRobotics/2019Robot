package frc.robot;

import frc.robot.hardware.Motor;

public class Statics {
    
    //From Google Java Style Guide: If you are going to name a constant, you need to make sure its all UPPERCASE WITH UNDERSCORES
    
    //Wheels
    public static int DRIVE_FL = 0,
                      DRIVE_RL = 3,
                      DRIVE_FR = 1,
                      DRIVE_RR = 2;

    //Xbox Controllers               
    public static int XBOX_CTRL1 = 0,
                      XBOX_CTRL2 = 1;

    //Ultrasonic Sensor(s)
    //TODO: Map this
    public static int ULTRASONIC_PING_PORT_1 = 0,
                      ULTRASONIC_ECHO_PORT_1 = 1;
    
    //Gyro Sensors
    //TODO: Map this (It should be good as it is since we are going to use the first port anyway)
    public static int GYRO_PORT = 0;
        
    //Other needed constants
    public static boolean DEBUG_MODE = false;
    public static Motor.WorkMode MOTOR_MODE = Motor.WorkMode.NORMAL_MODE;

}