package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;

public class PixyCamera {

    public static CameraServer cargoCam;
    public static CameraServer gorgonCam;

    public void startPixyCam() {
        cargoCam.startAutomaticCapture();
        gorgonCam.startAutomaticCapture();
    }

}