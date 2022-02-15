package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;

import edu.wpi.first.wpilibj.PS4Controller.Button;
import frc.robot.subsystems.Climber;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.IState;
import frc.robot.subsystems.parent.BaseSubsystem;

public class Climb extends BaseSequence<ClimbState> {

    public Climb(ClimbState neutralState, ClimbState startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getState()) {
            case GRIPMIDBAR:
            if(Buttons.Climb.getButton() && getTimeSinceStartOfState() > 30){
                setNextState(ClimbState.SWINGMIDHIGH);
            }
                break;
            case SWINGMIDHIGH:
            if(RobotMap.m_climbMotor.getSelectedSensorPosition() > 180 && !(RobotMap.m_l3Switch.get() || RobotMap.m_h4Switch.get())){
                abort();
            }
            if(Buttons.Climb.getButton() && (RobotMap.m_l3Switch.get() || RobotMap.m_h4Switch.get())){
                setNextState(ClimbState.GRIPHIGHBAR);
            }
                break;
            case GRIPHIGHBAR:
            if(Buttons.Climb.getButton() && getTimeSinceStartOfState() > 40){
                setNextState(ClimbState.RELEASEMIDBAR);
            }
                break;
            case RELEASEMIDBAR:
            if(Buttons.Climb.getButton() && getTimeSinceStartOfState() > 500){
                setNextState(ClimbState.SWINGHIGHTRAVERSAL);
            }
                break;
            case SWINGHIGHTRAVERSAL:
            if(RobotMap.m_climbMotor.getSelectedSensorPosition() > 270 && !(RobotMap.m_h2Switch.get() || RobotMap.m_l1Switch.get())){
                abort();
            }
            if(Buttons.Climb.getButton() && (RobotMap.m_h2Switch.get() || RobotMap.m_l1Switch.get())){
                setNextState(ClimbState.GRIPTRAVERSALBAR);
            }
                break;
            case GRIPTRAVERSALBAR:
            if(Buttons.Climb.getButton()){
                setNextState(ClimbState.RELEASEHIGHBAR);
            }
                break;
            case RELEASEHIGHBAR:
            if(Buttons.Climb.getButton() && getTimeSinceStartOfState() > 500){
                setNextState(ClimbState.SWINGTOREST);
            }
                break;
            case SWINGTOREST:
            if(Buttons.Climb.getButton()){
                setNextState(ClimbState.NEUTRAL);
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

enum ClimbState implements IState {
    NEUTRAL,
    GRIPMIDBAR(Robot.climber),
    SWINGMIDHIGH(Robot.climber),
    GRIPHIGHBAR(Robot.climber),
    RELEASEMIDBAR(Robot.climber),
    SWINGHIGHTRAVERSAL(Robot.climber),
    GRIPTRAVERSALBAR(Robot.climber),
    RELEASEHIGHBAR(Robot.climber),
    SWINGTOREST(Robot.climber);

    List<BaseSubsystem> requiredSubsystems;

    ClimbState(BaseSubsystem... subsystems) {
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
