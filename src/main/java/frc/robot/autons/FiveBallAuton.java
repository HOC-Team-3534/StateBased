package frc.robot.autons;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import frc.robot.Robot;
import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonState;
import frc.robot.autons.pathplannerfollower.PathPlannerFollower;
import frc.robot.sequences.parent.*;
import frc.robot.subsystems.parent.BaseDriveSubsystem;
import frc.robot.subsystems.parent.BaseSubsystem;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.requirements.IntakeReq;
import frc.robot.subsystems.requirements.ShooterReq;
import frc.robot.subsystems.requirements.SwerveDriveReq;
import frc.robot.subsystems.states.IntakeState;
import frc.robot.subsystems.states.ShooterState;
import frc.robot.subsystems.states.SwerveDriveState;

public class FiveBallAuton extends BaseAutonSequence<FiveBallAutonState> {

    int ballsShot = 0;

    public FiveBallAuton(FiveBallAutonState neutralState, FiveBallAutonState startState, BaseDriveSubsystem driveSubsystem) {
        super(neutralState, startState, driveSubsystem);
    }

    @Override
    public void process() {

        switch (getState()) {
            case DRIVE1:
                setPathPlannerFollowerAtStartOfState(true);
                if(this.getPlannerFollower().isFinished()){
                    setNextState(FiveBallAutonState.SHOOTBALL1);
                }
                break;
            case SHOOTBALL1:
                //THE FOLLOWING IS ONLY IN ORDER TO SET THE CORRECT INITIAL POSITION
//                setInitialPoseFromCurrentPath();
                ballsShot = 0;
                if (Robot.shooter.getShooterClosedLoopError() < 100 && getTimeSinceStartOfState() > 500) {
                    setNextState(FiveBallAutonState.PUNCH1);
                }
                break;
            case PUNCH1:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(FiveBallAutonState.RESETPUNCH1);
                    ballsShot++;
                }
                break;
            case RESETPUNCH1:
                //if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(FiveBallAutonState.PICKUPBALL1);
                //}
                break;
            case PICKUPBALL1:
                setPathPlannerFollowerAtStartOfState(false);
                if(this.getPlannerFollower().isFinished()){
                    setNextState(FiveBallAutonState.SHOOTBALL2);
                }
                break;
            case SHOOTBALL2:
                if ((ballsShot == 1 || (ballsShot == 2 && this.getTimeSinceStartOfState() > 500))
                        && Robot.shooter.getShooterClosedLoopError() < 100) {
                    setNextState(FiveBallAutonState.PUNCH2);
                }
                break;
            case PUNCH2:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(FiveBallAutonState.RESETPUNCH2);
                    ballsShot++;
                }
                break;
            case RESETPUNCH2:
                if(ballsShot == 3){
                    setNextState(FiveBallAutonState.PICKUPBALL2);
                }else if(this.getTimeSinceStartOfState() > 500){
                    setNextState(FiveBallAutonState.SHOOTBALL2);
                }
                break;
            case PICKUPBALL2:
                setPathPlannerFollowerAtStartOfState(false);
                if(this.getPlannerFollower().isFinished()){
                    setNextState(FiveBallAutonState.WAITFORINTAKE);
                }
                break;
            case WAITFORINTAKE:
                if(this.getTimeSinceStartOfState() > 500){
                    setNextState(FiveBallAutonState.PICKUPBALL3);
                }
            case PICKUPBALL3:
                setPathPlannerFollowerAtStartOfState(false);
                if(this.getPlannerFollower().isFinished()){
                    setNextState(FiveBallAutonState.SHOOTBALL3);
                }
                break;
            case SHOOTBALL3:
                if ((ballsShot == 3 || (ballsShot == 4 && this.getTimeSinceStartOfState() > 500))
                        && Robot.shooter.getShooterClosedLoopError() < 100) {
                    setNextState(FiveBallAutonState.PUNCH3);
                }
                break;
            case PUNCH3:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(FiveBallAutonState.RESETPUNCH3);
                    ballsShot++;
                }
                break;
            case RESETPUNCH3:
                if (this.getTimeSinceStartOfState() > 500) {
                    if(ballsShot == 5){
                        setNextState(FiveBallAutonState.NEUTRAL);
                    }else{
                        setNextState(FiveBallAutonState.SHOOTBALL3);
                    }
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

enum FiveBallAutonState implements IAutonState {
    NEUTRAL(-999),
    DRIVE1(0, new SwerveDriveReq(SwerveDriveState.DRIVE_AUTONOMOUSLY), new ShooterReq(ShooterState.AUTONPREUPTOSPEED)),
    SHOOTBALL1(-999, new ShooterReq(ShooterState.UPTOSPEED)),
    PUNCH1(-999, new ShooterReq(ShooterState.PUNCH)),
    RESETPUNCH1( -999, new ShooterReq(ShooterState.RESETPUNCH)),
    PICKUPBALL1( 1, new SwerveDriveReq(SwerveDriveState.DRIVE_AUTONOMOUSLY), new IntakeReq(IntakeState.KICKOUT), new ShooterReq(ShooterState.AUTONPREUPTOSPEED)),
    SHOOTBALL2( -999, new ShooterReq(ShooterState.UPTOSPEED), new IntakeReq(IntakeState.HOLDPOSITION)),
    PUNCH2( -999, new ShooterReq(ShooterState.PUNCH), new IntakeReq(IntakeState.RETRACT)),
    RESETPUNCH2( -999, new ShooterReq(ShooterState.RESETPUNCH)),
    PICKUPBALL2( 2, new SwerveDriveReq(SwerveDriveState.DRIVE_AUTONOMOUSLY), new IntakeReq(IntakeState.KICKOUT)),
    WAITFORINTAKE( -999, new IntakeReq(IntakeState.HOLDPOSITION)),
    PICKUPBALL3( 3, new SwerveDriveReq(SwerveDriveState.DRIVE_AUTONOMOUSLY), new IntakeReq(IntakeState.HOLDPOSITION), new ShooterReq(ShooterState.AUTONPREUPTOSPEED)),
    SHOOTBALL3( -999, new ShooterReq(ShooterState.UPTOSPEED), new IntakeReq(IntakeState.HOLDPOSITION)),
    PUNCH3( -999, new ShooterReq(ShooterState.PUNCH), new IntakeReq(IntakeState.RETRACT)),
    RESETPUNCH3( -999, new ShooterReq(ShooterState.RESETPUNCH));

    int pathIndex;
    Set<BaseSubsystem> requiredSubsystems;
    List<SubsystemRequirement> subsystemRequirements;

    FiveBallAutonState(int pathIndex, SubsystemRequirement... requirements) {
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