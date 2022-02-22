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
        intake = new IntakeSeq(IntakeState.NEUTRAL, IntakeState.EXTEND);

    }

    public void process(){

        if(drive.getState() == drive.getNeutralState()){
            drive.start();
        }
        if(Buttons.Intake.getButton()) {
            intake.start();
        }

        //drive.process();
        intake.process();
    }
}
