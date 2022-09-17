package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.subsystems.parent.ISubsystemState;
import frc.robot.subsystems.parent.SubsystemState;

import java.util.function.Consumer;

public enum ShooterState implements ISubsystemState<Shooter> {
    NEUTRAL((s) -> s.neutral()),
    AUTONPREUPTOSPEED((s) -> {
        if (Robot.swerveDrive.getPathStateController().getPathPlannerFollower() != null &&
                Robot.swerveDrive.getPathStateController().getPathPlannerFollower()
                        .getRemainingTimeSeconds() < 2.0) {
            s.upToSpeed(3000);
        } else {
            NEUTRAL.getState().process();
        }
    }),
    UPTOSPEED((s) -> {
        if (Robot.limelight.isTargetAcquired()) {
            s.upToSpeed();
            //TODO add option to switch to manual shooting rpm testing
            //Robot.shooter.upToSpeed(SmartDashboard.getNumber("Manual Testing RPM", 0.0));
        } else {
            s.upToSpeed(3000);
        }
    }),
    BURP((s) -> s.burp()),
    PUNCH((s) -> s.punch()),
    RESETPUNCH((s) -> s.resetPunch()),
    BOOT((s) -> s.boot());

    SubsystemState<Shooter> state;

    ShooterState(Consumer<Shooter> processFunction) {
        this.state = new SubsystemState<>(this.name(), processFunction, Robot.shooter);
    }

    @Override
    public SubsystemState<Shooter> getState() {
        return state;
    }
}
