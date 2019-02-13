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
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import java.text.DecimalFormat;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kAuto2 = "Auto 1";
  private static final String kAuto1 = "Auto 2";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public Gamepad Gamepad1;
  public Gamepad Gamepad2;
  
  public static WPI_TalonSRX mFrontLeft;
  public static WPI_TalonSRX mRearLeft;
  public static WPI_TalonSRX mFrontRight;
  public static WPI_TalonSRX mRearRight;

  public static SpeedControllerGroup mLeftSpeedControllers;
  public static SpeedControllerGroup mRightSpeedControllers;

  public static DifferentialDrive mDifferentialDrive;

  public static double mSpeedLimit;
  public static double mLeftSpeed;
  public static double mRightSpeed;

  public static boolean inReverseDrive = false;

  public static double TargetAngle = -1;

  public static DecimalFormat mDecimalFormat;

  public static GyroSensor mGyroSensor;
  public GyroPIDController gyroPIDController;

  public static UltrasonicSensor mUltrasonicSensor;
  public UltrasonicPIDController ultrasonicPIDController;
  public static double ultrasonicDistance;
  
  public static double gyroAngle;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Auto 1", kAuto1);
    m_chooser.addOption("Auto 2", kAuto2);
    SmartDashboard.putData("Auto choices", m_chooser);

    initializeGamepad();
    initializeMotorControllers();
    initializeSpeedLimit();
    initializeDifferentialDrive();
    initializeTankDrive();
    initializeGyroSensor();
    initializeGyroPIDController();
    initializeUltrasonicSensor();

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
      case kAuto1:
        // Put custom auto code here
        break;
      case kAuto2:
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
    updateSensors();
    updateButtonStates();
    // updateReverseDriveValue(Gamepad1.Right_Bumper_State);
    updateRobotTurnDegree(Gamepad.DPAD_State);
    if(TargetAngle != -1) {
      if(gyroPIDController.onTarget()) {
        TargetAngle = -1;
      }
      else {
        gyroPIDController.calculate();
        mDifferentialDrive.tankDrive(mLeftSpeed, mRightSpeed);
      }
    }
    else {
      updateSpeedLimit(Gamepad1.B_Button_State);
      updateDrive(Gamepad1.Left_Trigger_Axis_State, Gamepad1.Right_Trigger_Axis_State, Gamepad1.Left_Stick_Y_Axis_State, Gamepad1.Right_Stick_Y_Axis_State, inReverseDrive);
    }

    updateSmartDashboard();
    
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {

  }

  private void initializeGamepad() {
    Gamepad1 = new Gamepad(0);
    Gamepad1.putButtonStates();
  }

  private static void initializeMotorControllers() {
    mFrontLeft = new WPI_TalonSRX(Statics.Front_Left_CAN_ID);
    mRearLeft = new WPI_TalonSRX(Statics.Rear_Left_CAN_ID);
    mFrontRight = new WPI_TalonSRX(Statics.Front_Right_CAN_ID);
    mRearRight = new WPI_TalonSRX(Statics.Rear_Right_CAN_ID);

    mFrontLeft.configFactoryDefault();
    mRearLeft.configFactoryDefault();
    mFrontRight.configFactoryDefault();
    mRearRight.configFactoryDefault();
  }

  private static void initializeSpeedLimit() {
    mSpeedLimit = 1;
  }

  private static void initializeGyroSensor() {
    mGyroSensor = new GyroSensor();
    mGyroSensor.initializeGyroSensor();
  }

  private static void updateSpeedLimit(boolean speedButtonState) {
    if(speedButtonState) {
      mSpeedLimit += .2;
      if (mSpeedLimit > 1) mSpeedLimit = 0;
    }
    SmartDashboard.putNumber("Speed Limit", mSpeedLimit);
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
    Gamepad1.putButtonStates();
  }

  public void updateSensors() {
    gyroAngle = mGyroSensor.getAngle();
    ultrasonicDistance = mUltrasonicSensor.getRangeInches();
  }

  //else contains true tank drive
  public void updateDrive(double leftTriggerAxisState, double rightTriggerAxisState, double leftStickYAxisState, double rightStickYAxisState, boolean reverseDrive) {
    if(Math.abs(leftStickYAxisState) < .1 && Math.abs(rightStickYAxisState) < .1) {
      mLeftSpeed = leftTriggerAxisState * leftTriggerAxisState; //squared
      mRightSpeed = rightTriggerAxisState * rightTriggerAxisState; //squared

      if (mLeftSpeed > mRightSpeed) {
        if (mLeftSpeed > mSpeedLimit) {
          mLeftSpeed = mSpeedLimit;
        }
        // if(reverseDrive) {
          mDifferentialDrive.tankDrive(mLeftSpeed, mLeftSpeed);
        // }
        // else {
        //   mDifferentialDrive.tankDrive(-mLeftSpeed, -mLeftSpeed);
        // }
      }
      else if (mRightSpeed > mLeftSpeed) {
        if (mRightSpeed > mSpeedLimit) {
          mRightSpeed = mSpeedLimit;
        }
        // if(reverseDrive) {
          mDifferentialDrive.tankDrive(-mRightSpeed, -mRightSpeed);
        // }
        // else {
        //   mDifferentialDrive.tankDrive(mRightSpeed, mRightSpeed);
        // }
      }
    }
    else {
      mLeftSpeed = leftStickYAxisState * Math.abs(leftStickYAxisState);
      mRightSpeed = rightStickYAxisState * Math.abs(rightStickYAxisState);

      if(Math.abs(mLeftSpeed) > mSpeedLimit) {
        if(mLeftSpeed < 0) {
          mLeftSpeed = -mSpeedLimit;
        }
        else {
          mLeftSpeed = mSpeedLimit;
        }
      }
      if(Math.abs(mRightSpeed) > mSpeedLimit) {
        if(mRightSpeed < 0) {
          mRightSpeed = -mSpeedLimit;
        }
        else {
          mRightSpeed = mSpeedLimit;
        }
      }

      // if(reverseDrive) {
        mDifferentialDrive.tankDrive(mLeftSpeed, mRightSpeed);
      // }
      // else {
      //   mDifferentialDrive.tankDrive(-mLeftSpeed, -mRightSpeed);
      // }
    }
  }

    public void updateRobotTurnDegree(Double DPADDegree) {
      if(DPADDegree != -1) {
        TargetAngle = DPADDegree;
        gyroPIDController.setSetpoint(TargetAngle);
      }
    }

    // public void updateReverseDriveValue(boolean gamepad1RightBumperValue) {
    //   if(!inReverseDrive && gamepad1RightBumperValue) {
    //     inReverseDrive = true;
    //   }
    //   if(inReverseDrive && gamepad1RightBumperValue) {
    //     inReverseDrive = false;
    //   }
    // }

    public void updateSmartDashboard() {
    SmartDashboard.putNumber("Controller Left Trigger Axis State", Gamepad1.Left_Trigger_Axis_State);
    SmartDashboard.putNumber("Controller Right Trigger Axis State", Gamepad1.Right_Trigger_Axis_State);
    SmartDashboard.putNumber("Controller Left Stick Axis State", Gamepad1.Left_Stick_Y_Axis_State);
    SmartDashboard.putNumber("Controller Right Stick Axis State", Gamepad1.Right_Stick_Y_Axis_State);
    SmartDashboard.putNumber("Robot Turn Degree", TargetAngle);
    SmartDashboard.putNumber("Gyro Angle", gyroAngle);
    SmartDashboard.putNumber("LeftSpeed", mLeftSpeed);
    SmartDashboard.putNumber("RightSpeed", mRightSpeed);
    SmartDashboard.putNumber("Calculated PID Controller Value", gyroPIDController.get());
    SmartDashboard.putNumber("Ultrasonic Distance", ultrasonicDistance);
    SmartDashboard.putBoolean("Ultrasonic enabled", mUltrasonicSensor.getActualSensor().isEnabled());
    SmartDashboard.putBoolean("is range valid", mUltrasonicSensor.ultrasonicSensor.isRangeValid());
    } 

    private class gyroPIDOutput implements PIDOutput {

      public void pidWrite(double output) {
        mLeftSpeed = output;
        mRightSpeed = -output; //todo check this
      }
    }

    public void initializeGyroPIDController() {
      gyroPIDController = new GyroPIDController(.1, 0, 0, 0, mGyroSensor.getActualGyroSensor(), new gyroPIDOutput());
      gyroPIDController.setPercentTolerance(2);
      gyroPIDController.enable();
    }

    public void initializeUltrasonicSensor() {
      mUltrasonicSensor = new UltrasonicSensor();
      mUltrasonicSensor.initializeUltrasonic();
    }

    private class UltrasonicPIDOutput implements PIDOutput {
      
      public void pidWrite(double output) {
        mLeftSpeed = output;
        mRightSpeed = -output;
      }
    }

    public void initializeUltrasonicPIDController() {
      ultrasonicPIDController = new UltrasonicPIDController(.1, 0, 0, 0, mUltrasonicSensor.getActualSensor(), new UltrasonicPIDOutput());
      ultrasonicPIDController.setPercentTolerance(2);
      ultrasonicPIDController.enable();
    }
}
