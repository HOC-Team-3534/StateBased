// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.*;
import frc.robot.autons.Auton;
import frc.robot.sequences.SequenceProcessor;
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
    public static SequenceProcessor sequenceProcessor;
    public static RobotContainer robotContainer;
    public static BaseAutonSequence<? extends ISequencePhase> chosenAuton;

    private final SendableChooser<Auton> sendableChooser = new SendableChooser<>();
    private final Field2d m_field = new Field2d();

    public static boolean isAutonomous = false;
    private static long autonStartTime;
    
    private int loopCnt = 0;
    private int loopPeriod = 0;
    private int logCounter = 0;

    @Override
    public void robotInit() {

        /**
         * TODO Initialize Subsystems and Robot Wide Sensors
         */

        robotContainer = new RobotContainer();

        swerveDrive = new SwerveDrive();

        sequenceProcessor = new SequenceProcessor();

        /**
         * TODO Read in and set PathPlannerFollowers
         */

        SmartDashboard.putNumber("Auton Time Delay(ms)", 0.0);

        /**
         * TODO Add options to the sendableChooser for the different autons
         */

        SmartDashboard.putData(sendableChooser);

        SmartDashboard.putData("Field", m_field);

    }


    @Override
    public void robotPeriodic() {
    }

    @Override
    public void disabledInit() {
        /** TODO force release subsystems here */
        swerveDrive.forceRelease();
    }

    @Override
    public void disabledPeriodic() {

        long prevLoopTime = 0;

        while (this.isDisabled()) {

            log();

            long currentTime = System.currentTimeMillis();

            if (currentTime - prevLoopTime >= DESIGNATED_LOOP_PERIOD) {

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

            if (currentTime - prevLoopTime >= DESIGNATED_LOOP_PERIOD) {

                loopPeriod = (int) (currentTime - prevLoopTime);
                prevLoopTime = currentTime;
                loopCnt++;

                if (currentTime - autonStartTime > SmartDashboard.getNumber("Auton Time Delay(ms)", 0.0)) {
                    chosenAuton.process();
                }
                // run processes

                /** TODO Run subsystem process methods here */
                swerveDrive.process();
            }

            Timer.delay(0.001);
        }
    }

    @Override
    public void teleopInit() {
        /** TODO force release subsystems here */
        swerveDrive.forceRelease();
    }

    @Override
    public void teleopPeriodic() {

        isAutonomous = this.isAutonomous();

        long prevLoopTime = 0;

        while (this.isTeleop() && this.isEnabled()) {

            log();

            long currentTime = System.currentTimeMillis();

            if (currentTime - prevLoopTime >= DESIGNATED_LOOP_PERIOD) {

                loopPeriod = (int) (currentTime - prevLoopTime);
                prevLoopTime = currentTime;
                loopCnt++;

                sequenceProcessor.process();
                // run processes

                /** TODO Run subsystem process methods here */
                swerveDrive.process();
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

            /** TODO Out any SmartDashboard variables needed, often for debugging */

            m_field.setRobotPose(swerveDrive.getPose());

            logCounter = 0;
        }

    }
}
