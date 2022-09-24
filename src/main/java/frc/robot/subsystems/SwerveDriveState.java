package frc.robot.subsystems;

import frc.ISubsystemState;
import frc.SubsystemState;
import frc.robot.Robot;
import frc.robot.RobotContainer;

import java.util.function.Consumer;

public enum SwerveDriveState implements ISubsystemState<SwerveDrive> {
    NEUTRAL((s) -> s.neutral()),
    DRIVE((s) -> {
        if ((RobotContainer.Buttons.Creep.getButton())) {
            s.creep();
        } else {
            s.drive();
        }
    }),
    AIM((s) -> {
        if (Robot.limelight.isTargetAcquired() && RobotContainer.Buttons.SHOOT.getButton() ||
                Robot.limelight.isTargetAcquired() && Robot.isAutonomous) {
            s.aim();
        } else {
            DRIVE.getState().process(s);
        }
    }),
    DRIVE_AUTONOMOUSLY((s) -> {
        if (s.getStateFirstRunThrough()) {
            // TODO check if the start of the path is near current odometry for safety
        }
        if (s.getPathPlannerFollower() != null) {
            s.setModuleStatesAutonomous();
        } else {
            System.out.println("DRIVE PATH NOT SET. MUST CREATE PATHPLANNERFOLLOWER IN AUTON AND SET IN SWERVEDRIVE SUBSYSTEM");
        }
    });

    SubsystemState<SwerveDrive> state;

    SwerveDriveState(Consumer<SwerveDrive> processFunction) {
        this.state = new SubsystemState<>(this.name(), processFunction, Robot.swerveDrive);
    }

    @Override
    public SubsystemState<SwerveDrive> getState() {
        return state;
    }
}
