package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;

import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.IState;
import frc.robot.subsystems.parent.BaseSubsystem;

public class Climb extends BaseSequence<ClimbState> {

    int limitSwitchLoopCounter = 0;

    public Climb(ClimbState neutralState, ClimbState startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getState()) {
            case GRIPMIDBAR:
                if (getTimeSinceStartOfState() > 1000) {
                    setNextState(ClimbState.SWINGMIDHIGH);
                }
                break;
            case SWINGMIDHIGH:
                if ((RobotMap.m_l3Switch.get() || RobotMap.m_h4Switch.get()) && Robot.climber.getClimbArmDegree() > 180.0) {
                    setNextState(ClimbState.GRIPHIGHBAR);
                }
                break;
            case GRIPHIGHBAR:
                if (getTimeSinceStartOfState() > 1000 && (RobotMap.m_l3Switch.get() || RobotMap.m_h4Switch.get())) {
                    setNextState(ClimbState.RELEASEMIDBAR);
                }else if(!(RobotMap.m_l3Switch.get() || RobotMap.m_h4Switch.get())){
                    limitSwitchLoopCounter++;
                    if(limitSwitchLoopCounter >= 5) {
                        limitSwitchLoopCounter = 0;
                        setNextState(ClimbState.RETRYHIGHBAR);
                    }
                }else{
                    limitSwitchLoopCounter = 0;
                }
                break;
            case RETRYHIGHBAR:
                if(getTimeSinceStartOfState() > 1500){
                    setNextState(ClimbState.SWINGMIDHIGH);
                }
                break;
            case RELEASEMIDBAR:
                if (getTimeSinceStartOfState() > 1000) {
                    setNextState(ClimbState.SWINGHIGHTRAVERSAL);
                }
                break;
            case SWINGHIGHTRAVERSAL:
                if ((RobotMap.m_h2Switch.get() || RobotMap.m_l1Switch.get()) && Robot.climber.getClimbArmDegree() > 360.0) {
                    setNextState(ClimbState.GRIPTRAVERSALBAR);
                }
                break;
            case GRIPTRAVERSALBAR:
                if (getTimeSinceStartOfState() > 1000 && (RobotMap.m_h2Switch.get() || RobotMap.m_l1Switch.get())) {
                    setNextState(ClimbState.RELEASEHIGHBAR);
                }else if(!(RobotMap.m_h2Switch.get() || RobotMap.m_l1Switch.get())){
                    limitSwitchLoopCounter++;
                    if(limitSwitchLoopCounter >= 5) {
                        limitSwitchLoopCounter = 0;
                        setNextState(ClimbState.RETRYTRAVERSALBAR);
                    }
                }else{
                    limitSwitchLoopCounter = 0;
                }
                break;
            case RETRYTRAVERSALBAR:
                if(getTimeSinceStartOfState() > 1500){
                    setNextState(ClimbState.SWINGHIGHTRAVERSAL);
                }
                break;
            case RELEASEHIGHBAR:
                if (getTimeSinceStartOfState() > 1000) {
                    setNextState(ClimbState.SWINGTOREST);
                }
                break;
            case SWINGTOREST:
                if(RobotMap.m_climbMotor.getSelectedSensorPosition() >= 440){                
                    System.out.println("Hooray!");
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
        if (Robot.climber.abort()) {
            setNextState(getNeutralState());
            return updateState();
        }
        return false;
    }

}

enum ClimbState implements IState {
    NEUTRAL,
    GRIPMIDBAR(Robot.climber, Robot.swerveDrive),
    SWINGMIDHIGH(Robot.climber, Robot.swerveDrive),
    GRIPHIGHBAR(Robot.climber, Robot.swerveDrive),
    RETRYHIGHBAR(Robot.climber, Robot.swerveDrive),
    RELEASEMIDBAR(Robot.climber, Robot.swerveDrive),
    SWINGHIGHTRAVERSAL(Robot.climber, Robot.swerveDrive),
    GRIPTRAVERSALBAR(Robot.climber, Robot.swerveDrive),
    RETRYTRAVERSALBAR(Robot.climber, Robot.swerveDrive),
    RELEASEHIGHBAR(Robot.climber, Robot.swerveDrive),
    SWINGTOREST(Robot.climber, Robot.swerveDrive);

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
