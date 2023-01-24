package frc.robot.autons;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import frc.robot.Robot;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseAutonSequence;

public enum Auton {
	CORNER1_2BALL(new TwoBallAuton(	TwoBallAutonPhase.NEUTRAL,
									TwoBallAutonPhase.PICKUPBALL1,
									Robot.swerveDrive),
					Path.CORNER1_2BALL_1),
	CORNER1_1BALL(new OneBallAuton(	OneBallAutonPhase.NEUTRAL,
									OneBallAutonPhase.DRIVE1,
									Robot.swerveDrive),
					Path.CORNER1_1BALL_1),
	CORNER2_1BALL(new OneBallAuton(	OneBallAutonPhase.NEUTRAL,
									OneBallAutonPhase.DRIVE1,
									Robot.swerveDrive),
					Path.CORNER2_1BALL_1),
	CORNER3_1BALL(new OneBallAuton(	OneBallAutonPhase.NEUTRAL,
									OneBallAutonPhase.DRIVE1,
									Robot.swerveDrive),
					Path.CORNER3_1BALL_1),
	CORNER4_5BALL(	new FiveBallAuton(	FiveBallAutonPhase.NEUTRAL,
										FiveBallAutonPhase.DRIVE1,
										Robot.swerveDrive),
					Path.CORNER4_5BALL_PRE, Path.CORNER4_5BALL_1,
					Path.CORNER4_5BALL_2, Path.CORNER4_5BALL_3),
	CORNER4_3BALL(	new ThreeBallAuton(	ThreeBallAutonPhase.NEUTRAL,
										ThreeBallAutonPhase.DRIVE1,
										Robot.swerveDrive),
					Path.CORNER4_5BALL_PRE, Path.CORNER4_5BALL_1),
	NO_OP(new NoOpAuton(NoOpAutonPhase.NEUTRAL, NoOpAutonPhase.NEUTRAL,
						Robot.swerveDrive));

	BaseAutonSequence<? extends ISequencePhase> auton;
	List<Path> paths;

	Auton(BaseAutonSequence<? extends ISequencePhase> auton, Path... paths) {
		this.auton = auton;
		this.paths = Arrays.asList(paths);
	}

	public BaseAutonSequence<? extends ISequencePhase> getAuton() {
		auton.setPathPlannerFollowers(paths.stream().map(path -> path.getPath()).collect(Collectors.toList()));
		return auton;
	}
}
