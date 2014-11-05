package state.object;

import java.awt.Graphics2D;

import objects.GameObject;

/**
 * Object state defines a state within {@link objects.GameObject}'s finite state machine
 * @author Nex
 *
 */
public abstract class ObjectState {

	protected GameObject attachedTo;

	public ObjectState() {

	}
	
	//Accessors
	public void setAttachedGameObject(GameObject attachTo){
		attachedTo = attachTo;
	}

	/**
	 * Will be called on attaching a gameobject to this state.
	 */
	abstract public void enter();

	/**
	 * Updates this state. All state logic is done here.
	 */
	abstract public void update();
	
	/**
	 * Allows the state to draw in THE OBJECTS LOCAL COORDINATE SYSTEM.
	 * Draws any debugging / added effects this state has.
	 * This also allows (and was originally intended) for visual debugging capabilities.
	 * 
	 * In case of drawing errors, understand that the coordinate system when this method is called, is translated and rotated
	 * to be consistent with the object itself. See {@link objects.GameObject}'s constructLocalSystem() method.
	 * 
	 * @param g2d reference to renderer who's current system is the attached game object's LOCAL COORDINATE SYSTEM!
	 */
	abstract public void drawEffects(Graphics2D g2d);

	/**
	 * Will be called on an object changing from this state.
	 * Used for cleanup / changing back any altered properties of the attached gameObject
	 */
	abstract public void exit();

}
