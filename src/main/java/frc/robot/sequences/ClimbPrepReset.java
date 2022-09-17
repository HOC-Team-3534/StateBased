package frc.robot.sequences;

import frc.robot.Robot;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.sequences.parent.SequenceState;
import frc.robot.subsystems.ClimberState;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.ClimberReq;

import static frc.robot.sequences.ClimbPrepResetState.NEUTRAL;

enum ClimbPrepResetState implements ISequenceState {
    NEUTRAL,
    RESETARM(new ClimberReq(ClimberState.RESETARM));

    SequenceState state;

    ClimbPrepResetState(SubsystemRequirement... requirements) {
        state = new SequenceState(requirements);
    }

    @Override
    public SequenceState getState() {
        return state;
    }

}

public class ClimbPrepReset extends BaseSequence<ClimbPrepResetState> {

    public ClimbPrepReset(ClimbPrepResetState neutralState, ClimbPrepResetState startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getState()) {
            case RESETARM:
                if (Robot.climber.getClimbArmDegree() < 10) {
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
