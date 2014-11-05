package buffer;

import mathematics.Vec;
import objects.GameObject;

/**
 * A buffer which holds collision information about two objects.
 * @author Nex
 *
 */
public class CollisionBuffer {

	//Attributes
	public GameObject obj1;
	public GameObject obj2;
	public Vec obj1CollidedSide;
	public Vec obj2CollidedSide;
	public Vec obj1Heading;
	public Vec obj2Heading;
	
	/**
	 * Constructs the collision buffer.
	 * Stores objects and their forward vectors,
	 * Determines which side of each object collided with the other object
	 * @param o1 Object 1 involved in collision
	 * @param o2 Object 2 involved in collision
	 */
	public CollisionBuffer(GameObject o1, GameObject o2) {
		//Set member variables
		obj1 = o1;
		obj2 = o2;
		
		obj1CollidedSide = new Vec(2);
		obj2CollidedSide = new Vec(2);
		
		obj1Heading = obj1.getForward();
		obj2Heading = obj2.getForward();
		
		//Get difference in left side of obj1 from right side of obj2 positions
		double lrSideDistance = Math.abs(obj1.getXPos() - (obj2.getXPos() + obj2.getWidth()));
		//Get difference in right side of obj 1 from left side of obj2
		double rlSideDistance = Math.abs((obj1.getXPos() + obj1.getWidth()) - obj2.getXPos());
		//Get difference in top side of obj1 from bottom side of obj2
		double tbSideDistance = Math.abs(obj1.getYPos() - (obj2.getYPos() + obj2.getHeight()));
		//Get difference in bottom side of obj1 to top side of obj2
		double btSideDistance = Math.abs((obj1.getYPos() + obj1.getHeight()) - obj2.getYPos());
		
		//Determine which distance is the smallest
		if(lrSideDistance < rlSideDistance){
			//Left is closer than right
			if(tbSideDistance < btSideDistance){
				//Top is closer than bottom
				if(lrSideDistance < tbSideDistance){
					//Obj1's left side collided with obj2's right side
					obj1CollidedSide.setComponent(1, -obj1.getHeight());
					obj2CollidedSide.setComponent(1, obj2.getHeight());
				}
				else{
					//Obj1's top side collided with Obj2's bottom side
					obj1CollidedSide.setComponent(0, obj1.getWidth());
					obj2CollidedSide.setComponent(0, -obj2.getWidth());
				}
			}
			else{
				//Bottom is closer than top
				if(lrSideDistance < btSideDistance){
					//Obj1's left side collided with Obj2 right side
					obj1CollidedSide.setComponent(1, -obj1.getHeight());
					obj2CollidedSide.setComponent(1, obj2.getHeight());
				}
				else{
					//Obj1's bottom side collided with Obj2's top side
					obj1CollidedSide.setComponent(0,  -obj1.getWidth());
					obj2CollidedSide.setComponent(0, obj2.getWidth());
				}
			}
		}
		else{
			//Right is closer than left
			if(tbSideDistance < btSideDistance){
				//Top is closer than bottom
				if(rlSideDistance < tbSideDistance){
					//Obj1's right side collided with Obj2's left side
					obj1CollidedSide.setComponent(1, obj1.getHeight());
					obj2CollidedSide.setComponent(1, -obj2.getHeight());
				}
				else{
					//Obj1's top side collided with Obj2's bottom side
					obj1CollidedSide.setComponent(0, obj1.getWidth());
					obj2CollidedSide.setComponent(0, -obj2.getWidth());
				}
			}
			else{
				//Bottom is closer than top
				if(rlSideDistance < btSideDistance){
					//Obj1's right side collided with Obj2's left side
					obj1CollidedSide.setComponent(1, obj1.getHeight());
					obj2CollidedSide.setComponent(1, -obj2.getHeight());
				}
				else{
					//Obj1's bottom side collided with Obj2's top side
					obj1CollidedSide.setComponent(0, -obj1.getWidth());
					obj2CollidedSide.setComponent(0, obj2.getWidth());
				}
			}
		}
		
		
	}

}
