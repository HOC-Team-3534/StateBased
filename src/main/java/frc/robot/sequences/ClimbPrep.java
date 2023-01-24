package frc.robot.sequences;

import frc.robot.subsystems.Climber;
import frc.robot.subsystems.ClimberState;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.phase.SequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseSequence;
import frc.statebasedcontroller.subsystem.fundamental.state.ISubsystemState;

import static frc.robot.sequences.ClimbPrepPhase.PREPPEDFORCLIMB;
import static frc.robot.sequences.ClimbPrepPhase.SWINGARM;

enum ClimbPrepPhase implements ISequencePhase {
	NEUTRAL,
	PREPCLAW(ClimberState.PREPCLAW),
	SWINGARM(ClimberState.SWINGARM),
	PREPPEDFORCLIMB(ClimberState.SWINGARM); // stays in swing arm, just using state change as indicator

	SequencePhase phase;

	ClimbPrepPhase(ISubsystemState... states) {
		phase = new SequencePhase(states);
	}

	@Override
	public SequencePhase getPhase() {
		return phase;
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
				if (getTimeSinceStartOfPhase()	> 500
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
		return reset();
	}
}
