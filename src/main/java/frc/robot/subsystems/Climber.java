package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.RobotContainer.Buttons;
import frc.robot.RobotMap;
import frc.robot.subsystems.parent.BaseSubsystem;
import frc.robot.subsystems.states.ClimberState;

import static frc.robot.Constants.*;

public class Climber extends BaseSubsystem<ClimberState> {

    public Climber() {
        super(ClimberState.NEUTRAL);
    }

    @Override
    public void process() {

        super.process();

        SmartDashboard.putNumber("Arm Encoder Position", RobotMap.m_climbMotor.getSelectedSensorPosition());

        switch (getCurrentSubsystemState()) {
            case NEUTRAL:
                neutral();
                break;
            case PREPCLAW:
                prepClaw();
                break;
            case SWINGARM:
                swingArm();
                break;
            case GRIPMIDBAR:
                gripMidBar();
                break;
            case SWINGMIDHIGH1:
                swingMidHigh1();
                break;
            case SWINGMIDHIGH2:
                swingMidHigh2();
                break;
            case GRIPHIGHBAR:
                gripHighBar();
                break;
            case RETRYHIGHBAR:
                retryHighBar();
                break;
            case RECENTERMIDHIGHBAR:
                recenterMidHighBar();
                break;
            case RELEASEMIDBAR:
                releaseMidBar();
                break;
            case SWINGHIGHTRAVERSAL1:
                swingHighTraversal1();
                break;
            case SWINGHIGHTRAVERSAL2:
                swingHighTraversal2();
                break;
            case GRIPTRAVERSALBAR:
                gripTraversalBar();
                break;
            case RETRYTRAVERSALBAR:
                retryTraversalBar();
                break;
            case RECENTERHIGHTRAVERSALBAR:
                recenterHighTraversalBar();
                break;
            case RELEASEHIGHBAR:
                releaseHighBar();
                break;
            case SWINGTOREST:
                swingToRest();
                break;
            case RESETARM:
                resetArm();
                break;
            case MOVEARMMANUALLY:
                moveArmManually();
                break;
        }
    }

    public double getClimbArmDegree() {
        return RobotMap.m_climbMotor.getSelectedSensorPosition() * Constants.FALCON_TICKS_TO_ARM_DEGREES;
    }

    public void setClimbArmDegree(double degree) {
        RobotMap.m_climbMotor.set(ControlMode.MotionMagic, degree * Constants.ARM_DEGREES_TO_FALCON_TICKS);
    }

    private void setL1(Value value) {
        setWithADelayToOff(RobotMap.m_l1Claw, value, DelayToOff.CLIMB_CLAWS.millis);
    }

    private void setH2(Value value) {
        setWithADelayToOff(RobotMap.m_h2Claw, value, DelayToOff.CLIMB_CLAWS.millis);
    }

    private void setL3(Value value) {
        setWithADelayToOff(RobotMap.m_l3Claw, value, DelayToOff.CLIMB_CLAWS.millis);
    }

    private void setH4(Value value) {
        setWithADelayToOff(RobotMap.m_h4Claw, value, DelayToOff.CLIMB_CLAWS.millis);
    }

    private void setHighVelocity() {
        RobotMap.m_climbMotor.configMotionCruiseVelocity(MAX_ARM_VELOCITY_NATIVE_UNITS, 20);
    }

    private void setHighAcceleration() {
        RobotMap.m_climbMotor.configMotionAcceleration(MAX_ARM_ACCELERATION_NATIVE_UNITS, 20);
    }

    private void setLowVelocity() {
        RobotMap.m_climbMotor.configMotionCruiseVelocity(MAX_ARM_VELOCITY_NATIVE_UNITS_SLOW, 20);
    }

    private void setLowAcceleration() {
        RobotMap.m_climbMotor.configMotionAcceleration(MAX_ARM_ACCELERATION_NATIVE_UNITS_SLOW, 20);
    }

    public void prepClaw() {

        if (this.getStateFirstRunThrough()) {
            setL1(Value.kForward);
            setH2(Value.kReverse);
            setL3(Value.kReverse);
            setH4(Value.kReverse);
        }
    }

    public void swingArm() {

        if (this.getStateFirstRunThrough()) {
            setClimbArmDegree(MIDBAR_GRAB_ANGLE_COMMAND);
            setHighVelocity();
            setHighAcceleration();
        }
    }

    public void gripMidBar() {

        if (this.getStateFirstRunThrough()) {
            setH2(Value.kForward);
        }
    }

    public void swingMidHigh1() {

        if (this.getStateFirstRunThrough()) {
            setL3(Value.kForward);
            setClimbArmDegree(HIGHBAR_GRAB_ANGLE_COMMAND);
            setHighVelocity();
            setLowAcceleration();
        }
    }

    public void swingMidHigh2() {

        if (this.getStateFirstRunThrough()) {
            setL3(Value.kForward);
            setClimbArmDegree(HIGHBAR_GRAB_ANGLE_COMMAND);
            setLowVelocity();
            setLowAcceleration();
        }
    }

    public void gripHighBar() {

        if (this.getStateFirstRunThrough()) {
            setH4(Value.kForward);
            setClimbArmDegree(getClimbArmDegree());
            System.out.println("Grip High Bar Climb Degree: " + getClimbArmDegree());
        }
    }

    public void retryHighBar() {

        if (this.getStateFirstRunThrough()) {
            setH4(Value.kReverse);
            double angle = (getClimbArmDegree() < MIDHIGHBAR_SLOWDOWN_ANGLE) ? MIDHIGHBAR_SLOWDOWN_ANGLE : getClimbArmDegree() - 15.0;
            setClimbArmDegree(angle);
        }
    }

    public void recenterMidHighBar() {

        if (this.getStateFirstRunThrough()) {
            setClimbArmDegree(MIDHIGHBAR_RECENTER_ANGLE_COMMAND);
            setHighVelocity();
            setHighAcceleration();
        }
    }

    public void releaseMidBar() {

        if (this.getStateFirstRunThrough()) {
            setL1(Value.kReverse);
            setH2(Value.kReverse);
            setClimbArmDegree(TRAVERSALBAR_GRAB_ANGLE_COMMMAND);
            setHighVelocity();
            setHighAcceleration();
        }
    }

    public void swingHighTraversal1() {

        if (this.getStateFirstRunThrough()) {
            setL1(Value.kForward);
            setClimbArmDegree(TRAVERSALBAR_GRAB_ANGLE_COMMMAND);
            setHighVelocity();
            setHighAcceleration();
        }
    }

    public void swingHighTraversal2() {

        if (this.getStateFirstRunThrough()) {
            setL1(Value.kForward);
            setClimbArmDegree(TRAVERSALBAR_GRAB_ANGLE_COMMMAND);
            setLowVelocity();
            setLowAcceleration();
        }
    }

    public void gripTraversalBar() {

        if (this.getStateFirstRunThrough()) {
            setH2(Value.kForward);
            setClimbArmDegree(getClimbArmDegree());
            System.out.println("Grip Traversal Bar Climb Degree: " + getClimbArmDegree());
        }
    }

    public void retryTraversalBar() {

        if (this.getStateFirstRunThrough()) {
            setH2(Value.kReverse);
            double angle = (getClimbArmDegree() < HIGHTRAVERSAL_SLOWDOWN_ANGLE) ? HIGHTRAVERSAL_SLOWDOWN_ANGLE : getClimbArmDegree() - 15.0;
            setClimbArmDegree(angle);
        }
    }

    public void recenterHighTraversalBar() {

        if (this.getStateFirstRunThrough()) {
            setClimbArmDegree(HIGHTRAVERSALBAR_RECENTER_ANGLE_COMMAND);
            setHighVelocity();
            setHighAcceleration();
        }
    }

    public void releaseHighBar() {

        if (this.getStateFirstRunThrough()) {
            setL3(Value.kReverse);
            setH4(Value.kReverse);
            setClimbArmDegree(SWINGTOREST_ANGLE_COMMAND);
            setHighVelocity();
            setHighAcceleration();
        }
    }

    public void swingToRest() {
        if (this.getStateFirstRunThrough()) {
            setClimbArmDegree(SWINGTOREST_ANGLE_COMMAND);
        }
    }

    public void resetArm() {
        if (this.getStateFirstRunThrough()) {
            setL1(Value.kReverse);
            setClimbArmDegree(0.0);
            setHighVelocity();
            setHighAcceleration();
        }
    }

    public void moveArmManually() {
        if (Buttons.MoveClimbArmForward.getButton()) {
            RobotMap.m_climbMotor.set(ControlMode.PercentOutput, 0.30);
        } else if (Buttons.MoveClimbArmBackward.getButton()) {
            RobotMap.m_climbMotor.set(ControlMode.PercentOutput, -0.30);
        } else if (Buttons.ClimbArmEncoderReset.getButton()) {
            RobotMap.m_climbMotor.set(ControlMode.PercentOutput, 0.0);
            RobotMap.m_climbMotor.setSelectedSensorPosition(0.0);
        } else {
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

