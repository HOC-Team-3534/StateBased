// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.pathplanner.PathPlannerFollower;
import frc.robot.Constants.AUTO;
import frc.robot.autons.Auton;
import frc.robot.extras.Limelight;
import frc.robot.sequences.SequenceProcessor;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrive;
import frc.statebasedcontroller.sequence.fundamental.phase.ISequencePhase;
import frc.statebasedcontroller.sequence.fundamental.sequence.BaseAutonSequence;

import static frc.robot.Constants.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    public static SwerveDrive swerveDrive;
    public static Shooter shooter;
    public static Intake intake;
    public static Climber climber;
    public static SequenceProcessor sequenceProcessor;
    public static RobotContainer robotContainer;

    public static Limelight limelight;
    public static PneumaticsControlModule mainPCM;

    public static boolean isAutonomous = false;
    public static double designatedLoopPeriod = 20;
    public static BaseAutonSequence<? extends ISequencePhase> chosenAuton;
    public static PathPlannerFollower corner1OneBall1;
    public static PathPlannerFollower corner1TwoBall1;
    public static PathPlannerFollower corner2OneBall1;
    public static PathPlannerFollower corner3OneBall1;
    public static PathPlannerFollower corner4FiveBallPre;
    public static PathPlannerFollower corner4FiveBall1;
    public static PathPlannerFollower corner4FiveBall2;
    public static PathPlannerFollower corner4FiveBall3;
    private static long autonStartTime;
    private final SendableChooser<Auton> sendableChooser = new SendableChooser<>();
    private final Field2d m_field = new Field2d();
    private int loopCnt = 0;
    private int loopPeriod = 0;
    private int logCounter = 0;

    @Override
    public void robotInit() {

        if (ROBOTTYPE == RobotType.PBOT) {
            limelight = new Limelight(ty -> .0059 * Math.pow(ty, 2) - .229 * ty + 5.56, d -> 3.0, vel -> 3.0);
        } else {
            limelight = new Limelight(ty -> 0.0056 * Math.pow(ty, 2) - .11 * ty + 3.437, d -> 0.52 * Math.pow(d, 2) - 4.5 * d + 12.8, vel -> Math.sqrt(5200 * vel - 15935) / 52 + 225 / 52);
        }
        mainPCM = new PneumaticsControlModule(MAIN_PCM);

        // PortForwarder.add(5800, "limelight.local", 5800);
        // PortForwarder.add(5801, "limelight.local", 5801);
        // PortForwarder.add(5805, "limelight.local", 5805);

        robotContainer = new RobotContainer();

        swerveDrive = new SwerveDrive();

        if (Constants.ROBOTTYPE == Constants.RobotType.PBOT) {
            shooter = new Shooter(d -> 1018.0 + 1984.0 * Math.log(d));
        } else {
            shooter = new Shooter(d -> 1781.0 + 1003.6 * Math.log(d));
        }
        intake = new Intake();

        climber = new Climber();

        sequenceProcessor = new SequenceProcessor();

        corner1OneBall1 = new PathPlannerFollower("Corner 1 1 Ball 1", AUTO.kMaxSpeedMetersPerSecond, AUTO.kMaxAccelerationMetersPerSecondSquared);
        corner1TwoBall1 = new PathPlannerFollower("Corner 1 2 Ball 1", AUTO.kMaxSpeedMetersPerSecond, AUTO.kMaxAccelerationMetersPerSecondSquared);
        corner2OneBall1 = new PathPlannerFollower("Corner 2 1 Ball 1", AUTO.kMaxSpeedMetersPerSecond, AUTO.kMaxAccelerationMetersPerSecondSquared);
        corner3OneBall1 = new PathPlannerFollower("Corner 3 1 Ball 1", AUTO.kMaxSpeedMetersPerSecond, AUTO.kMaxAccelerationMetersPerSecondSquared);
        corner4FiveBallPre = new PathPlannerFollower("Corner 4 5 Ball Pre", AUTO.kMaxSpeedMetersPerSecond, AUTO.kMaxAccelerationMetersPerSecondSquared);
        corner4FiveBall1 = new PathPlannerFollower("Corner 4 5 Ball 1", AUTO.kMaxSpeedMetersPerSecond, AUTO.kMaxAccelerationMetersPerSecondSquared);
        corner4FiveBall2 = new PathPlannerFollower("Corner 4 5 Ball 2", AUTO.kMaxSpeedMetersPerSecond, AUTO.kMaxAccelerationMetersPerSecondSquared);
        corner4FiveBall3 = new PathPlannerFollower("Corner 4 5 Ball 3", AUTO.kMaxSpeedMetersPerSecond, AUTO.kMaxAccelerationMetersPerSecondSquared);

        SmartDashboard.putNumber("Auton Time Delay(ms)", 0.0);

        sendableChooser.setDefaultOption("CORNER 4: 3 BALL", Auton.CORNER4_3BALL);
        sendableChooser.addOption("CORNER 4: 5 BALL", Auton.CORNER4_5BALL);

        sendableChooser.addOption("CORNER 3: 1 BALL", Auton.CORNER3_1BALL);

        sendableChooser.addOption("CORNER 2: 1 BALL", Auton.CORNER2_1BALL);

        sendableChooser.addOption("CORNER 1: 2 BALL", Auton.CORNER1_2BALL);
        sendableChooser.addOption("CORNER 1: 1 BALL", Auton.CORNER1_1BALL);

        sendableChooser.addOption("NO AUTON (MUST BE STRAIGHT ALIGNED)", Auton.NO_OP);

        SmartDashboard.putData(sendableChooser);

        SmartDashboard.putData("Field", m_field);

    }


    @Override
    public void robotPeriodic() {
    }

    @Override
    public void disabledInit() {
        swerveDrive.forceRelease();
        shooter.forceRelease();
        intake.forceRelease();
        climber.forceRelease();
    }

    @Override
    public void disabledPeriodic() {

        long prevLoopTime = 0;

        while (this.isDisabled()) {

            log();

            long currentTime = System.currentTimeMillis();

            if (currentTime - prevLoopTime >= designatedLoopPeriod) {

                loopPeriod = (int) (currentTime - prevLoopTime);
                prevLoopTime = currentTime;
                loopCnt++;

                swerveDrive.neutral();
                swerveDrive.process();
            }

            Timer.delay(0.001);

        }
    }

    @Override
    public void autonomousInit() {
        chosenAuton = sendableChooser.getSelected().getAuton();
        chosenAuton.start();
        autonStartTime = System.currentTimeMillis();
    }

    @Override
    public void autonomousPeriodic() {
        isAutonomous = this.isAutonomous();

        long prevLoopTime = 0;

        while (this.isAutonomous() && this.isEnabled()) {

            log();

            long currentTime = System.currentTimeMillis();

            if (currentTime - prevLoopTime >= designatedLoopPeriod) {

                loopPeriod = (int) (currentTime - prevLoopTime);
                prevLoopTime = currentTime;
                loopCnt++;

                if (currentTime - autonStartTime > SmartDashboard.getNumber("Auton Time Delay(ms)", 0.0)) {
                    chosenAuton.process();
                }
                // run processes

                /** Run subsystem process methods here */
                swerveDrive.process();
                shooter.process();
                intake.process();
                //climber.process();
            }

            Timer.delay(0.001);
        }
    }

    @Override
    public void teleopInit() {
        swerveDrive.forceRelease();
        shooter.forceRelease();
        intake.forceRelease();
        climber.forceRelease();
    }

    @Override
    public void teleopPeriodic() {

        isAutonomous = this.isAutonomous();

        long prevLoopTime = 0;

        while (this.isTeleop() && this.isEnabled()) {

            log();

            long currentTime = System.currentTimeMillis();

            if (currentTime - prevLoopTime >= designatedLoopPeriod) {

                loopPeriod = (int) (currentTime - prevLoopTime);
                prevLoopTime = currentTime;
                loopCnt++;

                sequenceProcessor.process();
                // run processes

                /** Run subsystem process methods here */
                swerveDrive.process();
                shooter.process();
                intake.process();
                climber.process();
            }

            Timer.delay(0.001);
        }
    }

    @Override
    public void testInit() {
    }

    @Override
    public void testPeriodic() {
    }

    public void log() {

        logCounter++;

        if (logCounter > 5) {

            SmartDashboard.putBoolean("L1 switch 1", Climber.l1Switch.get());
            SmartDashboard.putBoolean("L1 switch 2", Climber.h2Switch.get());
            SmartDashboard.putBoolean("L3 switch 1", Climber.l3Switch.get());
            SmartDashboard.putBoolean("L3 switch 2", Climber.h4Switch.get());
            SmartDashboard.putNumber("Gyro", swerveDrive.getGyroRotation().getDegrees());

            SmartDashboard.putNumber("tx (inverted)", limelight.getHorizontalAngleOffset().getDegrees());
            SmartDashboard.putNumber("ty", limelight.getPixelAngle());
            SmartDashboard.putNumber("distance", limelight.getDistance());

            //SmartDashboard.putNumber("Moving Target Angle Offset", RobotMap.limelight.getLimelightShootProjection().getOffset().getDegrees());
            //SmartDashboard.putNumber("Moving Target Distance", RobotMap.limelight.getLimelightShootProjection().getDistance());

            SmartDashboard.putNumber("Odometry X", swerveDrive.getPose().getX());
            SmartDashboard.putNumber("Odometry Y", swerveDrive.getPose().getY());

            // Vector2d targetVectorVelocity = swerveDrive.getTargetOrientedVelocity();

            // SmartDashboard.putString("Target Velocity Vector", String.format("X: %.2f, Y: %.2f", targetVectorVelocity.x, targetVectorVelocity.y));

            SmartDashboard.putBoolean("Target Acquired", limelight.isTargetAcquired());

            SmartDashboard.putNumber("Target Angle Error", swerveDrive.getTargetShootRotationAngleError().getDegrees());

            m_field.setRobotPose(swerveDrive.getPose());

            logCounter = 0;
        }

    }
}
