package frc.robot.sequences;

import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.sequences.parent.SequenceState;
import frc.robot.subsystems.SwerveDriveState;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.SwerveDriveReq;

enum DriveState implements ISequenceState {
    NEUTRAL,
    DRIVE(new SwerveDriveReq(SwerveDriveState.DRIVE));

    SequenceState state;

    DriveState(SubsystemRequirement... requirements) {
        state = new SequenceState(requirements);
    }

    @Override
    public SequenceState getState() {
        return state;
    }

}

public class Drive extends BaseSequence<DriveState> {

    public Drive(DriveState neutralState, DriveState startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getState()) {
            case DRIVE:
                break;
            case NEUTRAL:
                break;
            default:
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
