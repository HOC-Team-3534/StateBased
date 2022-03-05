package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;

import frc.robot.subsystems.Climber;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.RobotMap;
import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.IState;
import frc.robot.subsystems.parent.BaseSubsystem;

public class ClimbReset extends BaseSequence<ClimbResetState> {

    public ClimbReset(ClimbResetState neutralState, ClimbResetState startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getState()) {
            case MOVEARMMANUALLY:
                if (!Buttons.MoveClimbArmManually.getButton()){
                    setNextState(ClimbResetState.NEUTRAL);
                }
                break;
            case NEUTRAL:
                break;
            default:
                break;

        }

    updateState();

    }

    @Override
    public boolean abort() {
        setNextState(getNeutralState());
        return updateState();
    }

}

enum ClimbResetState implements IState {
    NEUTRAL,
    MOVEARMMANUALLY(Robot.climber);

    List<BaseSubsystem> requiredSubsystems;

    ClimbResetState(BaseSubsystem... subsystems) {
        requiredSubsystems = Arrays.asList(subsystems);
    }

    @Override
    public List<BaseSubsystem> getRequiredSubsystems() {
        return requiredSubsystems;
    }

    @Override
    public boolean requireSubsystems(BaseSequence<? extends IState> sequence) {
        for (BaseSubsystem subsystem : requiredSubsystems) {
            if (subsystem.isRequiredByAnother(sequence)) {
                return false;
            }
        }
        for (BaseSubsystem subsystem : requiredSubsystems) {
            subsystem.require(sequence, this);
        }
        return true;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
