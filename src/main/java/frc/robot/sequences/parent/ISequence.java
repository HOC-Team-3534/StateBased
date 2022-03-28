package frc.robot.sequences.parent;

public interface ISequence<SeqS extends ISequenceState> {

    void process();

    SeqS getState();

    /**
     * 
     * @return when true, it is safe to control the mechanisms previously being controlled by the function
     */
    boolean abort();

}