package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.subsystems.parent.BaseSubsystem;

import frc.robot.Constants.DelayToOff;
import frc.robot.Constants.DelayToOff.*;

import java.util.HashMap;
import java.util.Map;

public class Climber extends BaseSubsystem {

    public Climber() {
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

    public double getClimbArmDegree() {
        return RobotMap.m_climbMotor.getSelectedSensorPosition() * Constants.FALCON_TICKS_TO_ARM_DEGREES;
    }

    public void setClimbArmDegree(double degree) {
        RobotMap.m_climbMotor.set(ControlMode.MotionMagic, degree * Constants.ARM_DEGREES_TO_FALCON_TICKS);
    }

    public void prepClaw() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_l1Claw, Value.kForward, DelayToOff.CLIMB_CLAWS.millis);
            setWithADelayToOff(RobotMap.m_h2Claw, Value.kReverse, DelayToOff.CLIMB_CLAWS.millis);
            setWithADelayToOff(RobotMap.m_l3Claw, Value.kReverse, DelayToOff.CLIMB_CLAWS.millis);
            setWithADelayToOff(RobotMap.m_h4Claw, Value.kReverse, DelayToOff.CLIMB_CLAWS.millis);
        }
    }

    public void swingArm() {

        if (this.getStateFirstRunThrough()) {
            setClimbArmDegree(90.0);
        }
    }

    public void gripMidBar() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_h2Claw, Value.kForward, DelayToOff.CLIMB_CLAWS.millis);
        }
    }

    public void swingMidHigh() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_l3Claw, Value.kForward, DelayToOff.CLIMB_CLAWS.millis);
            setClimbArmDegree(225.0);
        }
    }

    public void gripHighBar() {

        if (this.getStateFirstRunThrough()) {
            RobotMap.m_h4Claw.set(Value.kForward);
            setWithADelayToOff(RobotMap.m_h4Claw, Value.kForward, DelayToOff.CLIMB_CLAWS.millis);
            setClimbArmDegree(getClimbArmDegree());
            System.out.println("Grip High Bar Climb Degree: " + getClimbArmDegree());
        }
    }

    public void retryHighBar() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_h4Claw, Value.kReverse, DelayToOff.CLIMB_CLAWS.millis);
            double angle = (getClimbArmDegree() < 180.0) ? 180.0 : getClimbArmDegree() - 15.0;
            setClimbArmDegree(angle);
        }
    }

    public void releaseMidBar() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_l1Claw, Value.kReverse, DelayToOff.CLIMB_CLAWS.millis);
            setWithADelayToOff(RobotMap.m_h2Claw, Value.kReverse, DelayToOff.CLIMB_CLAWS.millis);
            setClimbArmDegree(405.0);
        }
    }

    public void swingHighTraversal() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_l1Claw, Value.kForward, DelayToOff.CLIMB_CLAWS.millis);
            setClimbArmDegree(405.0);
        }
    }

    public void gripTraversalBar() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_h2Claw, Value.kForward, DelayToOff.CLIMB_CLAWS.millis);
            setClimbArmDegree(getClimbArmDegree());
        }
    }

    public void retryTraversalBar() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_h2Claw, Value.kReverse, DelayToOff.CLIMB_CLAWS.millis);
            double angle = (getClimbArmDegree() < 360.0) ? 360.0 : getClimbArmDegree() - 15.0;
            setClimbArmDegree(angle);
        }
    }

    public void releaseHighBar() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_l3Claw, Value.kReverse, DelayToOff.CLIMB_CLAWS.millis);
            setWithADelayToOff(RobotMap.m_h4Claw, Value.kReverse, DelayToOff.CLIMB_CLAWS.millis);
            setClimbArmDegree(450.0);
        }
    }

    public void swingToRest() {
        if(this.getStateFirstRunThrough()) {
            setClimbArmDegree(450.0);
        }
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
        forceRelease();
        return true;
    }
}
