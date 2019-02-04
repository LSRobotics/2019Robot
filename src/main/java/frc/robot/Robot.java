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

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.PIDSourceType;

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

  public static double TargetAngle;

  public static DecimalFormat mDecimalFormat;

  public static GyroSensor mGyroSensor;
  public GyroPIDController gyroPIDController;
  
  public static double gyroAngle;

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
    initializeGyroSensor();
    initializeGyroPIDController();
    
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
    updateSensors();
    updateButtonStates();
    updateRobotTurnDegree(Gamepad.DPAD_State);
    if(TargetAngle != -1) {
      if(gyroPIDController.onTarget()) {
        TargetAngle = -1;
      // int rotateDistance = Math.floorMod((int)(gyroAngle - TargetAngle), 360);
      // SmartDashboard.putNumber("rotate Distance angle", rotateDistance);
      // if(rotateDistance <= 5 || Gamepad.Y_Button_State) {
      //   TargetAngle = -1;
      //   mDifferentialDrive.stopMotor();
      // }
      // else {
      //   if(rotateDistance >= 180) {
      //     mDifferentialDrive.tankDrive(.4, -.4); //TODO check max speed and minimum precision needed
      //   }
      //   else {
      //     mDifferentialDrive.tankDrive(-.4, .4);
      //   }
      // }
      }
      else {
        gyroPIDController.calculate();
        mDifferentialDrive.tankDrive(mLeftSpeed, mRightSpeed);
      }
    }
    else {
      updateSpeedLimit(Gamepad.Right_Bumper_State, Gamepad.Left_Bumper_State, Gamepad.B_Button_State);
      updateDrive(Gamepad.Left_Trigger_Axis_State, Gamepad.Right_Trigger_Axis_State, Gamepad.Left_Stick_Y_Axis_State, Gamepad.Right_Stick_Y_Axis_State);
    }

    updateSmartDashboard();
    
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

  private static void updateSpeedLimit(boolean increaseSpeedButtonState, boolean decreaseSpeedButtonState, boolean stopButtonState) {
    if(stopButtonState) {
      mSpeedLimit = 0;
    }

    if(increaseSpeedButtonState && !decreaseSpeedButtonState) {
      mSpeedLimit = Math.min(mSpeedLimit + .2, 1);
    }
    if(!increaseSpeedButtonState && decreaseSpeedButtonState) {
      mSpeedLimit = Math.max(mSpeedLimit - .2, 0);
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
    mGamepad.putButtonStates();
  }

  public void updateSensors() {
    gyroAngle = mGyroSensor.getAngle();
  }

  //else contains true tank drive
  public void updateDrive(double leftTriggerAxisState, double rightTriggerAxisState, double leftStickYAxisState, double rightStickYAxisState) {
    if(Math.abs(leftStickYAxisState) < .1 && Math.abs(rightStickYAxisState) < .1) {
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

      mDifferentialDrive.tankDrive(-mLeftSpeed, -mRightSpeed);
    }
  }

    public void updateRobotTurnDegree(Double DPADDegree) {
      if(DPADDegree != -1) {
        TargetAngle = DPADDegree;
      }
    }

    public void updateSmartDashboard() {
    SmartDashboard.putNumber("Controller Left Trigger Axis State", Gamepad.Left_Trigger_Axis_State);
    SmartDashboard.putNumber("Controller Right Trigger Axis State", Gamepad.Right_Trigger_Axis_State);
    SmartDashboard.putNumber("Controller Left Stick Axis State", Gamepad.Left_Stick_Y_Axis_State);
    SmartDashboard.putNumber("Controller Right Stick Axis State", Gamepad.Right_Stick_Y_Axis_State);
    SmartDashboard.putNumber("Robot Turn Degree", TargetAngle);
    SmartDashboard.putNumber("Gyro Angle", gyroAngle);
    }

    private class gyroPIDOutput implements PIDOutput {

      public void pidWrite(double output) {
        mLeftSpeed = output;
        mRightSpeed = -output;
      }
    }

    private class gyroPIDSource implements PIDSource {

      public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;
      }

      public double pidGet() {
        return gyroAngle;
      }

      public void setPIDSourceType(PIDSourceType pidsource) {
        
      }
    }

    public void initializeGyroPIDController() {
      gyroPIDController = new GyroPIDController(.1, 0, 0, 0, new gyroPIDSource(), new gyroPIDOutput());
      gyroPIDController.setPercentTolerance(1.0);
      gyroPIDController.setSetpoint(0);
    }
}
