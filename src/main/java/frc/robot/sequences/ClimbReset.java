package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.sequences.parent.SequenceState;
import frc.robot.subsystems.parent.BaseSubsystem;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.ClimberReq;
import frc.robot.subsystems.ClimberState;

public class ClimbReset extends BaseSequence<ClimbResetState> {

    public ClimbReset(ClimbResetState neutralState, ClimbResetState startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getState()) {
            case MOVEARMMANUALLY:
                if (!Buttons.MoveClimbArmManually.getButton()){
                    setNextState(ClimbResetState.NEUTRAL);
                }
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
        setNextState(getNeutralState());
        return updateState();
    }

}

enum ClimbResetState implements ISequenceState {
    NEUTRAL,
    MOVEARMMANUALLY(new ClimberReq(ClimberState.MOVEARMMANUALLY));

    SequenceState state;

    ClimbResetState(SubsystemRequirement... requirements) {
        state = new SequenceState(requirements);
    }

    @Override
    public SequenceState getState() {
        return state;
    }

}

