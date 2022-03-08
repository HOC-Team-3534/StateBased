package frc.robot.autons;

import frc.robot.Robot;
import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonState;

public enum Auton {
    STATION3_4BALL(new FourBallAuton(
            FourBallAutonStation3State.NEUTRAL,
            FourBallAutonStation3State.PICKUPBALL1, Robot.swerveDrive,
            "Station 3 4 Ball 1", "Station 3 4 Ball 2"));
    
    BaseAutonSequence<? extends IAutonState> auton;
    
    Auton(frc.robot.autons.parent.BaseAutonSequence<? extends IAutonState> auton){
        this.auton = auton;
    }
    
    public BaseAutonSequence<? extends IAutonState> getAuton(){
        return auton;
    }
}
