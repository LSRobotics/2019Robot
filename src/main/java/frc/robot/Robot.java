/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;

import frc.robot.software.*;
import frc.robot.hardware.*;
import frc.robot.hardware.Gamepad.Key;

public class Robot extends TimedRobot {

  static public Gamepad gp1, gp2;
  
  public static boolean reverseDrive = false;

  public static int step = 0;

  public static Cargo.Mode cargoMode = null;

  public static double cargoUltrasonicDistance;

  public static boolean reverseAuto = false;
  
  public static double gyroAngle;

  public static DigitalInput limitSwitch;

  public static Compressor mCompressor;

  public static boolean gorgonOpen = false;
  public static boolean climbOpen = false;
  public double lightMode;
  public boolean ballCall;
  public boolean RobotClimbLights = false;

  public double speedLimiter = 1;
  
  @Override
  public void robotInit() {
    
    mCompressor = new Compressor(0);

    gp1 = new Gamepad(0);
    gp2 = new Gamepad(1);

    Gyro.initialize();
    Cargo.initialize();
    RobotClimb.initialize();
    Gorgon.initialize();
    Camera.startPixyCam();
    OverRoller.initialize();
    Winch.initialize();
    Lights.initialize();
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
      teleopPeriodic();
  }

  @Override
  public void teleopPeriodic() {

    gp1.fetchData();
    gp2.fetchData();
  
    updateSensors();
    if (gp2.isKeyHeld(Key.J_LEFT_DOWN)) {
      cargoMode = null;
    }
    if (gp1.isKeyToggled(Key.RB)) {
      if (speedLimiter == 1) speedLimiter = .5;
      else speedLimiter = 1;
    }
    updateGorgon();
    updateCargoMechanism();
    updateClimb();
    updateOverRoller();
    updateDrive();
    callForBall();
    updateWinch();
    updatePixyCamera();
    updateLights();
    updateSmartDashboard();
  }

  @Override
  public void testPeriodic() {

  }

  public void callForBall() {
    if(gp1.isKeyHeld(Key.A)) {
      if(ballCall) {
        ballCall = false;
      }
      else {
        ballCall = true;
      }
    }
  }

  public void updateLights() {

    double finalAngle = Math.abs(gyroAngle % 360);

    if(gp2.isKeyHeld(Key.B) || lightMode == -.55) {
      lightMode = -.55;
    }
    else {
      if(ballCall) {
        lightMode = .93;
      }
      else {

        if(Utils.isDataInRange(finalAngle, 85, 95) 
          || Utils.isDataInRange(finalAngle,175, 185)
          || Utils.isDataInRange(finalAngle, 265, 275)
          || Utils.isDataInRange(finalAngle, 0, 5)
          || Utils.isDataInRange(finalAngle, 355, 359)) {
            lightMode = 0.77;
          }

        else {
          if(Cargo.ballCaptured()) {
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
    Lights.lightChange(lightMode);
  }
  public void updatePixyCamera() {
    if(gp1.isKeyToggled(Key.LB)) {
      Camera.changeCam();
    }
  }

  public void updateSensors() {
    gyroAngle = Gyro.getAngle();
    cargoUltrasonicDistance = Cargo.ultrasonicSensor.getRangeInches();
  }

  //else contains true tank drive
  public void updateDrive() {

    //Reverse Drive
    if(gp1.isKeyToggled(Key.RB)) {
      Chassis.flip();
    }

    //NFS Drive control
    if(gp1.isKeysChanged(Key.LT,Key.RT,Key.J_LEFT_X)) {
      double y = Utils.mapAnalog(-gp1.getValue(Key.RT)) - Utils.mapAnalog(-gp1.getValue(Key.LT));
      double x = Utils.mapAnalog(-gp1.getValue(Key.J_LEFT_X));
      Chassis.drive(y,x);
    }
  }

    public void updateSmartDashboard() {
    } 

    public void updateCargoMechanism() {
      if (cargoMode != null) {
        Cargo.runCargo();
      }
      else if(gp2.isKeyToggled(Key.A)) {
        cargoMode = Cargo.Mode.LOWPICKUP;
        Cargo.runCargo();
      }
      else if(gp2.isKeyToggled(Key.B)) {
        cargoMode = Cargo.Mode.HIGHSHOOT;
        Cargo.runCargo();
      }
      else if(gp2.isKeyToggled(Key.X)) {
        cargoMode = Cargo.Mode.LOWSHOOT;
        Cargo.runCargo();
      }
      else if(gp2.isKeyToggled(Key.Y)) {
        cargoMode = Cargo.Mode.HIGHPICKUP;
        Cargo.runCargo();
      }
      else {
        Cargo.stopCargo();
      }
    }

    public void updateOverRoller() {

      if(gp2.isKeyHeld(Key.DPAD_RIGHT) && OverRoller.getLeftEncoder() > 21 &&  OverRoller.getRightEncoder() < -21) {
        OverRoller.raiseArms();
      }
      else if(gp2.isKeyHeld(Key.DPAD_LEFT)) {
        OverRoller.lowerArms();
      }
      else {
        OverRoller.stopArms();
      }
    }

    public void updateWinch() {

      if(gp2.getValue(Key.J_RIGHT_Y) > .2 && Winch.getWinchEncoderValue() > -10000) {
        Winch.raiseGorgon();
      }
      else if(gp2.getValue(Key.J_RIGHT_Y) < -.2 && Winch.getWinchEncoderValue() < -10) {
       Winch.lowerGorgon();
      }
      else {
        Winch.stopGorgon();
      }
    }

    public void updateGorgon() {

      if(gp2.isKeyToggled(Key.DPAD_DOWN)) {
        gorgonOpen = !gorgonOpen;

        if(gorgonOpen) {
          Gorgon.openGorgon();
        }
        else {
          Gorgon.closeGorgon();
        }
      }
    }

    public void updateClimb() {
      if(gp2.isKeyToggled(Key.LB)) {
        if (climbOpen) {
          RobotClimb.closePenumatics();
        }
        else {
          RobotClimb.openPenumatics();
        }
        climbOpen = !climbOpen;
      }
    }

   }