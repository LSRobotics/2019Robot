package frc.robot.software;

public class Statics {
    

    //CAN Devices
    public final static int CHASSIS_L1 = 10;
    public final static int CHASSIS_L2 = 11;

    public final static int CHASSIS_R1 = 6;
    public final static int CHASSIS_R2 = 7;

    public final static int LOW_CARGO = 8;
    public final static int HIGH_CARGO = 9;

    public final static int Left_Over_Roller_CAN_ID = 4;
    public final static int Right_Over_Roller_CAN_ID = 1;
    public final static int UNUSED_CAN_ID = 5;

    public final static int Cargo_Ultrasonic_PingChannel = 0;
    public final static int Cargo_Ultrasonic_EchoChannel = 1;

    public final static int Scooter_Climb_CAN_ID = 3;
    public final static int Climb_Wheels_CAN_ID = 2;
    public final static int PCM_CLIMB_F = 2;
    public final static int PCM_CLIMB_R = 3;

    public final static int LIMIT_SWITCH = 2;

    public final static int OVERROLLER_ENCODER_L_A = 0;
    public final static int OVERROLLER_ENCODER_L_B = 1;

    public final static int PCM_GORGON_F = 0;
    public final static int PCM_GORGON_R = 1;


    public final static double GAMEPAD_AXIS_TOLERANCE = 0.1;

    public final static double CM_TO_IN = 0.393701;
    
    public final static double Low_Cargo_Motor_Speed = .5;
    public final static double Low_Cargo_Shoot_Motor_Speed = 1;
    public final static double Low_Cargo_Shoot_High_Motor_Speed = .5;
    public final static double High_Cargo_Motor_Speed = 0.7;

    public final static double CARGO_HOLD_DISTANCE = 6;

    public final static int MIN_TO_SEC = 60;
    public final static int SEC_TO_INTERVAL = 50;

    public final static double SPEED_LIMIT = .9;
    public final static double High_Cargo_Intake_Motor_Speed = .5;
    public final static int Light_PWM_Port = 0;

    public final static int IR_Sensor_Port = 3;
    public final static int Winch_CAN_ID = 4;

    public final static int TIME_PER_360 = 1700;
}