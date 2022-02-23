package frc.robot.sequences;

import frc.robot.RobotContainer.Buttons;

public class SequenceProcessor{

    /**
     * Create a new variable of each of the functions
     */
    
    public Drive drive;
    public Shoot shoot;
    public IntakeSeq intake;

    public SequenceProcessor(){

       /**
        * Instantiate each of the sequences
        */

        drive = new Drive(DriveState.NEUTRAL, DriveState.DRIVE);
        shoot = new Shoot(ShootState.NEUTRAL, ShootState.WAITNSPIN);
        intake = new IntakeSeq(IntakeState.NEUTRAL, IntakeState.EXTEND);

    }

    public void process(){

        drive.start();

        if(Buttons.Shoot.getButton()) {
            shoot.start();
        }

        if(Buttons.Intake.getButton()) {
            intake.start();
        }

        drive.process();
        shoot.process();
        intake.process();

    }
}
