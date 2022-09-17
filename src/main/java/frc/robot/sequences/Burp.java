package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import frc.robot.Robot;
import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.subsystems.parent.BaseSubsystem;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.ShooterReq;
import frc.robot.subsystems.ShooterState;

public class Burp extends BaseSequence<BurpState> {

    public Burp(BurpState neutralState, BurpState startState) {
        super(neutralState, startState);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void process() {
        switch (getState()) {
            case BURP:
                if(!Buttons.Burp.getButton()){
                    setNextState(BurpState.RESETPUNCH);
                }if (this.getTimeSinceStartOfState() > 500 && Robot.shooter.getShooterClosedLoopError() < 250) {
                    System.out.println("In state");
                    setNextState(BurpState.PUNCH);
                }
                break;
            case PUNCH:
                if (this.getTimeSinceStartOfState() > 250) {
                    setNextState(BurpState.RESETPUNCH);
                }
                break;
            case RESETPUNCH:
                if(!Buttons.Burp.getButton() && this.getTimeSinceStartOfState() > 250){
                    setNextState(BurpState.NEUTRAL);
                }else if (this.getTimeSinceStartOfState() > 250) {
                    setNextState(BurpState.BOOT);
                }
                break;
            case BOOT:
                if(this.getTimeSinceStartOfState() > 150){
                    setNextState(BurpState.BURP);
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

enum BurpState implements ISequenceState {
    NEUTRAL,
    BURP(new ShooterReq(ShooterState.BURP)),
    PUNCH(new ShooterReq(ShooterState.PUNCH)),
    RESETPUNCH(new ShooterReq(ShooterState.RESETPUNCH)),
    BOOT(new ShooterReq(ShooterState.BOOT));

    Set<BaseSubsystem> requiredSubsystems;
    List<SubsystemRequirement> subsystemRequirements;

    BurpState(SubsystemRequirement... requirements) {
        subsystemRequirements = Arrays.asList(requirements);
        requiredSubsystems = subsystemRequirements.stream().map(requirement -> requirement.getSubsystem()).collect(Collectors.toSet());
    }

    @Override
    public Set<BaseSubsystem> getRequiredSubsystems() {
        return requiredSubsystems;
    }

    @Override
    public boolean requireSubsystems(BaseSequence<? extends ISequenceState> sequence) {
        return ISequenceState.requireSubsystems(sequence, subsystemRequirements);
    }

}
