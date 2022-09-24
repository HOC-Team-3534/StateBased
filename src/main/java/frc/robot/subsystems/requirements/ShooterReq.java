package frc.robot.subsystems.requirements;

import frc.robot.Robot;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ShooterState;
import frc.statebasedcontroller.subsystem.fundamental.SubsystemRequirement;

public class ShooterReq extends SubsystemRequirement<Shooter, ShooterState> {
    public ShooterReq(ShooterState subsystemState) {
        super(Robot.shooter, subsystemState);
    }
}
