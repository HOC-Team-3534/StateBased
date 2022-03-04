package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {

    ILimelightAngleToDistanceFunction distanceFunction;
    NetworkTable table;
    
    public Limelight(ILimelightAngleToDistanceFunction distanceFunction){

        this.distanceFunction = distanceFunction;
        this.table = NetworkTableInstance.getDefault().getTable("limelight");

    }

    public void getTable(){
        table = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public double getHorOffset(){
        getTable();
        return table.getEntry("tx").getDouble(0.0);
    }

    public double getPixelAngle(){
        getTable();
        return table.getEntry("ty").getDouble(0.0);
    }

    public double getHeight(){
        getTable();
        return table.getEntry("tvert").getDouble(0.0);
    }

    public double getWidth(){
        getTable();
        return table.getEntry("thor").getDouble(0.0);
    }

    public double getSkew(){
        getTable();
        return table.getEntry("ts").getDouble(0.0);
    }

    public double getDistance(){
        return distanceFunction.getDistance(getPixelAngle());
    }

}
