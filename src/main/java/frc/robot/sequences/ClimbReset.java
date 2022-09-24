package frc.robot.sequences;

import frc.robot.RobotContainer.Buttons;
import frc.robot.subsystems.ClimberState;
import frc.robot.subsystems.requirements.ClimberReq;

import static frc.robot.sequences.ClimbResetPhase.NEUTRAL;

import frc.BaseSequence;
import frc.ISequencePhase;
import frc.SequencePhase;
import frc.SubsystemRequirement;

enum ClimbResetPhase implements ISequencePhase {
    NEUTRAL,
    MOVEARMMANUALLY(new ClimberReq(ClimberState.MOVEARMMANUALLY));

    SequencePhase state;

    ClimbResetPhase(SubsystemRequirement... requirements) {
        state = new SequencePhase(requirements);
    }

    @Override
    public SequencePhase getPhase() {
        return state;
    }

}

public class ClimbReset extends BaseSequence<ClimbResetPhase> {

    public ClimbReset(ClimbResetPhase neutralState, ClimbResetPhase startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getPhase()) {
            case MOVEARMMANUALLY:
                if (!Buttons.MoveClimbArmManually.getButton()) {
                    setNextPhase(NEUTRAL);
                }
                break;
            case NEUTRAL:
                break;
            default:
                break;

        }

        updatePhase();

    }

    @Override
    public boolean abort() {
        setNextPhase(getNeutralPhase());
        return updatePhase();
    }

}

