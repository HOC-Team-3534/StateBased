package frc.robot.autons;

import frc.robot.Robot;
import frc.robot.subsystems.ShooterState;
import frc.robot.subsystems.SwerveDriveState;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.phase.SequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseAutonSequence;
import frc.statebasedcontroller.subsystem.fundamental.state.ISubsystemState;
import frc.statebasedcontroller.subsystem.general.swervedrive.BaseDriveSubsystem;

import static frc.robot.autons.OneBallAutonPhase.*;

enum OneBallAutonPhase implements ISequencePhase {
	NEUTRAL,
	DRIVE1(	0, SwerveDriveState.DRIVE_AUTONOMOUSLY,
			ShooterState.AUTONPREUPTOSPEED),
	SHOOTBALL1(ShooterState.UPTOSPEED),
	PUNCH1(ShooterState.PUNCH),
	RESETPUNCH1(ShooterState.RESETPUNCH);

	SequencePhase phase;

	OneBallAutonPhase(ISubsystemState... states) {
		phase = new SequencePhase(states);
	}

	OneBallAutonPhase(int pathIndex, ISubsystemState... states) {
		phase = new SequencePhase(pathIndex, states);
	}

	@Override
	public SequencePhase getPhase() {
		return phase;
	}
}

public class OneBallAuton extends BaseAutonSequence<OneBallAutonPhase> {
	public OneBallAuton(OneBallAutonPhase neutralState,
						OneBallAutonPhase startState,
						BaseDriveSubsystem driveSubsystem) {
		super(neutralState, startState, driveSubsystem);
	}

	@Override
	public void process() {
		switch (getPhase()) {
			case DRIVE1:
				if (this.getPlannerFollower().isFinished()) {
					setNextPhase(SHOOTBALL1);
				}
				break;

			case SHOOTBALL1:
				if ((this.getTimeSinceStartOfPhase() > 1000)
					&& Robot.shooter.getShooterClosedLoopError() < 100) {
					setNextPhase(PUNCH1);
				}
				break;

			case PUNCH1:
				if (this.getTimeSinceStartOfPhase() > 500) {
					setNextPhase(RESETPUNCH1);
				}
				break;

			case RESETPUNCH1:
				if (this.getTimeSinceStartOfPhase() > 500) {
					setNextPhase(NEUTRAL);
				}
				break;

			case NEUTRAL:
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