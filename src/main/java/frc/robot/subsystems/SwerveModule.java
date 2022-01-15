package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public class SwerveModule {
    private static final double kWheelRadius = 0.0508;
    private static final double kCircumference = kWheelRadius * 2 * Math.PI;
    private static final double kDriveRatio = 8.16;
    private static final double kTurningRatio = 12.8;
    private static final int kEncoderResolution = 2048;
    double targetVelocity = 1 * 2048 / 600; // X RPM

    private static final double kModuleMaxAngularVelocity = SwerveDrive.kMaxAngularSpeed;
    private static final double kModuleMaxAngularAcceleration = 2 * Math.PI; // radians per second squared

    private WPI_TalonFX m_driveMotor;
    private double drivekP = 0.01;
    private double drivekI = 0;
    private double drivekD = 0;
    private double drivekF = 0;

    private WPI_TalonFX m_turningMotor;
    private double turningkP = 0.00015;
    private double turningkI = 0;
    private double turningkD = 0;
    private double turningkF = .025;

    public SwerveModule(WPI_TalonFX driveMotor, WPI_TalonFX turningMotor) {
        m_driveMotor = driveMotor;
        m_turningMotor = turningMotor;
        m_driveMotor.configFactoryDefault();
        m_turningMotor.configFactoryDefault();
        m_turningMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 20);
        m_turningMotor.configIntegratedSensorInitializationStrategy(SensorInitializationStrategy.BootToZero);
        TalonFXSensorCollection sensorCollection = m_turningMotor.getSensorCollection();
        double absoluteValue = sensorCollection.getIntegratedSensorAbsolutePosition();

        m_turningMotor.setNeutralMode(NeutralMode.Brake);
        setDrivePIDF(0.00015,0,0,0.048);
        setTurningPIDF(1,0.0,0,0.048);
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
        return new SwerveModuleState(m_driveMotor.getSelectedSensorVelocity()
        , getAngle());
      }
    
      public Rotation2d getAngle() {
        return new Rotation2d((2*Math.PI/(2048*kTurningRatio))*(m_turningMotor.getSelectedSensorPosition()%(2048*kTurningRatio)));
      }

      public double getAngleDegrees() {
        return 360*(m_turningMotor.getSelectedSensorPosition()%(2048*kTurningRatio))/(2048*kTurningRatio);
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
        
        double setTurnValue = (2048/360.0)*nearestDegree;

        inputVelocity = 2048/(10*kCircumference)*state.speedMetersPerSecond*kDriveRatio;
        m_driveMotor.set(TalonFXControlMode.Velocity, inputVelocity);
    
        inputAngle = nearestDegree;
        setpoint = setTurnValue*kTurningRatio;
        m_turningMotor.set(TalonFXControlMode.Position, setpoint);
      }


}