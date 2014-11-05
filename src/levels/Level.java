package levels;

import java.util.ArrayList;
import java.util.HashMap;

import engine.Engine;
import objects.GameObject;
import state.engine.EngineState;
import triggers.Trigger;

/**
 * Class which defines a level in the game
 * 
 * @author Nex
 *
 */
public class Level {

	//Attributes
	private ArrayList<GameObject> objects;

	//Accessors / Modifiers
	/**
	 * Get the list of objects
	 * @return The list of objects in this level
	 */
	public ArrayList<GameObject> getObjects(){
		return objects;
	}

	/**
	 * Add an object to the list of objects in this level
	 * @param objToAdd The object you want to add
	 */
	public void addObject(GameObject objToAdd){
		objects.add(objToAdd);
	}

	/**
	 * Constructs a level
	 */
	public Level() {
		init();
	}

	/**
	 * Initializes all member variables
	 */
	public void init() {
		objects = new ArrayList<GameObject>();
	}


	/**
	 * Loads a level's contents into the current state
	 */
	public void load(){
		EngineState currentState = Engine.currentInstance.getCurrentState();
		ArrayList<GameObject> copyList = new ArrayList<GameObject>(objects);
		for(GameObject obj : copyList){
			currentState.addObj(obj);
		}
	}

	/**
	 * Optimizes a level by combining adjacent gameObjects of proper dimensions
	 */
	public void optimize(){
		//Create hashmap of XPositions to objects
		HashMap<Integer, ArrayList<GameObject>> map = new HashMap<Integer,  ArrayList<GameObject>>();

		//Create an arraylist of gameObjects which must be removed after optimization
		ArrayList<GameObject> toRemove = new ArrayList<GameObject>();

		//Fill hasmap
		for(GameObject obj : objects){
			//If an object is already mapped to this key
			if(map.containsKey((int)obj.getXPos())){
				boolean merged = false;

				//For every gameObject currently in the map at this xPositino
				for(GameObject inMap : map.get((int)obj.getXPos())){

					if(obj.getState() == null && inMap.getState() != null) continue;
					if(inMap.getState() == null && obj.getState() != null) continue;
					//If they have the same state and are both triggerable or not
					if(((obj.getState() == null && inMap.getState() == null) || obj.getState().getClass() == inMap.getState().getClass()) && obj.isTriggerable() == inMap.isTriggerable()){
						
						//Set boolean indicating if their triggers are equal
						boolean equalTriggers = true;
						
						//If they are triggerable, make sure their triggers are the same
						if(obj.isTriggerable()){
							//Get arraylist of obj's triggers
							ArrayList<Trigger> objT = obj.getTriggers();
							ArrayList<Trigger> inMapT = inMap.getTriggers();
							
							equalTriggers = objT.size() == inMapT.size();
							
							//IF still equal
							if(equalTriggers){
								for(int i = 0; i < objT.size(); i++)
								{
									if(objT.get(i).getClass() != inMapT.get(i).getClass()) equalTriggers = false;
								}
							}	
						}
						
						//If these two objects have the same state and triggers (Nothing but size prevents them from combining)
						if(equalTriggers){
							
							//If the object in the map has the same width as this object, and it's only this object/that objects height away
							if(inMap.getWidth() == obj.getWidth()){
								if(inMap.getYPos() + inMap.getHeight() == obj.getYPos()){
									//Merge the objects
									inMap.setHeight(inMap.getHeight() + obj.getHeight());
									
									inMap.updateShape();
									
									//Add obj to the list of objects to remove after optimization
									toRemove.add(obj);
									merged = true;
								}
								else if(obj.getYPos() + obj.getHeight() == inMap.getYPos()){
									//Merge bjects
									inMap.setHeight(inMap.getHeight() + obj.getHeight());
									//In this case obj is on top, and because position is from top left corner, we must raise inMap to be at obj's position
									inMap.setPos(obj.getPos());

									inMap.updateShape();
									
									toRemove.add(obj);
									merged = true;
								}
							}//Ends if same width
						}//Ends if same trigger
					}//Ends if same state

				}
				//If the object was never merged
				if(!merged){
					//Add object to arraylist in map at this objects x positon
					map.get((int)obj.getXPos()).add(obj);
				}
			}
			//Else an object is not yet mapped to his key
			else{
				//Initialize array list at key
				map.put((int)obj.getXPos(), new ArrayList<GameObject>());

				//Add this object to the map
				map.get((int)obj.getXPos()).add(obj);
			}

		}
		//Remove list of obejcts that were merged.
		objects.removeAll(toRemove);
	}
}
