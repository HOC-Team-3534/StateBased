package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;

import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.IState;
import frc.robot.subsystems.parent.BaseSubsystem;

public class Burp extends BaseSequence<BurpState> {

    public Burp(BurpState neutralState, BurpState startState) {
        super(neutralState, startState);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void process() {
        switch (getState()) {
            case BURP:
                if(!Buttons.Burp.getButton()){
                    setNextState(BurpState.RETRACT);
                }if (this.getTimeSinceStartOfState() > 500 && RobotMap.shooter.getClosedLoopError() < 250) {
                    System.out.println("In state");
                    setNextState(BurpState.PUNCH);
                }
                break;
            case PUNCH:
                if (this.getTimeSinceStartOfState() > 250) {
                    setNextState(BurpState.RETRACT);
                }
                break;
            case RETRACT:
                if(!Buttons.Burp.getButton() && this.getTimeSinceStartOfState() > 250){
                    setNextState(BurpState.NEUTRAL);
                }else if (this.getTimeSinceStartOfState() > 250) {
                    setNextState(BurpState.BOOT);
                }
                break;
            case BOOT:
                if(this.getTimeSinceStartOfState() > 150){
                    setNextState(BurpState.BURP);
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

enum BurpState implements IState {
    NEUTRAL,
    BURP(Robot.shooter),
    PUNCH(Robot.shooter),
    RETRACT(Robot.shooter),
    BOOT(Robot.shooter);

    List<BaseSubsystem> requiredSubsystems;

    BurpState(BaseSubsystem... subsystems) {
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
