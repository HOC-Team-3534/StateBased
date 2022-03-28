package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.subsystems.parent.BaseSubsystem;
import frc.robot.subsystems.parent.IDistanceToShooterRPM;
import frc.robot.subsystems.states.ShooterState;

public class Shooter extends BaseSubsystem<ShooterState> {

    IDistanceToShooterRPM rpmFunction;

    public Shooter(IDistanceToShooterRPM rpmFunction) {
        super(ShooterState.NEUTRAL);
        // makes the graphical number to enter text - have to do a
        // put to do a get
        SmartDashboard.putNumber("Manual Testing RPM", 2000.0);
        SmartDashboard.putNumber("RPM MULTIPLIER (%)", 100.0);
        SmartDashboard.putNumber("AUTON RPM MULTIPLIER (%)", 100.0);
        this.rpmFunction = rpmFunction;
    }

    @Override
    public void process() {

        super.process();

        switch (getCurrentSubsystemState()) {
            case NEUTRAL:
                neutral();
                break;
            case AUTONPREUPTOSPEED:
                if (Robot.swerveDrive.getPathStateController().getPathPlannerFollower() != null) {
                    if (Robot.swerveDrive.getPathStateController().getPathPlannerFollower()
                            .getRemainingTimeSeconds() < 2.0) {
                        upToSpeed(3000);
                    } else {
                        neutral();
                    }
                } else {
                    neutral();
                }
                break;
            case UPTOSPEED:
                // grabs the number from SmartDashboard
                //waitNSpin(SmartDashboard.getNumber("Manual Testing RPM", 0.0));
                if (RobotMap.limelight.isTargetAcquired()) {
                    upToSpeed();
                } else {
                    upToSpeed(3000);
                }
                break;
            case BURP:
                burp();
                break;
            case PUNCH:
                punch();
                break;
            case RESETPUNCH:
                resetPunch();
                break;
            case BOOT:
                boot();
                break;
        }
    }

    public void shoot(double rpm) {
        double countsPer100MS = rpm * Constants.RPM_TO_COUNTS_PER_100MS;
        RobotMap.shooter.set(ControlMode.Velocity, countsPer100MS);
    }

    private void upToSpeed() {
        double rpmMultiplier = SmartDashboard.getNumber("RPM MULTIPLIER (%)", 100.0) / 100.0;
        shoot(rpmMultiplier * rpmFunction.getShooterRPM(RobotMap.limelight.getDistance()));
        //shoot(rpmMultiplier * rpmFunction.getShooterRPM(RobotMap.limelight.getLimelightShootProjection().getDistance()));
    }

    private void upToSpeed(double rpm) {
        shoot(rpm);
    }

    private void burp() {
        shoot(1300);
    }

    private void punch() {
        if (getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.pusher, Value.kForward, Constants.DelayToOff.SHOOTER_PUSHER.millis);
        }
    }

    private void resetPunch() {
        if (getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.pusher, Value.kReverse, Constants.DelayToOff.SHOOTER_PUSHER.millis);
        }
        if (this.getSequenceRequiring().getTimeSinceStartOfState() > 100) {
            RobotMap.shooterBoot.set(ControlMode.PercentOutput, -0.80);

        }
        if (this.getSequenceRequiring().getTimeSinceStartOfState() > 200) {
            RobotMap.shooterBoot.set(ControlMode.PercentOutput, 0.0);
        }

    }

    private void boot() {
        if (this.getStateFirstRunThrough()) {
            RobotMap.shooterBoot.set(ControlMode.PercentOutput, 0.90);
        }
        if (this.getSequenceRequiring().getTimeSinceStartOfState() > 100) {
            RobotMap.shooterBoot.set(ControlMode.PercentOutput, 0.0);
        }
    }

    @Override
    public void neutral() {
        shoot(0);
        RobotMap.shooterBoot.set(ControlMode.PercentOutput, 0.0);
    }

    @Override
    public boolean abort() {
        shoot(0);
        RobotMap.shooterBoot.set(ControlMode.PercentOutput, 0.0);
        return true;
    }

}

