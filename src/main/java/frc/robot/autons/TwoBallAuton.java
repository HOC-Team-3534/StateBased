package frc.robot.autons;

import frc.robot.Robot;
import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonPhase;
import frc.robot.autons.pathplannerfollower.PathPlannerFollower;
import frc.robot.sequences.parent.SequencePhase;
import frc.robot.subsystems.IntakeState;
import frc.robot.subsystems.ShooterState;
import frc.robot.subsystems.SwerveDriveState;
import frc.robot.subsystems.parent.BaseDriveSubsystem;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.IntakeReq;
import frc.robot.subsystems.requirements.ShooterReq;
import frc.robot.subsystems.requirements.SwerveDriveReq;

import static frc.robot.autons.TwoBallAutonPhase.*;

enum TwoBallAutonPhase implements IAutonPhase {
    NEUTRAL(-999),
    PICKUPBALL1(0, new SwerveDriveReq(SwerveDriveState.DRIVE_AUTONOMOUSLY), new IntakeReq(IntakeState.KICKOUT), new ShooterReq(ShooterState.AUTONPREUPTOSPEED)),
    SHOOTBALL1(-999, new ShooterReq(ShooterState.UPTOSPEED), new IntakeReq(IntakeState.HOLDPOSITION), new SwerveDriveReq((SwerveDriveState.AIM))),
    PUNCH1(-999, new ShooterReq(ShooterState.PUNCH), new IntakeReq(IntakeState.HOLDPOSITION)),
    RESETPUNCH1(-999, new ShooterReq(ShooterState.RESETPUNCH), new IntakeReq(IntakeState.RETRACT)),
    BOOT1(-999, new ShooterReq(ShooterState.BOOT));

    int pathIndex;
    SequencePhase state;

    TwoBallAutonPhase(int pathIndex, SubsystemRequirement... requirements) {
        this.pathIndex = pathIndex;
        state = new SequencePhase(requirements);
    }

    @Override
    public SequencePhase getPhase() {
        return state;
    }

    @Override
    public PathPlannerFollower getPath(BaseAutonSequence<? extends IAutonPhase> sequence) {
        return IAutonPhase.getPath(sequence, pathIndex);
    }
}

public class TwoBallAuton extends BaseAutonSequence<TwoBallAutonPhase> {

    int ballsShot = 0;

    public TwoBallAuton(TwoBallAutonPhase neutralState, TwoBallAutonPhase startState, BaseDriveSubsystem driveSubsystem) {
        super(neutralState, startState, driveSubsystem);
    }

    @Override
    public void process() {

        Robot.swerveDrive.setTargetShootRotationAngle();

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