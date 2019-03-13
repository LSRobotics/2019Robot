package frc.robot;

import edu.wpi.first.wpilibj.PWM;

public class Lights {
    public PWM lightPwm;
    public int port = Statics.Light_PWM_Port;

    public void initialize() {
        lightPwm = new PWM(port);
    }

    public void lightChange(double gyroDegree) {
        if(gyroDegree > 89 && gyroDegree < 91) {
            lightPwm.setRaw(100);
        }
        if(gyroDegree > 179 && gyroDegree < 181) {
            lightPwm.setRaw(150);
        }
        if(gyroDegree > 269 &&  gyroDegree < 271) {
            lightPwm.setRaw(190);
        }
        if(gyroDegree == 0) {
            lightPwm.setRaw(0);
        }
    }
}