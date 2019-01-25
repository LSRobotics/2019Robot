package frc.robot.software;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Created by TylerLiu on 2017/03/04.
 */
public class AutonChooser {

    public static SendableChooser<Runnable> chooser;

    public static void init() {
        chooser = new SendableChooser<>();

        // Add options here

        SmartDashboard.putData("Auton Chooser", chooser);
    }

    public static Runnable getSelected() {
        return chooser.getSelected();
    }
}