package frc.robot.functions;

import frc.robot.Robot;
import frc.robot.RobotContainer.Axes;
import frc.robot.subsystems.SwerveDrive;

public class Drive extends FunctionBase implements FunctionInterface {

    long originalTime = 0;

    public Drive() {

        reset();
        completed();

    }

    @Override
    public void process() {
        Robot.swerveDrive.drive(Axes.Drive_LeftRight.getAxis() * SwerveDrive.kMaxSpeed,
                Axes.Drive_ForwardBackward.getAxis() * SwerveDrive.kMaxSpeed,
                Axes.Drive_Rotation.getAxis() * SwerveDrive.kMaxAngularSpeed,
                false);

    }
}
