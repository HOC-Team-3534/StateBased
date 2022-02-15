package frc.robot.subsystems;

import java.util.ResourceBundle.Control;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.subsystems.parent.BaseSubsystem;

public class Climber extends BaseSubsystem {

    public static boolean lowIsPrepped = false;
    public static boolean midIsPrepped = false;
    public static boolean HighIsPrepped = false;

    public Climber(){
        //makes the graphical number to enter text - have to do a 
        //put to do a get
        //SmartDashboard.putNumber("RPM: ", 0.0);
    }

    @Override
    public void process() {
        isStillRequired();
        if(getStateRequiringName() == "PREPCLAW"){
            prepClaw();
        }else if(getStateRequiringName() == "SWINGARM"){
            swingArm();
        }else if(getStateRequiringName() == "PREPPEDFORCLIMB"){
            preppedForClimb();
        }else if(getStateRequiringName() == "GRIPMIDBAR"){
            gripMidBar();
        }else if(getStateRequiringName() == "SWINGMIDHIGH"){
            swingMidHigh();
        }else if(getStateRequiringName() == "GRIPHIGHBAR"){
            gripHighBar();
        }else if(getStateRequiringName() == "RELEASEMIDBAR"){
            releaseHighBar();
        }else if(getStateRequiringName() == "SWINGHIGHTRAVERSAL"){
            swingHighTraversal();
        }else if(getStateRequiringName() == "GRIPTRAVERSALBAR"){
            gripTraversalBar();
        }else if(getStateRequiringName() == "RELEASEHIGHBAR"){
            releaseHighBar();
        }else if(getStateRequiringName() == "SWINGTOREST"){
            swingToRest();
        }else{
            RobotMap.m_l1Claw.set(Value.kOff);
            RobotMap.m_h2Claw.set(Value.kOff);
            RobotMap.m_l3Claw.set(Value.kOff);
            RobotMap.m_h4Claw.set(Value.kOff);

        }

        // if (getStateRequiringName() == "SHOOT") {
        //     //grabs the number from SmartDashboard
        //     shoot(SmartDashboard.getNumber("RPM: ", 0.0));
        // } else {
        //     shoot(0);
        // }
        
    }
    
    public void prepClaw() {
        // double countsPer100MS = rpm * Constants.RPM_TO_COUNTS_PER_100MS;
        // RobotMap.shooter.set(ControMlMode.Velocity, countsPer100MS);
        // System.out.println("Speed is " + countsPer100S);
        RobotMap.m_l1Claw.set(Value.kForward);

    }

    public void swingArm() {
        double waitTime;
        
        RobotMap.m_l1Claw.set(Value.kOff);
        RobotMap.m_climbMotor.set(ControlMode.Position, 90);
        if(RobotMap.m_l1Switch.get() || RobotMap.m_h2Switch.get()){
            waitTime = System.currentTimeMillis();
            if(System.currentTimeMillis() - waitTime > 40){
                lowIsPrepped = true;
                
            }
        }
    }

    public void preppedForClimb(){
       

    }


    public void gripMidBar(){
       RobotMap.m_h2Claw.set(Value.kForward);
    }

    public void swingMidHigh(){
        RobotMap.m_h2Claw.set(Value.kOff);
        RobotMap.m_l3Claw.set(Value.kForward);
        RobotMap.m_climbMotor.set(ControlMode.PercentOutput, 100);

    }

    public void gripHighBar(){
       RobotMap.m_l3Claw.set(Value.kOff);
       RobotMap.m_h4Claw.set(Value.kForward);
       RobotMap.m_climbMotor.set(ControlMode.PercentOutput, 0);

    }

    public void releaseMidBar(){
       RobotMap.m_h4Claw.set(Value.kOff);
       RobotMap.m_l1Claw.set(Value.kReverse);
       RobotMap.m_h2Claw.set(Value.kReverse);

    }

    public void swingHighTraversal(){
        RobotMap.m_l1Claw.set(Value.kForward);
        RobotMap.m_h2Claw.set(Value.kOff);
        RobotMap.m_climbMotor.set(ControlMode.PercentOutput, 100);

    }


    public void gripTraversalBar(){
        RobotMap.m_l1Claw.set(Value.kOff);
        RobotMap.m_h2Claw.set(Value.kForward);
        RobotMap.m_climbMotor.set(ControlMode.PercentOutput, 0);
    }

    public void releaseHighBar(){
        RobotMap.m_h2Claw.set(Value.kOff);
        RobotMap.m_l3Claw.set(Value.kReverse);
        RobotMap.m_h4Claw.set(Value.kReverse);
    }

    public void swingToRest(){
       RobotMap.m_climbMotor.set(ControlMode.Position, 450);

    }
}
