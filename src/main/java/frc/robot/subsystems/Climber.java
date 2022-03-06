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
import frc.robot.RobotContainer.Buttons;

import java.util.HashMap;
import java.util.Map;

import static frc.robot.Constants.*;

public class Climber extends BaseSubsystem {

    public Climber() {
    }

    @Override
    public void process() {
        // PAUSE BUTTON LOGIC, MAYBE FIND A WAY TO MAKE IT A HOLD INSTEAD?
        // if pause button pressed && !neutral, climbstate laststate = laststate
        // setnextstate = paused , updatestate
        // if climb button pressed after pause, setnextstate = laststate, laststate =
        // null, updatestate
        super.process();
        if (getStateRequiringName() == "PREPCLAW") {
            prepClaw();
        } else if (getStateRequiringName() == "SWINGARM") {
            swingArm();
        } else if (getStateRequiringName() == "PREPPEDFORCLIMB") {

        } else if (getStateRequiringName() == "GRIPMIDBAR") {
            gripMidBar();
        } else if (getStateRequiringName() == "SWINGMIDHIGH1") {
            swingMidHigh1();
        } else if (getStateRequiringName() == "SWINGMIDHIGH2") {
            swingMidHigh2();
        } else if (getStateRequiringName() == "GRIPHIGHBAR") {
            gripHighBar();
        } else if (getStateRequiringName() == "RETRYHIGHBAR") {
            retryHighBar();
        } else if (getStateRequiringName() == "RECENTERMIDHIGHBAR") {
            recenterMidHighBar();
        } else if (getStateRequiringName() == "RELEASEMIDBAR") {
            releaseMidBar();
        } else if (getStateRequiringName() == "SWINGHIGHTRAVERSAL1") {
            swingHighTraversal1();
        } else if (getStateRequiringName() == "SWINGHIGHTRAVERSAL2") {
            swingHighTraversal2();
        } else if (getStateRequiringName() == "GRIPTRAVERSALBAR") {
            gripTraversalBar();
        } else if (getStateRequiringName() == "RETRYTRAVERSALBAR") {
            retryTraversalBar();
        } else if (getStateRequiringName() == "RECENTERHIGHTRAVERSALBAR") {
            recenterHighTraversalBar();
        } else if (getStateRequiringName() == "RELEASEHIGHBAR") {
            releaseHighBar();
        } else if (getStateRequiringName() == "SWINGTOREST") {
            swingToRest();
        } else if (getStateRequiringName() == "RESETARM") {
            resetArm();
        }else if(getStateRequiringName() == "MOVEARMMANUALLY"){
            moveArmManually();
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
            setClimbArmDegree(MIDBAR_GRAB_ANGLE_COMMAND);
            RobotMap.m_climbMotor.configMotionCruiseVelocity(MAX_ARM_VELOCITY_NATIVE_UNITS, 20);
            RobotMap.m_climbMotor.configMotionAcceleration(MAX_ARM_ACCELERATION_NATIVE_UNITS, 20);
        }
    }

    public void gripMidBar() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_h2Claw, Value.kForward, DelayToOff.CLIMB_CLAWS.millis);
        }
    }

    public void swingMidHigh1() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_l3Claw, Value.kForward, DelayToOff.CLIMB_CLAWS.millis);
            setClimbArmDegree(HIGHBAR_GRAB_ANGLE_COMMAND);
            RobotMap.m_climbMotor.configMotionCruiseVelocity(MAX_ARM_VELOCITY_NATIVE_UNITS, 20);
            RobotMap.m_climbMotor.configMotionAcceleration(MAX_ARM_ACCELERATION_NATIVE_UNITS, 20);
        }
    }

    public void swingMidHigh2() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_l3Claw, Value.kForward, DelayToOff.CLIMB_CLAWS.millis);
            setClimbArmDegree(HIGHBAR_GRAB_ANGLE_COMMAND);
            RobotMap.m_climbMotor.configMotionCruiseVelocity(MAX_ARM_VELOCITY_NATIVE_UNITS_SLOW, 20);
            RobotMap.m_climbMotor.configMotionAcceleration(MAX_ARM_ACCELERATION_NATIVE_UNITS_SLOW, 20);
        }
    }

    public void gripHighBar() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_h4Claw, Value.kForward, DelayToOff.CLIMB_CLAWS.millis);
            setClimbArmDegree(getClimbArmDegree());
            System.out.println("Grip High Bar Climb Degree: " + getClimbArmDegree());
        }
    }

    public void retryHighBar() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_h4Claw, Value.kReverse, DelayToOff.CLIMB_CLAWS.millis);
            double angle = (getClimbArmDegree() < MIDHIGHBAR_SLOWDOWN_ANGLE) ? MIDHIGHBAR_SLOWDOWN_ANGLE : getClimbArmDegree() - 15.0;
            setClimbArmDegree(angle);
        }
    }

    public void recenterMidHighBar(){

        if(this.getStateFirstRunThrough()){
            setClimbArmDegree(MIDHIGHBAR_RECENTER_ANGLE_COMMAND);
            RobotMap.m_climbMotor.configMotionCruiseVelocity(MAX_ARM_VELOCITY_NATIVE_UNITS, 20);
            RobotMap.m_climbMotor.configMotionAcceleration(MAX_ARM_ACCELERATION_NATIVE_UNITS, 20);
        }
    }

    public void releaseMidBar() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_l1Claw, Value.kReverse, DelayToOff.CLIMB_CLAWS.millis);
            setWithADelayToOff(RobotMap.m_h2Claw, Value.kReverse, DelayToOff.CLIMB_CLAWS.millis);
            setClimbArmDegree(TRAVERSALBAR_GRAB_ANGLE_COMMMAND);
            RobotMap.m_climbMotor.configMotionCruiseVelocity(MAX_ARM_VELOCITY_NATIVE_UNITS, 20);
            RobotMap.m_climbMotor.configMotionAcceleration(MAX_ARM_ACCELERATION_NATIVE_UNITS, 20);
        }
    }

    public void swingHighTraversal1() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_l1Claw, Value.kForward, DelayToOff.CLIMB_CLAWS.millis);
            setClimbArmDegree(TRAVERSALBAR_GRAB_ANGLE_COMMMAND);
            RobotMap.m_climbMotor.configMotionCruiseVelocity(MAX_ARM_VELOCITY_NATIVE_UNITS, 20);
            RobotMap.m_climbMotor.configMotionAcceleration(MAX_ARM_ACCELERATION_NATIVE_UNITS, 20);
        }
    }

    public void swingHighTraversal2() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_l1Claw, Value.kForward, DelayToOff.CLIMB_CLAWS.millis);
            setClimbArmDegree(TRAVERSALBAR_GRAB_ANGLE_COMMMAND);
            RobotMap.m_climbMotor.configMotionCruiseVelocity(MAX_ARM_VELOCITY_NATIVE_UNITS_SLOW, 20);
            RobotMap.m_climbMotor.configMotionAcceleration(MAX_ARM_ACCELERATION_NATIVE_UNITS_SLOW, 20);
        }
    }

    public void gripTraversalBar() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_h2Claw, Value.kForward, DelayToOff.CLIMB_CLAWS.millis);
            setClimbArmDegree(getClimbArmDegree());
            System.out.println("Grip Traversal Bar Climb Degree: " + getClimbArmDegree());
        }
    }

    public void retryTraversalBar() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_h2Claw, Value.kReverse, DelayToOff.CLIMB_CLAWS.millis);
            double angle = (getClimbArmDegree() < HIGHTRAVERSAL_SLOWDOWN_ANGLE) ? HIGHTRAVERSAL_SLOWDOWN_ANGLE : getClimbArmDegree() - 15.0;
            setClimbArmDegree(angle);
        }
    }

    public void recenterHighTraversalBar(){

        if(this.getStateFirstRunThrough()){
            setClimbArmDegree(HIGHTRAVERSALBAR_RECENTER_ANGLE_COMMAND);
            RobotMap.m_climbMotor.configMotionCruiseVelocity(MAX_ARM_VELOCITY_NATIVE_UNITS, 20);
            RobotMap.m_climbMotor.configMotionAcceleration(MAX_ARM_ACCELERATION_NATIVE_UNITS, 20);
        }
    }

    public void releaseHighBar() {

        if (this.getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_l3Claw, Value.kReverse, DelayToOff.CLIMB_CLAWS.millis);
            setWithADelayToOff(RobotMap.m_h4Claw, Value.kReverse, DelayToOff.CLIMB_CLAWS.millis);
            setClimbArmDegree(SWINGTOREST_ANGLE_COMMAND);
            RobotMap.m_climbMotor.configMotionCruiseVelocity(MAX_ARM_VELOCITY_NATIVE_UNITS, 20);
            RobotMap.m_climbMotor.configMotionAcceleration(MAX_ARM_ACCELERATION_NATIVE_UNITS, 20);
        }
    }

    public void swingToRest() {
        if (this.getStateFirstRunThrough()) {
            setClimbArmDegree(SWINGTOREST_ANGLE_COMMAND);
        }
    }

    public void resetArm(){
        if(this.getStateFirstRunThrough()){
            setWithADelayToOff(RobotMap.m_l1Claw, Value.kReverse, DelayToOff.CLIMB_CLAWS.millis);
            setClimbArmDegree(0.0);
            RobotMap.m_climbMotor.configMotionCruiseVelocity(MAX_ARM_VELOCITY_NATIVE_UNITS, 20);
            RobotMap.m_climbMotor.configMotionAcceleration(MAX_ARM_ACCELERATION_NATIVE_UNITS, 20);
        }
    }

    public void moveArmManually(){
        if(Buttons.MoveClimbArmForward.getButton()){
            RobotMap.m_climbMotor.set(ControlMode.PercentOutput, 0.30);
        }else if(Buttons.MoveClimbArmBackward.getButton()){
            RobotMap.m_climbMotor.set(ControlMode.PercentOutput, -0.30);
        }else if(Buttons.ClimbArmEncoderReset.getButton()){
            RobotMap.m_climbMotor.set(ControlMode.PercentOutput, 0.0);
            RobotMap.m_climbMotor.setSelectedSensorPosition(0.0);
        }else{
            RobotMap.m_climbMotor.set(ControlMode.PercentOutput, 0.0);
        }
        
    }

    @Override
    public void neutral() {
        RobotMap.m_l1Claw.set(Value.kOff);
        RobotMap.m_h2Claw.set(Value.kOff);
        RobotMap.m_l3Claw.set(Value.kOff);
        RobotMap.m_h4Claw.set(Value.kOff);
        RobotMap.m_climbMotor.set(ControlMode.PercentOutput, 0.0);
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
