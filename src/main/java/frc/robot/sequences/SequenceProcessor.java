package frc.robot.sequences;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotContainer.Buttons;
import frc.robot.subsystems.Climber;

public class SequenceProcessor {

    /**
     * Create a new variable of each of the functions
     */

    public Drive drive;
    public Shoot shoot;
    public Burp burp;
    public IntakeSeq intake;
    public Extake extake;
    public RollIn rollIn;
    public ClimbPrep climbPrep;
    public Climb climb;
    public ClimbPrepReset climbPrepReset;
    public ClimbReset climbReset;
    public GyroReset gyroReset;


    public SequenceProcessor() {

        /**
         * Instantiate each of the sequences
         */

        drive = new Drive(DrivePhase.NEUTRAL, DrivePhase.DRIVE);

        shoot = new Shoot(ShootPhase.NEUTRAL, ShootPhase.UPTOSPEED);
        burp = new Burp(BurpPhase.NEUTRAL, BurpPhase.BURP);
        intake = new IntakeSeq(IntakeSeqPhase.NEUTRAL, IntakeSeqPhase.EXTEND);
        extake = new Extake(ExtakePhase.NEUTRAL, ExtakePhase.EXTAKE);
        rollIn = new RollIn(RollInPhase.NEUTRAL, RollInPhase.ROLLIN);
        climbPrep = new ClimbPrep(ClimbPrepPhase.NEUTRAL, ClimbPrepPhase.PREPCLAW);
        climb = new Climb(ClimbPhase.NEUTRAL, ClimbPhase.GRIPMIDBAR);
        climbPrepReset = new ClimbPrepReset(ClimbPrepResetPhase.NEUTRAL, ClimbPrepResetPhase.RESETARM);
        climbReset = new ClimbReset(ClimbResetPhase.NEUTRAL, ClimbResetPhase.MOVEARMMANUALLY);
        gyroReset = new GyroReset(GyroResetPhase.NEUTRAL, GyroResetPhase.RESET);

    }

    public void process() {

        if (shoot.isNeutral()) {
            Robot.limelight.resetLimelightGlobalValues();
        }

        if (climb.isNeutral() && shoot.isNeutral()) {
            drive.start(Robot.swerveDrive);
        }
        if ((Buttons.RAMPSHOOTER.getButton() || Buttons.SHOOT.getButton()) && !Buttons.MoveClimbArmManually.getButton()) {
            shoot.start(Robot.swerveDrive);
        }
        if (Buttons.Burp.getButton()) {
            burp.start();
        }
        if (Buttons.Intake.getButton()) {
            intake.start();
        }
        if (Buttons.Extake.getButton()) {
            extake.start();
        }
        if (Buttons.RollIn.getButton() && !Buttons.MoveClimbArmManually.getButton()) {
            rollIn.start();
        }
        if (Buttons.ClimbPrep.getButton()) { // TODO: in last 35 seconds of match logic
            climbPrep.start();
        }
        if (Buttons.Climb.getButton()
                && Robot.climber.getSequenceRequiring().getPhase().equals(ClimbPrepPhase.PREPPEDFORCLIMB)
                && (!Climber.l1Switch.get() || !Climber.h2Switch.get())) {
            climb.start(Robot.climber, Robot.swerveDrive);
        }
        if (Buttons.ClimbPrepReset.getButton() && !climbPrep.isNeutral()) {
            climbPrepReset.start(Robot.climber);
        }
        if (Buttons.GyroReset.getButton()) {
            gyroReset.start();
        }
        if (Buttons.MoveClimbArmManually.getButton()) {
            climbReset.start();
        }

        drive.process();
        shoot.process();
        burp.process();
        intake.process();
        extake.process();
        rollIn.process();
        climbPrep.process();
        climbPrepReset.process();
        climb.process();
        climbReset.process();
        gyroReset.process();
        SmartDashboard.putBoolean("Prepped for climb: ", climbPrep.getPhase().name() == "PREPPEDFORCLIMB");
        SmartDashboard.putString("Climb State", climb.getPhase().name());
    }
}
