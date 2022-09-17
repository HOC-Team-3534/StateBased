package frc.robot.autons.pathplannerfollower;

import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import frc.robot.subsystems.parent.IDriveSubsystem;

/**
 * The middle-man between a {@link PathPlannerFollower} and a {@link frc.robot.subsystems.parent.BaseDriveSubsystem BaseDriveSubsystem},
 * basically taking the current {@link com.pathplanner.lib.PathPlannerTrajectory.PathPlannerState PathPlannerState} and calculating the
 * {@link CalculatedDriveVelocities} from the updated {@link SwerveDriveOdometry} and the {@link IDriveSubsystem#getGyroHeading() getGyroHeading()} (which should include the gryo offset).
 */
public class PathStateController {

    PathPlannerFollower pathPlannerFollower;
    PIDController x_pid, y_pid, rot_pid;

    /**
     * Creates the PathStateController, which can be used to calculate velocities
     * based on the current path using the {@link #getVelocitiesAtCurrentState(SwerveDriveOdometry, Rotation2d) getVelocitiesAtCurrentState} method
     *
     * @param x_pid   to correct position error in the x direction
     * @param y_pid   to correct position error in the y direction
     * @param rot_pid to correct heading error
     */
    public PathStateController(PIDController x_pid, PIDController y_pid, PIDController rot_pid) {
        this.x_pid = x_pid;
        this.y_pid = y_pid;
        this.rot_pid = rot_pid;
    }

    /**
     * @return the pathfollower with the {@link PathPlannerTrajectory} to be followed when in a pathfollowing autonomous state
     */
    public PathPlannerFollower getPathPlannerFollower() {
        return this.pathPlannerFollower;
    }

    /**
     * @param pathPlannerFollower the follower with the path for the {@link PathStateController} to follow
     */
    public void setPathPlannerFollower(PathPlannerFollower pathPlannerFollower) {
        this.pathPlannerFollower = pathPlannerFollower;
        resetPIDs();
    }

    /**
     * Reset the PID controllers
     */
    private void resetPIDs() {
        x_pid.reset();
        y_pid.reset();
        rot_pid.reset();
    }

    /**
     * Based on the current {@link com.pathplanner.lib.PathPlannerTrajectory.PathPlannerState PathPlannerState}, {@param odometry}, {@param currentOrientation},
     * the current {@link SwerveDriveOdometry} is compared to the expected position from the {@link com.pathplanner.lib.PathPlannerTrajectory.PathPlannerState PathPlannerState}
     * and those are used to correct for the base velocities given at the current part of the path
     *
     * @param odometry           the current position and direction of the motion of the robot
     * @param currentOrientation the current holonomic heading of the robot
     * @return the x, y, and angular velocity (in meters per second and radians per second)
     */
    public CalculatedDriveVelocities getVelocitiesAtCurrentState(SwerveDriveOdometry odometry, Rotation2d currentOrientation) {
        PathPlannerTrajectory.PathPlannerState pathState = this.pathPlannerFollower.getCurrentState();
        double fwd_back_position = pathState.poseMeters.getX(); //going down field, closer or farther from driver station
        double left_right_position = pathState.poseMeters.getY(); //side to side, parallel with driver station wall
        double angular_velocity = pathState.angularVelocity.getRadians();
        Rotation2d targetHolonomicHeading = pathState.holonomicRotation; //the orientation of the robot
        Rotation2d targetHeading = pathState.poseMeters.getRotation();  //the direction the robot should move in
        double x_velocity = pathState.velocityMetersPerSecond * targetHeading.getCos(); //meters per second down field
        double y_velocity = pathState.velocityMetersPerSecond * targetHeading.getSin(); //meters per second parallel to field

        double currentX = odometry.getPoseMeters().getX();
        double currentY = odometry.getPoseMeters().getY();

        // System.out.println(String.format("Current Commands Pos and Vel [ X(pos): %.2f Y(pos): %.2f ] [ X(vel): %.2f Y(vel): %.2f ]", fwd_back_position, left_right_position, x_velocity, y_velocity));
        // System.out.println(String.format("Current Commands Orientation [ Heading(pos): %.2f Heading(vel): %.2f ]", targetHolonomicHeading.getRadians(), angular_velocity));


        double output_x_vel = x_velocity + x_pid.calculate(currentX, fwd_back_position);
        double output_y_vel = y_velocity + y_pid.calculate(currentY, left_right_position);
        // TODO validate that the error below is calculated correctly, maybe plus?
        Rotation2d orientationError = targetHolonomicHeading.minus(currentOrientation);
        double output_rot_vel = angular_velocity + rot_pid.calculate(-orientationError.getRadians(), 0.0);

        return new CalculatedDriveVelocities(output_x_vel, output_y_vel, output_rot_vel);
    }

}
