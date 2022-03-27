package frc.robot.subsystems.states;

import frc.robot.Robot;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.parent.ISubsystemState;

public enum IntakeState implements ISubsystemState<Intake> {
    NEUTRAL,
    KICKOUT,
    RETRACT,
    EXTAKE,
    HOLDPOSITION;

    @Override
    public Intake getAssociatedSubsystem() {
        return Robot.intake;
    }
}
