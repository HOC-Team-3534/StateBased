package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.subsystems.parent.BaseSubsystem;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.ShooterReq;
import frc.robot.subsystems.requirements.SwerveDriveReq;
import frc.robot.subsystems.states.ShooterState;
import frc.robot.subsystems.states.SwerveDriveState;

public class Shoot extends BaseSequence<ShootState> {

    public Shoot(ShootState neutralState, ShootState startState) {
        super(neutralState, startState);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void process() {
        switch (getState()) {
            case UPTOSPEED:
                if (RobotMap.limelight.isValid()) {
                    RobotMap.limelight.setLockedOn();
                }
                if (!Buttons.RAMPSHOOTER.getButton() && !Buttons.SHOOT.getButton()) {
                    setNextState(ShootState.RESETPUNCH);
                }
                if (this.getTimeSinceStartOfState() > 500 && Buttons.SHOOT.getButton() && RobotMap.shooter.getClosedLoopError() < 150
                        && RobotMap.limelight.isLockedOn() && Math.abs(Robot.swerveDrive.getTargetShootRotationError().getDegrees()) < 3.0) {
                    System.out.println("In state");
                    setNextState(ShootState.PUNCH);
                }
                break;
            case PUNCH:
                if (this.getTimeSinceStartOfState() > 250) {
                    System.out.println("punching");
                    setNextState(ShootState.RESETPUNCH);

                }
                break;
            case RESETPUNCH:
                if (!Buttons.RAMPSHOOTER.getButton() && !Buttons.SHOOT.getButton() && this.getTimeSinceStartOfState() > 250) {
                    this.setNextState(ShootState.NEUTRAL);
                } else if (this.getTimeSinceStartOfState() > 350) {
                    //System.out.println("resetting");
                    setNextState(ShootState.BOOT);
                }
                break;
            case BOOT:
                if (this.getTimeSinceStartOfState() > 150) {
                    setNextState(ShootState.UPTOSPEED);
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

enum ShootState implements ISequenceState {
    NEUTRAL,
    UPTOSPEED(new ShooterReq(ShooterState.UPTOSPEED), new SwerveDriveReq(SwerveDriveState.AIM)),
    PUNCH(new ShooterReq(ShooterState.PUNCH), new SwerveDriveReq(SwerveDriveState.AIM)),
    RESETPUNCH(new ShooterReq(ShooterState.RESETPUNCH), new SwerveDriveReq(SwerveDriveState.AIM)),
    BOOT(new ShooterReq(ShooterState.BOOT), new SwerveDriveReq(SwerveDriveState.AIM));

    Set<BaseSubsystem> requiredSubsystems;
    List<SubsystemRequirement> subsystemRequirements;

    ShootState(SubsystemRequirement... requirements) {
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
