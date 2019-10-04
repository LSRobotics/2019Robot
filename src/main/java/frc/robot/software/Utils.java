package frc.robot.software;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Robot;
import frc.robot.hardware.*;

public class Utils {

    public enum BotLocation {
        LEFT, MIDDLE, RIGHT;
    }

    private static String gameData;
    final public static int DEFAULT_BREAK_TIME = 1200;
    public static boolean isOutputEnabled = true;
    public static Robot main;

    public static void initialize(Robot mainRobot) {
        main = mainRobot;
    }

    public static void fetchGameData() {

        // gamedata
        String dataBuffer;
        while (true) {
            dataBuffer = DriverStation.getInstance().getGameSpecificMessage();
            if (dataBuffer.length() > 0)
                break;
        }

        gameData = dataBuffer;
        report("GameData: " + gameData);

    }

    public static boolean isBlueAlliance() {
        return DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue;
    }

    public static BotLocation getLocation() {

        return BotLocation.values()[(DriverStation.getInstance().getLocation() - 1)];

    }

    public static boolean drive(double leftRight, double forwardBack, int millisecond) {
        try {
            report("Moving at speed of (" + leftRight + ", " + forwardBack + ") for" + millisecond + " ms");
            Chassis.drive(forwardBack, leftRight);
            if (!takeABreak(millisecond))
                return false;
            report("Moving done");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean takeABreak() {
        return takeABreak(DEFAULT_BREAK_TIME);
    }

    public static boolean takeABreak(int millisecond) {

        long time = System.currentTimeMillis();

        try {
            report("Idle for " + millisecond + " ms");
            while ((System.currentTimeMillis() - time) < millisecond) {

                Robot.gp1.fetchData();

                if (Robot.gp1.isGamepadChanged()) {
                    report("Interrupted by controller actions");
                    return false;
                }
                // I still need to sleep a bit -- how about a 1/200 sec nap?
                Thread.sleep(5);
            }
            report("Woke up");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public static boolean turnRobot(boolean isLeft) {

        double power = isLeft ? -0.5 : 0.5;
        double angleFactor = Gyro.getAbsAngle() / 90;

        double leftAngle, rightAngle;

        // Determine Angles
        leftAngle = 90 * Math.floor(angleFactor);
        rightAngle = leftAngle + 90;

        if(angleFactor == Math.floor(angleFactor)) {
            leftAngle += isLeft ? -90 : 90;
            rightAngle += isLeft ? -90 : 90;
        }

        Chassis.stop();
        Chassis.drive(0, power);

        while (!isDataClose(Gyro.getAbsAngle(), leftAngle, 3) && !isDataClose(Gyro.getAbsAngle(), rightAngle, 3)) {
            Robot.gp1.fetchData();
            Robot.gp2.fetchData();

            // Interrupt action if LB in GP1 is toggled
            if (Robot.gp1.isKeyToggled(Gamepad.Key.LB)) {
                Chassis.stop();
                return false;
            }

            main.updateTop();
        }

        Chassis.stop();

        return true;
    }

    public static double estimateDriveTime(double speed, double distance) {
        return distance / speed;
    }

    public static void report(String message) {
        if (isOutputEnabled) {
            DriverStation.reportWarning(message, false);
        }
    }

    public static boolean isDriverBusy() {
        return Robot.gp1.isGamepadChanged();
    }

    /**
     *
     * @param value the original value
     * @param min   the minimum value allowed in the range
     * @param max   the maximum value allowed in the range
     * @return the value within the range
     */
    public static double clipValue(double value, double min, double max) {
        if (value >= max)
            return max;
        else if (value <= min)
            return min;
        else
            return value;
    }

    public static boolean isDataInRange(double value, double min, double max) {

        if (min > max) {
            double temp = max;
            max = min;
            min = temp;
        }

        return (value == min || value > min) && (value < max || value == max);
    }

    public static boolean isDataClose(double value, double expected, double tolerance) {
        return Math.abs(value - expected) < tolerance || (Math.abs(value - expected)) == tolerance;
    }

    public static double mapAnalog(double value) {
        return mapAnalog(value, 0.1, 0.7);
    }

    public static double mapAnalog(double value, double absMin, double absMax) {

        boolean isNegative = (value < 0);

        value = Math.abs(value);

        if (value < absMin) {
            return 0;
        } else if (value > absMax || value == absMax) {
            return isNegative ? -1 : 1;
        } else {
            return (value - absMin) / absMax * (isNegative ? -1 : 1);
        }
    }
}
