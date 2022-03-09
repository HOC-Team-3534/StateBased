package frc.robot.autons;

import frc.robot.Robot;
import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonState;

public enum Auton {
    STATION3_4BALL(new FourBallAuton(
            FourBallAutonState.NEUTRAL,
            FourBallAutonState.PICKUPBALL1, Robot.swerveDrive,
            "Station 3 4 Ball 1", "Station 3 4 Ball 2")),
    STRAIGHTLINETEST(new StraightTestAuton(
            StraightTestAutonState.NEUTRAL,
            StraightTestAutonState.PICKUPBALL1, Robot.swerveDrive,
            "Straight Test"));
    
    BaseAutonSequence<? extends IAutonState> auton;
    
    Auton(frc.robot.autons.parent.BaseAutonSequence<? extends IAutonState> auton){
        this.auton = auton;
    }
    
    public BaseAutonSequence<? extends IAutonState> getAuton(){
        return auton;
    }
}
