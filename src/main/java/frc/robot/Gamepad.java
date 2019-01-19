package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;

public class Gamepad {

    //port that is given to xbox controller object
    public static int mGamepadPortNumber = 0;

    private static XboxController mXboxController = new XboxController(mGamepadPortNumber);

    //boolean states of buttons that can be called in the robot class to be used as needed..
    public static boolean A_Button_State = false;
    public static boolean B_Button_State = false;
    public static boolean X_Button_State = false;
    public static boolean Y_Button_State = false;
    public static boolean Left_Bumper_State = false;
    public static boolean Right_Bumper_State = false;
    public static boolean Left_Stick_Down_State = false;
    public static boolean Right_Stick_Down_State = false;
    public static boolean Start_Button_State = false;
    public static boolean Back_Button_State = false;

    //double states of the sticks and triggers to be called in the robot class to be used as needed.
    public static double Left_Stick_X_Axis_State = 0;
    public static double Left_Stick_Y_Axis_State = 0;
    public static double Right_Stick_X_Axis_State = 0;
    public static double Right_Stick_Y_Axis_State = 0;
    public static double Left_Trigger_Axis_State = 0;
    public static double Right_Trigger_Axis_State = 0;

    //State of the DPAD to be called in the robot class to be used as needed
    public static int DPAD_State = -1;

    //methods
    public void putNaturalState() {
        putGamepadButtonStates();
        putGamepadAxisStates();
        putGamepadDPADState();
    }

    public static void putGamepadButtonStates() {
        A_Button_State = mXboxController.getAButton();
        B_Button_State = mXboxController.getBButton();
        X_Button_State = mXboxController.getXButton();
        Y_Button_State = mXboxController.getYButton();
        Left_Bumper_State = mXboxController.getBumper(Hand.kLeft);
        Right_Bumper_State = mXboxController.getBumper(Hand.kRight);
        Left_Stick_Down_State = mXboxController.getStickButton(Hand.kLeft);
        Right_Stick_Down_State = mXboxController.getStickButton(Hand.kRight);
        Start_Button_State = mXboxController.getStartButton();
        Back_Button_State = mXboxController.getBackButton();
    }

    public static void putGamepadAxisStates() {
        Left_Stick_X_Axis_State = mXboxController.getX(Hand.kLeft);
        Left_Stick_Y_Axis_State = mXboxController.getY(Hand.kLeft);
        Right_Stick_X_Axis_State = mXboxController.getX(Hand.kRight);
        Right_Stick_Y_Axis_State  = mXboxController.getY(Hand.kRight);
        Left_Trigger_Axis_State = mXboxController.getTriggerAxis(Hand.kLeft);
        Right_Trigger_Axis_State = mXboxController.getTriggerAxis(Hand.kRight);
    }

    public static void putGamepadDPADState() {
        DPAD_State = mXboxController.getPOV();
    }

}