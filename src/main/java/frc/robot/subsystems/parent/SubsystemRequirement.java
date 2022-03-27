package frc.robot.sequences.parent;

import frc.robot.subsystems.parent.BaseSubsystem;
import frc.robot.subsystems.parent.ISubsystemState;

import java.util.InputMismatchException;

public class SubsystemRequirement<BaseS extends BaseSubsystem, SsS extends ISubsystemState<BaseS>> {

    BaseSubsystem<? extends ISubsystemState> subsystem;
    ISubsystemState subsystemState;

    public SubsystemRequirement(BaseS subsystem, SsS subsystemState) {
        this.subsystem = subsystem;
        this.subsystemState = subsystemState;

        if(!subsystemState.getAssociatedSubsystem().equals(subsystem)){
            throw new InputMismatchException(String.format("The subsystem state: %s, does not belong to the desired subsystem"));
        }
    }

    public BaseSubsystem<? extends ISubsystemState> getSubsystem() {
        return subsystem;
    }

    public ISubsystemState getSubsystemState() {
        return subsystemState;
    }
}
