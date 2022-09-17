package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.subsystems.parent.BaseSubsystem;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.IntakeReq;
import frc.robot.subsystems.IntakeState;

public class IntakeSeq extends BaseSequence<IntakeSeqState> {

    public IntakeSeq (IntakeSeqState neutralState, IntakeSeqState startState) {
        super(neutralState, startState);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void process() {
        switch (getState()) {
            case EXTEND:
                if (!Buttons.Intake.getButton()) {
                    setNextState(IntakeSeqState.RETRACT);
                }
                break;
            case RETRACT:
                if(Buttons.Intake.getButton()){
                    setNextState(IntakeSeqState.EXTEND);
                }
                if(getTimeSinceStartOfState() > 700){
                    setNextState(IntakeSeqState.NEUTRAL);
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

enum IntakeSeqState implements ISequenceState {
    NEUTRAL,
    EXTEND(new IntakeReq(IntakeState.KICKOUT)),
    RETRACT(new IntakeReq(IntakeState.RETRACT));

    Set<BaseSubsystem> requiredSubsystems;
    List<SubsystemRequirement> subsystemRequirements;

    IntakeSeqState(SubsystemRequirement... requirements) {
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

