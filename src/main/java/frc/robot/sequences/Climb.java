package frc.robot.sequences;

import static frc.robot.Constants.CLIMBER.*;
import frc.robot.Robot;
import frc.robot.RobotContainer.Buttons;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.ClimberState;
import frc.robot.subsystems.SwerveDriveState;
import frc.robot.subsystems.requirements.ClimberReq;
import frc.robot.subsystems.requirements.SwerveDriveReq;

import static frc.robot.sequences.ClimbPhase.*;

import frc.BaseSequence;
import frc.ISequencePhase;
import frc.SequencePhase;
import frc.SubsystemRequirement;

enum ClimbPhase implements ISequencePhase {
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

    SequencePhase state;

    ClimbPhase(SubsystemRequirement... requirements) {
        state = new SequencePhase(requirements);
    }

    @Override
    public SequencePhase getPhase() {
        return state;
    }

}

public class Climb extends BaseSequence<ClimbPhase> {

    int limitSwitchLoopCounter = 0;
    ClimbPhase savedPauseState;

    public Climb(ClimbPhase neutralState, ClimbPhase startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getPhase()) {
            case GRIPMIDBAR:
                if (getTimeSinceStartOfPhase() > 500) {
                    setNextPhase(SWINGMIDHIGH1);
                }
                break;
            case SWINGMIDHIGH1:
                if (Robot.climber.getClimbArmDegree() > MIDHIGHBAR_SLOWDOWN_ANGLE) {
                    setNextPhase(SWINGMIDHIGH2);
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getPhase();
                    setNextPhase(PAUSED);
                }
                break;
            case SWINGMIDHIGH2:
                if (!Climber.l3Switch.get() || !Climber.h4Switch.get()) {
                    setNextPhase(GRIPHIGHBAR);
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getPhase();
                    setNextPhase(PAUSED);
                }
                break;
            case GRIPHIGHBAR:
                if (getTimeSinceStartOfPhase() > 500 && (!Climber.l3Switch.get() || !Climber.h4Switch.get())) {
                    setNextPhase(RECENTERMIDHIGHBAR);
                } else if ((Climber.l3Switch.get() && Climber.h4Switch.get())) {
                    limitSwitchLoopCounter++;
                    if (limitSwitchLoopCounter >= 10) {
                        limitSwitchLoopCounter = 0;
                        setNextPhase(RETRYHIGHBAR);
                    }
                } else {
                    limitSwitchLoopCounter = 0;
                }
                break;
            case RETRYHIGHBAR:
                if (getTimeSinceStartOfPhase() > 1500) {
                    setNextPhase(SWINGMIDHIGH2);
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getPhase();
                    setNextPhase(PAUSED);
                }
                break;
            case RECENTERMIDHIGHBAR:
                if (Robot.climber.getClimbArmDegree() - RECENTER_ANGLE_TOLERANCE < MIDHIGHBAR_RECENTER_ANGLE_COMMAND) {
                    setNextPhase(RELEASEMIDBAR);
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getPhase();
                    setNextPhase(PAUSED);
                }
                break;
            case RELEASEMIDBAR:
                if (Robot.climber.getClimbArmDegree() > DONERELEASINGMIDBAR_ANGLE) {
                    setNextPhase(SWINGHIGHTRAVERSAL1);
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getPhase();
                    setNextPhase(PAUSED);
                }
                break;
            case SWINGHIGHTRAVERSAL1:
                if (Robot.climber.getClimbArmDegree() > HIGHTRAVERSAL_SLOWDOWN_ANGLE) {
                    setNextPhase(SWINGHIGHTRAVERSAL2);
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getPhase();
                    setNextPhase(PAUSED);
                }
                break;
            case SWINGHIGHTRAVERSAL2:
                if (!Climber.h2Switch.get() || !Climber.l1Switch.get()) {
                    setNextPhase(GRIPTRAVERSALBAR);
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getPhase();
                    setNextPhase(PAUSED);
                }
                break;
            case GRIPTRAVERSALBAR:
                if (getTimeSinceStartOfPhase() > 500 && (!Climber.h2Switch.get() || !Climber.l1Switch.get())) {
                    setNextPhase(RECENTERHIGHTRAVERSALBAR);
                } else if ((Climber.h2Switch.get() && Climber.l1Switch.get())) {
                    limitSwitchLoopCounter++;
                    if (limitSwitchLoopCounter >= 10) {
                        limitSwitchLoopCounter = 0;
                        setNextPhase(RETRYTRAVERSALBAR);
                    }
                } else {
                    limitSwitchLoopCounter = 0;
                }
                break;
            case RETRYTRAVERSALBAR:
                if (getTimeSinceStartOfPhase() > 1500) {
                    setNextPhase(SWINGHIGHTRAVERSAL2);
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getPhase();
                    setNextPhase(PAUSED);
                }
                break;
            case RECENTERHIGHTRAVERSALBAR:
                if (Robot.climber.getClimbArmDegree() - RECENTER_ANGLE_TOLERANCE < HIGHTRAVERSALBAR_RECENTER_ANGLE_COMMAND) {
                    setNextPhase(RELEASEHIGHBAR);
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getPhase();
                    setNextPhase(PAUSED);
                }
                break;
            case RELEASEHIGHBAR:
                setNextPhase(SWINGTOREST);
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getPhase();
                    setNextPhase(PAUSED);
                }
                break;
            case SWINGTOREST:
                if (Robot.climber.getClimbArmDegree() >= 445) {
                    System.out.println("Hooray!");
                }
                if (!Buttons.Climb.getButton()) {
                    savedPauseState = getPhase();
                    setNextPhase(PAUSED);
                }
                break;
            case PAUSED:
                if (Buttons.Climb.getButton()) {
                    setNextPhase(savedPauseState);
                    savedPauseState = null;
                }
                break;
            case NEUTRAL:
                break;
            default:
                break;
        }
        updatePhase();
    }

    @Override
    public boolean abort() {
        if (Robot.climber.abort()) {
            setNextPhase(getNeutralPhase());
            return updatePhase();
        }
        return false;
    }

}
