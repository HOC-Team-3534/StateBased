package frc.robot.autons;

import frc.pathplanner.PathPlannerFollower;
import frc.robot.Robot;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseAutonSequence;

public enum Auton {
        /** TODO add and configure all of the possible auton options */

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
