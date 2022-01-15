package frc.robot.functions;

public class FunctionBase implements FunctionInterface{

    boolean running;
    boolean finished;
    int state;

    @Override
    public void process(){
        
    }

    @Override
    public boolean isFinished(){

        return finished;

    }

    @Override
    public void reset(){

        state = 0;
        running = false;
        finished = false;

    }

    @Override
    public void completed(){

        running = false;
        finished = true;

    }

    @Override
    public void started(){

        running = true;

    }

    @Override
    public boolean isRunning(){

        return running;

    }

    @Override
    public void setState(int state){

        this.state = state;

    }
}