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
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;

import frc.robot.software.*;
import frc.robot.constants.SpeedCurve;
import frc.robot.hardware.*;
import frc.robot.hardware.Gamepad.Key;

public class Robot extends TimedRobot {

  static public Gamepad gp1, gp2;

  public static Cargo.Mode cargoMode = Cargo.Mode.IDLE;

  public static double gyroAngle;
  public static double driveSpeed = 1.0;

  public static Compressor mCompressor;

  public double lightMode;
  public boolean ballCall = false, isLowSpd = false;

  //Drive mode GUI variables and setup
  public static final String kDefaultDrive = "Default";
  public static final String kCustomDrive = "Right Stick Drive";
  public static final String kCustomDrive1 = "Left Stick Drive";
  public static final String kCustomDrive2 = "Both Stick Drive";
  public String m_driveSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  @Override
  public void robotInit() {

    //Drive mode GUI setup
    m_chooser.setDefaultOption("Default", kDefaultDrive);
    m_chooser.addOption("Right Stick Drive", kCustomDrive);
    m_chooser.addOption("Left Strick Drive", kCustomDrive1);
    m_chooser.addOption("Both Strick Drive", kCustomDrive2);
    SmartDashboard.putData("Drive choices", m_chooser);
    System.out.println("Drive Selected: " + m_driveSelected);

    mCompressor = new Compressor(0);

    gp1 = new Gamepad(0);
    gp2 = new Gamepad(1);

    Core.initialize(this);

    Chassis.initialize();
    Gyro.initialize();
    Cargo.initialize();
    RobotClimb.initialize();
    Gorgon.initialize();
    Camera.startPixyCam();
    OverRoller.initialize();
    Winch.initialize();
    Lights.initialize();

    NavX.initialize();
    RoboRIO.initialize();

    Chassis.setSpeedCurve(SpeedCurve.SQUARED);
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    m_driveSelected = m_chooser.getSelected();
  }

  @Override
  public void teleopInit() {
    m_driveSelected = m_chooser.getSelected();
  }

  @Override
  public void autonomousPeriodic() {
    teleopPeriodic();
  }

  @Override
  public void teleopPeriodic() {

    gp1.fetchData();
    gp2.fetchData();

    updateBottom();
    updateTop();
  }

  @Override
  public void testPeriodic() {
    // TODO: Add test code here
    teleopPeriodic();
  }

  public void updateTop() {

    if (gp2.isKeyToggled(Key.LB)) {
      Cargo.stopCargo();
    }
    updateSensors();
    updateCargoMechanism();
    updateClimb();
    updateOverRoller();
    callForBall();
    updateWinch();
    updatePixyCamera();
    updateGorgon();

    // Not necessarily "Top" but does not belong to chassis
    updateLights();
    updateSmartDashboard();
  }

  public void callForBall() {
    if (gp1.isKeyHeld(Key.A)) {
      if (ballCall) {
        ballCall = false;
      } else {
        ballCall = true;
      }
    }
  }

  public void updateLights() {

    double finalAngle = Math.abs(gyroAngle % 360);

    if (gp2.isKeyHeld(Key.B) || lightMode == -.55) {
      lightMode = -.55;
    } else {
      if (ballCall) {
        lightMode = .93;
      } else {

        if (Utils.isDataInRange(finalAngle, 85, 95) || Utils.isDataInRange(finalAngle, 175, 185)
            || Utils.isDataInRange(finalAngle, 265, 275) || Utils.isDataInRange(finalAngle, 0, 5)
            || Utils.isDataInRange(finalAngle, 355, 359)) {
          lightMode = 0.77;
        }

        else {
          if (Cargo.ballCaptured()) {
            lightMode = .57;
          } else if (Gorgon.isOpen()) {
            lightMode = .63;
          } else {
            if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue)
              lightMode = .85;
            else
              lightMode = .61;
          }
        }
      }
    }
    Lights.lightChange(lightMode);
  }

  public void updatePixyCamera() {
    if (gp1.isKeyToggled(Key.Y)) {
      Camera.changeCam();
    }
  }

  public void updateSensors() {
    gyroAngle = Gyro.getAngle();
  }

  //All code for driving
  public void updateBottom() {

    //raise drive speed
    if(gp1.isKeyToggled(Key.RB)) {
      if(driveSpeed + 0.25 <= 1.0) {
        driveSpeed += 0.25;
        Chassis.setSpeedFactor(driveSpeed);
        SmartDashboard.putNumber("Speed", driveSpeed);
      }
    }
    //lower drive speed
    else if(gp1.isKeyToggled(Key.LB)) {
      if(driveSpeed - 0.25 >= 0) {
        driveSpeed -= 0.25;
        Chassis.setSpeedFactor(driveSpeed);
        SmartDashboard.putNumber("Speed", driveSpeed);
      }
    }

    // Assistive Autonomous
    if (gp1.isKeyToggled(Key.DPAD_LEFT)) {
      AutoPilot.turnRobotByTime(true);
    } else if (gp1.isKeyToggled(Key.DPAD_RIGHT)) {
      AutoPilot.turnRobotByTime(false);
    }


    
    // Drive control 
    else {
      double x = 0,y = 0;
      switch(m_driveSelected){
        //Right Stick Drive
        case kCustomDrive:
           y = Utils.mapAnalog(gp1.getValue(Key.J_RIGHT_Y));
           x = Utils.mapAnalog(gp1.getValue(Key.J_RIGHT_X));
          break;
        //Left Stick Drive
        case kCustomDrive1:
           y = Utils.mapAnalog(gp1.getValue(Key.J_LEFT_Y));
           x = Utils.mapAnalog(gp1.getValue(Key.J_LEFT_X));
          break;
        //Both Stick Drive
        case kCustomDrive2:
           y = Utils.mapAnalog(gp1.getValue(Key.J_LEFT_Y));
           x = Utils.mapAnalog(gp1.getValue(Key.J_RIGHT_X));
          break;
        //Default is right stick drive
        case kDefaultDrive:
          y = Utils.mapAnalog(gp1.getValue(Key.J_RIGHT_Y));
          x = Utils.mapAnalog(gp1.getValue(Key.J_RIGHT_X));
          break;

      }
      Chassis.drive(y,x);
    }
  }

  public void updateSmartDashboard() {

    double [] rio = RoboRIO.getAccelerations();
    double [] navx = NavX.getVelocities();

    SmartDashboard.putNumber("RoboRIO X Acceleration", rio[0]);
    SmartDashboard.putNumber("RoboRIO Y Acceleration", rio[1]);
    SmartDashboard.putNumber("RoboRIO Z Acceleration", rio[2]);

    SmartDashboard.putNumber("Navx-MXP X Velocity", navx[0]);
    SmartDashboard.putNumber("Navx-MXP Y Velocity", navx[1]);
    SmartDashboard.putNumber("Navx-MXP Z Velocity", navx[2]);
  }

  //cargo pickup and shooting 
  public void updateCargoMechanism() {

    // Force stop
    if (gp1.isKeyHeld(Key.RT)) {
      Cargo.stopCargo();
    }

    if (cargoMode != Cargo.Mode.IDLE) {
      Cargo.run();
      //A button for low pickup
    } else if (gp1.isKeyToggled(Key.A)) {
      cargoMode = Cargo.Mode.LOWPICKUP;
      Cargo.run();
      //B button for high shot
    } else if (gp1.isKeyToggled(Key.B)) {
      cargoMode = Cargo.Mode.HIGHSHOOT;
      Cargo.run();
      //X button for low shot
    } else if (gp1.isKeyToggled(Key.X)) {
      cargoMode = Cargo.Mode.LOWSHOOT;
      Cargo.run();
      //Y button for high pickup
    } else if (gp1.isKeyToggled(Key.Y)) {
      cargoMode = Cargo.Mode.HIGHPICKUP;
      Cargo.run();
    } else {
      Cargo.stopCargo();
    }
  }

  public void updateOverRoller() {

    if (-gp2.getValue(Key.J_LEFT_Y) > 0.1
        // Todo: Test THESE
        && OverRoller.getLeftEncoder() > 21 && OverRoller.getRightEncoder() < -21) {

      OverRoller.raiseArms();
    } else if (-gp2.getValue(Key.J_LEFT_Y) < -0.1) {
      OverRoller.lowerArms();
    } else {
      OverRoller.stopArms();
    }
  }

  public void updateWinch() {

    if (-gp2.getValue(Key.J_RIGHT_Y) > .2
        // TODO: Test This
        && Winch.getWinchEncoderValue() > -10000) {
      Winch.raiseGorgon();
    } else if (-gp2.getValue(Key.J_RIGHT_Y) < -.2
        // TODO: Test this
        && Winch.getWinchEncoderValue() < -10) {
      Winch.lowerGorgon();
    } else {
      Winch.stopGorgon();
    }
  }

  public void updateGorgon() {

    if (gp2.isKeyToggled(Key.DPAD_UP)) {

      Gorgon.actuate();
    }
  }

  public void updateClimb() {
    if (gp2.isKeyToggled(Key.DPAD_DOWN)) {
      RobotClimb.actuate();
    }
  }

}