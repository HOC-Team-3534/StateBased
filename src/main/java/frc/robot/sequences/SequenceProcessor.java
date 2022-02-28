package frc.robot.sequences;

import javax.swing.text.StyleContext.SmallAttributeSet;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotContainer.Buttons;
import frc.robot.RobotMap;

public class SequenceProcessor{

    /**
     * Create a new variable of each of the functions
     */

    public Drive drive;
    public Shoot shoot;
    public IntakeSeq intake;
    public ClimbPrep climbPrep;
    public Climb climb;

    public SequenceProcessor() {

        /**
         * Instantiate each of the sequences
         */

        drive = new Drive(DriveState.NEUTRAL, DriveState.DRIVE);
        
        shoot = new Shoot(ShootState.NEUTRAL, ShootState.WAITNSPIN);
        intake = new IntakeSeq(IntakeState.NEUTRAL, IntakeState.EXTEND);
        climbPrep = new ClimbPrep(ClimbPrepState.NEUTRAL, ClimbPrepState.PREPCLAW);
        climb = new Climb(ClimbState.NEUTRAL, ClimbState.GRIPMIDBAR);
    }

    public void process() {

        if (climb.isNeutral()) {
            drive.start(Robot.swerveDrive);
        }
        if(Buttons.Shoot.getButton()) {
            shoot.start();
        }
        if(Buttons.Intake.getButton()) {
            intake.start();
        }
        if (Buttons.ClimbPrep.getButton()) { // TODO: in last 35 seconds of match logic
            climbPrep.start();
        }
        if (Buttons.Climb.getButton()
                && Robot.climber.getSequenceRequiring().getState().getName() == "PREPPEDFORCLIMB"
                && (RobotMap.m_l1Switch.get() || RobotMap.m_h2Switch.get())) {
            climb.start(Robot.climber, Robot.swerveDrive);
        }

        drive.process();
        shoot.process();
        intake.process();
        climbPrep.process();
        climb.process();
        SmartDashboard.putBoolean("Prepped for climb: ", climbPrep.getState().getName() == "PREPPEDFORCLIMB");
    }
}
