package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.subsystems.parent.BaseSubsystem;

public class Intake extends BaseSubsystem {

    public Intake(){
        //makes the graphical number to enter text - have to do a 
        //put to do a get
        //SmartDashboard.putNumber("RPM: ", 0.0);
    }

    @Override
    public void process() {
        super.process();
        if(getStateRequiringName() == "EXTEND"){
            kickOut();
        }else if(getStateRequiringName() == "RETRACT"){
            retract();
        }else{
            neutral();
        }

        // if (getStateRequiringName() == "SHOOT") {
        //     //grabs the number from SmartDashboard
        //     shoot(SmartDashboard.getNumber("RPM: ", 0.0));
        // } else {
        //     shoot(0);
        // }
        
    }

    @Override
    public void neutral() {
        RobotMap.m_intakeKickers.set(Value.kOff);
        RobotMap.m_intakeRoller.set(ControlMode.PercentOutput, 0.0);
    }

    public void kickOut() {
        if(this.getStateFirstRunThrough()) {
            RobotMap.m_intakeKickers.set(Value.kForward);
        }else if(this.getSequenceRequiring().getTimeSinceStartOfSequence() > 500){
            RobotMap.m_intakeKickers.set(Value.kOff);
        }
        RobotMap.m_intakeRoller.set(ControlMode.PercentOutput, 0.40);
    }

    public void retract() {
        if(this.getStateFirstRunThrough()) {
            RobotMap.m_intakeKickers.set(Value.kReverse);
        }else if(this.getSequenceRequiring().getTimeSinceStartOfSequence() > 1000){
            RobotMap.m_intakeKickers.set(Value.kOff);
        }
    }
}