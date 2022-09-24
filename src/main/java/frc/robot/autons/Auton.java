package frc.robot.autons;

import frc.pathplanner.PathPlannerFollower;
import frc.robot.Robot;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseAutonSequence;

public enum Auton {
    CORNER1_2BALL(new TwoBallAuton(
            TwoBallAutonPhase.NEUTRAL,
            TwoBallAutonPhase.PICKUPBALL1, Robot.swerveDrive)),
    CORNER1_1BALL(new OneBallAuton(
            OneBallAutonPhase.NEUTRAL,
            OneBallAutonPhase.DRIVE1, Robot.swerveDrive)),
    CORNER2_1BALL(new OneBallAuton(
            OneBallAutonPhase.NEUTRAL,
            OneBallAutonPhase.DRIVE1, Robot.swerveDrive)),
    CORNER3_1BALL(new OneBallAuton(
            OneBallAutonPhase.NEUTRAL,
            OneBallAutonPhase.DRIVE1, Robot.swerveDrive)),
    CORNER4_5BALL(new FiveBallAuton(
            FiveBallAutonPhase.NEUTRAL,
            FiveBallAutonPhase.DRIVE1, Robot.swerveDrive)),
    CORNER4_3BALL(new ThreeBallAuton(
            ThreeBallAutonPhase.NEUTRAL,
            ThreeBallAutonPhase.DRIVE1, Robot.swerveDrive)),
    NO_OP(new NoOpAuton(
            NoOpAutonPhase.NEUTRAL,
            NoOpAutonPhase.NEUTRAL, Robot.swerveDrive));

    BaseAutonSequence<? extends ISequencePhase> auton;

    Auton(BaseAutonSequence<? extends ISequencePhase> auton) {
        this.auton = auton;
    }

    public void setPathPlannerFollowers(PathPlannerFollower... pathPlannerFollowers) {
        this.auton.setPathPlannerFollowers(pathPlannerFollowers);
    }

    public BaseAutonSequence<? extends ISequencePhase> getAuton() {
        return auton;
    }
}
