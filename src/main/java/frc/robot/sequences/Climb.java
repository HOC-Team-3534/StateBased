package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;

import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.IState;
import frc.robot.subsystems.parent.BaseSubsystem;

public class Climb extends BaseSequence<ClimbState> {

    int limitSwitchLoopCounter = 0;
    ClimbState savedPauseState;

    public Climb(ClimbState neutralState, ClimbState startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getState()) {
            case GRIPMIDBAR:
                if (getTimeSinceStartOfState() > 500) {
                    setNextState(ClimbState.SWINGMIDHIGH1);
                }
                break;
            case SWINGMIDHIGH1:
                if (Robot.climber.getClimbArmDegree() > Constants.MIDHIGHBAR_SLOWDOWN_ANGLE) {
                    setNextState(ClimbState.SWINGMIDHIGH2);
                }
                if(!Buttons.Climb.getButton()){
                    savedPauseState = getState();
                    setNextState(ClimbState.PAUSED);
                }
                break;
            case SWINGMIDHIGH2:
                if(!RobotMap.m_l3Switch.get() || !RobotMap.m_h4Switch.get()) {
                    setNextState(ClimbState.GRIPHIGHBAR);
                }
                if(!Buttons.Climb.getButton()){
                    savedPauseState = getState();
                    setNextState(ClimbState.PAUSED);
                }
                break;
            case GRIPHIGHBAR:
                if (getTimeSinceStartOfState() > 500 && (!RobotMap.m_l3Switch.get() || !RobotMap.m_h4Switch.get())) {
                    setNextState(ClimbState.RECENTERMIDHIGHBAR);
                }else if((RobotMap.m_l3Switch.get() && RobotMap.m_h4Switch.get())){
                    limitSwitchLoopCounter++;
                    if(limitSwitchLoopCounter >= 10) {
                        limitSwitchLoopCounter = 0;
                        setNextState(ClimbState.RETRYHIGHBAR);
                    }
                }else{
                    limitSwitchLoopCounter = 0;
                }
                break;
            case RETRYHIGHBAR:
                if(getTimeSinceStartOfState() > 1500){
                    setNextState(ClimbState.SWINGMIDHIGH2);
                }
                if(!Buttons.Climb.getButton()){
                    savedPauseState = getState();
                    setNextState(ClimbState.PAUSED);
                }
                break;
            case RECENTERMIDHIGHBAR:
                if(Robot.climber.getClimbArmDegree() - Constants.RECENTER_ANGLE_TOLERANCE < Constants.MIDHIGHBAR_RECENTER_ANGLE_COMMAND){
                    setNextState(ClimbState.RELEASEMIDBAR);
                }
                if(!Buttons.Climb.getButton()){
                    savedPauseState = getState();
                    setNextState(ClimbState.PAUSED);
                }
                break;
            case RELEASEMIDBAR:
                if (Robot.climber.getClimbArmDegree() > Constants.DONERELEASINGMIDBAR_ANGLE) {
                    setNextState(ClimbState.SWINGHIGHTRAVERSAL1);
                }
                if(!Buttons.Climb.getButton()){
                    savedPauseState = getState();
                    setNextState(ClimbState.PAUSED);
                }
                break;
            case SWINGHIGHTRAVERSAL1:
                if (Robot.climber.getClimbArmDegree() > Constants.HIGHTRAVERSAL_SLOWDOWN_ANGLE) {
                    setNextState(ClimbState.SWINGHIGHTRAVERSAL2);
                }
                if(!Buttons.Climb.getButton()){
                    savedPauseState = getState();
                    setNextState(ClimbState.PAUSED);
                }
                break;
            case SWINGHIGHTRAVERSAL2:
                if(!RobotMap.m_h2Switch.get() || !RobotMap.m_l1Switch.get()){
                    setNextState(ClimbState.GRIPTRAVERSALBAR);
                }
                if(!Buttons.Climb.getButton()){
                    savedPauseState = getState();
                    setNextState(ClimbState.PAUSED);
                }
                break;
            case GRIPTRAVERSALBAR:
                if (getTimeSinceStartOfState() > 500 && (!RobotMap.m_h2Switch.get() || !RobotMap.m_l1Switch.get())) {
                    setNextState(ClimbState.RECENTERHIGHTRAVERSALBAR);
                }else if((RobotMap.m_h2Switch.get() && RobotMap.m_l1Switch.get())){
                    limitSwitchLoopCounter++;
                    if(limitSwitchLoopCounter >= 10) {
                        limitSwitchLoopCounter = 0;
                        setNextState(ClimbState.RETRYTRAVERSALBAR);
                    }
                }else{
                    limitSwitchLoopCounter = 0;
                }
                break;
            case RETRYTRAVERSALBAR:
                if(getTimeSinceStartOfState() > 1500){
                    setNextState(ClimbState.SWINGHIGHTRAVERSAL2);
                }
                if(!Buttons.Climb.getButton()){
                    savedPauseState = getState();
                    setNextState(ClimbState.PAUSED);
                }
                break;
            case RECENTERHIGHTRAVERSALBAR:
                if(Robot.climber.getClimbArmDegree() - Constants.RECENTER_ANGLE_TOLERANCE < Constants.HIGHTRAVERSALBAR_RECENTER_ANGLE_COMMAND){
                    setNextState(ClimbState.RELEASEHIGHBAR);
                }
                if(!Buttons.Climb.getButton()){
                    savedPauseState = getState();
                    setNextState(ClimbState.PAUSED);
                }
                break;
            case RELEASEHIGHBAR:
                setNextState(ClimbState.SWINGTOREST);
                if(!Buttons.Climb.getButton()){
                    savedPauseState = getState();
                    setNextState(ClimbState.PAUSED);
                }
                break;
            case SWINGTOREST:
                if(RobotMap.m_climbMotor.getSelectedSensorPosition() >= 445){
                    System.out.println("Hooray!");
                }
                if(!Buttons.Climb.getButton()){
                    savedPauseState = getState();
                    setNextState(ClimbState.PAUSED);
                }
                break;
            case PAUSED:
                if(Buttons.Climb.getButton()){
                    setNextState(savedPauseState);
                    savedPauseState = null;
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
    PAUSED(Robot.climber, Robot.swerveDrive),
    GRIPMIDBAR(Robot.climber, Robot.swerveDrive),
    SWINGMIDHIGH1(Robot.climber, Robot.swerveDrive),
    SWINGMIDHIGH2(Robot.climber, Robot.swerveDrive),
    GRIPHIGHBAR(Robot.climber, Robot.swerveDrive),
    RETRYHIGHBAR(Robot.climber, Robot.swerveDrive),
    RECENTERMIDHIGHBAR(Robot.climber, Robot.swerveDrive),
    RELEASEMIDBAR(Robot.climber, Robot.swerveDrive),
    SWINGHIGHTRAVERSAL1(Robot.climber, Robot.swerveDrive),
    SWINGHIGHTRAVERSAL2(Robot.climber, Robot.swerveDrive),
    GRIPTRAVERSALBAR(Robot.climber, Robot.swerveDrive),
    RETRYTRAVERSALBAR(Robot.climber, Robot.swerveDrive),
    RECENTERHIGHTRAVERSALBAR(Robot.climber, Robot.swerveDrive),
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
        return IState.requireSubsystems(sequence, requiredSubsystems, this);
    }

    @Override
    public String getName() {
        return this.name();
    }
}
