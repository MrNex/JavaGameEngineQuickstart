package engine.manager;

import objects.GameObject;
import objects.MovableGameObject;
import triggers.Trigger;
import engine.Engine;
import buffer.CollisionBuffer;


/**
 * CollisionManager will tap into the current list of objects
 * and query any movableGameObject if it has intersected with any other gameobject.
 * If a collision returns true, it is resolved by reverting the movableGameObject
 * to it's previousPosition before the collision took place.
 * 
 * @author Nex
 *
 */
public class CollisionManager extends Manager {

	/**
	 * Constructs collision manager
	 */
	public CollisionManager() {
		super();
	}

	/**
	 * Initializes member variables
	 */
	@Override
	public void init() {

	}

	/**
	 * Iterates through the list of objects in the current state and 
	 * determines if any movablegameobject is colliding with any other
	 * gameobject. 
	 * 
	 *  Upon detecting a collision it is resolved by reverting the 
	 *  movablegameobject back to it's previousPosition, before the collision took place.
	 */
	@Override
	public void update() {
		for(GameObject obj1 : Engine.currentInstance.getCurrentState().getObjList()){
			
			if(obj1 instanceof MovableGameObject){
				
				for(GameObject obj2 : Engine.currentInstance.getCurrentState().getObjList()){
					if(obj1 == obj2) continue;
					
					//Get the collision buffer!
					CollisionBuffer cBuff = new CollisionBuffer(obj1, obj2);
					
					if(obj1.isColliding(obj2) && obj2.isColliding(obj1)){
						//Only revert if both objects are solid
						if(obj1.isSolid() && obj2.isSolid())
							((MovableGameObject)obj1).revert();
						
						//Triggers are pulled / activated even if objects aren't solid.
						
						//If object 1 is triggerable
						if(obj1.isTriggerable()){
							//For each trigger it has
							for(Trigger t : obj1.getTriggers()){
								t.action(obj2, cBuff);
							}
						}
						
						//If object 2 is triggerable
						if(obj2.isTriggerable()){
							//pull each trigger it has
							for(Trigger t : obj2.getTriggers()){
								t.action(obj1, cBuff);
							}
						}//Ends if obj2 is triggerable
					}//Ends if colliding
					
				}//ends loop of possible colliding objects
			
			}//Ends if object is movable
			
		}//Ends loop of all objects
		
	}//Ends update

}//Ends class
