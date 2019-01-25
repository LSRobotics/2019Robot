package frc.robot.hardware;

import edu.wpi.first.wpilibj.PWM;

final public class PWMDevice {
    private PWM obj;

    public PWMDevice(int portNumber) {
        this.obj = new PWM(portNumber);
    }

    public void moveBySpeed(double speed) {
        this.obj.setSpeed(speed);
    }

    public void moveByPosition(double position) {
        this.obj.setPosition(position);
    }
}
