package frc.robot.autons;

import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonPhase;
import frc.robot.autons.pathplannerfollower.PathPlannerFollower;
import frc.robot.sequences.parent.SequencePhase;
import frc.robot.subsystems.parent.BaseDriveSubsystem;
import frc.robot.subsystems.parent.SubsystemRequirement;

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