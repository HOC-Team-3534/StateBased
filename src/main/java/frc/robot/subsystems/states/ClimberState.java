package frc.robot.subsystems.states;

import frc.robot.Robot;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.parent.ISubsystemState;

public enum ClimberState implements ISubsystemState<Climber> {
    NEUTRAL,
    PREPCLAW,
    SWINGARM,
    GRIPMIDBAR,
    SWINGMIDHIGH1,
    SWINGMIDHIGH2,
    GRIPHIGHBAR,
    RETRYHIGHBAR,
    RECENTERMIDHIGHBAR,
    RELEASEMIDBAR,
    SWINGHIGHTRAVERSAL1,
    SWINGHIGHTRAVERSAL2,
    GRIPTRAVERSALBAR,
    RETRYTRAVERSALBAR,
    RECENTERHIGHTRAVERSALBAR,
    RELEASEHIGHBAR,
    SWINGTOREST,
    RESETARM,
    MOVEARMMANUALLY;

    @Override
    public Climber getAssociatedSubsystem() {
        return Robot.climber;
    }
}
