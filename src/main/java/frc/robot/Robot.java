/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import java.text.DecimalFormat;

/**
 * The VM is configured to automatically run 0this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public static Gamepad mGamepad;
  
  public static VictorSP mFrontLeft;
  public static VictorSP mMiddleLeft;
  public static VictorSP mRearLeft;
  public static VictorSP mFrontRight;
  public static VictorSP mMiddleRight;
  public static VictorSP mRearRight;

  public static SpeedControllerGroup mLeftSpeedControllers;
  public static SpeedControllerGroup mRightSpeedControllers;

  public static DifferentialDrive mDifferentialDrive;

  public static double mSpeedLimit;
  public static double mLeftSpeed;
  public static double mRightSpeed;

  public static DecimalFormat mDecimalFormat;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    initializeGamepad();
    initializeMotorControllers();
    initializeSpeedLimit();
    initializeDifferentialDrive();
    initializeTankDrive();
    
    mDecimalFormat = (DecimalFormat) DecimalFormat.getNumberInstance();
    mDecimalFormat.applyPattern("0.##");

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    updateButtonStates();
    updateSpeedLimit(Gamepad.Right_Bumper_State, Gamepad.Left_Bumper_State, Gamepad.B_Button_State);
    updateDrive(Gamepad.Left_Trigger_Axis_State, Gamepad.Right_Trigger_Axis_State, Gamepad.Left_Stick_Y_Axis_State, Gamepad.Right_Stick_Y_Axis_State);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  private static void initializeGamepad() {
    mGamepad = new Gamepad();
    mGamepad.putButtonStates();
  }

  private static void initializeMotorControllers() {
    mFrontLeft = new VictorSP(Statics.Front_Left_Channel);
    //mMiddleLeft = new VictorSP(Statics.Middle_Left_Channel);
    mRearLeft = new VictorSP(Statics.Rear_Left_Channel);
    mFrontRight = new VictorSP(Statics.Front_Right_Channel);
    //mMiddleRight = new VictorSP(Statics.Middle_Right_Channel);
    mRearRight = new VictorSP(Statics.Rear_Right_Channel);
  }

  private static void initializeSpeedLimit() {
    mSpeedLimit = .1;
  }

  private static void updateSpeedLimit(boolean increaseSpeedButtonState, boolean decreaseSpeedButtonState, boolean stopButtonState) {
    if(stopButtonState) {
      mSpeedLimit = 0;
    }

    if(increaseSpeedButtonState && !decreaseSpeedButtonState) {
      mSpeedLimit = Math.max(mSpeedLimit + .1, 0);
    }
    if(!increaseSpeedButtonState && decreaseSpeedButtonState) {
      mSpeedLimit = Math.max(mSpeedLimit - .1, 0);
    }
    SmartDashboard.putString("Speed Limit", mDecimalFormat.format(mSpeedLimit));
  }

  private static void initializeDifferentialDrive() {
    mLeftSpeedControllers = new SpeedControllerGroup(mFrontLeft, mRearLeft);
    mRightSpeedControllers = new SpeedControllerGroup(mFrontRight, mRearRight);

    mDifferentialDrive = new DifferentialDrive(mLeftSpeedControllers, mRightSpeedControllers);
  }

  private static void initializeTankDrive() {
    mDifferentialDrive.tankDrive(mSpeedLimit, mSpeedLimit);
  }

  public void updateButtonStates() {
    mGamepad.putButtonStates();
  }

  //else contains true tank drive
  public void updateDrive(double leftTriggerAxisState, double rightTriggerAxisState, double leftStickYAxisState, double rightStickYAxisState) {
    if(Math.abs(leftStickYAxisState) < .1 && Math.abs(rightStickYAxisState) < .1) { //TODO mess with the stick values to figure out when Sticks are not being used. 
      mLeftSpeed = leftTriggerAxisState * leftTriggerAxisState; //squared
      mRightSpeed = rightTriggerAxisState * rightTriggerAxisState; //squared

      if (mLeftSpeed > mRightSpeed) {
        if (mLeftSpeed > mSpeedLimit) {
          mLeftSpeed = mSpeedLimit;
        }
        mDifferentialDrive.tankDrive(-mLeftSpeed, -mLeftSpeed);
      }
      else if (mRightSpeed > mLeftSpeed) {
        if (mRightSpeed > mSpeedLimit) {
          mRightSpeed = mSpeedLimit;
        }
        mDifferentialDrive.tankDrive(mRightSpeed, mRightSpeed);
      }
    }
    else {
      mLeftSpeed = leftStickYAxisState * Math.abs(leftStickYAxisState);
      mRightSpeed = rightStickYAxisState * Math.abs(rightStickYAxisState);

      mDifferentialDrive.tankDrive(mLeftSpeed, mRightSpeed);
    }
  }

}
