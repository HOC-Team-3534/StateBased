package frc.robot.sequences;

import frc.robot.Robot;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.sequences.parent.SequenceState;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.ClimberState;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.ClimberReq;

import static frc.robot.sequences.ClimbPrepState.PREPPEDFORCLIMB;
import static frc.robot.sequences.ClimbPrepState.SWINGARM;

enum ClimbPrepState implements ISequenceState {
    NEUTRAL,
    PREPCLAW(new ClimberReq(ClimberState.PREPCLAW)),
    SWINGARM(new ClimberReq(ClimberState.SWINGARM)),
    PREPPEDFORCLIMB(new ClimberReq(ClimberState.SWINGARM)); //stays in swing arm, just using state change as indicator

    SequenceState state;

    ClimbPrepState(SubsystemRequirement... requirements) {
        state = new SequenceState(requirements);
    }

    @Override
    public SequenceState getState() {
        return state;
    }

}

public class ClimbPrep extends BaseSequence<ClimbPrepState> {

    public ClimbPrep(ClimbPrepState neutralState, ClimbPrepState startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getState()) {
            case PREPCLAW:
                if (getTimeSinceStartOfState() > 50) {
                    setNextState(SWINGARM);
                }
                break;
            case SWINGARM:
                if (getTimeSinceStartOfState() > 500
                        && (!Climber.l1Switch.get() || !Climber.h2Switch.get())) {
                    setNextState(PREPPEDFORCLIMB);
                }
                break;
            case PREPPEDFORCLIMB:
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
