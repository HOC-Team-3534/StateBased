package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.subsystems.parent.BaseSubsystem;

public class Shooter extends BaseSubsystem {

    public Shooter() {
        // makes the graphical number to enter text - have to do a
        // put to do a get
        SmartDashboard.putNumber("RPM: ", 3500.0);
    }

    @Override
    public void process() {

        super.process();

        if (getStateRequiringName() == "WAITNSPIN") {
            // grabs the number from SmartDashboard
            waitNSpin(SmartDashboard.getNumber("RPM: ", 0.0));
        } else if (getStateRequiringName() == "PUNCH") {
            punch();
        } else if (getStateRequiringName() == "RETRACT") {
            retract();
        } else {
            neutral();
        }
    }

    public void shoot(double rpm) {
        double countsPer100MS = rpm * Constants.RPM_TO_COUNTS_PER_100MS;
        RobotMap.shooter.set(ControlMode.Velocity, countsPer100MS);
        System.out.println("Speed is " + countsPer100MS);
    }

    private void waitNSpin(double rpm) {
        shoot(rpm);
    }

    private void punch() {

        if (this.getStateFirstRunThrough()) {
            RobotMap.pusher.set(Value.kForward);
        } else if (this.getSequenceRequiring().getTimeSinceStartOfState() > 250) {
            RobotMap.pusher.set(Value.kOff);
        }
    }

    private void retract() {

        if (this.getStateFirstRunThrough()) {
            RobotMap.pusher.set(Value.kReverse);
        } else if (this.getSequenceRequiring().getTimeSinceStartOfState() > 250) {
            RobotMap.pusher.set(Value.kOff);
        }
    }

    @Override
    public void neutral() {
        shoot(0);
    }

    @Override
    public boolean abort() {
        shoot(0);
        forceRelease();
        return true;
    }

}
