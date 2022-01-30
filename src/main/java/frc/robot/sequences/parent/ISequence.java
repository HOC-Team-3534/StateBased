package frc.robot.sequences.parent;

public interface ISequence<S extends IState> {

    void process();

    S getState();

    /**
     * 
     * @return when true, it is safe to control the mechanisms previously being controlled by the function
     */
    boolean abort();

}