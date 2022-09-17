package frc.robot.autons;

import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonState;
import frc.robot.autons.pathplannerfollower.PathPlannerFollower;
import frc.robot.sequences.parent.BaseSequence;
import frc.robot.sequences.parent.ISequenceState;
import frc.robot.subsystems.parent.BaseDriveSubsystem;
import frc.robot.subsystems.parent.BaseSubsystem;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.IntakeReq;
import frc.robot.subsystems.requirements.ShooterReq;
import frc.robot.subsystems.requirements.SwerveDriveReq;
import frc.robot.subsystems.states.IntakeState;
import frc.robot.subsystems.states.ShooterState;
import frc.robot.subsystems.states.SwerveDriveState;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                if(this.getPlannerFollower().isFinished()){
                    setNextState(TwoBallAutonState.SHOOTBALL1);
                }
                break;
            case SHOOTBALL1:
                if (RobotMap.limelight.isValid()) {
                    RobotMap.limelight.setTargetAcquired();
                }
                if (((ballsShot == 0 && this.getTimeSinceStartOfState() > 2000) || (ballsShot >= 1 && this.getTimeSinceStartOfState() > 500))
                        && Math.abs(Robot.swerveDrive.getTargetShootRotationAngleError().getDegrees()) < 3.0 &&
                        RobotMap.shooter.getClosedLoopError() < 100 && RobotMap.limelight.isTargetAcquired()) {
                    setNextState(TwoBallAutonState.PUNCH1);
                }
                break;
            case PUNCH1:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(TwoBallAutonState.RESETPUNCH1);
                    ballsShot++;
                }
                break;
            case RESETPUNCH1:
                if (this.getTimeSinceStartOfState() > 1500) {
                    if(ballsShot == 3){
                        setNextState(TwoBallAutonState.NEUTRAL);
                    }else{
                        setNextState(TwoBallAutonState.BOOT1);
                    }
                }
                break;
            case BOOT1:
                if(this.getTimeSinceStartOfState() > 150){
                    setNextState(TwoBallAutonState.SHOOTBALL1);
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

enum TwoBallAutonState implements IAutonState {
    NEUTRAL(-999),
    PICKUPBALL1( 0, new SwerveDriveReq(SwerveDriveState.DRIVE_AUTONOMOUSLY), new IntakeReq(IntakeState.KICKOUT), new ShooterReq(ShooterState.AUTONPREUPTOSPEED)),
    SHOOTBALL1( -999, new ShooterReq(ShooterState.UPTOSPEED), new IntakeReq(IntakeState.HOLDPOSITION), new SwerveDriveReq((SwerveDriveState.AIM))),
    PUNCH1( -999, new ShooterReq(ShooterState.PUNCH), new IntakeReq(IntakeState.HOLDPOSITION)),
    RESETPUNCH1( -999, new ShooterReq(ShooterState.RESETPUNCH), new IntakeReq(IntakeState.RETRACT)),
    BOOT1(-999, new ShooterReq(ShooterState.BOOT));

    int pathIndex;
    Set<BaseSubsystem> requiredSubsystems;
    List<SubsystemRequirement> subsystemRequirements;

    TwoBallAutonState(int pathIndex, SubsystemRequirement... requirements) {
        this.pathIndex = pathIndex;
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

    @Override
    public PathPlannerFollower getPath(BaseAutonSequence<? extends IAutonState> sequence) {
        return IAutonState.getPath(sequence, pathIndex);
    }
}