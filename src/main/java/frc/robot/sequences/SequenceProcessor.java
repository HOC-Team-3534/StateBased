package frc.robot.sequences;

import frc.robot.Robot;
import frc.robot.RobotContainer.Buttons.*;

public class SequenceProcessor {

    /**
     * TODO Create a new variable of each of the functions
     */

    public Drive drive;
    public GyroReset gyroReset;


    public SequenceProcessor() {

        /**
         * TODO Instantiate each of the sequences
         */

        drive = new Drive(DrivePhase.NEUTRAL, DrivePhase.DRIVE);
        gyroReset = new GyroReset(GyroResetPhase.NEUTRAL, GyroResetPhase.RESET);

    }

    public void process() {

        /** TODO set the logic for starting each and every sequence, then process each one*/

        if (true) {
            drive.start(Robot.swerveDrive);
        }

        drive.process();
        gyroReset.process();
    }
}
