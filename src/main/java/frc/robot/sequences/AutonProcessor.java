package frc.robot.sequences;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotContainer.Buttons;
import frc.robot.RobotMap;

public class AutonProcessor {

    /**
     * Create a new variable of each of the autons
     */

   Autons autons;

    public AutonProcessor() {

        /**
         * Instantiate each of the autons
         */
        autons = new Autons(AutonState.NEUTRAL, AutonState.PICKUPBALL1, AutonPathValues.FROMPOSITION1);

    }

    public void process() {

        autons.start();

        autons.process();
    }
}
