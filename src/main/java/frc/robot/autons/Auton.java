package frc.robot.autons;

import java.util.Arrays;
import java.util.List;

import frc.pathplanner.PathPlannerFollower;
import frc.robot.Robot;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseAutonSequence;

public enum Auton {
    CORNER1_2BALL(new TwoBallAuton(
            TwoBallAutonPhase.NEUTRAL,
            TwoBallAutonPhase.PICKUPBALL1, Robot.swerveDrive),
            Robot.corner1TwoBall1),
    CORNER1_1BALL(new OneBallAuton(
            OneBallAutonPhase.NEUTRAL,
            OneBallAutonPhase.DRIVE1, Robot.swerveDrive),
            Robot.corner1OneBall1),
    CORNER2_1BALL(new OneBallAuton(
            OneBallAutonPhase.NEUTRAL,
            OneBallAutonPhase.DRIVE1, Robot.swerveDrive),
            Robot.corner2OneBall1),
    CORNER3_1BALL(new OneBallAuton(
            OneBallAutonPhase.NEUTRAL,
            OneBallAutonPhase.DRIVE1, Robot.swerveDrive),
            Robot.corner3OneBall1),
    CORNER4_5BALL(new FiveBallAuton(
            FiveBallAutonPhase.NEUTRAL,
            FiveBallAutonPhase.DRIVE1, Robot.swerveDrive),
            Robot.corner4FiveBallPre, Robot.corner4FiveBall1, Robot.corner4FiveBall2, Robot.corner4FiveBall3),
    CORNER4_3BALL(new ThreeBallAuton(
            ThreeBallAutonPhase.NEUTRAL,
            ThreeBallAutonPhase.DRIVE1, Robot.swerveDrive),
            Robot.corner4FiveBallPre, Robot.corner4FiveBall1),
    NO_OP(new NoOpAuton(
            NoOpAutonPhase.NEUTRAL,
            NoOpAutonPhase.NEUTRAL, Robot.swerveDrive));

    BaseAutonSequence<? extends ISequencePhase> auton;
    List<PathPlannerFollower> paths;

    Auton(BaseAutonSequence<? extends ISequencePhase> auton, PathPlannerFollower... paths) {
        this.auton = auton;
        this.paths = Arrays.asList(paths);
    }

    public BaseAutonSequence<? extends ISequencePhase> getAuton() {
        auton.setPathPlannerFollowers(paths);
        return auton;
    }
}
