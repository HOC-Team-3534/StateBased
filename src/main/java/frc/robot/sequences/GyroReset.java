package frc.robot.sequences;


import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Robot;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.sequences.parent.SequenceState;
import frc.robot.subsystems.parent.BaseSubsystem;
import frc.robot.subsystems.parent.SubsystemRequirement;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GyroReset extends BaseSequence<GyroResetState>{

    public GyroReset(GyroResetState neutralState, GyroResetState startState) {
        super(neutralState, startState);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void process() {
        switch (getState()) {
            case RESET:
                Robot.swerveDrive.setGyroOffset(new Rotation2d().minus(Robot.pigeon.getRotation2d()));
                setNextState(GyroResetState.NEUTRAL);
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

