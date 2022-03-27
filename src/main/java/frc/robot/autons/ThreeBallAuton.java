package frc.robot.autons;

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

public class ThreeBallAuton extends BaseAutonSequence<ThreeBallAutonState> {

    int ballsShot = 0;

    public ThreeBallAuton(ThreeBallAutonState neutralState, ThreeBallAutonState startState, BaseDriveSubsystem driveSubsystem) {
        super(neutralState, startState, driveSubsystem);
    }

    @Override
    public void process() {

        switch (getState()) {
            case DRIVE1:
                setPathPlannerFollowerAtStartOfState(true);
                if(this.getPlannerFollower().isFinished()){
                    setNextState(ThreeBallAutonState.SHOOTBALL1);
                }
                break;
            case SHOOTBALL1:
                //THE FOLLOWING IS ONLY IN ORDER TO SET THE CORRECT INITIAL POSITION
                ballsShot = 0;
//                setInitialPoseFromCurrentPath();
                if (RobotMap.shooter.getClosedLoopError() < 100 && getTimeSinceStartOfState() > 500) {
                    setNextState(ThreeBallAutonState.PUNCH1);
                }
                break;
            case PUNCH1:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(ThreeBallAutonState.RESETPUNCH1);
                    ballsShot++;
                }
                break;
            case RESETPUNCH1:
                //if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(ThreeBallAutonState.PICKUPBALL1);
                //}
                break;
            case PICKUPBALL1:
                setPathPlannerFollowerAtStartOfState(false);
                if(this.getPlannerFollower().isFinished()){
                    setNextState(ThreeBallAutonState.SHOOTBALL2);
                }
                break;
            case SHOOTBALL2:
                if ((ballsShot == 1 || (ballsShot == 2 && this.getTimeSinceStartOfState() > 500))
                        && RobotMap.shooter.getClosedLoopError() < 100) {
                    setNextState(ThreeBallAutonState.PUNCH2);
                }
                break;
            case PUNCH2:
                if (this.getTimeSinceStartOfState() > 500) {
                    setNextState(ThreeBallAutonState.RESETPUNCH2);
                    ballsShot++;
                }
                break;
            case RESETPUNCH2:
                if(ballsShot == 3){
                    setNextState(ThreeBallAutonState.NEUTRAL);
                }else if(this.getTimeSinceStartOfState() > 500){
                    setNextState(ThreeBallAutonState.SHOOTBALL2);
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

enum ThreeBallAutonState implements IAutonState {
    NEUTRAL(-999),
    DRIVE1(0, new SwerveDriveReq(SwerveDriveState.DRIVE_AUTONOMOUSLY), new ShooterReq(ShooterState.AUTONPREUPTOSPEED)),
    SHOOTBALL1(-999, new ShooterReq(ShooterState.UPTOSPEED)),
    PUNCH1(-999, new ShooterReq(ShooterState.PUNCH)),
    RESETPUNCH1( -999, new ShooterReq(ShooterState.RESETPUNCH)),
    PICKUPBALL1( 1, new SwerveDriveReq(SwerveDriveState.DRIVE_AUTONOMOUSLY), new IntakeReq(IntakeState.KICKOUT), new ShooterReq(ShooterState.AUTONPREUPTOSPEED)),
    SHOOTBALL2( -999, new ShooterReq(ShooterState.UPTOSPEED), new IntakeReq(IntakeState.HOLDPOSITION)),
    PUNCH2( -999, new ShooterReq(ShooterState.PUNCH), new IntakeReq(IntakeState.RETRACT)),
    RESETPUNCH2( -999, new ShooterReq(ShooterState.RESETPUNCH));

    int pathIndex;
    Set<BaseSubsystem> requiredSubsystems;
    List<SubsystemRequirement> subsystemRequirements;

    ThreeBallAutonState(int pathIndex, SubsystemRequirement... requirements) {
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