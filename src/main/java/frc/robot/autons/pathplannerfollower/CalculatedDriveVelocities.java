package frc.robot.autons.pathplannerfollower;

public class CalculatedDriveVelocities {

    double xVel, yVel, rotVel;
    
    public CalculatedDriveVelocities(double xVel, double yVel, double rotVel){
        this.xVel = xVel;
        this.yVel = yVel;
        this.rotVel = rotVel;
    }

    public double getXVel() {
        return xVel;
    }

    public double getYVel() {
        return yVel;
    }

    public double getRotVel() {
        return rotVel;
    }
    
}
