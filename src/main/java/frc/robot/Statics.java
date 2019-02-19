package frc.robot;

public class Statics {
    
    public static int Front_Left_CAN_ID = 10;
    public static int Rear_Left_CAN_ID = 11;

    public static int Front_Right_CAN_ID = 6;
    public static int Rear_Right_CAN_ID = 7;

    public static int Low_Cargo_CAN_ID = 8;
    public static int High_Cargo_CAN_ID = 9;

    public static int Left_Over_Roller_CAN_ID = 4;
    public static int Right_Over_Roller_CAN_ID = 3;
    public static int Front_Over_Roller_CAN_ID = 5;

    public static int Left_Ultrasonic_PingChannel = 0;
    public static int Left_Ultrasonic_EchoChannel = 1;
    public static int Right_Ultrasonic_PingChannel = 2;
    public static int Right_Ultrasonic_EchoChannel = 3;
    public static int Cargo_Ultrasonic_PingChannel = 4;
    public static int Cargo_Ultrasonic_EchoChannel = 5;

    public static int LIDAR_Sensor_Channel = 6;

    public final static double PID_GYRO_TOLERANCE = 2;
    public final static double GYRO_P = .25;
    public final static double GYRO_I = 0;
    public final static double GYRO_D = .15;
    public final static double GYRO_F = 0;

    public final static double GAMEPAD_AXIS_TOLERANCE = 0.1;

    public final static double LIDAR_CALIBRATION_OFFSET = -18; //TODO figure out what this value is or if we need it at all.

    public final static double CM_TO_IN = 0.393701;
    
    public static double Low_Cargo_Motor_Speed = .5;
    public static double High_Cargo_Motor_Speed = .5;
    public static double Left_Over_Roller_Motor_Speed = .5;
    public static double Right_Over_Roller_Motor_Speed = .5;
    public static double Front_Over_Rollor_Motor_Speed = .5;

    public static int left_Over_Roller_Encoder_A_Channel = 0;
    public static int left_Over_Roller_Encoder_B_Channel = 1;

    public static int Gorgon_Solenoid_Forward_Channel = 0;
    public static int Gorgon_Solenoid_Reverse_Channel = 1;

    public static int Scooter_Climb_CAN_ID = 1;
    public static int Climb_Wheels_CAN_ID = 2;
    public static int Robot_Climb_Solenoid_Forward_Channel = 2;
    public static int Robot_Climb_Solenoid_Reverse_Channel = 3;

    public static int Limit_Switch_Channel = 8;
    public static double Cargo_Hold_Distance = 6;
}