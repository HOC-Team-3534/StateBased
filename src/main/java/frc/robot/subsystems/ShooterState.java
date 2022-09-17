package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.subsystems.parent.ISubsystemState;

import java.util.function.Consumer;

public enum ShooterState implements ISubsystemState<Shooter> {
    NEUTRAL((s) -> s.neutral()),
    AUTONPREUPTOSPEED((s) -> {
        if (Robot.swerveDrive.getPathStateController().getPathPlannerFollower() != null &&
                Robot.swerveDrive.getPathStateController().getPathPlannerFollower()
                .getRemainingTimeSeconds() < 2.0) {
                s.upToSpeed(3000);
        } else {
            NEUTRAL.process();
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

    Consumer<Shooter> processFunction;

    ShooterState(Consumer<Shooter> processFunction){
        this.processFunction = processFunction;
    }

    @Override
    public Shooter getAssociatedSubsystem() {
        return Robot.shooter;
    }

    @Override
    public void process() {
        processFunction.accept(getAssociatedSubsystem());
    }
}
