package frc.robot.sequences;

import frc.robot.Robot;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequencePhase;
import frc.robot.sequences.parent.SequencePhase;
import frc.robot.subsystems.ClimberState;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.ClimberReq;

import static frc.robot.sequences.ClimbPrepResetPhase.NEUTRAL;

enum ClimbPrepResetPhase implements ISequencePhase {
    NEUTRAL,
    RESETARM(new ClimberReq(ClimberState.RESETARM));

    SequencePhase state;

    ClimbPrepResetPhase(SubsystemRequirement... requirements) {
        state = new SequencePhase(requirements);
    }

    @Override
    public SequencePhase getPhase() {
        return state;
    }

}

public class ClimbPrepReset extends BaseSequence<ClimbPrepResetPhase> {

    public ClimbPrepReset(ClimbPrepResetPhase neutralState, ClimbPrepResetPhase startState) {
        super(neutralState, startState);
    }

    @Override
    public void process() {

        switch (getPhase()) {
            case RESETARM:
                if (Robot.climber.getClimbArmDegree() < 10) {
                    setNextPhase(NEUTRAL);
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
        setNextPhase(getNeutralPhase());
        return updatePhase();
    }

}
