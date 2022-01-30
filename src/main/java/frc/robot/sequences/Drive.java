package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;

import frc.robot.Robot;
import frc.robot.subsystems.BaseSubsystem;

public class Drive extends BaseSequence<DriveState> {

    public Drive(DriveState neutralState, DriveState startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getState()) {
            case DRIVE:
                break;
            case NEUTRAL:
                break;
            default:
                break;

        }
    }

    @Override
    public boolean abort() {
        // TODO Auto-generated method stub
        return false;
    }

}

enum DriveState implements IState {
    NEUTRAL,
    DRIVE(Robot.swerveDrive);

    List<BaseSubsystem> requiredSubsystems;

    DriveState(BaseSubsystem... subsystems) {
        requiredSubsystems = Arrays.asList(subsystems);
    }

    @Override
    public List<BaseSubsystem> getRequiredSubsystems() {
        return requiredSubsystems;
    }

    @Override
    public boolean requireSubsystems(BaseSequence<? extends IState> sequence) {
        for (BaseSubsystem subsystem : requiredSubsystems) {
            if (subsystem.isRequired()) {
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
