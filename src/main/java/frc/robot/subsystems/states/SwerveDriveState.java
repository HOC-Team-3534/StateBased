package frc.robot.subsystems.states;

import frc.robot.Robot;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.parent.ISubsystemState;

public enum SwerveDriveState implements ISubsystemState<SwerveDrive> {
    NEUTRAL,
    DRIVE,
    AIM,
    DRIVE_AUTONOMOUSLY;

    @Override
    public SwerveDrive getAssociatedSubsystem() {
        return Robot.swerveDrive;
    }
}
