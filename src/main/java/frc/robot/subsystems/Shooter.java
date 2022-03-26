package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.subsystems.parent.BaseSubsystem;
import frc.robot.subsystems.parent.IDistanceToShooterRPM;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Shooter extends BaseSubsystem {

    String[] autonPreShootStateStrings = { "PICKUPBALL1", "PICKUPBALL3" };
    String[] autonShootStateStrings = { "SHOOTBALL1", "SHOOTBALL2", "SHOOTBALL3" };
    String[] autonPunchStateStrings = { "PUNCH1", "PUNCH2", "PUNCH3" };
    String[] autonResetPunchStateStrings = { "RESETPUNCH1", "RESETPUNCH2", "RESETPUNCH3" };
    Set<String> autonPreShootStates = new HashSet<>(Arrays.asList(autonPreShootStateStrings));
    Set<String> autonShootStates = new HashSet<>(Arrays.asList(autonShootStateStrings));
    Set<String> autonPunchStates = new HashSet<>(Arrays.asList(autonPunchStateStrings));
    Set<String> autonResetPunchStates = new HashSet<>(Arrays.asList(autonResetPunchStateStrings));

    IDistanceToShooterRPM rpmFunction;

    public Shooter(IDistanceToShooterRPM rpmFunction) {
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

        // TODO add in auton control of shooter once vision branch merged in
        if (getStateRequiringName() == "WAITNSPIN") {
            // grabs the number from SmartDashboard
            //waitNSpin(SmartDashboard.getNumber("Manual Testing RPM", 0.0));
            if (RobotMap.limelight.isLockedOn()) {
                waitNSpin();
            } else {
                waitNSpin(3000);
            }
        } else if (autonPreShootStates.contains(getStateRequiringName())) {
            if (Robot.swerveDrive.getPathStateController().getPathPlannerFollower() != null) {
                if (Robot.swerveDrive.getPathStateController().getPathPlannerFollower()
                        .getRemainingTimeSeconds() < 1.0) {
                    autonShoot();
                } else {
                    neutral();
                }
            } else {
                neutral();
            }
        } else if (autonShootStates.contains(getStateRequiringName())) {
            autonShoot();
        } else if (getStateRequiringName() == "BURP") {
            burp();
        } else if (getStateRequiringName() == "PUNCH" || autonPunchStates.contains(getStateRequiringName())) {
            punch();
        } else if (getStateRequiringName() == "RESETPUNCH" || getStateRequiringName() == "RETRACT"
                || autonResetPunchStates.contains(getStateRequiringName())) {
            resetPunch();
        } else if (getStateRequiringName() == "BOOT") {
            boot();
        } else {
            neutral();
        }
    }

    public void shoot(double rpm) {
        double countsPer100MS = rpm * Constants.RPM_TO_COUNTS_PER_100MS;
        RobotMap.shooter.set(ControlMode.Velocity, countsPer100MS);
        // System.out.println("Speed is " + countsPer100MS);

    }

    public double getRPM(double distance){
        double output = rpmFunction.getShooterRPM(distance);
        if(output < 2500){
            output = 2500;
        }
        return output;
    }

    private void waitNSpin() {
        double rpmMultiplier = SmartDashboard.getNumber("RPM MULTIPLIER (%)", 100.0) / 100.0;
        shoot(rpmMultiplier * rpmFunction.getShooterRPM(RobotMap.limelight.getDistance()));
    }

    private void waitNSpin(double rpm) {
        shoot(rpm);
    }

    private void autonShoot() {
        double autonRPMMultiplier = SmartDashboard.getNumber("AUTON RPM MULTIPLIER (%)", 100.0) / 100.0;
        shoot(autonRPMMultiplier * rpmFunction.getShooterRPM(RobotMap.limelight.getDistance()
                /* getRPM(Robot.swerveDrive.getMetersFromLocation(Constants.HUB_LOCATION)*/));
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
