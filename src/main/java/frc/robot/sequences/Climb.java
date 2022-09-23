package frc.robot.sequences;

import static frc.robot.Constants.CLIMBER.*;
import frc.robot.Robot;
import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.sequences.parent.SequenceState;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.ClimberState;
import frc.robot.subsystems.SwerveDriveState;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.ClimberReq;
import frc.robot.subsystems.requirements.SwerveDriveReq;

import static frc.robot.sequences.ClimbState.*;

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
                    setNextState(SWINGMIDHIGH1);
                }
                break;
            case SWINGMIDHIGH1:
                if (Robot.climber.getClimbArmDegree() > MIDHIGHBAR_SLOWDOWN_ANGLE) {
                    setNextState(SWINGMIDHIGH2);
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getState();
                    setNextState(PAUSED);
                }
                break;
            case SWINGMIDHIGH2:
                if (!Climber.l3Switch.get() || !Climber.h4Switch.get()) {
                    setNextState(GRIPHIGHBAR);
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getState();
                    setNextState(PAUSED);
                }
                break;
            case GRIPHIGHBAR:
                if (getTimeSinceStartOfState() > 500 && (!Climber.l3Switch.get() || !Climber.h4Switch.get())) {
                    setNextState(RECENTERMIDHIGHBAR);
                } else if ((Climber.l3Switch.get() && Climber.h4Switch.get())) {
                    limitSwitchLoopCounter++;
                    if (limitSwitchLoopCounter >= 10) {
                        limitSwitchLoopCounter = 0;
                        setNextState(RETRYHIGHBAR);
                    }
                } else {
                    limitSwitchLoopCounter = 0;
                }
                break;
            case RETRYHIGHBAR:
                if (getTimeSinceStartOfState() > 1500) {
                    setNextState(SWINGMIDHIGH2);
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getState();
                    setNextState(PAUSED);
                }
                break;
            case RECENTERMIDHIGHBAR:
                if (Robot.climber.getClimbArmDegree() - RECENTER_ANGLE_TOLERANCE < MIDHIGHBAR_RECENTER_ANGLE_COMMAND) {
                    setNextState(RELEASEMIDBAR);
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getState();
                    setNextState(PAUSED);
                }
                break;
            case RELEASEMIDBAR:
                if (Robot.climber.getClimbArmDegree() > DONERELEASINGMIDBAR_ANGLE) {
                    setNextState(SWINGHIGHTRAVERSAL1);
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getState();
                    setNextState(PAUSED);
                }
                break;
            case SWINGHIGHTRAVERSAL1:
                if (Robot.climber.getClimbArmDegree() > HIGHTRAVERSAL_SLOWDOWN_ANGLE) {
                    setNextState(SWINGHIGHTRAVERSAL2);
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getState();
                    setNextState(PAUSED);
                }
                break;
            case SWINGHIGHTRAVERSAL2:
                if (!Climber.h2Switch.get() || !Climber.l1Switch.get()) {
                    setNextState(GRIPTRAVERSALBAR);
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getState();
                    setNextState(PAUSED);
                }
                break;
            case GRIPTRAVERSALBAR:
                if (getTimeSinceStartOfState() > 500 && (!Climber.h2Switch.get() || !Climber.l1Switch.get())) {
                    setNextState(RECENTERHIGHTRAVERSALBAR);
                } else if ((Climber.h2Switch.get() && Climber.l1Switch.get())) {
                    limitSwitchLoopCounter++;
                    if (limitSwitchLoopCounter >= 10) {
                        limitSwitchLoopCounter = 0;
                        setNextState(RETRYTRAVERSALBAR);
                    }
                } else {
                    limitSwitchLoopCounter = 0;
                }
                break;
            case RETRYTRAVERSALBAR:
                if (getTimeSinceStartOfState() > 1500) {
                    setNextState(SWINGHIGHTRAVERSAL2);
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getState();
                    setNextState(PAUSED);
                }
                break;
            case RECENTERHIGHTRAVERSALBAR:
                if (Robot.climber.getClimbArmDegree() - RECENTER_ANGLE_TOLERANCE < HIGHTRAVERSALBAR_RECENTER_ANGLE_COMMAND) {
                    setNextState(RELEASEHIGHBAR);
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getState();
                    setNextState(PAUSED);
                }
                break;
            case RELEASEHIGHBAR:
                setNextState(SWINGTOREST);
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getState();
                    setNextState(PAUSED);
                }
                break;
            case SWINGTOREST:
                if (Robot.climber.getClimbArmDegree() >= 445) {
                    System.out.println("Hooray!");
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getState();
                    setNextState(PAUSED);
                }
                break;
            case PAUSED:
                if (Buttons.Climb.getButton()) {
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
