package frc.robot.sequences;

import frc.robot.Robot;
import frc.robot.RobotContainer.Buttons;
import frc.robot.RobotMap;

public class SequenceProcessor {

    /**
     * Create a new variable of each of the functions
     */

    public Drive drive;
    public ClimbPrep climbPrep;
    public Climb climb;

    public SequenceProcessor() {

        /**
         * Instantiate each of the sequences
         */

        drive = new Drive(DriveState.NEUTRAL, DriveState.DRIVE);
        climbPrep = new ClimbPrep(ClimbPrepState.NEUTRAL, ClimbPrepState.PREPCLAW);
        climb = new Climb(ClimbState.NEUTRAL, ClimbState.GRIPMIDBAR);

    }

    public void process() {

        if (climb.isNeutral()) {
            drive.start(Robot.swerveDrive);
        }
        if (Buttons.ClimbPrep.getButton()) { // TODO: in last 35 seconds of match logic
            climbPrep.start();
        }
        if (Buttons.Climb.getButton()
                && Robot.climber.getSequenceRequiring().getState().getName() == "PREPPEDFORCLIMB"
                && (RobotMap.m_l1Switch.get() || RobotMap.m_h2Switch.get())) {
            climb.start(Robot.climber, Robot.swerveDrive);
        }

        drive.process();
        climbPrep.process();
        climb.process();

    }
}
