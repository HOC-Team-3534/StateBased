package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap;
import frc.robot.subsystems.parent.BaseSubsystem;

public class Climber extends BaseSubsystem {

    public Climber() {
        // makes the graphical number to enter text - have to do a
        // put to do a get
        // SmartDashboard.putNumber("RPM: ", 0.0);
    }

    @Override
    public void process() {
        super.process();
        if (getStateRequiringName() == "PREPCLAW") {
            prepClaw();
        } else if (getStateRequiringName() == "SWINGARM") {
            swingArm();
        } else if (getStateRequiringName() == "PREPPEDFORCLIMB") {

        } else if (getStateRequiringName() == "GRIPMIDBAR") {
            gripMidBar();
        } else if (getStateRequiringName() == "SWINGMIDHIGH") {
            swingMidHigh();
        } else if (getStateRequiringName() == "GRIPHIGHBAR") {
            gripHighBar();
        } else if (getStateRequiringName() == "RETRYHIGHBAR") {
            retryHighBar();
        } else if (getStateRequiringName() == "RELEASEMIDBAR") {
            releaseMidBar();
        } else if (getStateRequiringName() == "SWINGHIGHTRAVERSAL") {
            swingHighTraversal();
        } else if (getStateRequiringName() == "GRIPTRAVERSALBAR") {
            gripTraversalBar();
        } else if (getStateRequiringName() == "RETRYTRAVERSALBAR") {
            retryTraversalBar();
        } else if (getStateRequiringName() == "RELEASEHIGHBAR") {
            releaseHighBar();
        } else if (getStateRequiringName() == "SWINGTOREST") {
            swingToRest();
        } else {
            neutral();
        }
        SmartDashboard.putNumber("Arm Encoder Position", RobotMap.m_climbMotor.getSelectedSensorPosition());
    }

    public void prepClaw() {

        if (this.getStateFirstRunThrough()) {
            RobotMap.m_l1Claw.set(Value.kForward);
        } else if (this.getSequenceRequiring().getTimeSinceStartOfState() > 500) {
            RobotMap.m_l1Claw.set(Value.kOff);
        }
    }

    public void swingArm() {
        RobotMap.m_climbMotor.set(ControlMode.Position, 90);
    }

    public void gripMidBar() {

        if (this.getStateFirstRunThrough()) {
            RobotMap.m_h2Claw.set(Value.kForward);
        } else if (this.getSequenceRequiring().getTimeSinceStartOfState() > 500) {
            RobotMap.m_h2Claw.set(Value.kOff);
        }
    }

    public void swingMidHigh() {

        if (this.getStateFirstRunThrough()) {
            RobotMap.m_l3Claw.set(Value.kForward);
        } else if (this.getSequenceRequiring().getTimeSinceStartOfState() > 500) {
            RobotMap.m_l3Claw.set(Value.kOff);
        }
        RobotMap.m_climbMotor.set(ControlMode.PercentOutput, 1.0);
    }

    public void gripHighBar() {

        if (this.getStateFirstRunThrough()) {
            RobotMap.m_h4Claw.set(Value.kForward);
        } else if (this.getSequenceRequiring().getTimeSinceStartOfState() > 500) {
            RobotMap.m_h4Claw.set(Value.kOff);
        }
    }

    public void retryHighBar() {

        if (this.getStateFirstRunThrough()) {
            RobotMap.m_h4Claw.set(Value.kReverse);
        } else if (this.getSequenceRequiring().getTimeSinceStartOfState() > 500) {
            RobotMap.m_h4Claw.set(Value.kOff);
        }
    }

    public void releaseMidBar() {

        if (this.getStateFirstRunThrough()) {
            RobotMap.m_l1Claw.set(Value.kReverse);
            RobotMap.m_h2Claw.set(Value.kReverse);
        } else if (this.getSequenceRequiring().getTimeSinceStartOfState() > 500) {
            RobotMap.m_l1Claw.set(Value.kOff);
            RobotMap.m_h2Claw.set(Value.kOff);
        }
    }

    public void swingHighTraversal() {

        if (this.getStateFirstRunThrough()) {
            RobotMap.m_l1Claw.set(Value.kForward);
        } else if (this.getSequenceRequiring().getTimeSinceStartOfState() > 500) {
            RobotMap.m_l1Claw.set(Value.kOff);
        }
    }

    public void gripTraversalBar() {

        if (this.getStateFirstRunThrough()) {
            RobotMap.m_h2Claw.set(Value.kForward);
        } else if (this.getSequenceRequiring().getTimeSinceStartOfState() > 500) {
            RobotMap.m_h2Claw.set(Value.kOff);
        }
    }

    public void retryTraversalBar() {

        if (this.getStateFirstRunThrough()) {
            RobotMap.m_h2Claw.set(Value.kReverse);
        } else if (this.getSequenceRequiring().getTimeSinceStartOfState() > 500) {
            RobotMap.m_h2Claw.set(Value.kOff);
        }
    }

    public void releaseHighBar() {

        if (this.getStateFirstRunThrough()) {
            RobotMap.m_l3Claw.set(Value.kReverse);
            RobotMap.m_h4Claw.set(Value.kReverse);
        } else if (this.getSequenceRequiring().getTimeSinceStartOfState() > 500) {
            RobotMap.m_l3Claw.set(Value.kOff);
            RobotMap.m_h4Claw.set(Value.kOff);
        }
    }

    public void swingToRest() {
        RobotMap.m_climbMotor.set(ControlMode.Position, 450);
    }

    @Override
    public void neutral() {
        RobotMap.m_l1Claw.set(Value.kOff);
        RobotMap.m_h2Claw.set(Value.kOff);
        RobotMap.m_l3Claw.set(Value.kOff);
        RobotMap.m_h4Claw.set(Value.kOff);
    }

    @Override
    public boolean abort() {
        RobotMap.m_l1Claw.set(Value.kOff);
        RobotMap.m_h2Claw.set(Value.kOff);
        RobotMap.m_l3Claw.set(Value.kOff);
        RobotMap.m_h4Claw.set(Value.kOff);
        RobotMap.m_climbMotor.set(ControlMode.PercentOutput, 0.0);
        return true;
    }
}
