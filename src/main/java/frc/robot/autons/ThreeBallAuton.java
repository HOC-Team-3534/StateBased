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

import static frc.robot.autons.ThreeBallAutonPhase.*;

enum ThreeBallAutonPhase implements ISequencePhase {
    NEUTRAL,
    DRIVE1(0, SwerveDriveState.DRIVE_AUTONOMOUSLY, ShooterState.AUTONPREUPTOSPEED),
    SHOOTBALL1(ShooterState.UPTOSPEED),
    PUNCH1(ShooterState.PUNCH),
    RESETPUNCH1(ShooterState.RESETPUNCH),
    PICKUPBALL1(1, SwerveDriveState.DRIVE_AUTONOMOUSLY, IntakeState.KICKOUT, ShooterState.AUTONPREUPTOSPEED),
    SHOOTBALL2(SwerveDriveState.AIM, ShooterState.UPTOSPEED, IntakeState.HOLDPOSITION),
    PUNCH2(ShooterState.PUNCH, IntakeState.RETRACT),
    RESETPUNCH2(ShooterState.RESETPUNCH),
    BOOT1(ShooterState.BOOT);

   SequencePhase phase;
   
   ThreeBallAutonPhase(ISubsystemState... states) {
       phase = new SequencePhase(states);
   }
   
   ThreeBallAutonPhase(int pathIndex, ISubsystemState... states) {
       phase = new SequencePhase(pathIndex, states);
   }
   
   @Override
   public SequencePhase getPhase() {
       return phase;
   }
}

public class ThreeBallAuton extends BaseAutonSequence<ThreeBallAutonPhase> {

    int ballsShot = 0;

    public ThreeBallAuton(ThreeBallAutonPhase neutralState, ThreeBallAutonPhase startState, BaseDriveSubsystem driveSubsystem) {
        super(neutralState, startState, driveSubsystem);
    }

    @Override
    public void process() {

        Robot.swerveDrive.setTargetShootRotationAngle(() -> Robot.limelight.getHorizontalAngleOffset());

        switch (getPhase()) {
            case NEUTRAL:
                break;
            case DRIVE1:
                setPathPlannerFollowerAtStartOfState(true);
                if (this.getPlannerFollower().isFinished()) {
                    setNextPhase(SHOOTBALL1);
                }
                break;
            case SHOOTBALL1:
                //THE FOLLOWING IS ONLY IN ORDER TO SET THE CORRECT INITIAL POSITION
                ballsShot = 0;
//                setInitialPoseFromCurrentPath();
                if (Robot.shooter.getShooterClosedLoopError() < 100 && getTimeSinceStartOfPhase() > 500) {
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
                //if (this.getTimeSinceStartOfState() > 500) {
                setNextPhase(PICKUPBALL1);
                //}
                break;
            case PICKUPBALL1:
                setPathPlannerFollowerAtStartOfState(false);
                if (this.getPlannerFollower().isFinished()) {
                    setNextPhase(SHOOTBALL2);
                    Robot.limelight.resetLimelightGlobalValues();
                }
                break;
            case SHOOTBALL2:
                if (Robot.limelight.isValid()) {
                    Robot.limelight.setTargetAcquired();
                }
                if ((ballsShot == 1 || (ballsShot == 2 && this.getTimeSinceStartOfPhase() > 500
                        && Robot.limelight.isTargetAcquired() && Math.abs(Robot.swerveDrive.getTargetShootRotationAngleError().getDegrees()) < 3.0))
                        && Robot.shooter.getShooterClosedLoopError() < 100) {
                    setNextPhase(PUNCH2);
                }
                break;
            case PUNCH2:
                if (this.getTimeSinceStartOfPhase() > 500) {
                    setNextPhase(RESETPUNCH2);
                    ballsShot++;
                }
                break;
            case RESETPUNCH2:
                if (ballsShot == 3 && this.getTimeSinceStartOfPhase() > 500) {
                    setNextPhase(NEUTRAL);
                } else if (this.getTimeSinceStartOfPhase() > 500) {
                    setNextPhase(BOOT1);
                }
                break;
            case BOOT1:
                if (this.getTimeSinceStartOfPhase() > 150) {
                    setNextPhase(SHOOTBALL2);
                }
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