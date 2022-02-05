package frc.robot.sequences;

import frc.robot.RobotContainer.Buttons;

public class SequenceProcessor{

    /**
     * Create a new variable of each of the functions
     */
    
    public Drive drive;
    public Shoot shoot;

    public SequenceProcessor(){

       /**
        * Instantiate each of the sequences
        */

        drive = new Drive(DriveState.NEUTRAL, DriveState.DRIVE);
        shoot = new Shoot(ShootState.NEUTRAL, ShootState.SHOOT);

    }

    public void process(){

        if(drive.getState() == drive.getNeutralState()){
            drive.start();
        }
        if(shoot.getState() == shoot.getNeutralState() && Buttons.Shoot.getButton()) {
            shoot.start();
        }

        drive.process();
        shoot.process();

    }
}
