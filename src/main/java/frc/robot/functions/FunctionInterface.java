package frc.robot.functions;

public interface FunctionInterface{

    public void process();

    public boolean isFinished();

    public void reset();

    public void completed();

    public void started();

    public boolean isRunning();

    public void setState(int state);

}