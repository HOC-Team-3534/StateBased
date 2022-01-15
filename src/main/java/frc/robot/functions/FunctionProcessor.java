package frc.robot.functions;

import frc.robot.RobotContainer;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.subsystems.SwerveDrive;

public class FunctionProcessor{

    /**
     * Create a new variable of each of the functions
     */
    
    public Drive drive;

    public FunctionProcessor(){

       /**
        * Instantiate each of the functions
        */

        drive = new Drive();

    }

    public void process(){

       /**
        * Call all of the process methods in each of the functions
        * Pay special attention to the order in which the function
        * methods are called
        */
        if(true){

            drive.process();

        }
    }
}
