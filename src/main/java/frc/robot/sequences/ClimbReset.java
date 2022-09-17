package frc.robot.sequences;

import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.sequences.parent.SequenceState;
import frc.robot.subsystems.ClimberState;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.ClimberReq;

import static frc.robot.sequences.ClimbResetState.NEUTRAL;

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

public class ClimbReset extends BaseSequence<ClimbResetState> {

    public ClimbReset(ClimbResetState neutralState, ClimbResetState startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getState()) {
            case MOVEARMMANUALLY:
                if (!Buttons.MoveClimbArmManually.getButton()) {
                    setNextState(NEUTRAL);
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

