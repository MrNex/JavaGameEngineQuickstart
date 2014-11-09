package engine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EmptyStackException;
import java.util.Stack;

import javax.swing.Timer;

import mathematics.Vec;
import objects.GameObject;
import engine.manager.*;
import state.*;
import state.engine.*;



/**
 * Engine which runs on finite state machine.
 * Uses EngineStates
 * @author Nex
 *
 */
public class Engine {

	//Static variables
	public static Engine currentInstance;

	//Enums
	public enum Managers{
		INPUTMANAGER, COLLISIONMANAGER, CONTENTMANAGER, CAMERAMANAGER, SCREENMANAGER, SPRITEMANAGER
	}

	//Attributes
	private boolean running;
	private Stack<EngineState> stateStack;
	private Manager[] managers;
	private Timer drawTimer;

	
	//Accessors / Modifiers
	/**
	 * Pushes a new state onto the state stack
	 * @param newState State to push as new current state
	 */
	public void pushState(EngineState newState){
		stateStack.push(newState);
	}
	
	/**
	 * Pops the current state off of the stack if there is one
	 */
	public void popState(){
		try{
			stateStack.pop();
		}
		catch(EmptyStackException e){
			System.out.println("No state to pop from engine state stack.");
		}
	}
	
	/**
	 * Gets the current state from the state stack
	 * @return The state on top of the state stack
	 */
	public EngineState getCurrentState(){
		
		EngineState returnState;
		try{
			returnState = stateStack.peek();
		}
		catch(EmptyStackException e){
			returnState = null;
			System.out.println("No state to get from engine.");
		}
		
		return returnState;
	}
	
	
	/**
	 * Constructs an engine
	 */
	public Engine() {
		init();
	}

	/**
	 * Initializes engine members
	 */
	public void init()
	{	
		//Set this as current instance of engine
		currentInstance = this;

		//Set internal variables
		running = false;

		//Initialize state stack
		stateStack = new Stack<EngineState>();

		//Create managers
		managers = new Manager[6];

		//Create input manager
		managers[Managers.INPUTMANAGER.ordinal()] = new InputManager();
		//Creates collision manager
		managers[Managers.COLLISIONMANAGER.ordinal()] = new CollisionManager();
		//Creates content manager, uses ImageLoader, SpriteLoader, and LevelLoader to load content during initialization
		managers[Managers.CONTENTMANAGER.ordinal()] = new ContentManager();
		//Create the camera manager
		managers[Managers.CAMERAMANAGER.ordinal()] = new CameraManager();
		//Creates the screen manager, hooking up input manager to the game window.
		managers[Managers.SCREENMANAGER.ordinal()] = new ScreenManager();
		//Creates the Sprite Manager
		managers[Managers.SPRITEMANAGER.ordinal()] = new SpriteManager();


		//Create the current state
		pushState(new EngineState());
		
		
		//Create objects!
		//((GameState)currentState).loadNextLevel();
		
		//Create timer for screen manager
		drawTimer = new Timer(1000/60, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Update camera
				managers[Engine.Managers.CAMERAMANAGER.ordinal()].update();
				//Update screen
				managers[Engine.Managers.SCREENMANAGER.ordinal()].update();
			}

		});

		drawTimer.setRepeats(true);

	}

	/**
	 * Begins running the engine
	 */
	public void start()
	{		
		//Set running to true
		running = true;
		//Begin drawloop
		drawTimer.start();
		//Run
		run();
	}
	

	/**
	 * This is the run loop for the engine.
	 * Constantly updates the current state of the engine.
	 */
	private void run()
	{
		while(running)
		{
			//Update managers
			managers[Managers.INPUTMANAGER.ordinal()].update();

			//TODO: Offload to statemanager to keep track of stateStack
			getCurrentState().update();
			managers[Managers.SPRITEMANAGER.ordinal()].update();
			//After objects update, update collisions.
			managers[Managers.COLLISIONMANAGER.ordinal()].update();
		}
	}


	/**
	 * Gets an engine components manager.
	 * Index values are as follows:
	 * 0 - Input Manager
	 * 1 - Screen Manager
	 * @param index Index of the component manager needed
	 * @return The desired component manager
	 */
	public Manager getManager(Managers manager){
		return managers[manager.ordinal()];
	}

}
