package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import frc.robot.Robot;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.subsystems.parent.BaseSubsystem;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.ClimberReq;
import frc.robot.subsystems.states.ClimberState;

public class ClimbPrep extends BaseSequence<ClimbPrepState> {

    public ClimbPrep(ClimbPrepState neutralState, ClimbPrepState startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getState()) {
            case PREPCLAW:
                if (getTimeSinceStartOfState() > 50) {
                    setNextState(ClimbPrepState.SWINGARM);
                }
                break;
            case SWINGARM:
                if (getTimeSinceStartOfState() > 500
                        && (!Robot.climber.l1Switch.get() || !Robot.climber.h2Switch.get())) {
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

enum ClimbPrepState implements ISequenceState {
    NEUTRAL,
    PREPCLAW(new ClimberReq(ClimberState.PREPCLAW)),
    SWINGARM(new ClimberReq(ClimberState.SWINGARM)),
    PREPPEDFORCLIMB(new ClimberReq(ClimberState.SWINGARM)); //stays in swing arm, just using state change as indicator

    Set<BaseSubsystem> requiredSubsystems;
    List<SubsystemRequirement> subsystemRequirements;

    ClimbPrepState(SubsystemRequirement... requirements) {
        subsystemRequirements = Arrays.asList(requirements);
        requiredSubsystems = subsystemRequirements.stream().map(requirement -> requirement.getSubsystem()).collect(Collectors.toSet());
    }

    @Override
    public Set<BaseSubsystem> getRequiredSubsystems() {
        return requiredSubsystems;
    }

    @Override
    public boolean requireSubsystems(BaseSequence<? extends ISequenceState> sequence) {
        return ISequenceState.requireSubsystems(sequence, subsystemRequirements);
    }

}
