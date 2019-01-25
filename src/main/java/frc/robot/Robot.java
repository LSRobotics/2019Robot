package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.hardware.*;
import frc.robot.software.*;

/**
 * Hi Nick,
 * 		I would highly recommend you to look up something called "MVC" project structure, which is probably important to collaboration. 
 *		And for static classes, it would be great if you strictly follows whatever that's on APCS class.
 *		I'm sorry that your code has been reworked, but I will make sure that it works on the Robot without Blood & Tears.
 *     
 * 	    -- Pat Liao
 */

final public class Robot extends TimedRobot {

	private static double speedFactor = 1.0;
	private static boolean requireUpdate = false;
	private static boolean isSNP = false; // Means "is Sniping Mode Triggered"
	private static XboxGp gp1;
	private static XboxGp gp2;
	private static Camera pixyCam;
	private static Timer camTimer;
	private static UltrasonicSensor ultrasonicA;

	private int speedSwitch = 0;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {

		// Hardware
		DriveTrain.init(Statics.DRIVE_FL, Statics.DRIVE_RL, Statics.DRIVE_FR, Statics.DRIVE_RR);

		gp1 = new XboxGp(Statics.XBOX_CTRL1);
		gp2 = new XboxGp(Statics.XBOX_CTRL2);

		pixyCam = new Camera();

		pixyCam.setScreenRatio(640 / 400);
		pixyCam.setResolution(400 / 2);

		if (Statics.DEBUG_MODE) {
			DriverStation.reportWarning("DEBUG", false);
			RobotUtil.isOutputEnabled = true;

		}

		Motor.setMode(Statics.MOTOR_MODE);
		camTimer = new Timer("Camera FPS Capping Timer");

		ultrasonicA = new UltrasonicSensor(Statics.ULTRASONIC_PING_PORT_1, Statics.ULTRASONIC_ECHO_PORT_1);

	}

	@Override
	public void autonomousInit() {
		pixyCam.setQuickMode(true);
	}

	@Override
	public void disabledPeriodic() {
		pixyCam.setQuickMode(false);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		teleopPeriodic();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopInit() {
		pixyCam.setQuickMode(true);
	}

	@Override
	public void teleopPeriodic() {
		preLoop();
		gp1.fetchData();
		gp2.fetchData();
		speedControl();
		driveControl();
		requireUpdate = false;
		postLoop();
		postSensorData();
	}

	@Override
	public void testInit() {
		RobotUtil.isOutputEnabled = true;
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		preLoop();
		gp1.fetchData();
		gp2.fetchData();
		speedControl();
		driveControl();
		if (Statics.DEBUG_MODE) {
			this.postSensorData();
		}
		requireUpdate = false;
		postLoop();
	}

	public void speedControl() {

		final double GEAR_INTERVAL = 0.2;
		/**
		 * 6-level global speed control with toggling Y on the gamepad
		 */
		if (gp1.isKeyToggled(XboxGp.Key.Y)) {

			if (speedSwitch > -1 && speedSwitch < 6) {

				speedFactor = RobotUtil.clipValue((0.1 + GEAR_INTERVAL * speedSwitch), 0, 1);
				speedSwitch += 1;
			} else {
				speedFactor = 0.1;
				speedSwitch = 0;
			}

			DriveTrain.setSpeed(speedFactor);
			requireUpdate = true;
		}
	}

	public void driveControl() {
		/**
		 * Reverse Gear Trigger (Button Y, AKA "Triangle" in Dualshock 4)
		 */
		if (gp1.isKeyToggled(XboxGp.Key.LB)) {
			DriveTrain.flip();
			requireUpdate = true;
		}

		/**
		 * Sniping Mode (First used in FTC 2017 Relic Recovery as the programming member
		 * in Team 11319) Use Right Bumper for toggling
		 */
		if (gp1.isKeyToggled(XboxGp.Key.RB)) {
			isSNP = !isSNP;
			speedFactor = isSNP ? 0.6 : 1.0;

			speedSwitch = isSNP ? 3 : 5;

			DriveTrain.setSpeed(speedFactor);
			requireUpdate = true;
		}
		/**
		 * NFS Drive Control (Might improve driving experience + less likely wearing out
		 * the gearboxes due to rapid speed change)
		 */
		else if (gp1.isKeyChanged(XboxGp.Key.RT) || gp1.isKeyChanged(XboxGp.Key.LT)
				|| gp1.isKeyChanged(XboxGp.Key.J_LEFT_X) || requireUpdate) {

			DriveTrain.tankDrive(gp1.getValue(XboxGp.Key.J_LEFT_X) * 0.5,
					(gp1.getValue(XboxGp.Key.RT) - gp1.getValue(XboxGp.Key.LT)));
		}

	}

	public void postSensorData() {
		RobotUtil.report("Ultrasonic sensor reading: " + ultrasonicA.getRange());
	}

	@Deprecated
	public void postPDPData() {

	}

	public void preLoop() {

		if (!camTimer.isBusy()) {
			camTimer.start();
		}

		if (!pixyCam.isQuickMode()) {
			pixyCam.setQuickMode(true);
		}

	}

	public void postLoop() {

		if (camTimer.getElaspedTimeInMs() > 500) {
			camTimer.stop();
			pixyCam.updateDynamicRes(20);
		}
	}
}
