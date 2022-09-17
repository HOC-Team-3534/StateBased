package frc.robot.sequences.parent;

import frc.robot.subsystems.parent.BaseSubsystem;
import frc.robot.subsystems.parent.SubsystemRequirement;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SequenceState {

    List<SubsystemRequirement> subsystemRequirements;
    Set<BaseSubsystem> requiredSubsystems;

    public SequenceState(SubsystemRequirement... requirements) {
        subsystemRequirements = Arrays.asList(requirements);
        requiredSubsystems = subsystemRequirements.stream().map(requirement -> requirement.getSubsystem()).collect(Collectors.toSet());
    }

    public Set<BaseSubsystem> getRequiredSubsystems() {
        return requiredSubsystems;
    }

    public boolean requireSubsystems(BaseSequence<? extends ISequenceState> sequence) {
        return ISequenceState.requireSubsystems(sequence, subsystemRequirements);
    }

}
