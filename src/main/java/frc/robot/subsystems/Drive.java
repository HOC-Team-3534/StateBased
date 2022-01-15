package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.robot.RobotMap;

public class Drive extends SystemBase implements SystemInterface{
    
    private WPI_TalonFX frontLeft = RobotMap.frontLeftDrive, frontRight = RobotMap.frontRightDrive, backLeft = RobotMap.backLeftDrive, backRight = RobotMap.backRightDrive;

    
}
