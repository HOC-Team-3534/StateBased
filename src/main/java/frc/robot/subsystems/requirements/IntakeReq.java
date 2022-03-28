package frc.robot.subsystems.requirements;

import frc.robot.Robot;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.states.IntakeState;

public class IntakeReq extends SubsystemRequirement<Intake, IntakeState> {
    public IntakeReq(IntakeState subsystemState) {
        super(Robot.intake, subsystemState);
    }
}
