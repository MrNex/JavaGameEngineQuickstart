package engine.manager;

/**
 * Defines a component of the {@link engine.Engine} which takes
 * care of a specific portion of what makes a game.
 * @author Nex
 *
 */
public abstract class Manager {

	/**
	 * Manager Constructor
	 */
	public Manager() {	
		init();
	}
	
	
	//Methods
	/**
	 * initialize member variables.
	 */
	public abstract void init();
	
	/**
	 * Update this component of the engine.
	 */
	public abstract void update();
	

}
