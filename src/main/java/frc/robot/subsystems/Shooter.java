package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.subsystems.parent.BaseSubsystem;

public class Shooter extends BaseSubsystem {

    public Shooter(){
        //makes the graphical number to enter text - have to do a 
        //put to do a get
        SmartDashboard.putNumber("RPM: ", 0.0);
    }

    @Override
    public void process() {
        isStillRequired();
        if (getStateRequiringName() == "SHOOT") {
            //grabs the number from SmartDashboard
            shoot(SmartDashboard.getNumber("RPM: ", 0.0));
        } else {
            shoot(0);
        }
        
    }
    
    public void shoot(double rpm) {
        double countsPer100MS = rpm * Constants.RPM_TO_COUNTS_PER_100MS;
        RobotMap.shooter.set(ControlMode.Velocity, countsPer100MS);
        System.out.println("Speed is " + countsPer100MS);
    }


}
