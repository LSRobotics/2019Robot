package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;

public class PixyCamera {

    UsbCamera cam0;
    UsbCamera cam1;
    VideoSink server;
    boolean switched = false;

    public void startPixyCam() {
        cam0 = CameraServer.getInstance().startAutomaticCapture(0);
        cam0.setResolution(240,160);
        cam0.setFPS(15);
        cam1 = CameraServer.getInstance().startAutomaticCapture(1);
        cam1.setResolution(240, 160);
        cam1.setFPS(15);
        server = CameraServer.getInstance().getServer();
        server.setSource(cam0);
    }

    public void changeCam() {
        if(switched) {
            switched = false;
            server.setSource(cam0);
        }
        else {
            switched = true;
            server.setSource(cam1);
        }
    }

}                                         