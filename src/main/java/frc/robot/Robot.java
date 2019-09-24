/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */

public class Robot extends TimedRobot {
  // private static final String kAuto2 = "Auto 1";
  // private static final String kAuto1 = "Auto 2";
  // private String m_autoSelected;
  // private final SendableChooser<String> m_chooser = new SendableChooser<>();

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

  public onGoingPath currentPath = null;
  public static int step = 0;

  public static GyroSensor mGyroSensor;
  public GyroPIDController gyroPIDController;

  public static CargoMode cargoMode = null;

  public static double cargoUltrasonicDistance;

  public static LIDARSensor mLIDARSensor;
  public LIDARPIDController lidarpidController;
  public double lidarDistance;

  public static boolean reverseAuto = false;
  
  public static double gyroAngle;

  public PixyCamera pixyCam;

  public static CargoMechanism cargoMechanism;

  public static OverRoller overRoller;

  public static Gorgon gorgon;

  public static RobotClimb climb;
  public static DigitalInput limitSwitch;

  public static Compressor mCompressor;

  public static boolean gorgonOpen = false;
  public static boolean climbOpen = false;

  public Lights lights;
  public double lightMode;
  public boolean ballCall;
  public boolean climbLights = false;

  public double speedLimiter = 1;
  public IRSensor irSensor;
  public Winch winch;

  //TODO switch camera feed when reversed is pressed

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  
  @Override
  public void robotInit() {
    // m_chooser.setDefaultOption("Auto 1", kAuto1);
    // m_chooser.addOption("Auto 2", kAuto2);
    // SmartDashboard.putData("Auto choices", m_chooser);
    
    mCompressor = new Compressor(0);
    mCompressor.setClosedLoopControl(true);

    initializeGamepad();
    initializeMotorControllers();
    initializeDifferentialDrive();
    initializeGyroSensor();
    initializeGyroPIDController();
    initializeLIDARSensor(); //TODO LIDAR because it wasn't tested before bag day.
    initializeLIDARPID();
    initializeCargoMechanism();
    initializeClimb();
    initializeGorgon();
    initializePixyCam();
    initializeOverRoller();
    initializeWinch();
    initializeLights();
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
    // TODO real initation m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kAuto1);
    // System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    updateSensors();
    updateButtonStates();
    updateTargetAngle();
    if (MechanismsGamepad.Right_Stick_Down_State) {
      currentPath = null;
      TargetAngle = -1;
      cargoMode = null;
      step = 0;
    }
    if (ChassisGamepad.Right_Bumper_State) {
      if (speedLimiter == 1) speedLimiter = .5;
      else speedLimiter = 1;
    }
    updateGorgon();
    updateCargoMechanism();
    updateClimb();
    updateOverRoller();
    updateMove();
    callForBall();
    updateWinch();
    updatePixyCam();
    updateLights();
    updateSmartDashboard();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    updateSensors();
    updateButtonStates();
    updateTargetAngle();
    if (MechanismsGamepad.Right_Stick_Down_State) {
      currentPath = null;
      TargetAngle = -1;
      cargoMode = null;
      step = 0;
    }
    if (ChassisGamepad.Right_Bumper_State) {
      if (speedLimiter == 1) speedLimiter = .5;
      else speedLimiter = 1;
    }
    updateGorgon();
    updateCargoMechanism();
    updateClimb();
    updateOverRoller();
    updateMove();
    callForBall();
    updateWinch();
    updatePixyCam();
    updateLights();
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
    else if(currentPath != null) {
      runPresetPaths();
    }
    else {
      updateDrive();
    }
  }

  private void initializeGamepad() {
    ChassisGamepad = new Gamepad(0);
    ChassisGamepad.putButtonStates();
    MechanismsGamepad = new Gamepad(1);
    MechanismsGamepad.putButtonStates();
  }

  public void initializeLights() {
    lights = new Lights();
    lights.initialize();
  }

  public void callForBall() {
    if(ChassisGamepad.A_Button_State) {
      if(ballCall) {
        ballCall = false;
      }
      else {
        ballCall = true;
      }
    }
  }

  public void updateLights() {
    if(MechanismsGamepad.Left_Bumper_State || lightMode == -.55) {
      lightMode = -.55;
    }
    else {
      if(ballCall) {
        lightMode = .93;
      }
      else {
        if (Math.abs(gyroAngle % 360) > 87 && Math.abs(gyroAngle % 360) < 93) {
          lightMode = .77;
        }
        else if (Math.abs(gyroAngle % 360) > 177 && Math.abs(gyroAngle % 360) < 183) {
          lightMode = .77;
        }
        else if (Math.abs(gyroAngle % 360) > 267 && Math.abs(gyroAngle % 360) < 273) {
          lightMode = .77;
        }
        else if (Math.abs(gyroAngle % 360) > 357 || Math.abs(gyroAngle % 360) < 3) {
          lightMode = .77;
        }
        else {
          if(cargoMechanism.ballCaptured()) {
            lightMode = .57;
          }
          else if(gorgonOpen) {
            lightMode = .63;
          }
          else {
            if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue) lightMode = .85;
            else lightMode = .61;
          }
        }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
      }
    }
    lights.lightChange(lightMode);
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

  public void initializePixyCam() {
    pixyCam = new PixyCamera();
    pixyCam.startPixyCam();
  }

  public void updatePixyCam() {
    if(ChassisGamepad.Left_Bumper_State) {
      pixyCam.changeCam();
    }
  }

  private static void initializeDifferentialDrive() {
    mLeftSpeedControllers = new SpeedControllerGroup(mFrontLeft, mRearLeft);
    mRightSpeedControllers = new SpeedControllerGroup(mFrontRight, mRearRight);
  
    mDifferentialDrive = new DifferentialDrive(mLeftSpeedControllers, mRightSpeedControllers);
  }

  public void updateButtonStates() {
    ChassisGamepad.putButtonStates();
    MechanismsGamepad.putButtonStates();
    if(ChassisGamepad.Right_Bumper_State) {
      reverseDrive = !reverseDrive;
    }
  }

  public void updateSensors() {
    gyroAngle = mGyroSensor.getAngle();
    cargoUltrasonicDistance = cargoMechanism.ultrasonicSensor.getRangeInches();
    lidarDistance = mLIDARSensor.getDistance();
  }

  //else contains true tank drive
  public void updateDrive() {
    if(Math.abs(ChassisGamepad.Left_Stick_Y_Axis_State) < Statics.GAMEPAD_AXIS_TOLERANCE && Math.abs(ChassisGamepad.Right_Stick_Y_Axis_State) < Statics.GAMEPAD_AXIS_TOLERANCE) {
      mLeftSpeed = Math.pow(ChassisGamepad.Left_Trigger_Axis_State, 3); //cubed
      mRightSpeed = Math.pow(ChassisGamepad.Right_Trigger_Axis_State, 3); //cubed
      if (mLeftSpeed > mRightSpeed) {
        mRightSpeed = mLeftSpeed;
      }
      else if (mRightSpeed > mLeftSpeed) {
        mRightSpeed = -mRightSpeed;
        mLeftSpeed = mRightSpeed;
      }
    }

    else {
      mLeftSpeed = Math.pow(ChassisGamepad.Left_Stick_Y_Axis_State, 3);
      mRightSpeed = Math.pow(ChassisGamepad.Right_Stick_Y_Axis_State, 3);
    }
    // if (reverseDrive) {
    //   mLeftSpeed = -mLeftSpeed;
    //   mRightSpeed = -mRightSpeed;
    // }
    mDifferentialDrive.tankDrive(-mLeftSpeed*Statics.SPEED_LIMIT*speedLimiter, -mRightSpeed*Statics.SPEED_LIMIT*speedLimiter);
  }

  public void updateTargetAngle() {
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
    SmartDashboard.putNumber("Chassis Controller D-PAD", ChassisGamepad.DPAD_State);

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
    SmartDashboard.putNumber("Mechanism Controller D-PAD", MechanismsGamepad.DPAD_State);
    SmartDashboard.putNumber("Left Motor Amp output", overRoller.leftOverRollerMotorController.getOutputCurrent());
    SmartDashboard.putNumber("Right Motor Amp output", overRoller.rightOverRollerMotorController.getOutputCurrent());
    SmartDashboard.putNumber("Front Left Motor", mFrontLeft.getOutputCurrent());
    SmartDashboard.putNumber("Front Right Motor", mFrontRight.getOutputCurrent());
    SmartDashboard.putNumber("Back Left Motor", mRearLeft.getOutputCurrent());
    SmartDashboard.putNumber("Back Right Motor", mRearRight.getOutputCurrent());
    
    //all other values
    SmartDashboard.putNumber("Currrent Target Angle in Degrees", TargetAngle);
    SmartDashboard.putNumber("Gyro Angle in Degrees", gyroAngle);
    SmartDashboard.putNumber("Left Tank Drive Speed", mLeftSpeed);
    SmartDashboard.putNumber("Right Tank Drive Speed", mRightSpeed);
    SmartDashboard.putBoolean("Cargo in Place", cargoMechanism.ultrasonicSensor.getRangeInches() < Statics.CARGO_HOLD_DISTANCE);
    SmartDashboard.putNumber("cargo sensor distance", cargoMechanism.ultrasonicSensor.getRangeInches());
    SmartDashboard.putBoolean("cargo sensor valid", cargoMechanism.ultrasonicSensor.isRangeValid());
    SmartDashboard.putBoolean("Compresor Running", mCompressor.getPressureSwitchValue());
    SmartDashboard.putNumber("Left Overroller Encoder", overRoller.getLeftEncoder());
    SmartDashboard.putNumber("Right Overroller Encoder", overRoller.getRightEncoder());
    SmartDashboard.putNumber("Winch Encoder", winch.getWinchEncoderValue());    
    } 

    private class gyroPIDOutput implements PIDOutput {

      public void pidWrite(double output) {
        mLeftSpeed = output;
        mRightSpeed = -output; 
      }
    }

    public void initializeGyroPIDController() {
      gyroPIDController = new GyroPIDController(Statics.GYRO_P, Statics.GYRO_I, Statics.GYRO_D, Statics.GYRO_F, mGyroSensor.getActualGyroSensor(), new gyroPIDOutput());
      gyroPIDController.setPercentTolerance(Statics.PID_GYRO_TOLERANCE);
      gyroPIDController.enable();
    }

    public void initializeLIDARSensor() {
      mLIDARSensor = new LIDARSensor(new DigitalInput(Statics.LIDAR_Sensor_Channel));
    }
    
    public void initializeIRSensor() {
      irSensor = new IRSensor(new DigitalInput(Statics.IR_Sensor_Port));
    }

    public void initializeLIDARPID() {
      lidarpidController = new LIDARPIDController(.1, 0, 0, 0, mLIDARSensor, new LIDARPIDOutput()); //TODO LIDAR make static
      lidarpidController.setPercentTolerance(1);
      lidarpidController.enable();
    }

    private class LIDARPIDOutput implements PIDOutput {

      public void pidWrite(double output) {
        mLeftSpeed = output;
        mRightSpeed = output;
      }
    }

    public void initializeCargoMechanism() {
      cargoMechanism = new CargoMechanism();
      cargoMechanism.initialize();
    }

    public void runPresetPaths() {
      if(irSensor.tapeDetected()) {
        if (step == 0) {
          gyroPIDController.setSetpoint(0);
          if(!gyroPIDController.onTarget()) {
            gyroPIDController.calculate();
            mDifferentialDrive.tankDrive(mLeftSpeed, mRightSpeed);
          }
          else {
            step++;
          }
        }
        if (step == 1) {
          if(lidarDistance > 2) {
            lidarpidController.setSetpoint(32);
            if(!lidarpidController.onTarget()) {
              lidarpidController.calculate();
              mDifferentialDrive.tankDrive(mLeftSpeed, mRightSpeed);
            }
          else {
            step = 0;
            currentPath = null;
          }
        }
      }
    }
  }

    public void updateCargoMechanism() {
      if (cargoMode != null) {
        cargoMechanism.runCargo();
      }
      else if(MechanismsGamepad.A_Button_State) {
        cargoMode = CargoMode.LOWPICKUP;
        cargoMechanism.runCargo();
      }
      else if(MechanismsGamepad.B_Button_State) {
        cargoMode = CargoMode.HIGHSHOOT;
        cargoMechanism.runCargo();
      }
      else if(MechanismsGamepad.X_Button_State) {
        cargoMode = CargoMode.LOWSHOOT;
        cargoMechanism.runCargo();
      }
      else if(MechanismsGamepad.Y_Button_State) {
        cargoMode = CargoMode.HIGHPICKUP;
        cargoMechanism.runCargo();
      }
      else {
        cargoMechanism.stopCargo();
      }
    }

    public void initializeOverRoller() {
      overRoller = new OverRoller();
      overRoller.initialize();
    }

    public void updateOverRoller() {
      if(MechanismsGamepad.DPAD_State == 0 && overRoller.getLeftEncoder() > 21 && overRoller.getRightEncoder() < -21) {
        overRoller.raiseArms();
      }
      else if(MechanismsGamepad.DPAD_State == 180) {
        overRoller.lowerArms();
      }
      else {
        overRoller.stopArms();
      }
    }

    public void initializeWinch() {
      winch = new Winch();
      winch.initializeWinch();
    }

    public void updateWinch() {
      if(MechanismsGamepad.Right_Stick_Y_Axis_State > .2 && winch.getWinchEncoderValue() > -10000) {
        winch.raiseGorgon();
      }
      else if(MechanismsGamepad.Right_Stick_Y_Axis_State < -.2 && winch.getWinchEncoderValue() < -10) {
       winch.lowerGorgon();
      }
      else {
        winch.stopGorgon();
      }
    }

    public void initializeGorgon() {
      gorgon = new Gorgon();
      gorgon.initialize();
    }

    public void updateGorgon() {
      if(MechanismsGamepad.DPAD_State == 90) {
        gorgon.openGorgon();
        gorgonOpen = true;
      }
      if(MechanismsGamepad.DPAD_State == 270) {
        gorgon.closeGorgon();
        gorgonOpen = false;
      }
    }

    public void initializeClimb() {
      climb = new RobotClimb();
      climb.initialize();
    }

    public void updateClimb() {
      if(MechanismsGamepad.Left_Bumper_State) {
        if (climbOpen) {
          climb.closePenumatics();
        }
        else {
          climb.openPenumatics();
        }
        climbOpen = !climbOpen;
      }
    }

   }