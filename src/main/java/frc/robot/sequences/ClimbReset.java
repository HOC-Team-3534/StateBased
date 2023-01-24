package frc.robot.sequences;

import frc.robot.RobotContainer.Buttons;
import frc.robot.subsystems.ClimberState;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.phase.SequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseSequence;
import frc.statebasedcontroller.subsystem.fundamental.state.ISubsystemState;

import static frc.robot.sequences.ClimbResetPhase.NEUTRAL;

enum ClimbResetPhase implements ISequencePhase {
	NEUTRAL,
	MOVEARMMANUALLY(ClimberState.MOVEARMMANUALLY);

	SequencePhase phase;

	ClimbResetPhase(ISubsystemState... states) {
		phase = new SequencePhase(states);
	}

	@Override
	public SequencePhase getPhase() {
		return phase;
	}
}

public class ClimbReset extends BaseSequence<ClimbResetPhase> {
	public ClimbReset(	ClimbResetPhase neutralState,
						ClimbResetPhase startState) {
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
		return reset();
	}
}
