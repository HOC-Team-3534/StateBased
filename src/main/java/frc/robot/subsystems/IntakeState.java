package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.subsystems.parent.ISubsystemState;

import java.util.function.Consumer;

public enum IntakeState implements ISubsystemState<Intake> {
    NEUTRAL((s) -> s.neutral()),
    KICKOUT((s) -> s.kickOut()),
    RETRACT((s) -> s.retract()),
    EXTAKE((s) -> s.extake()),
    ROLLIN((s) -> s.rollIn()),
    HOLDPOSITION((s) -> {});

    Consumer<Intake> processFunction;

    IntakeState(Consumer<Intake> processFunction){
        this.processFunction = processFunction;
    }

    @Override
    public Intake getAssociatedSubsystem() {
        return Robot.intake;
    }

    @Override
    public void process() {
        processFunction.accept(getAssociatedSubsystem());
    }
}
