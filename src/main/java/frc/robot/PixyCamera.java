package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;

public class PixyCamera {

    public void startPixyCam() {
        CameraServer.getInstance().startAutomaticCapture().setResolution(240, 180);
    } 

}