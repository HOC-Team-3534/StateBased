package frc.robot.extras;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {

    ILimelightAngleToDistanceFunction distanceFunction;
    NetworkTable table;
    boolean isValid;
    boolean isTargetAcquired;
    double savedDistance = -999;
    double savedTX = 0;

    public Limelight(ILimelightAngleToDistanceFunction distanceFunction) {

        this.distanceFunction = distanceFunction;
        getTable();

    }

    public void getTable() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public double getHorizontalAngleOffset() {
        getTable();
        if (isValid()) {
            savedTX = table.getEntry("tx").getDouble(0.0);
        }
        return savedTX;
    }

    private double getPixelAngle() {
        getTable();
        return table.getEntry("ty").getDouble(0.0);
    }

    public double getHeight() {
        getTable();
        return table.getEntry("tvert").getDouble(0.0);
    }

    public double getWidth() {
        getTable();
        return table.getEntry("thor").getDouble(0.0);
    }

    public double getSkew() {
        getTable();
        return table.getEntry("ts").getDouble(0.0);
    }

    public double getDistance() {
        if (isValid()) {
            savedDistance = distanceFunction.getDistance(getPixelAngle());
        }
        return savedDistance;
    }

    private boolean getValidTarget() {
        getTable();
        return table.getEntry("tv").getDouble(0.0) == 1;
    }

    public boolean isValid() {
        isValid = getValidTarget();
        return isValid;
    }

    public void resetLimelightGlobalValues(){
        isTargetAcquired = false;
        savedDistance = -999;
        savedTX = 0;
    }

    public boolean isTargetAcquired(){
        return isTargetAcquired;
    }

    public void setTargetAcquired(){
        isTargetAcquired = true;
    }

}
