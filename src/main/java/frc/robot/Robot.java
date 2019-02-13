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

//TODO Ultrasonic paths
//TODO controls
//TODO Switch to Navx
//TODO Second Gamepad

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
    m_autoSelected = SmartDashboard.getString("Auto Selector", kAuto1);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kAuto1:
        // TODO Put custom auto code here
        break;
      case kAuto2:
        // TODO Put custom auto code here
      default:
        // TODO Put default auto code here
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
    // TODO updateReverseDriveValue(Gamepad1.Right_Bumper_State);
    updateRobotTurnDegree();
    updateMove();
    updateSmartDashboard();
    
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {

  }

  private void updateMove() {
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
      updateSpeedLimit();
      updateDrive();
    }
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

  private void updateSpeedLimit() {
    if(Gamepad1.B_Button_State) {
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
  public void updateDrive() {
    if(Math.abs(Gamepad1.Left_Stick_Y_Axis_State) < .1 && Math.abs(Gamepad1.Right_Stick_Y_Axis_State) < .1) {
      mLeftSpeed = Math.pow(Gamepad1.Left_Trigger_Axis_State, 2); //squared
      mRightSpeed = Math.pow(Gamepad1.Right_Trigger_Axis_State, 2); //squared

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
      mLeftSpeed = Gamepad1.Left_Stick_Y_Axis_State * Math.abs(Gamepad1.Left_Stick_Y_Axis_State);
      mRightSpeed = Gamepad1.Right_Stick_Y_Axis_State * Math.abs(Gamepad1.Right_Stick_Y_Axis_State);

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

    public void updateRobotTurnDegree() {
      if(Gamepad.DPAD_State != -1) {
        TargetAngle = Gamepad.DPAD_State;
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
    SmartDashboard.putBoolean("Range valid", UltrasonicSensor.ultrasonicSensor.isRangeValid());
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
