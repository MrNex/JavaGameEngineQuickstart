package objects;

import mathematics.Vec;

/**
 * Movable game object defines any gameObjects which need to move
 * and have resolved collisions. While all gameobjects can technically move,
 * if there is a collision there's no simple resolution, and if we do nothing 
 * the gameobject will get "stuck" inside of the other object.
 * 
 * Movable game objects solve this problem by tracking a previous position.
 * In case of a collision MovableGameObjects will be reverted back
 * to their previousPosition by the collision manager.
 * 
 * Movable game objects also implement an activeCheckpoint. If movableGameobjects
 * collide with a deathTrigger they are set back to their activeCheckpoint.
 * @author Nex
 *
 */
public class MovableGameObject extends GameObject {

	//Attributes
	protected Vec previousPosition;

	public MovableGameObject(double xx, double yy, double w, double h, Vec fwd) {
		super(xx, yy, w, h, fwd);
		previousPosition = new Vec(2);
	}

	/**
	 * Updates previousPosition and increments position by the movementVector
	 * Also makes call to updateShape
	 * @param movementVec The vector to increment position by
	 */
	public void move(Vec movementVec){
		previousPosition.copy(position);
		position.add(movementVec);
		updateShape();
	}

	/**
	 * Reverts the position back to the previous position
	 * And makes call to updateShape
	 */
	public void revert(){
		position.copy(previousPosition);
		updateShape();
	}
	
	/**
	 * Sets the previousosition to the currentPosition
	 */
	public void refresh(){
		previousPosition.copy(position);
	}

}
