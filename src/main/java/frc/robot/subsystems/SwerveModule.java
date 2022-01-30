package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import com.ctre.phoenix.motorcontrol.TalonSRXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public class SwerveModule {
    private static final double kWheelRadius = 0.0508;
    private static final double kCircumference = kWheelRadius * 2 * Math.PI;
    private static final double kDriveRatio = 6.75;
    private static final double kTurningRatio = 1;
    private static final int kTurningEncoderResolution = 1440;
    private static final int kDrivingEncoderResolution = 2048;
    //double targetVelocity = 1 * kEncoderResolution / 600; // X RPM

    private static final double kModuleMaxAngularVelocity = SwerveDrive.kMaxAngularSpeed;
    private static final double kModuleMaxAngularAcceleration = 2 * Math.PI; // radians per second squared

    private WPI_TalonFX m_driveMotor;
    private double drivekP = 0.00015;
    private double drivekI = 0;
    private double drivekD = 0;
    private double drivekF = 0.048;

    private WPI_TalonFX m_turningMotor;
    private double turningkP = 5;
    private double turningkI = 0;
    private double turningkD = 100;
    private double turningkF = 0;

    private WPI_TalonSRX m_turningEncoder;

    public SwerveModule(WPI_TalonFX driveMotor, WPI_TalonFX turningMotor, WPI_TalonSRX turningEncoder) {
        m_driveMotor = driveMotor;
        m_turningMotor = turningMotor;
        m_turningEncoder = turningEncoder;
        m_driveMotor.configFactoryDefault();
        m_turningMotor.configFactoryDefault();
        m_turningEncoder.configFactoryDefault();
        m_driveMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 20);
        SupplyCurrentLimitConfiguration config = new SupplyCurrentLimitConfiguration();
        config.currentLimit = 20;
        m_driveMotor.configSupplyCurrentLimit(config);
        m_turningMotor.configRemoteFeedbackFilter(m_turningEncoder, 0, 20);
        m_turningMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.RemoteSensor0, 0, 20);
        m_turningMotor.setSensorPhase(false);
        m_turningMotor.configSupplyCurrentLimit(config);
        m_turningEncoder.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.QuadEncoder, 0, 20);
        m_turningEncoder.setSelectedSensorPosition(0);

        m_turningMotor.setNeutralMode(NeutralMode.Brake);
        setDrivePIDF(drivekP, drivekI, drivekD, drivekF);
        setTurningPIDF(turningkP, turningkI, turningkD, turningkF);
    }

    void setDrivePIDF(double p, double i, double d, double f) {
        drivekP = p;
        drivekI = i;
        drivekD = d;
        drivekF = f;
        m_driveMotor.config_kP(0, drivekP);
        m_driveMotor.config_kI(0, drivekI);
        m_driveMotor.config_kD(0, drivekD);
        m_driveMotor.config_kF(0, drivekF);
    }

    void setTurningPIDF(double p, double i, double d, double f) {
        turningkP = p;
        turningkI = i;
        turningkD = d;
        turningkF = f;
        m_turningMotor.config_kP(0, turningkP);
        m_turningMotor.config_kI(0, turningkI);
        m_turningMotor.config_kD(0, turningkD);
        m_turningMotor.config_kF(0, turningkF);
    }

    public SwerveModuleState getState() {
        return new SwerveModuleState(m_driveMotor.getSelectedSensorVelocity(), getAngle());
    }

    public Rotation2d getAngle() {
        return new Rotation2d((2 * Math.PI / (kTurningEncoderResolution * kTurningRatio))
                * (m_turningMotor.getSelectedSensorPosition() % (kTurningEncoderResolution * kTurningRatio)));
    }

    public double getAngleDegrees() {
        return 360 * (m_turningMotor.getSelectedSensorPosition() % (kTurningEncoderResolution * kTurningRatio)) / (kTurningEncoderResolution * kTurningRatio);
    }

    private double inputAngle;
    private double setpoint;
    private double inputVelocity;

    public double getInputAngle() {
        return this.inputAngle;
    }

    public void setInputAngle(double inputAngle) {
        this.inputAngle = inputAngle;
    }

    public double getSetpoint() {
        return this.setpoint;
    }

    public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;
    }

    public double getInputVelocity() {
        return this.inputVelocity;
    }

    public void setInputVelocity(double inputVelocity) {
        this.inputVelocity = inputVelocity;
    }

    public void setDesiredState(SwerveModuleState state) {
        state = SwerveModuleState.optimize(state, getAngle());
        long nearestDegree = Math.round(state.angle.getDegrees());

        double setTurnValue = (kTurningEncoderResolution / 360.0) * nearestDegree;
        //unit is encoder counts per 100 ms
        inputVelocity = kDrivingEncoderResolution / (10 * kCircumference) * state.speedMetersPerSecond * kDriveRatio;
        m_driveMotor.set(TalonFXControlMode.Velocity, inputVelocity);

        inputAngle = nearestDegree;
        setpoint = setTurnValue * kTurningRatio;
        m_turningMotor.set(TalonFXControlMode.Position, setpoint);
    }

}