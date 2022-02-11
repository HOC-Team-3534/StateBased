package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;

import frc.robot.Robot;
import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.IState;
import frc.robot.subsystems.parent.BaseSubsystem;

public class IntakeSeq extends BaseSequence<IntakeState> {

    public IntakeSeq (IntakeState neutralState, IntakeState startState) {
        super(neutralState, startState);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void process() {
        switch (getState()) {
            case KICKOUT:
                if (!Buttons.Intake.getButton()) {
                    setNextState(IntakeState.RETRACT);
                }
                break;
            case RETRACT:
                if(getTimeSinceStartOfState() > 1000){
                    setNextState(IntakeState.NEUTRAL);
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

enum IntakeState implements IState {
    NEUTRAL,
    KICKOUT(Robot.intake),
    RETRACT(Robot.intake);

    List<BaseSubsystem> requiredSubsystems;

    IntakeState(BaseSubsystem... subsystems) {
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
