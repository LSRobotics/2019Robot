package frc.robot.hardware;

import java.util.Arrays;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.software.*;

final public class XboxGp {

    final private static int NUM_KEYS = 20;
    private Timer p = new Timer("Gamepad");
    private boolean isDebug = false;

    public enum Key {
        J_LEFT_X, J_LEFT_Y, J_RIGHT_X, J_RIGHT_Y, LT, RT, J_LEFT_DOWN, J_RIGHT_DOWN, A, B, X, Y, LB, RB, BACK, START,
        DPAD_UP, DPAD_DOWN, DPAD_LEFT, DPAD_RIGHT
    }

    private int portNumber;
    private XboxController xGP;
    public boolean[] states = new boolean[NUM_KEYS];
    public double[] values = new double[NUM_KEYS];
    final private static Key key_index[] = Key.values();

    public XboxGp(int xboxPort) {

        // Initialize Arrays
        Arrays.fill(states, false);
        Arrays.fill(values, 0);

        portNumber = xboxPort;
        xGP = new XboxController(portNumber);
    }

    public void setDebugMode(boolean isDebugMode) {
        isDebug = isDebugMode;
    }

    public double getValue(Key key) {
        return values[key.ordinal()];
    }

    public double getRawReading(Key key) {

        int pov = xGP.getPOV();

        switch (key) {
        case J_LEFT_X:
            return xGP.getX(Hand.kLeft);
        case J_LEFT_Y:
            return xGP.getY(Hand.kLeft);
        case J_RIGHT_X:
            return xGP.getX(Hand.kRight);
        case J_RIGHT_Y:
            return xGP.getY(Hand.kRight);
        case LT:
            return xGP.getTriggerAxis(Hand.kLeft);
        case RT:
            return xGP.getTriggerAxis(Hand.kRight);
        case A:
            return xGP.getAButton() ? 1 : 0;
        case B:
            return xGP.getBButton() ? 1 : 0;
        case X:
            return xGP.getXButton() ? 1 : 0;
        case Y:
            return xGP.getYButton() ? 1 : 0;
        case LB:
            return xGP.getBumper(Hand.kLeft) ? 1 : 0;
        case RB:
            return xGP.getBumper(Hand.kRight) ? 1 : 0;
        case BACK:
            return xGP.getBackButton() ? 1 : 0;
        case START:
            return xGP.getStartButton() ? 1 : 0;
        case J_LEFT_DOWN:
            return xGP.getStickButton(Hand.kLeft) ? 1 : 0;
        case J_RIGHT_DOWN:
            return xGP.getStickButton(Hand.kRight) ? 1 : 0;
        case DPAD_RIGHT:
            return pov < 0 ? 0 : ((pov >= 45 && pov <= 135) ? 1 : 0);
        case DPAD_DOWN:
            return pov < 0 ? 0 : ((pov >= 135 && pov <= 225) ? 1 : 0);
        case DPAD_LEFT:
            return pov < 0 ? 0 : ((pov >= 225 && pov <= 315) ? 1 : 0);
        case DPAD_UP:
            return pov < 0 ? 0 : ((pov == 315 && pov <= 45) ? 1 : 0);
        default:
            return 0;
        }
    }

    public boolean isKeyChanged(Key key) {
        return states[key.ordinal()];
    }

    public boolean isKeyHeld(Key key) {
        return getValue(key) > 0;
    }

    public boolean isGamepadChanged() {
        for (boolean i : states) {
            if (i) {
                return true;
            }
        }
        return false;
    }

    public boolean isKeyToggled(Key key) {
        return isKeyChanged(key) && (getValue(key) > 0);
    }

    public void fetchData() {

        if (isDebug)
            p.start();

        for (int i = 0; i < NUM_KEYS; ++i) {

            double tempVal = getRawReading(key_index[i]);

            if (tempVal != values[i]) {
                states[i] = true;
                values[i] = tempVal;
            } else {
                states[i] = false;
            }
        }

        if (isDebug) {
            p.stop();
            RobotUtil.report(p.toString());
        }
    }
}
