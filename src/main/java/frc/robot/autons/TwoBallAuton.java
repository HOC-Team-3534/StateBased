package frc.robot.autons;

import frc.robot.Robot;
import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonState;
import frc.robot.autons.pathplannerfollower.PathPlannerFollower;
import frc.robot.sequences.parent.SequenceState;
import frc.robot.subsystems.IntakeState;
import frc.robot.subsystems.ShooterState;
import frc.robot.subsystems.SwerveDriveState;
import frc.robot.subsystems.parent.BaseDriveSubsystem;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.IntakeReq;
import frc.robot.subsystems.requirements.ShooterReq;
import frc.robot.subsystems.requirements.SwerveDriveReq;

import static frc.robot.autons.TwoBallAutonState.*;

enum TwoBallAutonState implements IAutonState {
    NEUTRAL(-999),
    PICKUPBALL1(0, new SwerveDriveReq(SwerveDriveState.DRIVE_AUTONOMOUSLY), new IntakeReq(IntakeState.KICKOUT), new ShooterReq(ShooterState.AUTONPREUPTOSPEED)),
    SHOOTBALL1(-999, new ShooterReq(ShooterState.UPTOSPEED), new IntakeReq(IntakeState.HOLDPOSITION), new SwerveDriveReq((SwerveDriveState.AIM))),
    PUNCH1(-999, new ShooterReq(ShooterState.PUNCH), new IntakeReq(IntakeState.HOLDPOSITION)),
    RESETPUNCH1(-999, new ShooterReq(ShooterState.RESETPUNCH), new IntakeReq(IntakeState.RETRACT)),
    BOOT1(-999, new ShooterReq(ShooterState.BOOT));

    int pathIndex;
    SequenceState state;

    TwoBallAutonState(int pathIndex, SubsystemRequirement... requirements) {
        this.pathIndex = pathIndex;
        state = new SequenceState(requirements);
    }

    @Override
    public SequenceState getState() {
        return state;
    }

    @Override
    public PathPlannerFollower getPath(BaseAutonSequence<? extends IAutonState> sequence) {
        return IAutonState.getPath(sequence, pathIndex);
    }
}

public class TwoBallAuton extends BaseAutonSequence<TwoBallAutonState> {

    int ballsShot = 0;

    public TwoBallAuton(TwoBallAutonState neutralState, TwoBallAutonState startState, BaseDriveSubsystem driveSubsystem) {
        super(neutralState, startState, driveSubsystem);
    }

    @Override
    public void process() {

        Robot.swerveDrive.setTargetShootRotationAngle();

        switch (getState()) {
            case PICKUPBALL1:
                setPathPlannerFollowerAtStartOfState(true);
                ballsShot = 0;
                if (this.getPlannerFollower().isFinished()) {
                    setNextState(SHOOTBALL1);
                }
                break;
            case SHOOTBALL1:
                if (Robot.limelight.isValid()) {
                    Robot.limelight.setTargetAcquired();
                }
                if (((ballsShot == 0 && this.getTimeSinceStartOfState() > 2000) || (ballsShot >= 1 && this.getTimeSinceStartOfState() > 500))
                        && Math.abs(Robot.swerveDrive.getTargetShootRotationAngleError().getDegrees()) < 3.0 &&
                        Robot.shooter.getShooterClosedLoopError() < 100 && Robot.limelight.isTargetAcquired()) {
                    setNextState(PUNCH1);
                }
                break;
            case PUNCH1:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(RESETPUNCH1);
                    ballsShot++;
                }
                break;
            case RESETPUNCH1:
                if (this.getTimeSinceStartOfState() > 1500) {
                    if (ballsShot == 3) {
                        setNextState(NEUTRAL);
                    } else {
                        setNextState(BOOT1);
                    }
                }
                break;
            case BOOT1:
                if (this.getTimeSinceStartOfState() > 150) {
                    setNextState(SHOOTBALL1);
                }
                break;
            case NEUTRAL:
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