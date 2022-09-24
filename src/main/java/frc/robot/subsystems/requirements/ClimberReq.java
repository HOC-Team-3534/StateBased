package frc.robot.subsystems.requirements;

import frc.robot.Robot;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.ClimberState;
import frc.statebasedcontroller.subsystem.fundamental.SubsystemRequirement;

public class ClimberReq extends SubsystemRequirement<Climber, ClimberState> {
    public ClimberReq(ClimberState subsystemState) {
        super(Robot.climber, subsystemState);
    }
}
