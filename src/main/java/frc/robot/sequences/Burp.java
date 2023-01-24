package frc.robot.sequences;

import frc.robot.Robot;
import frc.robot.RobotContainer.Buttons;
import frc.robot.subsystems.ShooterState;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.phase.SequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseSequence;
import frc.statebasedcontroller.subsystem.fundamental.state.ISubsystemState;

import static frc.robot.sequences.BurpPhase.*;

enum BurpPhase implements ISequencePhase {
	NEUTRAL,
	BURP(ShooterState.BURP),
	PUNCH(ShooterState.PUNCH),
	RESETPUNCH(ShooterState.RESETPUNCH),
	BOOT(ShooterState.BOOT);

	SequencePhase phase;

	BurpPhase(ISubsystemState... states) {
		phase = new SequencePhase(states);
	}

	@Override
	public SequencePhase getPhase() {
		return phase;
	}
}

public class Burp extends BaseSequence<BurpPhase> {
	public Burp(BurpPhase neutralState, BurpPhase startState) {
		super(neutralState, startState);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void process() {
		switch (getPhase()) {
			case BURP:
				if (!Buttons.Burp.getButton()) {
					setNextPhase(RESETPUNCH);
				}
				if (this.getTimeSinceStartOfPhase() > 500
					&& Robot.shooter.getShooterClosedLoopError() < 250) {
					System.out.println("In state");
					setNextPhase(PUNCH);
				}
				break;

			case PUNCH:
				if (this.getTimeSinceStartOfPhase() > 250) {
					setNextPhase(RESETPUNCH);
				}
				break;

			case RESETPUNCH:
				if (!Buttons.Burp.getButton()
					&& this.getTimeSinceStartOfPhase() > 250) {
					setNextPhase(NEUTRAL);
				} else if (this.getTimeSinceStartOfPhase() > 250) {
					setNextPhase(BOOT);
				}
				break;

			case BOOT:
				if (this.getTimeSinceStartOfPhase() > 150) {
					setNextPhase(BURP);
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
		// TODO Auto-generated method stub
		return false;
	}
}
