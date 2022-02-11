package frc.robot.sequences;

import frc.robot.RobotContainer.Buttons;

public class SequenceProcessor{

    /**
     * Create a new variable of each of the functions
     */
    
    public Drive drive;

    public IntakeSeq intake;

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
        if(intake.getState() == intake.getNeutralState() && Buttons.Intake.getButton()) {
            intake.start();
        }

        drive.process();
        intake.process();
    }
}
