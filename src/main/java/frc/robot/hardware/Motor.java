package frc.robot.hardware;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.*;

public class Motor {

    public enum Model {
        TALON_SRX_PWM, VICTOR_SP, SPARK, VICTOR, TALON_SRX_CAN;
    }

    private SpeedController motor;
    private double speed = 1.0;
    public static Model DEFAULT_MODEL = Model.TALON_SRX_CAN;
    private boolean isReverse = false;
          public Motor(int port) {
        this(port, DEFAULT_MODEL);
    }

    public Motor(int port, Model model) {
        this(port, model, false);
    }

    public Motor(int port, boolean isReverse) {
        this(port, DEFAULT_MODEL, isReverse);
    }

    public Motor(int port, Model model, boolean isReverse) {
        switch (model) {
        case TALON_SRX_PWM:
            motor = new Talon(port);
            break;
        case VICTOR:
            motor = new Victor(port);
            break;
        case VICTOR_SP:
            motor = new VictorSP(port);
            break;
        case SPARK:
            motor = new Spark(port);
        case TALON_SRX_CAN:
            motor = new WPI_TalonSRX(port);
        default:
            break;
        }
        motor.setInverted(isReverse);
    }

    public void setReverse(boolean isReverse) {
        this.isReverse = isReverse;
        motor.setInverted(isReverse);
    }

    public boolean isReverse() {
        return isReverse;
    }

    public void flip() {
        motor.setInverted(!isReverse);
    }

    public void setSpeed(double newSpeed) {
        speed = newSpeed;
    }

    public void move(double value) {

        motor.set(value * speed);

    }

    public void stop() {
        move(0);
    }

    public void move(boolean forward, boolean reverse) {

        if (forward == reverse)
            move(0);
        else if (forward)
            move(1);
        else
            move(-1);
    }
}