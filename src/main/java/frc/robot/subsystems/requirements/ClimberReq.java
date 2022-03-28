package frc.robot.subsystems.requirements;

import frc.robot.Robot;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.states.ClimberState;

public class ClimberReq extends SubsystemRequirement<Climber, ClimberState> {
    public ClimberReq(ClimberState subsystemState) {
        super(Robot.climber, subsystemState);
    }
}
