package frc.robot.sequences.parent;

import java.util.List;

import frc.robot.subsystems.parent.BaseSubsystem;

public interface IState{
    
    List<BaseSubsystem> getRequiredSubsystems();

    boolean requireSubsystems(BaseSequence<? extends IState> sequence);

    String getName();

}