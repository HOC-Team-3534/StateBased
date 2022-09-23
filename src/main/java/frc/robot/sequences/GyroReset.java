package frc.robot.sequences;


import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Robot;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.sequences.parent.SequenceState;
import frc.robot.subsystems.parent.SubsystemRequirement;

import static frc.robot.sequences.GyroResetState.NEUTRAL;

enum GyroResetState implements ISequenceState {
    NEUTRAL,
    RESET;

    SequenceState state;

    GyroResetState(SubsystemRequirement... requirements) {
        state = new SequenceState(requirements);
    }

    @Override
    public SequenceState getState() {
        return state;
    }

}

public class GyroReset extends BaseSequence<GyroResetState> {

    public GyroReset(GyroResetState neutralState, GyroResetState startState) {
        super(neutralState, startState);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void process() {
        switch (getState()) {
            case RESET:
                Robot.swerveDrive.resetGyro();
                setNextState(NEUTRAL);
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

