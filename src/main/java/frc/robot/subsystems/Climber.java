package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer.Buttons;
import frc.robot.subsystems.parent.BaseSubsystem;

import static frc.robot.Constants.*;

public class Climber extends BaseSubsystem<ClimberState> {

    public static DigitalInput l1Switch;
    public static DigitalInput h2Switch;
    public static DigitalInput l3Switch;
    public static DigitalInput h4Switch;
    static WPI_TalonFX climbMotor;
    static PneumaticsControlModule climbPCM;
    static DoubleSolenoid l1Claw;
    static DoubleSolenoid h2Claw;
    static DoubleSolenoid l3Claw;
    static DoubleSolenoid h4Claw;

    public Climber() {
        super(ClimberState.NEUTRAL);

        climbMotor = new WPI_TalonFX(CLIMB_ARM_MOTOR);
        climbMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        climbMotor.configIntegratedSensorInitializationStrategy(SensorInitializationStrategy.BootToZero);
        climbMotor.configMotionCruiseVelocity(MAX_ARM_VELOCITY_NATIVE_UNITS, 20);
        climbMotor.configMotionAcceleration(MAX_ARM_ACCELERATION_NATIVE_UNITS, 20);
        climbMotor.config_kP(0, 0.075);
        climbMotor.config_kI(0, 0.0);
        climbMotor.config_kD(0, 0.0);
        climbMotor.config_kF(0, 0.0);

        climbPCM = new PneumaticsControlModule(CLIMB_PCM);

        l1Claw = climbPCM.makeDoubleSolenoid(L1_EXTEND, L1_RETRACT);
        h2Claw = climbPCM.makeDoubleSolenoid(H2_EXTEND, H2_RETRACT);
        l3Claw = climbPCM.makeDoubleSolenoid(L3_EXTEND, L3_RETRACT);
        h4Claw = climbPCM.makeDoubleSolenoid(H4_EXTEND, H4_RETRACT);

        l1Switch = new DigitalInput(L1_SWITCH);
        h2Switch = new DigitalInput(H2_SWITCH);
        l3Switch = new DigitalInput(L3_SWITCH);
        h4Switch = new DigitalInput(H4_SWITCH);
    }

    @Override
    public void process() {
        super.process();
        SmartDashboard.putNumber("Arm Encoder Position", climbMotor.getSelectedSensorPosition());
    }

    public double getClimbArmDegree() {
        return climbMotor.getSelectedSensorPosition() * FALCON_TICKS_TO_ARM_DEGREES;
    }

    private void setClimbArmDegree(double degree) {
        climbMotor.set(ControlMode.MotionMagic, degree * ARM_DEGREES_TO_FALCON_TICKS);
    }

    private void setL1(Value value) {
        setWithADelayToOff(l1Claw, value, DelayToOff.CLIMB_CLAWS.millis);
    }

    private void setH2(Value value) {
        setWithADelayToOff(h2Claw, value, DelayToOff.CLIMB_CLAWS.millis);
    }

    private void setL3(Value value) {
        setWithADelayToOff(l3Claw, value, DelayToOff.CLIMB_CLAWS.millis);
    }

    private void setH4(Value value) {
        setWithADelayToOff(h4Claw, value, DelayToOff.CLIMB_CLAWS.millis);
    }

    private void setHighVelocity() {
        climbMotor.configMotionCruiseVelocity(MAX_ARM_VELOCITY_NATIVE_UNITS, 20);
    }

    private void setHighAcceleration() {
        climbMotor.configMotionAcceleration(MAX_ARM_ACCELERATION_NATIVE_UNITS, 20);
    }

    private void setLowVelocity() {
        climbMotor.configMotionCruiseVelocity(MAX_ARM_VELOCITY_NATIVE_UNITS_SLOW, 20);
    }

    private void setLowAcceleration() {
        climbMotor.configMotionAcceleration(MAX_ARM_ACCELERATION_NATIVE_UNITS_SLOW, 20);
    }

    protected void prepClaw() {

        if (this.getStateFirstRunThrough()) {
            setL1(Value.kForward);
            setH2(Value.kReverse);
            setL3(Value.kReverse);
            setH4(Value.kReverse);
        }
    }

    protected void swingArm() {

        if (this.getStateFirstRunThrough()) {
            setClimbArmDegree(MIDBAR_GRAB_ANGLE_COMMAND);
            setHighVelocity();
            setHighAcceleration();
        }
    }

    protected void gripMidBar() {

        if (this.getStateFirstRunThrough()) {
            setH2(Value.kForward);
        }
    }

    protected void swingMidHigh1() {

        if (this.getStateFirstRunThrough()) {
            setL3(Value.kForward);
            setClimbArmDegree(HIGHBAR_GRAB_ANGLE_COMMAND);
            setHighVelocity();
            setLowAcceleration();
        }
    }

    protected void swingMidHigh2() {

        if (this.getStateFirstRunThrough()) {
            setL3(Value.kForward);
            setClimbArmDegree(HIGHBAR_GRAB_ANGLE_COMMAND);
            setLowVelocity();
            setLowAcceleration();
        }
    }

    protected void gripHighBar() {

        if (this.getStateFirstRunThrough()) {
            setH4(Value.kForward);
            setClimbArmDegree(getClimbArmDegree());
            System.out.println("Grip High Bar Climb Degree: " + getClimbArmDegree());
        }
    }

    protected void retryHighBar() {

        if (this.getStateFirstRunThrough()) {
            setH4(Value.kReverse);
            double angle = (getClimbArmDegree() < MIDHIGHBAR_SLOWDOWN_ANGLE) ? MIDHIGHBAR_SLOWDOWN_ANGLE : getClimbArmDegree() - 15.0;
            setClimbArmDegree(angle);
        }
    }

    protected void recenterMidHighBar() {

        if (this.getStateFirstRunThrough()) {
            setClimbArmDegree(MIDHIGHBAR_RECENTER_ANGLE_COMMAND);
            setHighVelocity();
            setLowAcceleration();
        }
    }

    protected void releaseMidBar() {

        if (this.getStateFirstRunThrough()) {
            setL1(Value.kReverse);
            setH2(Value.kReverse);
            setClimbArmDegree(TRAVERSALBAR_GRAB_ANGLE_COMMMAND);
            setHighVelocity();
            setHighAcceleration();
        }
    }

    protected void swingHighTraversal1() {

        if (this.getStateFirstRunThrough()) {
            setL1(Value.kForward);
            setClimbArmDegree(TRAVERSALBAR_GRAB_ANGLE_COMMMAND);
            setHighVelocity();
            setHighAcceleration();
        }
    }

    protected void swingHighTraversal2() {

        if (this.getStateFirstRunThrough()) {
            setL1(Value.kForward);
            setClimbArmDegree(TRAVERSALBAR_GRAB_ANGLE_COMMMAND);
            setLowVelocity();
            setLowAcceleration();
        }
    }

    protected void gripTraversalBar() {

        if (this.getStateFirstRunThrough()) {
            setH2(Value.kForward);
            setClimbArmDegree(getClimbArmDegree());
            System.out.println("Grip Traversal Bar Climb Degree: " + getClimbArmDegree());
        }
    }

    protected void retryTraversalBar() {

        if (this.getStateFirstRunThrough()) {
            setH2(Value.kReverse);
            double angle = (getClimbArmDegree() < HIGHTRAVERSAL_SLOWDOWN_ANGLE) ? HIGHTRAVERSAL_SLOWDOWN_ANGLE : getClimbArmDegree() - 15.0;
            setClimbArmDegree(angle);
        }
    }

    protected void recenterHighTraversalBar() {

        if (this.getStateFirstRunThrough()) {
            setClimbArmDegree(HIGHTRAVERSALBAR_RECENTER_ANGLE_COMMAND);
            setHighVelocity();
            setLowAcceleration();
        }
    }

    protected void releaseHighBar() {

        if (this.getStateFirstRunThrough()) {
            setL3(Value.kReverse);
            setH4(Value.kReverse);
            setClimbArmDegree(SWINGTOREST_ANGLE_COMMAND);
            setHighVelocity();
            setLowAcceleration();
        }
    }

    protected void swingToRest() {
        if (this.getStateFirstRunThrough()) {
            setClimbArmDegree(SWINGTOREST_ANGLE_COMMAND);
        }
    }

    protected void resetArm() {
        if (this.getStateFirstRunThrough()) {
            setL1(Value.kReverse);
            setClimbArmDegree(0.0);
            setHighVelocity();
            setHighAcceleration();
        }
    }

    protected void moveArmManually() {
        if (Buttons.MoveClimbArmForward.getButton()) {
            climbMotor.set(ControlMode.PercentOutput, 0.30);
        } else if (Buttons.MoveClimbArmBackward.getButton()) {
            climbMotor.set(ControlMode.PercentOutput, -0.30);
        } else if (Buttons.ClimbArmEncoderReset.getButton()) {
            climbMotor.set(ControlMode.PercentOutput, 0.0);
            climbMotor.setSelectedSensorPosition(0.0);
        } else {
            climbMotor.set(ControlMode.PercentOutput, 0.0);
        }
    }

    @Override
    public void neutral() {
        l1Claw.set(Value.kOff);
        h2Claw.set(Value.kOff);
        l3Claw.set(Value.kOff);
        h4Claw.set(Value.kOff);
        climbMotor.set(ControlMode.PercentOutput, 0.0);
    }

    @Override
    public boolean abort() {
        l1Claw.set(Value.kOff);
        h2Claw.set(Value.kOff);
        l3Claw.set(Value.kOff);
        h4Claw.set(Value.kOff);
        climbMotor.set(ControlMode.PercentOutput, 0.0);
        return true;
    }
}

