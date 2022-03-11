package frc.robot.autons;

import frc.robot.Robot;
import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonState;
import frc.robot.autons.pathplannerfollower.PathPlannerFollower;

public enum Auton {
    CORNER1_2BALL(new TwoBallAuton(
            TwoBallAutonState.NEUTRAL,
            TwoBallAutonState.PICKUPBALL1, Robot.swerveDrive,
            new PathPlannerFollower("Corner 1 2 Ball 1"))),
    CORNER1_1BALL(new OneBallAuton(
            OneBallAutonState.NEUTRAL,
            OneBallAutonState.DRIVE1, Robot.swerveDrive,
            new PathPlannerFollower("Corner 1 1 Ball 1"))),
    CORNER2_1BALL(new OneBallAuton(
            OneBallAutonState.NEUTRAL,
            OneBallAutonState.DRIVE1, Robot.swerveDrive,
            new PathPlannerFollower("Corner 2 1 Ball 1"))),
    CORNER4_5BALL(new FiveBallAuton(
            FiveBallAutonState.NEUTRAL,
            FiveBallAutonState.SHOOTBALL1, Robot.swerveDrive,
            new PathPlannerFollower("Corner 4 5 Ball 1"), new PathPlannerFollower("Corner 4 5 Ball 2"), new PathPlannerFollower("Corner 4 5 Ball 3"))),
    CORNER4_3BALL(new ThreeBallAuton(
            ThreeBallAutonState.NEUTRAL,
            ThreeBallAutonState.SHOOTBALL1, Robot.swerveDrive,
            new PathPlannerFollower("Corner 4 5 Ball 1"))),
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
