package frc.robot.functions;

import frc.robot.Robot;
import frc.robot.RobotContainer.Axes;

public class Drive extends FunctionBase implements FunctionInterface {

    long originalTime = 0;

    public Drive() {

        reset();
        completed();

    }

    @Override
    public void process() {
        Robot.swerveDrive.drive(Axes.Drive_ForwardBackward.getAxis(),
                Axes.Drive_LeftRight.getAxis(),
                Axes.Drive_Rotation.getAxis(),
                false);

    }
}
