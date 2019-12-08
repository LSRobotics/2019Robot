package frc.robot.hardware;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import frc.robot.constants.*;

public class RoboRIO {

    private static BuiltInAccelerometer accel;
    private static Axis _axis;
    private static double[] velocity = { 0, 0, 0 };
    private static boolean isActive = false;

    /**
     * Initialize RoboRIO's accelerometer (MUST call this method when robot starts)
     */
    public static void initialize() {

        isActive = true;
        accel = new BuiltInAccelerometer();

        new Thread(new Runnable() {

            @Override
            public synchronized void run() {

                while (isActive) {
                    velocity[0] += getAcceleration(Axis.X) * 0.05;
                    velocity[1] += getAcceleration(Axis.Y) * 0.05;
                    velocity[2] += getAcceleration(Axis.Z) * 0.05;

                    try {
                        Thread.sleep(50);
                    } catch (Exception e) {
                        // Shhhhhh
                    }

                }
            }
        }).run();

    }

    /**
     * Get velocity based on self-implemented integrator.
     * 
     * @return Velocity in the following format: {X-axis, Y-axis, Z-axis} (In metres
     *         per second)
     */
    public static double[] getVelocities() {
        return velocity;
    }

    /**
     * Get the accelerometer object of RoboRIO.
     * 
     * @return accelerometer.
     */
    public static BuiltInAccelerometer getAccelerometer() {
        return accel;
    }

    /**
     * Set robot's forward/back axis (Which helps getForwardAcceleration())
     * 
     * @param axis robot's forward axis;
     */
    public static void setForwardAxis(Axis axis) {
        _axis = axis;
    }

    /**
     * Get acceleration of the Robot in three axes.
     * 
     * @return acceleration in the following format: {X-axis, Y-axis, Z-axis} (In
     *         metres per second squared)
     */
    public static double[] getAccelerations() {
        return new double[] { accel.getX() * 9.8, accel.getY() * 9.8, accel.getZ() * 9.8 };
    }

    public static double getAcceleration(Axis axis) {
        switch (axis) {
        case X:
            return accel.getX() * 9.8;
        case Y:
            return accel.getY() * 9.8;
        case Z:
        default:
            return accel.getZ() * 9.8;
        }
    }

    public static double getForwardAcceleration() {

        return getAcceleration(_axis);
    }
}
