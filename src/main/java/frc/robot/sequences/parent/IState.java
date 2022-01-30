package frc.robot.sequences;

import java.util.List;

import frc.robot.subsystems.BaseSubsystem;

public interface IState{
    
    List<BaseSubsystem> getRequiredSubsystems();

    boolean requireSubsystems(BaseSequence<? extends IState> sequence);

    String getName();

}