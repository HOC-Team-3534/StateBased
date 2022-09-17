package frc.robot.subsystems;

import com.swervedrivespecialties.swervelib.Mk4SwerveModuleHelper;
import com.swervedrivespecialties.swervelib.SwerveModule;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.drive.Vector2d;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import static frc.robot.Constants.*;
import frc.robot.Robot;
import frc.robot.RobotContainer.Axes;
import frc.robot.autons.pathplannerfollower.CalculatedDriveVelocities;
import frc.robot.autons.pathplannerfollower.PathStateController;
import frc.robot.subsystems.parent.BaseDriveSubsystem;

public class SwerveDrive extends BaseDriveSubsystem<SwerveDriveState> {

	static SwerveModule frontLeftModule;
	static SwerveModule frontRightModule;
	static SwerveModule backLeftModule;
	static SwerveModule backRightModule;

	static ShuffleboardTab tab = Shuffleboard.getTab("Drivetrain");

	double frontLeft_stateAngle = 0.0,
			frontRight_stateAngle = 0.0,
			backLeft_stateAngle = 0.0,
			backRight_stateAngle = 0.0;

	static PIDController xPID = new PIDController(20,0,0),
			yPID = new PIDController(20,0,0),
			rotPID = new PIDController(8,0,0),
			limelightPID = new PIDController(0.185, 0.0, 0.0);

	static Rotation2d targetShootRotationAngle = new Rotation2d();

	static PathStateController pathStateController = new PathStateController(xPID, yPID, rotPID);

	public SwerveDrive() {
		super(frontLeftModule, frontRightModule, backLeftModule,
				backRightModule, SwerveDriveState.NEUTRAL);

		frontLeftModule = Mk4SwerveModuleHelper.createFalcon500(
				tab.getLayout("Front Left Module", BuiltInLayouts.kList)
						.withSize(2, 4)
						.withPosition(0, 0),
				// This can either be STANDARD or FAST depending on your gear configuration
				Mk4SwerveModuleHelper.GearRatio.L2,
				// This is the ID of the drive motor
				FRONT_LEFT_MODULE_DRIVE_MOTOR,
				// This is the ID of the steer motor
				FRONT_LEFT_MODULE_STEER_MOTOR,
				// This is the ID of the steer encoder
				FRONT_LEFT_MODULE_STEER_ENCODER,
				// This is how much the steer encoder is offset from true zero (In our case,
				// zero is facing straight forward)
				FRONT_LEFT_MODULE_STEER_OFFSET);


		// We will do the same for the other modules
		frontRightModule = Mk4SwerveModuleHelper.createFalcon500(
				tab.getLayout("Front Right Module", BuiltInLayouts.kList)
						.withSize(2, 4)
						.withPosition(2, 0),
				Mk4SwerveModuleHelper.GearRatio.L2,
				FRONT_RIGHT_MODULE_DRIVE_MOTOR,
				FRONT_RIGHT_MODULE_STEER_MOTOR,
				FRONT_RIGHT_MODULE_STEER_ENCODER,
				FRONT_RIGHT_MODULE_STEER_OFFSET);

		backLeftModule = Mk4SwerveModuleHelper.createFalcon500(
				tab.getLayout("Back Left Module", BuiltInLayouts.kList)
						.withSize(2, 4)
						.withPosition(4, 0),
				Mk4SwerveModuleHelper.GearRatio.L2,
				BACK_LEFT_MODULE_DRIVE_MOTOR,
				BACK_LEFT_MODULE_STEER_MOTOR,
				BACK_LEFT_MODULE_STEER_ENCODER,
				BACK_LEFT_MODULE_STEER_OFFSET);

		backRightModule = Mk4SwerveModuleHelper.createFalcon500(
				tab.getLayout("Back Right Module", BuiltInLayouts.kList)
						.withSize(2, 4)
						.withPosition(6, 0),
				Mk4SwerveModuleHelper.GearRatio.L2,
				BACK_RIGHT_MODULE_DRIVE_MOTOR,
				BACK_RIGHT_MODULE_STEER_MOTOR,
				BACK_RIGHT_MODULE_STEER_ENCODER,
				BACK_RIGHT_MODULE_STEER_OFFSET);

		setPathStateController(pathStateController);
	}

	public Vector2d getRobotCentricVelocity(){
		ChassisSpeeds chassisSpeeds = this.getSwerveDriveKinematics().toChassisSpeeds(this.getSwerveModuleStates());
		return new Vector2d(chassisSpeeds.vxMetersPerSecond, chassisSpeeds.vyMetersPerSecond);
	}

	public Vector2d getTargetOrientedVelocity(){
		Vector2d robotCentricVelocity = getRobotCentricVelocity();
		robotCentricVelocity.rotate(180.0);
		return robotCentricVelocity;
	}

	public void setTargetShootRotationAngle() {;
		//this.targetShootRotationAngle = getGyroHeading().plus(Robot.limelight.getLimelightShootProjection().getOffset());
		this.targetShootRotationAngle = getGyroHeading().plus(Robot.limelight.getHorizontalAngleOffset());
	}

	public Rotation2d getTargetShootRotationAngleError(){ return targetShootRotationAngle.minus(getGyroHeading()); }

	@Override
	public Rotation2d getGyroHeading() {
		return Robot.pigeon.getRotation2d().plus(this.getGyroOffset());
	}

	@Override
	public void resetGyro() {
		Robot.pigeon.reset();
	}

	protected void drive(){
		drive(Axes.Drive_ForwardBackward.getAxis() * PHYSICAL_MAX_VELOCITY,
				Axes.Drive_LeftRight.getAxis() * PHYSICAL_MAX_VELOCITY,
				Axes.Drive_Rotation.getAxis() * PHYSICAL_MAX_ANGULAR_VELOCITY,
				PHYSICAL_MAX_VELOCITY,
				true);
	}

	private void drive(double xSpeed, double ySpeed, double rot, double max_speed, boolean fieldRelative) {
		var swerveModuleStates = getSwerveDriveKinematics().toSwerveModuleStates(
				fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
						xSpeed, ySpeed, rot, getGyroHeading())
						: new ChassisSpeeds(xSpeed, ySpeed, rot));
		SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, max_speed);
		if (Math.abs(swerveModuleStates[0].speedMetersPerSecond) + Math.abs(swerveModuleStates[1].speedMetersPerSecond)
				+ Math.abs(swerveModuleStates[2].speedMetersPerSecond)
				+ Math.abs(swerveModuleStates[3].speedMetersPerSecond) > 0.001) {
			frontLeft_stateAngle = swerveModuleStates[0].angle.getRadians();
			frontRight_stateAngle = swerveModuleStates[1].angle.getRadians();
			backLeft_stateAngle = swerveModuleStates[2].angle.getRadians();
			backRight_stateAngle = swerveModuleStates[3].angle.getRadians();
		}
		frontLeftModule.set(swerveModuleStates[0].speedMetersPerSecond
						/ PHYSICAL_MAX_VELOCITY * MAX_VOLTAGE,
				frontLeft_stateAngle);
		frontRightModule.set(swerveModuleStates[1].speedMetersPerSecond
						/ PHYSICAL_MAX_VELOCITY * MAX_VOLTAGE,
				frontRight_stateAngle);
		backLeftModule.set(swerveModuleStates[2].speedMetersPerSecond
						/ PHYSICAL_MAX_VELOCITY * MAX_VOLTAGE,
				backLeft_stateAngle);
		backRightModule.set(swerveModuleStates[3].speedMetersPerSecond
						/ PHYSICAL_MAX_VELOCITY * MAX_VOLTAGE,
				backRight_stateAngle);
	}

	protected void driveOnPath() {
		CalculatedDriveVelocities velocities = this.getPathStateController()
				.getVelocitiesAtCurrentState(this.getSwerveDriveOdometry(), this.getGyroHeading());
		drive(velocities.getXVel(), velocities.getYVel(), velocities.getRotVel(), MAX_VELOCITY_AUTONOMOUS,true);
	}

	protected void creep(){
		drive(Axes.Drive_ForwardBackward.getAxis() * MAX_VELOCITY_CREEP,
				Axes.Drive_LeftRight.getAxis() * MAX_VELOCITY_CREEP,
				Axes.Drive_Rotation.getAxis() * MAX_ANGULAR_VELOCITY_CREEP,
				MAX_VELOCITY_CREEP,
				true);
	}

	protected void aim(){
		double angleError = getTargetShootRotationAngleError().getDegrees();
		if(angleError > 2.0){
			angleError = 2.0;
		}else if(angleError < -2.0){
			angleError = -2.0;
		}
		double pidOutput = limelightPID.calculate(-angleError,0.0);
		drive(Axes.Drive_ForwardBackward.getAxis() * MAX_VELOCITY_CREEP,
				Axes.Drive_LeftRight.getAxis() * MAX_VELOCITY_CREEP,
				pidOutput * MAX_ANGULAR_VELOCITY_CREEP,
				MAX_VELOCITY_CREEP,
				true);
	}

	@Override
	public void neutral() {
		drive(0.0, 0.0, 0.0, PHYSICAL_MAX_VELOCITY, false);
	}

	@Override
	public boolean abort() {
		drive(0.0, 0.0, 0.0, PHYSICAL_MAX_VELOCITY, false);
		return true;
	}
}

