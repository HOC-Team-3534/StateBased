package frc.robot.autons.parent;

import frc.robot.sequences.parent.IState;

public interface IAutonState extends IState {

    String getPathName(BaseAutonSequence<? extends IAutonState> sequence);

    boolean isPathFollowing();

}
