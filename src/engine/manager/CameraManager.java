package engine.manager;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import objects.MovableGameObject;
import engine.Engine;
import engine.Engine.Managers;
import mathematics.Vec;

/**
 * A component of the engine which manages the in game camera
 * @author Nex
 *
 */
public class CameraManager extends Manager {

	//Attributes
	private Vec position;
	private MovableGameObject follow;
	private AffineTransform savedSystem;

	//Accessors / modifiers
	/**
	 * Get the camera's position vector
	 * @return The camera's current position
	 */
	public Vec getPosition(){
		return position;
	}
	
	/**
	 * Gets the gameobject the camera is currently following.
	 * @return The vector the camera is currently snapping to
	 */
	public MovableGameObject getFollow(){
		return follow;
	}
	
	/**
	 * Sets the gameobject the camera is following of camera.
	 * Follow is a gameObject which the camera, if not null,
	 * Will automatically get its position and snap to every update cycle.
	 * @param toFollow
	 */
	public void setFollow(MovableGameObject toFollow){
		follow = toFollow;
	}
	
	
	/**
	 * Constructs a camera
	 */
	public CameraManager() {
		super();
	}

	/**
	 * Initializes camera member variables
	 */
	@Override
	public void init() {
		//Initialize camera at 0, 0
		position = new Vec(2);
		
		//set follow vector to explicit null
		follow = null;
	}

	/**
	 * Snaps the camera to the follow vector if  it is not null
	 */
	@Override
	public void update() {
		if(follow != null){
			snapTo(follow.getPos());
		}
	}

	/**
	 * Snaps the camera to a given position
	 */
	public void snapTo(Vec position) {
		this.position.copy(position);
	}
	
	/**
	 * Configures the renderer to adhere to the camera's established coordinate system as opposed to the worldSystem.
	 * @param g2d Reference to the graphics renderer
	 */
	public void constructCameraCoordinateSystem(Graphics2D g2d){
		//Save the current system
		savedSystem = new AffineTransform(g2d.getTransform());
		
		AffineTransform transform = g2d.getTransform();
		
		//Set translationVec to the negative of camera's position
		Vec translationVec = Vec.scalarMultiply(position, -1);
		
		//Get reference to screenManager to get screen dimensions
		ScreenManager screen = (ScreenManager)Engine.currentInstance.getManager(Managers.SCREENMANAGER);
		
		//Add half of screen dimensions to translationVec
		//Get the screen dimensions
		Vec screenDimensions = new Vec(2);
		screenDimensions.setComponent(0, screen.getWindow().getWidth());
		screenDimensions.setComponent(1, screen.getWindow().getHeight());
		//Scale by 1/2
		screenDimensions.scalarMultiply(0.5);
		//Add
		translationVec.add(screenDimensions);
		
		//Create affine transform
		transform.translate(translationVec.getComponent(0), translationVec.getComponent(1));
		
		//Apply affine transform
		g2d.setTransform(transform);
	}
	
	/**
	 * Configures the renderer to revert back to the world coordinate system instead of Camera coordinate system
	 * @param g2d reference to renderer
	 */
	public void destructCameraCoordinateSystem(Graphics2D g2d){
		if(savedSystem != null)
			g2d.setTransform(savedSystem);
	}

}
