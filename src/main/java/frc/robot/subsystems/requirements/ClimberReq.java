package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.subsystems.parent.SubsystemRequirement;

public class ClimberReq extends SubsystemRequirement<Climber, ClimberState> {
    public ClimberReq(ClimberState subsystemState) {
        super(Robot.climber, subsystemState);
    }
}
