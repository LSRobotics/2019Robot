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
//TODO Lights
//TODO SmartDashboard Design

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

  public Gamepad ChassisGamepad;
  public Gamepad MechanismsGamepad;
  
  public static WPI_TalonSRX mFrontLeft;
  public static WPI_TalonSRX mRearLeft;
  public static WPI_TalonSRX mFrontRight;
  public static WPI_TalonSRX mRearRight;

  public static SpeedControllerGroup mLeftSpeedControllers;
  public static SpeedControllerGroup mRightSpeedControllers;

  public static DifferentialDrive mDifferentialDrive;

  public static double mLeftSpeed;
  public static double mRightSpeed;

  public static boolean reverseDrive = false;

  public static double TargetAngle = -1;

  public static DecimalFormat mDecimalFormat;

  public static GyroSensor mGyroSensor;
  public GyroPIDController gyroPIDController;

  public static UltrasonicSensor mLeftUltrasonicSensor;
  public static UltrasonicSensor mRightUltrasonicSensor;
  public UltrasonicPIDController ultrasonicPIDController;
  public static double leftUltrasonicDistance;
  public static double rightUltrasonicDistance;
  
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
    initializeDifferentialDrive();
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
      default:
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
      updateDrive();
    }
  }

  private void initializeGamepad() {
    ChassisGamepad = new Gamepad(0);
    ChassisGamepad.putButtonStates();
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

  private static void initializeGyroSensor() {
    mGyroSensor = new GyroSensor();
    mGyroSensor.initializeGyroSensor();
  }

  private static void initializeDifferentialDrive() {
    mLeftSpeedControllers = new SpeedControllerGroup(mFrontLeft, mRearLeft);
    mRightSpeedControllers = new SpeedControllerGroup(mFrontRight, mRearRight);

    mDifferentialDrive = new DifferentialDrive(mLeftSpeedControllers, mRightSpeedControllers);
  }

  public void updateButtonStates() {
    ChassisGamepad.putButtonStates();
    if(ChassisGamepad.Right_Bumper_State) reverseDrive = !reverseDrive;
  }

  public void updateSensors() {
    gyroAngle = mGyroSensor.getAngle();
    leftUltrasonicDistance = mLeftUltrasonicSensor.getRangeInches();
    rightUltrasonicDistance = mRightUltrasonicSensor.getRangeInches();
  }

  //else contains true tank drive
  public void updateDrive() {
    if(Math.abs(ChassisGamepad.Left_Stick_Y_Axis_State) < .1 && Math.abs(ChassisGamepad.Right_Stick_Y_Axis_State) < .1) {
      mLeftSpeed = Math.pow(ChassisGamepad.Left_Trigger_Axis_State, 2); //squared
      mRightSpeed = Math.pow(ChassisGamepad.Right_Trigger_Axis_State, 2); //squared
      if (mLeftSpeed > mRightSpeed) {
        mRightSpeed = mLeftSpeed;
      }
      else if (mRightSpeed > mLeftSpeed) {
        mLeftSpeed = mRightSpeed;
      }
    }


    else {
      mLeftSpeed = ChassisGamepad.Left_Stick_Y_Axis_State * Math.abs(ChassisGamepad.Left_Stick_Y_Axis_State);
      mRightSpeed = ChassisGamepad.Right_Stick_Y_Axis_State * Math.abs(ChassisGamepad.Right_Stick_Y_Axis_State);
      
    }
    if (reverseDrive) {
      mLeftSpeed = -mLeftSpeed;
      mRightSpeed = -mRightSpeed;
    } 
    mDifferentialDrive.tankDrive(mLeftSpeed, mRightSpeed);
  }

  public void updateRobotTurnDegree() {
    if(ChassisGamepad.DPAD_State != -1) {
      TargetAngle = ChassisGamepad.DPAD_State;
      gyroPIDController.setSetpoint(TargetAngle);
    }
  }

    public void updateSmartDashboard() {
    //all Chassis Gamepad Values
    SmartDashboard.putNumber("Chassis Controller Left Trigger Axis State", ChassisGamepad.Left_Trigger_Axis_State);
    SmartDashboard.putNumber("Chassis Controller Right Trigger Axis State", ChassisGamepad.Right_Trigger_Axis_State);
    SmartDashboard.putNumber("Chassis Controller Left Stick  Y Axis State", ChassisGamepad.Left_Stick_Y_Axis_State);
    SmartDashboard.putNumber("Chassis Controller Right Stick Y Axis State", ChassisGamepad.Right_Stick_Y_Axis_State);
    SmartDashboard.putBoolean("Chassis Controller A Button State", ChassisGamepad.A_Button_State);
    SmartDashboard.putBoolean("Chassis Controller B Button State", ChassisGamepad.B_Button_State);
    SmartDashboard.putBoolean("Chassis Controller X Button State", ChassisGamepad.X_Button_State);
    SmartDashboard.putBoolean("Chassis Controller Y Button State", ChassisGamepad.Y_Button_State);
    SmartDashboard.putBoolean("Chassis Controller Left Bumper State", ChassisGamepad.Left_Bumper_State);
    SmartDashboard.putBoolean("Chassis Controller Right Bumper State", ChassisGamepad.Right_Bumper_State);

    //all Mechanism Gamepad Values
    SmartDashboard.putNumber("Mechanisms Controller Left Trigger Axis State", MechanismsGamepad.Left_Trigger_Axis_State);
    SmartDashboard.putNumber("Mechanisms Controller Right Trigger Axis State", MechanismsGamepad.Right_Trigger_Axis_State);
    SmartDashboard.putNumber("Mechanisms Controller Left Stick Y Axis State", MechanismsGamepad.Left_Stick_Y_Axis_State);
    SmartDashboard.putNumber("Mechanisms Controller Right Stick Y Axis State", MechanismsGamepad.Right_Stick_Y_Axis_State);
    SmartDashboard.putBoolean("MechanismS Controller A Button State", MechanismsGamepad.A_Button_State);
    SmartDashboard.putBoolean("Mechanisms Controller B Button State", MechanismsGamepad.B_Button_State);
    SmartDashboard.putBoolean("Mechanisms Controller X Button State", MechanismsGamepad.X_Button_State);
    SmartDashboard.putBoolean("Mechanisms Controller Y Button State", MechanismsGamepad.Y_Button_State);
    SmartDashboard.putBoolean("Mechanisms Controller Left Bumper State", MechanismsGamepad.Left_Bumper_State);
    SmartDashboard.putBoolean("Mechanisms Controller Right Bumper State", MechanismsGamepad.Right_Bumper_State);

    //all other values
    SmartDashboard.putNumber("Currrent Target Angle in Degrees", TargetAngle);
    SmartDashboard.putNumber("Gyro Angle in Degrees", gyroAngle);
    SmartDashboard.putNumber("Left Tank Drive Speed", mLeftSpeed);
    SmartDashboard.putNumber("Right Tank Drive Speed", mRightSpeed);
    SmartDashboard.putNumber("Calculated Gyro PID Controller Output Value", gyroPIDController.get());
    SmartDashboard.putNumber("Calculated Ultrasonic PID Controller Output Value", ultrasonicPIDController.get());
    SmartDashboard.putNumber("Left Ultrasonic Distance in inches", leftUltrasonicDistance);
    SmartDashboard.putNumber("Right Ultrasonic Distance in inches", rightUltrasonicDistance);
    SmartDashboard.putBoolean("Validity of Left Ultrasonic Range", mLeftUltrasonicSensor.isRangeValid());
    SmartDashboard.putBoolean("Validity of Right Ultrasonic Range", mRightUltrasonicSensor.isRangeValid());
    } 

    private class gyroPIDOutput implements PIDOutput {

      public void pidWrite(double output) {
        mLeftSpeed = output;
        mRightSpeed = -output; //TODO check this
      }
    }

    public void initializeGyroPIDController() {
      gyroPIDController = new GyroPIDController(.1, 0, 0, 0, mGyroSensor.getActualGyroSensor(), new gyroPIDOutput());
      gyroPIDController.setPercentTolerance(2);
      gyroPIDController.enable();
    }

    public void initializeUltrasonicSensor() {
      mLeftUltrasonicSensor = new UltrasonicSensor(Statics.Left_Ultrasonic_PingChannel, Statics.Left_Ultrasonic_EchoChannel);
      mRightUltrasonicSensor = new UltrasonicSensor(Statics.Right_Ultrasonic_PingChannel, Statics.Right_Ultrasonic_EchoChannel);
    }

    private class UltrasonicPIDOutput implements PIDOutput {
      
      public void pidWrite(double output) {
        mLeftSpeed = output;
        mRightSpeed = -output;
      }
    }

    public void initializeUltrasonicPIDController() {
      ultrasonicPIDController = new UltrasonicPIDController(.1, 0, 0, 0, mLeftUltrasonicSensor.getActualSensor(), new UltrasonicPIDOutput());
      ultrasonicPIDController.setPercentTolerance(2);
      ultrasonicPIDController.enable();
    }
}
