package state.engine;

import java.util.ArrayList;


import state.State;
import objects.*;

/**
 * Defines a state in the {@link engine.Engine}'s finite state machine.
 * @author Nex
 *
 */
public class EngineState extends State {

	//Attributes
	protected ArrayList<GameObject> objects;			//Current list of objs in gameState
	protected ArrayList<GameObject> toRemove;			//Current list of objs being removed this update loop
	protected ArrayList<GameObject> toAdd;				//Current list of objs being added this update loop
	
	/**
	 * Constructs an engine state
	 * 	Calls constructor of State
	 * 	which calls init();
	 */
	public EngineState() {
		super();
	}

	//Accessors
	/**
	 * Creates a new copy of the object list and returns it.
	 * @return A new copy of the list of objects currently in engine state.
	 */
	public ArrayList<GameObject> getObjList ()
	{
		return new ArrayList<GameObject>(objects);
	}

	/**
	 * Initializes all member variables in EngineState
	 * Member variables include:
	 * Objects - A list of gameobjects currently in this state of the engine.
	 * toAdd - A list of gameobjects we need to add before next cycle of the engine.
	 * toRemove - A list of gameobjects we need to remove before next cycle of the engine.
	 */
	@Override
	protected void init() {
		//Initialize array lists
		objects = new ArrayList<GameObject>();
		toRemove = new ArrayList<GameObject>();
		toAdd = new ArrayList<GameObject>();
	}

	/**
	 * Adds an object to the engine state
	 * @param objToAdd The object being added
	 */
	public void addObj(GameObject objToAdd){
		toAdd.add(objToAdd);
	}

	/**
	 * Removes an object from the engine state
	 * @param objToRemove The object being removed
	 */
	public void removeObj(GameObject objToRemove){
		toRemove.add(objToRemove);
	}
	
	/**
	 * Removes everything from the current state
	 */
	public void wipeState(){
		toAdd.clear();
		toRemove.clear();
		toRemove.addAll(objects);
	}


	/**
	 * Updates the engine state.
	 * Updates every object currently in the engine state
	 * Removes gameobjects which need to be removed, and clears the remove list
	 * Adds gameobjects which need to be added, and clears the add list
	 */
	@Override
	public void update() {
		//Update every gameobject in objects
		for(GameObject obj : objects)
			obj.update();

		//remove every gameobject in toRemove from objects
		for(GameObject obj : toRemove)
			objects.remove(obj);
		toRemove.clear();

		//add every game object in toAdd to objects
		for(GameObject obj : toAdd)
			objects.add(obj);
		toAdd.clear();
	}

}
