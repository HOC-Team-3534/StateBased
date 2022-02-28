package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;

import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.IState;
import frc.robot.subsystems.parent.BaseSubsystem;

public class Shoot extends BaseSequence<ShootState> {

    public Shoot(ShootState neutralState, ShootState startState) {
        super(neutralState, startState);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void process() {
        switch (getState()) {
            case WAITNSPIN:
                if (!Buttons.Shoot.getButton()) {
                    setNextState(ShootState.NEUTRAL);
                }
                if (this.getTimeSinceStartOfState() > 1500 && RobotMap.shooter.getClosedLoopError() < 100) {
                    System.out.println("In state");
                    setNextState(ShootState.PUNCH);
                }
                break;
            case PUNCH:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(ShootState.RETRACT);
                }
                break;
            case RETRACT:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(ShootState.WAITNSPIN);
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
        // TODO Auto-generated method stub
        return false;
    }

}

enum ShootState implements IState {
    NEUTRAL,
    WAITNSPIN(Robot.shooter),
    PUNCH(Robot.shooter),
    RETRACT(Robot.shooter);

    List<BaseSubsystem> requiredSubsystems;

    ShootState(BaseSubsystem... subsystems) {
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
