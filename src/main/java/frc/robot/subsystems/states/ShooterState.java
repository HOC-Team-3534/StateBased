package frc.robot.subsystems.states;

import frc.robot.Robot;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.parent.ISubsystemState;

public enum ShooterState implements ISubsystemState<Shooter> {
    NEUTRAL,
    AUTONPREUPTOSPEED,
    UPTOSPEED,
    BURP,
    PUNCH,
    RESETPUNCH,
    BOOT;

    @Override
    public Shooter getAssociatedSubsystem() {
        return Robot.shooter;
    }
}
