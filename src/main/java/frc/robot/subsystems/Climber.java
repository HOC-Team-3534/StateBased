package frc.robot.subsystems;

import java.util.ResourceBundle.Control;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.subsystems.parent.BaseSubsystem;

public class Climber extends BaseSubsystem {

    public static boolean isPrepped = false;

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
        RobotMap.m_bottomFrontClaw.set(Value.kForward);

    }

    public void swingArm() {
        double waitTime;
        
        RobotMap.m_climbMotor.set(ControlMode.Position, 90);
        if(RobotMap.m_bottomFrontSwitch.get() || RobotMap.m_topFrontSwitch.get()){
            waitTime = System.currentTimeMillis();
            if(System.currentTimeMillis() - waitTime > 40){
                isPrepped = true;
                
            }
        }
    }

    public void preppedForClimb(){
        RobotMap.m_topFrontClaw.set(Value.kForward);

    }
}