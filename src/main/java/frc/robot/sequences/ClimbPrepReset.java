package frc.robot.sequences;

import frc.robot.Robot;
import frc.robot.subsystems.ClimberState;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.phase.SequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseSequence;
import frc.statebasedcontroller.subsystem.fundamental.state.ISubsystemState;

import static frc.robot.sequences.ClimbPrepResetPhase.NEUTRAL;

enum ClimbPrepResetPhase implements ISequencePhase {
    NEUTRAL,
    RESETARM(ClimberState.RESETARM);

    SequencePhase phase;
    
    ClimbPrepResetPhase(ISubsystemState... states) {
        phase = new SequencePhase(states);
    }
    
    @Override
    public SequencePhase getPhase() {
        return phase;
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
        return reset();
    }

}
