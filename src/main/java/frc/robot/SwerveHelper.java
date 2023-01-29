package frc.robot;

import frc.robot.Constants.Drive;
import frc.statebasedcontroller.subsystem.general.swervedrive.swervelib.SDSModuleConfiguration;
import frc.statebasedcontroller.subsystem.general.swervedrive.swervelib.SwerveConstants;

public class SwerveHelper {
    public static boolean loadSwerveConstants() {
        SwerveConstants.fillNecessaryConstantsForFalcon(Drive.Calculated.MAX_FWD_REV_SPEED_MPS_EST,
                                                        Drive.Calculated.MAX_ROTATE_SPEED_RAD_PER_SEC_EST,
                                                        2 * Math.PI,
                                                        Drive.Calculated.KINEMATICS,
                                                        SDSModuleConfiguration.SDSMK4(SDSModuleConfiguration.driveGearRatios.SDSMK4_L2),
                                                        0.1, 1, 1, 1, 10.0,
                                                        10.0, 1, 1, 0.25, 0.25);
        return true;
    }
}
