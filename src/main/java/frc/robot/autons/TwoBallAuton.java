package frc.robot.autons;

import frc.robot.Robot;
import frc.robot.subsystems.IntakeState;
import frc.robot.subsystems.ShooterState;
import frc.robot.subsystems.SwerveDriveState;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.phase.SequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseAutonSequence;
import frc.statebasedcontroller.subsystem.fundamental.state.ISubsystemState;
import frc.statebasedcontroller.subsystem.general.swervedrive.BaseDriveSubsystem;

import static frc.robot.autons.TwoBallAutonPhase.*;

enum TwoBallAutonPhase implements ISequencePhase {
    NEUTRAL,
    PICKUPBALL1(0, SwerveDriveState.DRIVE_AUTONOMOUSLY, IntakeState.KICKOUT, ShooterState.AUTONPREUPTOSPEED),
    SHOOTBALL1(ShooterState.UPTOSPEED, IntakeState.HOLDPOSITION, SwerveDriveState.AIM),
    PUNCH1(ShooterState.PUNCH, IntakeState.HOLDPOSITION),
    RESETPUNCH1(ShooterState.RESETPUNCH, IntakeState.RETRACT),
    BOOT1(ShooterState.BOOT);

    SequencePhase phase;
    
    TwoBallAutonPhase(ISubsystemState... states) {
        phase = new SequencePhase(states);
    }
    
    TwoBallAutonPhase(int pathIndex, ISubsystemState... states) {
        phase = new SequencePhase(pathIndex, states);
    }
    
    @Override
    public SequencePhase getPhase() {
        return phase;
    }
}

public class TwoBallAuton extends BaseAutonSequence<TwoBallAutonPhase> {

    int ballsShot = 0;

    public TwoBallAuton(TwoBallAutonPhase neutralState, TwoBallAutonPhase startState, BaseDriveSubsystem driveSubsystem) {
        super(neutralState, startState, driveSubsystem);
    }

    @Override
    public void process() {

        Robot.swerveDrive.setTargetShootRotationAngle(() -> Robot.limelight.getHorizontalAngleOffset());

        switch (getPhase()) {
            case PICKUPBALL1:
                setPathPlannerFollowerAtStartOfState(true);
                ballsShot = 0;
                if (this.getPlannerFollower().isFinished()) {
                    setNextPhase(SHOOTBALL1);
                }
                break;
            case SHOOTBALL1:
                if (Robot.limelight.isValid()) {
                    Robot.limelight.setTargetAcquired();
                }
                if (((ballsShot == 0 && this.getTimeSinceStartOfPhase() > 2000) || (ballsShot >= 1 && this.getTimeSinceStartOfPhase() > 500))
                        && Math.abs(Robot.swerveDrive.getTargetShootRotationAngleError().getDegrees()) < 3.0 &&
                        Robot.shooter.getShooterClosedLoopError() < 100 && Robot.limelight.isTargetAcquired()) {
                    setNextPhase(PUNCH1);
                }
                break;
            case PUNCH1:
                if (this.getTimeSinceStartOfPhase() > 500) {
                    setNextPhase(RESETPUNCH1);
                    ballsShot++;
                }
                break;
            case RESETPUNCH1:
                if (this.getTimeSinceStartOfPhase() > 1500) {
                    if (ballsShot == 3) {
                        setNextPhase(NEUTRAL);
                    } else {
                        setNextPhase(BOOT1);
                    }
                }
                break;
            case BOOT1:
                if (this.getTimeSinceStartOfPhase() > 150) {
                    setNextPhase(SHOOTBALL1);
                }
                break;
            case NEUTRAL:
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