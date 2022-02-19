package frc.robot.sequences;

import frc.robot.Robot;
import frc.robot.RobotContainer.Buttons;

public class SequenceProcessor{

    /**
     * Create a new variable of each of the functions
     */
    
    public Drive drive;
    public ClimbPrep climbPrep;
    public Climb climb;

    public SequenceProcessor(){

       /**
        * Instantiate each of the sequences
        */

        drive = new Drive(DriveState.NEUTRAL, DriveState.DRIVE);
        climbPrep = new ClimbPrep(ClimbPrepState.NEUTRAL, ClimbPrepState.PREPCLAW);
        climb = new Climb(ClimbState.NEUTRAL, ClimbState.GRIPMIDBAR);

    }

    public void process(){

        if(climb.getState().getName() == "NEUTRAL"){
            drive.start(Robot.swerveDrive);
        }
        if(Buttons.ClimbPrep.getButton()){
            climbPrep.start();
        }
        if(Robot.climber.getSequenceRequiring().getState().getName() == "PREPPEDFORCLIMB" && Buttons.Climb.getButton()){
            climb.start(Robot.climber, Robot.swerveDrive);
        }

        drive.process();
        climbPrep.process();
        climb.process();

    }
}
