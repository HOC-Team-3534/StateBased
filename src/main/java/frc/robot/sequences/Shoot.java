package frc.robot.sequences;

import frc.robot.Robot;
import frc.robot.RobotContainer.Buttons;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.sequences.parent.SequenceState;
import frc.robot.subsystems.ShooterState;
import frc.robot.subsystems.SwerveDriveState;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.ShooterReq;
import frc.robot.subsystems.requirements.SwerveDriveReq;

import static frc.robot.sequences.ShootState.*;

enum ShootState implements ISequenceState {
    NEUTRAL,
    UPTOSPEED(new ShooterReq(ShooterState.UPTOSPEED), new SwerveDriveReq(SwerveDriveState.AIM)),
    PUNCH(new ShooterReq(ShooterState.PUNCH), new SwerveDriveReq(SwerveDriveState.AIM)),
    RESETPUNCH(new ShooterReq(ShooterState.RESETPUNCH), new SwerveDriveReq(SwerveDriveState.AIM)),
    BOOT(new ShooterReq(ShooterState.BOOT), new SwerveDriveReq(SwerveDriveState.AIM));

    SequenceState state;

    ShootState(SubsystemRequirement... requirements) {
        state = new SequenceState(requirements);
    }

    @Override
    public SequenceState getState() {
        return state;
    }

}

public class Shoot extends BaseSequence<ShootState> {

    public Shoot(ShootState neutralState, ShootState startState) {
        super(neutralState, startState);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void process() {

        Robot.limelight.updateLimelightShootProjection();
        Robot.swerveDrive.setTargetShootRotationAngle();

        switch (getState()) {
            case UPTOSPEED:
                if (Robot.limelight.isValid()) {
                    Robot.limelight.setTargetAcquired();
                }
                if (!Buttons.RAMPSHOOTER.getButton() && !Buttons.SHOOT.getButton()) {
                    setNextState(RESETPUNCH);
                }
                if (this.getTimeSinceStartOfState() > 500 && Buttons.SHOOT.getButton() && Robot.shooter.getCalculatedRPMError() < 35
                        && Robot.limelight.isTargetAcquired() && Math.abs(Robot.swerveDrive.getTargetShootRotationAngleError().getDegrees()) < 3.0) {
                    System.out.println("In state");
                    setNextState(PUNCH);
                }
                break;
            case PUNCH:
                if (this.getTimeSinceStartOfState() > 250) {
                    System.out.println("punching");
                    setNextState(RESETPUNCH);

                }
                break;
            case RESETPUNCH:
                if (!Buttons.RAMPSHOOTER.getButton() && !Buttons.SHOOT.getButton() && this.getTimeSinceStartOfState() > 250) {
                    this.setNextState(NEUTRAL);
                } else if (this.getTimeSinceStartOfState() > 350) {
                    //System.out.println("resetting");
                    setNextState(BOOT);
                }
                break;
            case BOOT:
                if (this.getTimeSinceStartOfState() > 150) {
                    setNextState(UPTOSPEED);
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
