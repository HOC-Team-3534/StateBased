package frc.robot.sequences;

import java.util.Arrays;
import java.util.List;

import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.IState;
import frc.robot.subsystems.parent.BaseSubsystem;

public class Shoot extends BaseSequence<ShootState> {

    public Shoot(ShootState neutralState, ShootState startState) {
        super(neutralState, startState);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void process() {

        RobotMap.limelight.updateLimelightShootProjection();

        switch (getState()) {
            case WAITNSPIN:
                if (RobotMap.limelight.isValid()) {
                    RobotMap.limelight.setTargetAcquired();
                }
                if (!Buttons.RAMPSHOOTER.getButton() && !Buttons.SHOOT.getButton()) {
                    setNextState(ShootState.RESETPUNCH);
                }
                if (this.getTimeSinceStartOfState() > 500 && Buttons.SHOOT.getButton() && RobotMap.shooter.getClosedLoopError() < 150
                        && RobotMap.limelight.isTargetAcquired() && Math.abs(Robot.swerveDrive.getTargetShootRotationAngleError().getDegrees()) < 3.0) {
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
                    setNextState(ShootState.WAITNSPIN);
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

enum ShootState implements IState {
    NEUTRAL,
    WAITNSPIN(Robot.shooter, Robot.swerveDrive),
    PUNCH(Robot.shooter, Robot.swerveDrive),
    RESETPUNCH(Robot.shooter, Robot.swerveDrive),
    BOOT(Robot.shooter, Robot.swerveDrive);

    List<BaseSubsystem> requiredSubsystems;

    ShootState(BaseSubsystem... subsystems) {
        requiredSubsystems = Arrays.asList(subsystems);
    }

    @Override
    public List<BaseSubsystem> getRequiredSubsystems() {
        return requiredSubsystems;
    }

    @Override
    public boolean requireSubsystems(BaseSequence<? extends IState> sequence) {
        return IState.requireSubsystems(sequence, requiredSubsystems, this);
    }

    @Override
    public String getName() {
        return this.name();
    }
}
