package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.sequences.parent.SequenceState;
import frc.robot.subsystems.parent.BaseSubsystem;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.ClimberReq;
import frc.robot.subsystems.requirements.SwerveDriveReq;
import frc.robot.subsystems.ClimberState;
import frc.robot.subsystems.SwerveDriveState;

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
                if(!Robot.climber.l3Switch.get() || !Robot.climber.h4Switch.get()) {
                    setNextState(ClimbState.GRIPHIGHBAR);
                }
                if(!Buttons.Climb.getButton()){
                    savedPauseState = getState();
                    setNextState(ClimbState.PAUSED);
                }
                break;
            case GRIPHIGHBAR:
                if (getTimeSinceStartOfState() > 500 && (!Robot.climber.l3Switch.get() || !Robot.climber.h4Switch.get())) {
                    setNextState(ClimbState.RECENTERMIDHIGHBAR);
                }else if((Robot.climber.l3Switch.get() && Robot.climber.h4Switch.get())){
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
                if(!Robot.climber.h2Switch.get() || !Robot.climber.l1Switch.get()){
                    setNextState(ClimbState.GRIPTRAVERSALBAR);
                }
                if(!Buttons.Climb.getButton()){
                    savedPauseState = getState();
                    setNextState(ClimbState.PAUSED);
                }
                break;
            case GRIPTRAVERSALBAR:
                if (getTimeSinceStartOfState() > 500 && (!Robot.climber.h2Switch.get() || !Robot.climber.l1Switch.get())) {
                    setNextState(ClimbState.RECENTERHIGHTRAVERSALBAR);
                }else if((Robot.climber.h2Switch.get() && Robot.climber.l1Switch.get())){
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
                if(Robot.climber.getClimbArmDegree() >= 445){
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

enum ClimbState implements ISequenceState {
    NEUTRAL,
    PAUSED(new ClimberReq(ClimberState.NEUTRAL), new SwerveDriveReq(SwerveDriveState.NEUTRAL)),
    GRIPMIDBAR(new ClimberReq(ClimberState.GRIPMIDBAR), new SwerveDriveReq(SwerveDriveState.NEUTRAL)),
    SWINGMIDHIGH1(new ClimberReq(ClimberState.SWINGMIDHIGH1), new SwerveDriveReq(SwerveDriveState.NEUTRAL)),
    SWINGMIDHIGH2(new ClimberReq(ClimberState.SWINGMIDHIGH2), new SwerveDriveReq(SwerveDriveState.NEUTRAL)),
    GRIPHIGHBAR(new ClimberReq(ClimberState.GRIPHIGHBAR), new SwerveDriveReq(SwerveDriveState.NEUTRAL)),
    RETRYHIGHBAR(new ClimberReq(ClimberState.RETRYHIGHBAR), new SwerveDriveReq(SwerveDriveState.NEUTRAL)),
    RECENTERMIDHIGHBAR(new ClimberReq(ClimberState.RECENTERMIDHIGHBAR), new SwerveDriveReq(SwerveDriveState.NEUTRAL)),
    RELEASEMIDBAR(new ClimberReq(ClimberState.RELEASEMIDBAR), new SwerveDriveReq(SwerveDriveState.NEUTRAL)),
    SWINGHIGHTRAVERSAL1(new ClimberReq(ClimberState.SWINGHIGHTRAVERSAL1), new SwerveDriveReq(SwerveDriveState.NEUTRAL)),
    SWINGHIGHTRAVERSAL2(new ClimberReq(ClimberState.SWINGHIGHTRAVERSAL2), new SwerveDriveReq(SwerveDriveState.NEUTRAL)),
    GRIPTRAVERSALBAR(new ClimberReq(ClimberState.GRIPTRAVERSALBAR), new SwerveDriveReq(SwerveDriveState.NEUTRAL)),
    RETRYTRAVERSALBAR(new ClimberReq(ClimberState.RETRYTRAVERSALBAR), new SwerveDriveReq(SwerveDriveState.NEUTRAL)),
    RECENTERHIGHTRAVERSALBAR(new ClimberReq(ClimberState.RECENTERHIGHTRAVERSALBAR), new SwerveDriveReq(SwerveDriveState.NEUTRAL)),
    RELEASEHIGHBAR(new ClimberReq(ClimberState.RELEASEHIGHBAR), new SwerveDriveReq(SwerveDriveState.NEUTRAL)),
    SWINGTOREST(new ClimberReq(ClimberState.SWINGTOREST), new SwerveDriveReq(SwerveDriveState.NEUTRAL));

    SequenceState state;

    ClimbState(SubsystemRequirement... requirements) {
        state = new SequenceState(requirements);
    }

    @Override
    public SequenceState getState() {
        return state;
    }

}
