package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.subsystems.parent.BaseSubsystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Intake extends BaseSubsystem {

    String[] autonKickOutStateStrings = {"PICKUPBALL1", "PICKUPBALL2", "WAITFORINTAKE1", "PICKUPBALL3"};
    String[] autonRetractStateStrings = {"RESETPUNCH1", "RESETPUNCH2", "RESETPUNCH3"};
    Set<String> autonKickOutStates = new HashSet<>(Arrays.asList(autonKickOutStateStrings));
    Set<String> autonRetractStates = new HashSet<>(Arrays.asList(autonRetractStateStrings));

    public Intake(){
        //makes the graphical number to enter text - have to do a 
        //put to do a get
        //SmartDashboard.putNumber("RPM: ", 0.0);
    }

    @Override
    public void process() {

        super.process();

        if(getStateRequiringName() == "EXTEND" || autonKickOutStates.contains(getStateRequiringName())){
            kickOut();
        }else if(getStateRequiringName() == "RETRACT" || autonRetractStates.contains(getStateRequiringName())) {
            retract();
        }else if(getStateRequiringName() == "EXTAKE"){
            extake();
        }else{
            neutral();
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