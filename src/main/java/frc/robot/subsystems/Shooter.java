package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.subsystems.parent.BaseSubsystem;

public class Shooter extends BaseSubsystem {

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

        if (getStateRequiringName() == "WAITNSPIN") {
            // grabs the number from SmartDashboard
            waitNSpin(SmartDashboard.getNumber("RPM: ", 0.0));
        } else if (getStateRequiringName() == "BURP") {
            burp();
        } else if (getStateRequiringName() == "PUNCH") {
            punch();
        }else if (getStateRequiringName() == "RETRACT") {
            retract();
        } else {
            neutral();
        }
    }

    public void shoot(double rpm) {
        double countsPer100MS = rpm * Constants.RPM_TO_COUNTS_PER_100MS;
        RobotMap.shooter.set(ControlMode.Velocity, countsPer100MS);
        //System.out.println("Speed is " + countsPer100MS);
    }

    private void waitNSpin(double rpm) {
        // shoot(rpm);
        shoot(rpmFunction.getShooterRPM(RobotMap.limelight.getDistance()));
        
    }

    private void burp() {
        shoot(2000);
    }

    private void punch() {
        setWithADelayToOff(RobotMap.pusher, Value.kForward, Constants.DelayToOff.SHOOTER_PUSHER.millis);
    }

    private void retract() {
        setWithADelayToOff(RobotMap.pusher, Value.kReverse, Constants.DelayToOff.SHOOTER_PUSHER.millis);
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
