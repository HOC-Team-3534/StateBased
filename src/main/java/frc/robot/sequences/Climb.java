package frc.robot.sequences;

import static frc.robot.Constants.CLIMBER.*;
import frc.robot.Robot;
import frc.robot.RobotContainer.Buttons;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.ClimberState;
import frc.robot.subsystems.SwerveDriveState;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseSequence;
import frc.statebasedcontroller.subsystem.fundamental.state.ISubsystemState;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.phase.SequencePhase;

import static frc.robot.sequences.ClimbPhase.*;

enum ClimbPhase implements ISequencePhase {
    NEUTRAL,
    PAUSED(ClimberState.NEUTRAL, SwerveDriveState.NEUTRAL),
    GRIPMIDBAR(ClimberState.GRIPMIDBAR, SwerveDriveState.NEUTRAL),
    SWINGMIDHIGH1(ClimberState.SWINGMIDHIGH1, SwerveDriveState.NEUTRAL),
    SWINGMIDHIGH2(ClimberState.SWINGMIDHIGH2, SwerveDriveState.NEUTRAL),
    GRIPHIGHBAR(ClimberState.GRIPHIGHBAR, SwerveDriveState.NEUTRAL),
    RETRYHIGHBAR(ClimberState.RETRYHIGHBAR, SwerveDriveState.NEUTRAL),
    RECENTERMIDHIGHBAR(ClimberState.RECENTERMIDHIGHBAR, SwerveDriveState.NEUTRAL),
    RELEASEMIDBAR(ClimberState.RELEASEMIDBAR, SwerveDriveState.NEUTRAL),
    SWINGHIGHTRAVERSAL1(ClimberState.SWINGHIGHTRAVERSAL1, SwerveDriveState.NEUTRAL),
    SWINGHIGHTRAVERSAL2(ClimberState.SWINGHIGHTRAVERSAL2, SwerveDriveState.NEUTRAL),
    GRIPTRAVERSALBAR(ClimberState.GRIPTRAVERSALBAR, SwerveDriveState.NEUTRAL),
    RETRYTRAVERSALBAR(ClimberState.RETRYTRAVERSALBAR, SwerveDriveState.NEUTRAL),
    RECENTERHIGHTRAVERSALBAR(ClimberState.RECENTERHIGHTRAVERSALBAR, SwerveDriveState.NEUTRAL),
    RELEASEHIGHBAR(ClimberState.RELEASEHIGHBAR, SwerveDriveState.NEUTRAL),
    SWINGTOREST(ClimberState.SWINGTOREST, SwerveDriveState.NEUTRAL);

    SequencePhase phase;

    ClimbPhase(ISubsystemState... states) {
        phase = new SequencePhase(states);
    }

    @Override
    public SequencePhase getPhase() {
        return phase;
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
            return reset();
        }
        return false;
    }

}
