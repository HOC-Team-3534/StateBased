package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;

import frc.robot.Robot;
import frc.robot.sequences.parent.BaseAutonSequence;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.IState;
import frc.robot.subsystems.parent.BaseSubsystem;

public class Autons extends BaseAutonSequence<AutonState> {

    public Autons(AutonState neutralState, AutonState startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getState()) {
            case MOVETO1:
                break;
            case PICKUP1:
                break;
            case MOVETOSHOOT1:
                break;
            case SHOOT1:
                break;
            case MOVETO2:
                break;
            case PICKUP2:
                break;
            case MOVETO3:
                break;
            case PICKUP3:
                break;
            case SHOOT2:
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
        // TODO Auto-generated method stub
        return false;
    }

}

enum AutonState implements IState {
    NEUTRAL,
    MOVETO1(Robot.swerveDrive),
    PICKUP1(Robot.intake),
    MOVETOSHOOT1(Robot.swerveDrive),
    SHOOT1(Robot.shooter),
    MOVETO2(Robot.swerveDrive),
    PICKUP2(Robot.intake),
    MOVETO3(Robot.swerveDrive),
    PICKUP3(Robot.intake),
    MOVETOSHOOT2(Robot.swerveDrive),
    SHOOT2(Robot.shooter);

    List<BaseSubsystem> requiredSubsystems;

    AutonState(BaseSubsystem... subsystems) {
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
