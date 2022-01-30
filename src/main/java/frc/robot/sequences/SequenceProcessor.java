package frc.robot.sequences;

public class SequenceProcessor{

    /**
     * Create a new variable of each of the functions
     */
    
    public Drive drive;

    public SequenceProcessor(){

       /**
        * Instantiate each of the sequences
        */

        drive = new Drive(DriveState.NEUTRAL, DriveState.DRIVE);

    }

    public void process(){

        if(drive.getState() == drive.getNeutralState()){
            drive.start();
        }

        drive.process();

    }
}
