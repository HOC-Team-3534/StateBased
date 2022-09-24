package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.BearSwerveHelper;
import frc.robot.Constants.DRIVE;
import frc.robot.RobotContainer.Axes;
import frc.statebasedcontroller.subsystem.general.swervedrive.BaseDriveSubsystem;
import frc.statebasedcontroller.subsystem.requiredadditions.swervedrive.StateBasedSwerveDrivetrainModel;

public class SwerveDrive extends BaseDriveSubsystem<SwerveDriveState> {

    static ShuffleboardTab tab = Shuffleboard.getTab("Drivetrain");

    static StateBasedSwerveDrivetrainModel dt = BearSwerveHelper.createBearSwerve();

    static PIDController limelightPID = new PIDController(0.185, 0.0, 0.0);

    public SwerveDrive() {
        super(dt, DRIVE.KINEMATICS, SwerveDriveState.NEUTRAL);
    }

    protected void drive() {
        setModuleStates(Axes.Drive_ForwardBackward.getAxis(), Axes.Drive_LeftRight.getAxis(), Axes.Drive_Rotation.getAxis());
    }

    protected void creep() {
        setModuleStatesCreep(Axes.Drive_ForwardBackward.getAxis(), Axes.Drive_LeftRight.getAxis(), Axes.Drive_Rotation.getAxis());
    }

    protected void aim() {
        double angleError = getTargetShootRotationAngleError().getDegrees();
        if (angleError > 2.0) {
            angleError = 2.0;
        } else if (angleError < -2.0) {
            angleError = -2.0;
        }
        double pidOutput = limelightPID.calculate(-angleError, 0.0);
        setModuleStatesCreepAim(Axes.Drive_ForwardBackward.getAxis(), Axes.Drive_LeftRight.getAxis(), pidOutput);
    }

    @Override
    public void neutral() {
        setModuleStates(new ChassisSpeeds(0.0, 0.0, 0.0));
    }

    @Override
    public boolean abort() {
        setModuleStates(new ChassisSpeeds(0.0, 0.0, 0.0));
        return true;
    }
}

