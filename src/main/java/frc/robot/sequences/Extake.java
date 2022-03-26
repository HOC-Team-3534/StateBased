package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;

import frc.robot.Robot;
import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.IState;
import frc.robot.subsystems.parent.BaseSubsystem;

public class Extake extends BaseSequence<ExtakeState> {

    public Extake (ExtakeState neutralState, ExtakeState startState) {
        super(neutralState, startState);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void process() {
        switch (getState()) {
            case EXTAKE:
                if (!Buttons.Extake.getButton()) {
                    setNextState(ExtakeState.NEUTRAL);
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

enum ExtakeState implements IState {
    NEUTRAL,
    EXTAKE(Robot.intake);

    List<BaseSubsystem> requiredSubsystems;

    ExtakeState(BaseSubsystem... subsystems) {
        requiredSubsystems = Arrays.asList(subsystems);
    }

    @Override
    public List<BaseSubsystem> getRequiredSubsystems() {
        return requiredSubsystems;
    }

    @Override
    public boolean requireSubsystems(BaseSequence<? extends IState> sequence) {
        return IState.requireSubsystems(sequence, requiredSubsystems, this);
    }

    @Override
    public String getName() {
        return this.name();
    }
}
