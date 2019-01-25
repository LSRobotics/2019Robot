package frc.robot.software;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.hardware.*;

public class RobotUtil {

    public enum BotLocation {
        LEFT, MIDDLE, RIGHT;
    }

    private static String gameData;
    final public static int DEFAULT_BREAK_TIME = 1700;
    public static boolean isOutputEnabled = false;

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

    public static void tankDrive(double leftRight, double forwardBack, int millisecond) {
        try {
            report("Moving at speed of (" + leftRight + ", " + forwardBack + ") for" + millisecond + " ms");
            DriveTrain.tankDrive(leftRight, forwardBack);
            Thread.sleep(millisecond);
            DriveTrain.tankDrive(0, 0);
            report("Moving done");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void takeABreak() {
        takeABreak(DEFAULT_BREAK_TIME);
    }

    public static void takeABreak(int millisecond) {
        try {
            report("Idle for " + millisecond + " ms");
            Thread.sleep(millisecond);
            report("Woke up");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void report(String message) {
        if (isOutputEnabled) {
            DriverStation.reportWarning(message, false);
        }
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
}
