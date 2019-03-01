package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class Gamepad {

    //port that is given to xbox controller object
    public static int mGamepadPortNumber;

    private XboxController mXboxController;

    //boolean states of buttons that can be called in the robot class to be used as needed..
    public boolean A_Button_State = false;
    public boolean B_Button_State = false;
    public boolean X_Button_State = false;
    public boolean Y_Button_State = false;
    public boolean Left_Bumper_State = false;
    public boolean Right_Bumper_State = false;
    public boolean Right_Stick_Down_State = false;

    //double states of the sticks and triggers to be called in the robot class to be used as needed.
    public double Left_Stick_X_Axis_State = 0;
    public double Left_Stick_Y_Axis_State = 0;
    public double Right_Stick_X_Axis_State = 0;
    public double Right_Stick_Y_Axis_State = 0;
    public double Left_Trigger_Axis_State = 0;
    public double Right_Trigger_Axis_State = 0;

    //State of the DPAD to be called in the robot class to be used as needed
    public double DPAD_State = -1;

    //methods
    public void putButtonStates() {
        putGamepadButtonStates();
        putGamepadAxisStates();
        putGamepadDPADState();
    }

    public Gamepad(int gamepadPortNumber) {
        mGamepadPortNumber = gamepadPortNumber;
        mXboxController = new XboxController(gamepadPortNumber);
    }

    public void putGamepadButtonStates() {
        A_Button_State = mXboxController.getAButtonPressed();
        B_Button_State = mXboxController.getBButtonPressed();
        X_Button_State = mXboxController.getXButtonPressed();
        Y_Button_State = mXboxController.getYButtonPressed();
        Left_Bumper_State = mXboxController.getBumper(Hand.kLeft);
        Right_Bumper_State = mXboxController.getBumper(Hand.kRight);
        Right_Stick_Down_State = mXboxController.getStickButton(Hand.kRight);
    }

    public void putGamepadAxisStates() {
        Left_Stick_X_Axis_State = mXboxController.getX(Hand.kLeft);
        Left_Stick_Y_Axis_State = mXboxController.getY(Hand.kLeft);
        Right_Stick_X_Axis_State = mXboxController.getX(Hand.kRight);
        Right_Stick_Y_Axis_State  = mXboxController.getY(Hand.kRight);
        Left_Trigger_Axis_State = mXboxController.getTriggerAxis(Hand.kLeft);
        Right_Trigger_Axis_State = mXboxController.getTriggerAxis(Hand.kRight);
    }

    public void putGamepadDPADState() {
        DPAD_State = mXboxController.getPOV();
    }

}