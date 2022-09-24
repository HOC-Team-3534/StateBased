package frc.robot.sequences;

import frc.robot.Robot;
import frc.robot.RobotContainer.Buttons;
import frc.robot.subsystems.ShooterState;
import frc.robot.subsystems.SwerveDriveState;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.phase.SequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseSequence;
import frc.statebasedcontroller.subsystem.fundamental.state.ISubsystemState;


import static frc.robot.sequences.ShootPhase.*;

enum ShootPhase implements ISequencePhase {
    NEUTRAL,
    UPTOSPEED(ShooterState.UPTOSPEED, SwerveDriveState.AIM),
    PUNCH(ShooterState.PUNCH, SwerveDriveState.AIM),
    RESETPUNCH(ShooterState.RESETPUNCH, SwerveDriveState.AIM),
    BOOT(ShooterState.BOOT, SwerveDriveState.AIM);

    SequencePhase phase;
    
    ShootPhase(ISubsystemState... states) {
        phase = new SequencePhase(states);
    }
    
    @Override
    public SequencePhase getPhase() {
        return phase;
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
