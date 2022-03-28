package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.subsystems.parent.BaseSubsystem;
import frc.robot.subsystems.states.IntakeState;

public class Intake extends BaseSubsystem<IntakeState> {

    public Intake(){
        super(IntakeState.NEUTRAL);
    }

    @Override
    public void process() {

        super.process();

        switch (getCurrentSubsystemState()) {
            case NEUTRAL:
                neutral();
                break;
            case KICKOUT:
                kickOut();
                break;
            case RETRACT:
                retract();
                break;
            case EXTAKE:
                extake();
                break;
            case HOLDPOSITION:
                break;
        }
    }

    @Override
    public void neutral() {
        RobotMap.m_intakeKickers.set(Value.kOff);
        RobotMap.m_intakeRoller.set(ControlMode.PercentOutput, 0.0);
    }

    public void kickOut() {
        if(getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_intakeKickers, Value.kForward, Constants.DelayToOff.INTAKE_KICKERS.millis);
            RobotMap.m_intakeRoller.set(ControlMode.PercentOutput, 0.80);
        }
    }

    public void retract() {
        if(getStateFirstRunThrough()) {
            setWithADelayToOff(RobotMap.m_intakeKickers, Value.kReverse, Constants.DelayToOff.INTAKE_KICKERS.millis);
        }
    }

    public void extake(){
        RobotMap.m_intakeRoller.set(ControlMode.PercentOutput, -0.80);
    }

    @Override
    public boolean abort() {
        RobotMap.m_intakeRoller.set(ControlMode.PercentOutput, 0.0);
        RobotMap.m_intakeKickers.set(Value.kOff);
        return true;
    }
}

