package engine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

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
		INPUTMANAGER, COLLISIONMANAGER, CONTENTMANAGER, CAMERAMANAGER, SCREENMANAGER
	}

	//Attributes
	private boolean running;
	private EngineState currentState;
	private Manager[] managers;
	private Timer drawTimer;

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


		//Create managers
		managers = new Manager[5];

		//Create input manager
		managers[Managers.INPUTMANAGER.ordinal()] = new InputManager();
		//Creates collision manager
		managers[Managers.COLLISIONMANAGER.ordinal()] = new CollisionManager();
		//Creates content manager, uses ImageLoader and LevelLoader to load content during initialization
		managers[Managers.CONTENTMANAGER.ordinal()] = new ContentManager();
		//Create the camera manager
		managers[Managers.CAMERAMANAGER.ordinal()] = new CameraManager();
		//Creates the screen manager, hooking up input manager to the game window.
		managers[Managers.SCREENMANAGER.ordinal()] = new ScreenManager();


		//Create the current state
		currentState = new EngineState();
		
		
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
			currentState.update();

			//After objects update, update collisions.
			managers[Managers.COLLISIONMANAGER.ordinal()].update();
		}
	}

	/**
	 * Gets the current state of the engine
	 * @return The current state of the engine
	 */
	public EngineState getCurrentState()
	{
		return currentState;
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
