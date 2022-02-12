package frc.robot.sequences;

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

        if(drive.getState() == drive.getNeutralState()){
            drive.start();
        }
        if(climbPrep.getState() == climbPrep.getNeutralState()){
            climbPrep.start();
        }
        if(climb.getState() == climb.getNeutralState() && climbPrep.getTimeSinceStartOfSequence() > 7000){
            climb.start();
        }

        drive.process();
        climbPrep.process();
        climb.start();

    }
}
