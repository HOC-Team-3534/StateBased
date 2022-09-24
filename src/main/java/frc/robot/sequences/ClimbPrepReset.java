package frc.robot.sequences;

import frc.robot.Robot;
import frc.robot.subsystems.ClimberState;
import frc.robot.subsystems.requirements.ClimberReq;
import frc.statebasedcontroller.sequence.fundamental.BaseSequence;
import frc.statebasedcontroller.sequence.fundamental.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.SequencePhase;
import frc.statebasedcontroller.subsystem.fundamental.SubsystemRequirement;

import static frc.robot.sequences.ClimbPrepResetPhase.NEUTRAL;

enum ClimbPrepResetPhase implements ISequencePhase {
    NEUTRAL,
    RESETARM(new ClimberReq(ClimberState.RESETARM));

    SequencePhase state;

    ClimbPrepResetPhase(SubsystemRequirement... requirements) {
        state = new SequencePhase(requirements);
    }

    @Override
    public SequencePhase getPhase() {
        return state;
    }

}

public class ClimbPrepReset extends BaseSequence<ClimbPrepResetPhase> {

    public ClimbPrepReset(ClimbPrepResetPhase neutralState, ClimbPrepResetPhase startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getPhase()) {
            case RESETARM:
                if (Robot.climber.getClimbArmDegree() < 10) {
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
