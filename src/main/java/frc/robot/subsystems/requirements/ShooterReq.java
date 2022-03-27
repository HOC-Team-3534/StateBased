package frc.robot.subsystems.requirements;

import frc.robot.Robot;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.parent.SubsystemRequirement;
import frc.robot.subsystems.states.ShooterState;

public class ShooterReq extends SubsystemRequirement<Shooter, ShooterState> {
    public ShooterReq(ShooterState subsystemState) {
        super(Robot.shooter, subsystemState);
    }
}
