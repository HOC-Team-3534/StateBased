package frc.robot.sequences;

import frc.robot.subsystems.Climber;
import frc.robot.subsystems.ClimberState;
import frc.robot.subsystems.requirements.ClimberReq;
import frc.statebasedcontroller.sequence.fundamental.BaseSequence;
import frc.statebasedcontroller.sequence.fundamental.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.SequencePhase;
import frc.statebasedcontroller.subsystem.fundamental.SubsystemRequirement;

import static frc.robot.sequences.ClimbPrepPhase.PREPPEDFORCLIMB;
import static frc.robot.sequences.ClimbPrepPhase.SWINGARM;


enum ClimbPrepPhase implements ISequencePhase {
    NEUTRAL,
    PREPCLAW(new ClimberReq(ClimberState.PREPCLAW)),
    SWINGARM(new ClimberReq(ClimberState.SWINGARM)),
    PREPPEDFORCLIMB(new ClimberReq(ClimberState.SWINGARM)); //stays in swing arm, just using state change as indicator

    SequencePhase state;

    ClimbPrepPhase(SubsystemRequirement... requirements) {
        state = new SequencePhase(requirements);
    }

    @Override
    public SequencePhase getPhase() {
        return state;
    }

}

public class ClimbPrep extends BaseSequence<ClimbPrepPhase> {

    public ClimbPrep(ClimbPrepPhase neutralState, ClimbPrepPhase startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getPhase()) {
            case PREPCLAW:
                if (getTimeSinceStartOfPhase() > 50) {
                    setNextPhase(SWINGARM);
                }
                break;
            case SWINGARM:
                if (getTimeSinceStartOfPhase() > 500
                        && (!Climber.l1Switch.get() || !Climber.h2Switch.get())) {
                    setNextPhase(PREPPEDFORCLIMB);
                }
                break;
            case PREPPEDFORCLIMB:
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
