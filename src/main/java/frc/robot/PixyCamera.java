package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

public class PixyCamera {

    public void startPixyCam() {
        CameraServer.getInstance().startAutomaticCapture();
    }

}