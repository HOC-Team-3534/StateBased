package frc.robot.extras;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.drive.Vector2d;
import frc.robot.Robot;

public class Limelight {

    ILimelightAngleToDistanceFunction distanceFunction;
    IDistanceToAverageShootVelocityFunction distanceToAverageShootVelocityFunction;
    IAverageShootVelocityToDistanceFunction averageShootVelocityToDistanceFunction;
    NetworkTable table;
    boolean isTargetAcquired;
    double savedDistance = -999;
    double savedTX = 0;

    long lastTimeTableSet = 0;

    LimelightShootProjection limelightShootProjection;

    public Limelight(ILimelightAngleToDistanceFunction distanceFunction, IDistanceToAverageShootVelocityFunction distanceToAverageVelocityFunction, IAverageShootVelocityToDistanceFunction averageShootVelocityToDistanceFunction) {

        this.distanceFunction = distanceFunction;
        this.distanceToAverageShootVelocityFunction = distanceToAverageVelocityFunction;
        this.averageShootVelocityToDistanceFunction = averageShootVelocityToDistanceFunction;
        getTable();

    }

    public void getTable() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public Rotation2d getHorizontalAngleOffset() {
        if(System.currentTimeMillis() - lastTimeTableSet > 20){
            lastTimeTableSet = System.currentTimeMillis();
            getTable();
        }
        if (isValid()) {
            savedTX = -table.getEntry("tx").getDouble(0.0);
        }
        return new Rotation2d(savedTX / 180.0 * Math.PI);
    }

    public double getPixelAngle() {
        return table.getEntry("ty").getDouble(0.0);
    }

    public double getDistance() {
        if(System.currentTimeMillis() - lastTimeTableSet > 20){
            lastTimeTableSet = System.currentTimeMillis();
            getTable();
        }
        if (isValid()) {
            savedDistance = distanceFunction.getDistance(getPixelAngle()); // getHorizontalAngleOffset().getCos();
        }
        return savedDistance;
    }

    public double getHeight() {
        return table.getEntry("tvert").getDouble(0.0);
    }

    public double getWidth() {
        return table.getEntry("thor").getDouble(0.0);
    }

    public double getSkew() {
        return table.getEntry("ts").getDouble(0.0);
    }

    public boolean isValid() {
        if(System.currentTimeMillis() - lastTimeTableSet > 20){
            lastTimeTableSet = System.currentTimeMillis();
            getTable();
        }
        return table.getEntry("tv").getDouble(0.0) == 1;
    }

    public boolean isTargetAcquired(){
        return isTargetAcquired;
    }

    public void setTargetAcquired(){
        isTargetAcquired = true;
    }

    public void resetLimelightGlobalValues(){
        isTargetAcquired = false;
        savedDistance = -999;
        savedTX = 0;
    }

    public LimelightShootProjection getLimelightShootProjection(){
        return limelightShootProjection;
    }

    public void updateLimelightShootProjection(){

        // Known Variables
        double straightLineDistance = getDistance();
        Rotation2d offset = getHorizontalAngleOffset();
        Translation2d targetLocation = new Translation2d(straightLineDistance, offset);
        Vector2d targetMotion = Robot.swerveDrive.getTargetOrientedVelocity();

        /*
         * - Find the vector of the average velocity of the game piece, if shot straight towards target, no motion, aligned.
         * - Combine the motion of the average velocity of the game piece with the relative motion of the target
         * - Back calculate the distance for that overall average velocity
         */
        Vector2d baseAvgVel = Utils.createVector2d(distanceToAverageShootVelocityFunction.getAverageVelocity(straightLineDistance), offset);
        Vector2d desiredAverageVelocity = Utils.getCombinedMotion(baseAvgVel, targetMotion);
        double projectedDistance = averageShootVelocityToDistanceFunction.getDistance(desiredAverageVelocity.magnitude());

        /*
         * - Find the time elapsed during the shot
         * - Find the projection location of the target using its current position and the integral of
         *   velocity using the calculated total time elapsed during flight
         * - Generate the offset angle from the "imaginary" location
         */
        double totalTime = projectedDistance / desiredAverageVelocity.magnitude();
        Translation2d projectedLocation = Utils.getPositionAfterMotion(targetLocation, targetMotion, totalTime);
        Rotation2d projectedOffset = new Rotation2d(Math.atan(projectedLocation.getY() / projectedLocation.getX()));

        limelightShootProjection = new LimelightShootProjection(projectedDistance, projectedOffset);

    }

}
