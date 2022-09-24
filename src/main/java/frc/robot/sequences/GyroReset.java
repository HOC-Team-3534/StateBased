package frc.robot.sequences;

import frc.robot.Robot;
import frc.statebasedcontroller.sequence.fundamental.BaseSequence;
import frc.statebasedcontroller.sequence.fundamental.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.SequencePhase;
import frc.statebasedcontroller.subsystem.fundamental.SubsystemRequirement;

import static frc.robot.sequences.GyroResetPhase.NEUTRAL;

enum GyroResetPhase implements ISequencePhase {
    NEUTRAL,
    RESET;

    SequencePhase state;

    GyroResetPhase(SubsystemRequirement... requirements) {
        state = new SequencePhase(requirements);
    }

    @Override
    public SequencePhase getPhase() {
        return state;
    }

}

public class GyroReset extends BaseSequence<GyroResetPhase> {

    public GyroReset(GyroResetPhase neutralState, GyroResetPhase startState) {
        super(neutralState, startState);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void process() {
        switch (getPhase()) {
            case RESET:
                Robot.swerveDrive.resetGyro();
                setNextPhase(NEUTRAL);
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
        // TODO Auto-generated method stub
        return false;
    }

}

