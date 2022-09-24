package frc.robot.subsystems;

import frc.robot.Robot;
import frc.statebasedcontroller.subsystem.fundamental.state.ISubsystemState;
import frc.statebasedcontroller.subsystem.fundamental.state.SubsystemState;

import java.util.function.Consumer;

public enum ClimberState implements ISubsystemState<Climber> {
    NEUTRAL((s) -> s.neutral()),
    PREPCLAW((s) -> s.prepClaw()),
    SWINGARM((s) -> s.swingArm()),
    GRIPMIDBAR((s) -> s.gripMidBar()),
    SWINGMIDHIGH1((s) -> s.swingMidHigh1()),
    SWINGMIDHIGH2((s) -> s.swingMidHigh2()),
    GRIPHIGHBAR((s) -> s.gripHighBar()),
    RETRYHIGHBAR((s) -> s.retryHighBar()),
    RECENTERMIDHIGHBAR((s) -> s.recenterMidHighBar()),
    RELEASEMIDBAR((s) -> s.releaseMidBar()),
    SWINGHIGHTRAVERSAL1((s) -> s.swingHighTraversal1()),
    SWINGHIGHTRAVERSAL2((s) -> s.swingHighTraversal2()),
    GRIPTRAVERSALBAR((s) -> s.gripTraversalBar()),
    RETRYTRAVERSALBAR((s) -> s.retryTraversalBar()),
    RECENTERHIGHTRAVERSALBAR((s) -> s.recenterHighTraversalBar()),
    RELEASEHIGHBAR((s) -> s.releaseHighBar()),
    SWINGTOREST((s) -> s.swingToRest()),
    RESETARM((s) -> s.resetArm()),
    MOVEARMMANUALLY((s) -> s.moveArmManually());

    SubsystemState<Climber> state;

    ClimberState(Consumer<Climber> processFunction) {
        this.state = new SubsystemState<>(this, processFunction);
    }

    @Override
    public SubsystemState<Climber> getState() {
        return state;
    }

    @Override
    public Climber getSubsystem() {
        return Robot.climber;
    }
}
