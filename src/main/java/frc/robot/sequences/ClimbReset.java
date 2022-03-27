package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.subsystems.parent.BaseSubsystem;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.ClimberReq;
import frc.robot.subsystems.states.ClimberState;

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

enum ClimbResetState implements ISequenceState {
    NEUTRAL,
    MOVEARMMANUALLY(new ClimberReq(ClimberState.MOVEARMMANUALLY));

    Set<BaseSubsystem> requiredSubsystems;
    List<SubsystemRequirement> subsystemRequirements;

    ClimbResetState(SubsystemRequirement... requirements) {
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

