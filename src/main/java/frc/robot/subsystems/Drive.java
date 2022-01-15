package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.robot.RobotMap;

public class Drive extends SystemBase implements SystemInterface{
    
    private WPI_TalonFX frontLeft = RobotMap.frontLeftMotor, frontRight = RobotMap.frontRightMotor, backLeft = RobotMap.backLeftMotor, backRight = RobotMap.backRightMotor;

    
}
