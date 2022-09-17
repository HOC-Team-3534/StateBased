package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.parent.BaseSubsystem;
import frc.robot.subsystems.parent.IDistanceToShooterRPM;
import frc.robot.subsystems.states.ShooterState;

import static frc.robot.Constants.*;

public class Shooter extends BaseSubsystem<ShooterState> {

    public static WPI_TalonFX shooter;
    public static WPI_TalonSRX shooterBoot;
    
    static DoubleSolenoid pusher;

    IDistanceToShooterRPM rpmFunction;

    public Shooter(IDistanceToShooterRPM rpmFunction) {
        super(ShooterState.NEUTRAL);

        shooter = new WPI_TalonFX(SHOOTER_MOTOR);
        shooter.setInverted(true);
        shooter.selectProfileSlot(0, 0);
        shooter.config_kF(0, 0.0553); // .05
        shooter.config_kP(0, 0.17);
        shooter.config_kD(0, 3);
        shooter.config_kF(1, 0.06); // .05
        shooter.config_kP(1, 0.1);
        shooter.config_kD(1, 5);
        shooter.configClosedloopRamp(0.5);

        shooterBoot = new WPI_TalonSRX(Constants.SHOOTER_BOOT);
        shooterBoot.setInverted(true);

        pusher = Robot.mainPCM.makeDoubleSolenoid(PUSHER_FORWARD, PUSHER_REVERSE);
        
        // makes the graphical number to enter text - have to do a
        // put to do a get
        SmartDashboard.putNumber("Manual Testing RPM", 2000.0);
        SmartDashboard.putNumber("RPM MULTIPLIER (%)", 100.0);
        SmartDashboard.putNumber("AUTON RPM MULTIPLIER (%)", 100.0);
        this.rpmFunction = rpmFunction;
    }

    @Override
    public void process() {

        super.process();

        switch (getCurrentSubsystemState()) {
            case NEUTRAL:
                neutral();
                break;
            case AUTONPREUPTOSPEED:
                if (Robot.swerveDrive.getPathStateController().getPathPlannerFollower() != null) {
                    if (Robot.swerveDrive.getPathStateController().getPathPlannerFollower()
                            .getRemainingTimeSeconds() < 2.0) {
                        upToSpeed(3000);
                    } else {
                        neutral();
                    }
                } else {
                    neutral();
                }
                break;
            case UPTOSPEED:
                // grabs the number from SmartDashboard
                //waitNSpin(SmartDashboard.getNumber("Manual Testing RPM", 0.0));
                if (Robot.limelight.isTargetAcquired()) {
                    upToSpeed();
                    //upToSpeed(SmartDashboard.getNumber("Manual Testing RPM", 0.0));
                } else {
                    upToSpeed(3000);
                }
                break;
            case BURP:
                burp();
                break;
            case PUNCH:
                punch();
                break;
            case RESETPUNCH:
                resetPunch();
                break;
            case BOOT:
                boot();
                break;
        }
    }

    public void shoot(double rpm) {
        double countsPer100MS = rpm * Constants.RPM_TO_COUNTS_PER_100MS;
        shooter.set(ControlMode.Velocity, countsPer100MS);
    }

    private double getShooterRPM(){
        return shooter.getSelectedSensorVelocity() / Constants.RPM_TO_COUNTS_PER_100MS;
    }

    public double getShooterClosedLoopError(){
        return shooter.getClosedLoopError();
    }

    public double getCalculatedRPMError(){
        double rpmMultiplier = SmartDashboard.getNumber("RPM MULTIPLIER (%)", 100.0) / 100.0;
        return Math.abs(getShooterRPM() - rpmMultiplier * rpmFunction.getShooterRPM((Robot.limelight.getDistance())));
        //return Math.abs(getShooterRPM() - rpmMultiplier * SmartDashboard.getNumber("Manual Testing RPM", 0.0));
    }

    private void upToSpeed() {
        if(getStateFirstRunThrough()){
            shooter.selectProfileSlot(0, 0);
        }
        double rpmMultiplier = SmartDashboard.getNumber("RPM MULTIPLIER (%)", 100.0) / 100.0;
        shoot(rpmMultiplier * rpmFunction.getShooterRPM(Robot.limelight.getDistance()));
        //shoot(rpmMultiplier * rpmFunction.getShooterRPM(RobotMap.limelight.getLimelightShootProjection().getDistance()));
    }

    private void upToSpeed(double rpm) {
        if(getStateFirstRunThrough()){
            shooter.selectProfileSlot(0, 0);
        }
        shoot(rpm);
    }

    private void burp() {
        if(getStateFirstRunThrough()){
            shooter.selectProfileSlot(1, 0);
        }
        shoot(1300);
    }

    private void punch() {
        if (getStateFirstRunThrough()) {
            setWithADelayToOff(pusher, Value.kForward, Constants.DelayToOff.SHOOTER_PUSHER.millis);
        }
    }

    private void resetPunch() {
        if (getStateFirstRunThrough()) {
            setWithADelayToOff(pusher, Value.kReverse, Constants.DelayToOff.SHOOTER_PUSHER.millis);
        }
        if (this.getSequenceRequiring().getTimeSinceStartOfState() > 50) {
            shooterBoot.set(ControlMode.PercentOutput, -0.70);

        }
        if (this.getSequenceRequiring().getTimeSinceStartOfState() > 240) {
            shooterBoot.set(ControlMode.PercentOutput, 0.0);
        }

    }

    private void boot() {
        if (this.getStateFirstRunThrough()) {
            shooterBoot.set(ControlMode.PercentOutput, 0.90);
        }
        if (this.getSequenceRequiring().getTimeSinceStartOfState() > 140) {
            shooterBoot.set(ControlMode.PercentOutput, 0.0);
        }
    }

    @Override
    public void neutral() {
        shoot(0);
        shooterBoot.set(ControlMode.PercentOutput, 0.0);
    }

    @Override
    public boolean abort() {
        shoot(0);
        shooterBoot.set(ControlMode.PercentOutput, 0.0);
        return true;
    }

}

