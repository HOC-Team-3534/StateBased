package frc.robot.autons;

import frc.pathplanner.PathPlannerFollower;
import frc.statebasedcontroller.sequence.fundamental.BaseAutonSequence;
import frc.statebasedcontroller.sequence.fundamental.IAutonPhase;
import frc.statebasedcontroller.sequence.fundamental.SequencePhase;
import frc.statebasedcontroller.subsystem.fundamental.SubsystemRequirement;
import frc.statebasedcontroller.subsystem.general.swervedrive.BaseDriveSubsystem;

enum NoOpAutonPhase implements IAutonPhase {
    NEUTRAL(-999);

    int pathIndex;
    SequencePhase state;

    NoOpAutonPhase(int pathIndex, SubsystemRequirement... requirements) {
        this.pathIndex = pathIndex;
        state = new SequencePhase(requirements);
    }

    @Override
    public SequencePhase getPhase() {
        return state;
    }

    @Override
    public PathPlannerFollower getPath(BaseAutonSequence<? extends IAutonPhase> sequence) {
        return IAutonPhase.getPath(sequence, pathIndex);
    }
}

public class NoOpAuton extends BaseAutonSequence<NoOpAutonPhase> {

    public NoOpAuton(NoOpAutonPhase neutralState, NoOpAutonPhase startState, BaseDriveSubsystem driveSubsystem) {
        super(neutralState, startState, driveSubsystem);
    }

    @Override
    public void process() {

        switch (getPhase()) {
            case NEUTRAL:
                break;

        }
        updatePhase();
    }

    @Override
    public boolean abort() {
        // TODO Auto-generated method stub
        return false;
    }

}