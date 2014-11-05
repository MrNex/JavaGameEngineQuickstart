package triggers;

import buffer.CollisionBuffer;
import objects.GameObject;

/**
 * Trigger defines a component of GameObject which causes actions upon collision with any other gameObject
 * @author Nex
 *
 */
public abstract class Trigger {

	//Attributes
	protected GameObject attachedTo;

	/**
	 * Constructs a trigger
	 */
	public Trigger() { }

	//Accessors & Modifiers
	/**
	 * gets the gameobject this trigger is attached to
	 * @return The gameobject this trigger is attached to
	 */
	public GameObject getAttachedObj(){
		return attachedTo;
	}

	/**
	 * Sets the gameobject his trigger is attached to
	 * @param attachTo The gameobject the trigger should attach to
	 */
	public void setAttachedObj(GameObject attachTo){
		attachedTo = attachTo;
	}



	/**
	 * Defines the action taken when this trigger is triggered
	 * @param triggeredBy The gameobject which triggered this trigger.
	 */
	public abstract void action(GameObject triggeredBy, CollisionBuffer cBuff);
}
