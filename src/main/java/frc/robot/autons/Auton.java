package frc.robot.autons;

import frc.robot.Robot;
import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonState;
import frc.robot.autons.pathplannerfollower.PathPlannerFollower;

public enum Auton {
    CORNER1_2BALL(new TwoBallAuton(
            TwoBallAutonState.NEUTRAL,
            TwoBallAutonState.PICKUPBALL1, Robot.swerveDrive)),
    CORNER1_1BALL(new OneBallAuton(
            OneBallAutonState.NEUTRAL,
            OneBallAutonState.DRIVE1, Robot.swerveDrive)),
    CORNER2_1BALL(new OneBallAuton(
            OneBallAutonState.NEUTRAL,
            OneBallAutonState.DRIVE1, Robot.swerveDrive)),
    CORNER3_1BALL(new OneBallAuton(
            OneBallAutonState.NEUTRAL,
            OneBallAutonState.DRIVE1, Robot.swerveDrive)),
    CORNER4_5BALL(new FiveBallAuton(
            FiveBallAutonState.NEUTRAL,
            FiveBallAutonState.DRIVE1, Robot.swerveDrive)),
    CORNER4_3BALL(new ThreeBallAuton(
            ThreeBallAutonState.NEUTRAL,
            ThreeBallAutonState.DRIVE1, Robot.swerveDrive)),
    NO_OP(new NoOpAuton(
            NoOpAutonState.NEUTRAL,
            NoOpAutonState.NEUTRAL, Robot.swerveDrive));

    BaseAutonSequence<? extends IAutonState> auton;

    Auton(frc.robot.autons.parent.BaseAutonSequence<? extends IAutonState> auton) {
        this.auton = auton;
    }

    public void setPathPlannerFollowers(PathPlannerFollower... pathPlannerFollowers) {
        this.auton.setPathPlannerFollowers(pathPlannerFollowers);
    }

    public BaseAutonSequence<? extends IAutonState> getAuton() {
        return auton;
    }
}
