package frc.robot.autons;

import frc.robot.Robot;
import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonState;
import frc.robot.autons.pathplannerfollower.PathPlannerFollower;

public enum Auton {
    STATION3_4BALL(new FourBallAuton(
            FourBallAutonState.NEUTRAL,
            FourBallAutonState.PICKUPBALL1, Robot.swerveDrive,
            new PathPlannerFollower("Station 3 4 Ball 1"), new PathPlannerFollower("Station 3 4 Ball 2"))),
    STRAIGHTLINETEST(new StraightTestAuton(
            StraightTestAutonState.NEUTRAL,
            StraightTestAutonState.PICKUPBALL1, Robot.swerveDrive,
            new PathPlannerFollower("Straight Test"))),
    STATION1_2BALL(new TwoBallAuton(
            TwoBallAutonState.NEUTRAL,
            TwoBallAutonState.PICKUPBALL1, Robot.swerveDrive,
            new PathPlannerFollower("Station 1 2 Ball Auton 1"))),
    NO_OP(new NoOpAuton(
            NoOpAutonState.NEUTRAL,
            NoOpAutonState.NEUTRAL, Robot.swerveDrive));
    
    BaseAutonSequence<? extends IAutonState> auton;
    
    Auton(frc.robot.autons.parent.BaseAutonSequence<? extends IAutonState> auton){
        this.auton = auton;
    }
    
    public BaseAutonSequence<? extends IAutonState> getAuton(){
        return auton;
    }
}
