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
        isStillRequired();
        if(getStateRequiringName() == "KICKOUT"){
            kickOut();
        }else if(getStateRequiringName() == "RETRACT"){
            retract();
        }else{
            RobotMap.m_intakeKickers.set(Value.kOff);
            RobotMap.m_intakeRoller.set(ControlMode.PercentOutput, 0);
        }

        // if (getStateRequiringName() == "SHOOT") {
        //     //grabs the number from SmartDashboard
        //     shoot(SmartDashboard.getNumber("RPM: ", 0.0));
        // } else {
        //     shoot(0);
        // }
        
    }
    
    public void kickOut() {
        // double countsPer100MS = rpm * Constants.RPM_TO_COUNTS_PER_100MS;
        // RobotMap.shooter.set(ControMlMode.Velocity, countsPer100MS);
        // System.out.println("Speed is " + countsPer100S);
        RobotMap.m_intakeKickers.set(Value.kForward);
        RobotMap.m_intakeRoller.set(ControlMode.PercentOutput, 40);
        

    }

    public void retract() {
        RobotMap.m_intakeKickers.set(Value.kReverse);
        RobotMap.m_intakeRoller.set(ControlMode.PercentOutput, 0);
    }


}