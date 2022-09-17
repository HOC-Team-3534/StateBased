package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.subsystems.parent.ISubsystemState;
import frc.robot.subsystems.parent.SubsystemState;

import java.util.function.Consumer;

public enum IntakeState implements ISubsystemState<Intake> {
    NEUTRAL((s) -> s.neutral()),
    KICKOUT((s) -> s.kickOut()),
    RETRACT((s) -> s.retract()),
    EXTAKE((s) -> s.extake()),
    ROLLIN((s) -> s.rollIn()),
    HOLDPOSITION((s) -> {
    });

    SubsystemState<Intake> state;

    IntakeState(Consumer<Intake> processFunction) {
        this.state = new SubsystemState<>(this.name(), processFunction, Robot.intake);
    }

    @Override
    public SubsystemState<Intake> getState() {
        return state;
    }
}
