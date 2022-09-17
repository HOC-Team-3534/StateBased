package frc.robot.autons;

import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonState;
import frc.robot.autons.pathplannerfollower.PathPlannerFollower;
import frc.robot.sequences.parent.SequenceState;
import frc.robot.subsystems.parent.BaseDriveSubsystem;
import frc.robot.subsystems.parent.SubsystemRequirement;

enum NoOpAutonState implements IAutonState {
    NEUTRAL(-999);

    int pathIndex;
    SequenceState state;

    NoOpAutonState(int pathIndex, SubsystemRequirement... requirements) {
        this.pathIndex = pathIndex;
        state = new SequenceState(requirements);
    }

    @Override
    public SequenceState getState() {
        return state;
    }

    @Override
    public PathPlannerFollower getPath(BaseAutonSequence<? extends IAutonState> sequence) {
        return IAutonState.getPath(sequence, pathIndex);
    }
}

public class NoOpAuton extends BaseAutonSequence<NoOpAutonState> {

    public NoOpAuton(NoOpAutonState neutralState, NoOpAutonState startState, BaseDriveSubsystem driveSubsystem) {
        super(neutralState, startState, driveSubsystem);
    }

    @Override
    public void process() {

        switch (getState()) {
            case NEUTRAL:
                break;

        }
        updateState();
    }

    @Override
    public boolean abort() {
        // TODO Auto-generated method stub
        return false;
    }

}