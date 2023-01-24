package frc.robot.autons;

import frc.pathplanner.PathPlannerFollower;
import frc.robot.Constants.Auto;

public enum Path {
	CORNER1_2BALL_1("Corner 1 2 Ball 1"),
	CORNER1_1BALL_1("Corner 1 1 Ball 1"),
	CORNER2_1BALL_1("Corner 2 1 Ball 1"),
	CORNER3_1BALL_1("Corner 3 1 Ball 1"),
	CORNER4_5BALL_PRE("Corner 4 5 Ball Pre"),
	CORNER4_5BALL_1("Corner 4 5 Ball 1"),
	CORNER4_5BALL_2("Corner 4 5 Ball 2"),
	CORNER4_5BALL_3("Corner 4 5 Ball 3");

	String pathName;
	PathPlannerFollower path;

	Path() {
		this.pathName = this.name();
	}

	Path(String pathName) {
		this.pathName = pathName;
	}

	public void loadPath() {
		path = new PathPlannerFollower(	pathName, Auto.kMaxSpeedMetersPerSecond,
										Auto.kMaxAccelerationMetersPerSecondSquared);
	}

	public PathPlannerFollower getPath() {
		return path;
	}
}