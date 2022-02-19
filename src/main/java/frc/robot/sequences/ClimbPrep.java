package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;

import frc.robot.subsystems.Climber;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.RobotMap;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.IState;
import frc.robot.subsystems.parent.BaseSubsystem;

public class ClimbPrep extends BaseSequence<ClimbPrepState> {

    public ClimbPrep(ClimbPrepState neutralState, ClimbPrepState startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getState()) {
            case PREPCLAW:
                if (getTimeSinceStartOfState() > 500) {
                    setNextState(ClimbPrepState.SWINGARM);
                }
                break;
            case SWINGARM:
                if (getTimeSinceStartOfState() > 500
                        && (RobotMap.m_l1Switch.get() || RobotMap.m_h2Switch.get())) {
                    setNextState(ClimbPrepState.PREPPEDFORCLIMB);
                }
                break;
            case PREPPEDFORCLIMB:
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

enum ClimbPrepState implements IState {
    NEUTRAL,
    PREPCLAW(Robot.climber),
    SWINGARM(Robot.climber),
    PREPPEDFORCLIMB(Robot.climber);

    List<BaseSubsystem> requiredSubsystems;

    ClimbPrepState(BaseSubsystem... subsystems) {
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
