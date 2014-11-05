package state;

import java.awt.Graphics2D;

/**
 * Defines an abstracted state for any finite state machine.
 * @author Nex
 *
 */
public abstract class State {

	/**
	 * Constructs a default state
	 */
	public State() {

		init();

	}

	/**
	 * Initializes any member variables in this state
	 */
	protected abstract void init();

	/**
	 * Updates this state
	 */
	public abstract void update();

}
