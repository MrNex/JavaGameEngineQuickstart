package engine.manager;

/**
 * Class defines a component of the engine which handles sprites and animations.
 * Tracks timing for animations and defines fps 
 * @author Nex
 *
 */
public class SpriteManager extends Manager {

	//Immutable Attributes
	private final double fps = 6.0;
	
	//Attributes
	private long previousTime;		//Tracks the time previous cycle in ms
	private long currentTime;			//Tracks the time current cycle in ms
	private double deltaTime;			//Tracks the change in time from last frameChange to this frameChange in seconds
	private double deltaFrames;			//Tracks the change in frames from last cycle to this cycle
	private boolean reset;
	
	
	//Accessors
	/**
	 * GEts the number of frames passed since last update
	 * @return
	 */
	public double getDeltaFrames(){
		return deltaFrames;
	}
	
	
	/**
	 * Constructs a sprite manager
	 * 
	 * Initialize all member variables
	 */
	public SpriteManager() {
		super();
	}

	
	/**
	 * Initializes all member variables
	 */
	@Override
	public void init() {
		currentTime = System.currentTimeMillis();
		previousTime = currentTime;
		
		deltaTime = 0;
		deltaFrames = 0;
	}
	
	/**
	 * Resets the number of frames which passed since last 
	 * frame change
	 */
	private void resetDeltaFrames(){
		deltaFrames = 0;
		reset = false;
	}
	
	/**
	 * Sets the number of frames which passed since last 
	 * frame change to be reset upon the next update cycle
	 */
	public void flagDeltaFramesReset(){
		reset = true;
	}

	/**
	 * Updates the SpriteManager
	 * If any sprites updated their frame, resets the number of frames which has passed.
	 * Gets time passed since last update,
	 * Increments time passed since last frame change
	 * Calculates number of frames passed since last frameChange
	 * Decrements time passed since last frameChange by time needed for deltaFrames to pass
	 * Updates previous time to be current time for next cycle
	 */
	@Override
	public void update() {
		if(reset) resetDeltaFrames();
		
		currentTime = System.currentTimeMillis();
		
		deltaTime += ((double)(currentTime - previousTime))/1000.0;
		deltaFrames += (int)((deltaTime * fps));
		
		deltaTime -= ((double)deltaFrames) * (1.0/(double)fps);
		previousTime = currentTime;

	}

}
