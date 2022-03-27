package frc.robot.sequences.parent;

import java.util.List;

import frc.robot.subsystems.parent.BaseSubsystem;

public interface IState{
    
    List<BaseSubsystem> getRequiredSubsystems();

    boolean requireSubsystems(BaseSequence<? extends IState> sequence);

    String getName();

    static boolean requireSubsystems(BaseSequence<? extends IState> sequence, List<BaseSubsystem> requiredSubsystems, IState state){
        for (BaseSubsystem subsystem : requiredSubsystems) {
            if (subsystem.isRequiredByAnother(sequence)) {
                return false;
            }
        }
        for (BaseSubsystem subsystem : requiredSubsystems) {
            subsystem.require(sequence, state);
        }
        return true;
    }

}