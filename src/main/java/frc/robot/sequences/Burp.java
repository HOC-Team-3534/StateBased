package frc.robot.sequences;

import frc.robot.Robot;
import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.sequences.parent.SequenceState;
import frc.robot.subsystems.ShooterState;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.ShooterReq;

import static frc.robot.sequences.BurpState.*;

enum BurpState implements ISequenceState {
    NEUTRAL,
    BURP(new ShooterReq(ShooterState.BURP)),
    PUNCH(new ShooterReq(ShooterState.PUNCH)),
    RESETPUNCH(new ShooterReq(ShooterState.RESETPUNCH)),
    BOOT(new ShooterReq(ShooterState.BOOT));

    SequenceState state;

    BurpState(SubsystemRequirement... requirements) {
        state = new SequenceState(requirements);
    }

    @Override
    public SequenceState getState() {
        return state;
    }
}

public class Burp extends BaseSequence<BurpState> {

    public Burp(BurpState neutralState, BurpState startState) {
        super(neutralState, startState);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void process() {
        switch (getState()) {
            case BURP:
                if (!Buttons.Burp.getButton()) {
                    setNextState(RESETPUNCH);
                }
                if (this.getTimeSinceStartOfState() > 500 && Robot.shooter.getShooterClosedLoopError() < 250) {
                    System.out.println("In state");
                    setNextState(PUNCH);
                }
                break;
            case PUNCH:
                if (this.getTimeSinceStartOfState() > 250) {
                    setNextState(RESETPUNCH);
                }
                break;
            case RESETPUNCH:
                if (!Buttons.Burp.getButton() && this.getTimeSinceStartOfState() > 250) {
                    setNextState(NEUTRAL);
                } else if (this.getTimeSinceStartOfState() > 250) {
                    setNextState(BOOT);
                }
                break;
            case BOOT:
                if (this.getTimeSinceStartOfState() > 150) {
                    setNextState(BURP);
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
