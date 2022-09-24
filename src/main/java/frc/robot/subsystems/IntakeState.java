package frc.robot.subsystems;

import frc.robot.Robot;
import frc.statebasedcontroller.subsystem.fundamental.state.ISubsystemState;
import frc.statebasedcontroller.subsystem.fundamental.state.SubsystemState;

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
        this.state = new SubsystemState<>(this, processFunction);
    }

    @Override
    public SubsystemState<Intake> getState() {
        return state;
    }

    @Override
    public Intake getSubsystem() {
        return Robot.intake;
    }
}
