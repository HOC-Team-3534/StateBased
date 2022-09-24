package frc.robot.sequences;

import frc.robot.Robot;
import frc.robot.RobotContainer.Buttons;
import frc.robot.subsystems.ShooterState;
import frc.robot.subsystems.SwerveDriveState;
import frc.robot.subsystems.requirements.ShooterReq;
import frc.robot.subsystems.requirements.SwerveDriveReq;

import static frc.robot.sequences.ShootPhase.*;

import frc.BaseSequence;
import frc.ISequencePhase;
import frc.SequencePhase;
import frc.SubsystemRequirement;

enum ShootPhase implements ISequencePhase {
    NEUTRAL,
    UPTOSPEED(new ShooterReq(ShooterState.UPTOSPEED), new SwerveDriveReq(SwerveDriveState.AIM)),
    PUNCH(new ShooterReq(ShooterState.PUNCH), new SwerveDriveReq(SwerveDriveState.AIM)),
    RESETPUNCH(new ShooterReq(ShooterState.RESETPUNCH), new SwerveDriveReq(SwerveDriveState.AIM)),
    BOOT(new ShooterReq(ShooterState.BOOT), new SwerveDriveReq(SwerveDriveState.AIM));

    SequencePhase state;

    ShootPhase(SubsystemRequirement... requirements) {
        state = new SequencePhase(requirements);
    }

    @Override
    public SequencePhase getPhase() {
        return state;
    }

}

public class Shoot extends BaseSequence<ShootPhase> {

    public Shoot(ShootPhase neutralState, ShootPhase startState) {
        super(neutralState, startState);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void process() {

        Robot.limelight.updateLimelightShootProjection();
        Robot.swerveDrive.setTargetShootRotationAngle(() -> Robot.limelight.getHorizontalAngleOffset());

        switch (getPhase()) {
            case UPTOSPEED:
                if (Robot.limelight.isValid()) {
                    Robot.limelight.setTargetAcquired();
                }
                if (!Buttons.RAMPSHOOTER.getButton() && !Buttons.SHOOT.getButton()) {
                    setNextPhase(RESETPUNCH);
                }
                if (this.getTimeSinceStartOfPhase() > 500 && Buttons.SHOOT.getButton() && Robot.shooter.getCalculatedRPMError() < 35
                        && Robot.limelight.isTargetAcquired() && Math.abs(Robot.swerveDrive.getTargetShootRotationAngleError().getDegrees()) < 3.0) {
                    System.out.println("In state");
                    setNextPhase(PUNCH);
                }
                break;
            case PUNCH:
                if (this.getTimeSinceStartOfPhase() > 250) {
                    System.out.println("punching");
                    setNextPhase(RESETPUNCH);

                }
                break;
            case RESETPUNCH:
                if (!Buttons.RAMPSHOOTER.getButton() && !Buttons.SHOOT.getButton() && this.getTimeSinceStartOfPhase() > 250) {
                    this.setNextPhase(NEUTRAL);
                } else if (this.getTimeSinceStartOfPhase() > 350) {
                    //System.out.println("resetting");
                    setNextPhase(BOOT);
                }
                break;
            case BOOT:
                if (this.getTimeSinceStartOfPhase() > 150) {
                    setNextPhase(UPTOSPEED);
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
        // TODO Auto-generated method stub
        return false;
    }

}
