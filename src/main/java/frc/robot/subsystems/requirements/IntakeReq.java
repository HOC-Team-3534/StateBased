package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.subsystems.parent.SubsystemRequirement;

public class IntakeReq extends SubsystemRequirement<Intake, IntakeState> {
    public IntakeReq(IntakeState subsystemState) {
        super(Robot.intake, subsystemState);
    }
}
