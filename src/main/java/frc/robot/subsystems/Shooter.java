package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.subsystems.parent.BaseSubsystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Shooter extends BaseSubsystem {

    String[] autonShootStateStrings = {"SHOOTBALL1", "SHOOTBALL2"};
    String[] autonPunchStateStrings = {"PICKUPBALL1", "PICKUPBALL2"};
    String[] autonResetPunchStateStrings = {"SHOOTBALL1", "SHOOTBALL3"};
    Set<String> autonShootStates = new HashSet<>(Arrays.asList(autonShootStateStrings));
    Set<String> autonPunchStates = new HashSet<>(Arrays.asList(autonPunchStateStrings));
    Set<String> autonResetPunchStates = new HashSet<>(Arrays.asList(autonResetPunchStateStrings));

    IDistanceToShooterRPM rpmFunction;

    public Shooter(IDistanceToShooterRPM rpmFunction) {
        // makes the graphical number to enter text - have to do a
        // put to do a get
        SmartDashboard.putNumber("RPM: ", 4250.0);
        this.rpmFunction = rpmFunction;
    }

    @Override
    public void process() {

        super.process();

        //TODO add in auton control of shooter once vision branch merged in
        if (getStateRequiringName() == "WAITNSPIN") {
            // grabs the number from SmartDashboard
            // waitNSpin(SmartDashboard.getNumber("RPM: ", 0.0));
            waitNSpin();
        } else if (autonShootStates.contains(getStateRequiringName())){
            autonShoot();
        } else if (getStateRequiringName() == "BURP") {
            burp();
        } else if (getStateRequiringName() == "PUNCH" || autonPunchStates.contains(getStateRequiringName())) {
            punch();
        }else if (getStateRequiringName() == "RESETPUNCH" || autonResetPunchStates.contains(getStateRequiringName())) {
            resetPunch();
        } else {
            neutral();
        }
    }

    public void shoot(double rpm) {
        double countsPer100MS = rpm * Constants.RPM_TO_COUNTS_PER_100MS;
        RobotMap.shooter.set(ControlMode.Velocity, countsPer100MS);
        //System.out.println("Speed is " + countsPer100MS);
    }

    private void waitNSpin() {
        shoot(rpmFunction.getShooterRPM(RobotMap.limelight.getDistance()));
    }

    private void waitNSpin(double rpm) {
        shoot(rpm);
    }

    private void autonShoot() {
        shoot(rpmFunction.getShooterRPM(Robot.swerveDrive.getMetersFromLocation(Constants.HUB_LOCATION)));
    }

    private void burp() {
        shoot(2000);
    }

    private void punch() {
        if(getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.pusher, Value.kForward, Constants.DelayToOff.SHOOTER_PUSHER.millis);
        }
    }

    private void resetPunch() {
        if(getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.pusher, Value.kReverse, Constants.DelayToOff.SHOOTER_PUSHER.millis);
        }
    }

    @Override
    public void neutral() {
        shoot(0);
    }

    @Override
    public boolean abort() {
        shoot(0);
        return true;
    }

}
