package frc.robot.autons;

import frc.BaseAutonSequence;
import frc.BaseDriveSubsystem;
import frc.IAutonPhase;
import frc.PathPlannerFollower;
import frc.SequencePhase;
import frc.SubsystemRequirement;

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