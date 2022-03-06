package frc.robot.autons;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Robot;
import frc.robot.autons.parent.BaseAutonSequence;
import frc.robot.autons.parent.IAutonState;
import frc.robot.sequences.parent.BaseSequence;

public enum Auton {
    STATION1_4BALL(new FourBallAutonStation1(FourBallAutonStation1State.NEUTRAL, FourBallAutonStation1State.PICKUPBALL1, Robot.swerveDrive, "", ""));
    
    BaseAutonSequence<? extends IAutonState> auton;
    
    Auton(frc.robot.autons.parent.BaseAutonSequence<? extends IAutonState> auton){
        this.auton = auton;
    }
    
    public BaseAutonSequence<? extends IAutonState> getAuton(){
        return auton;
    }
}
