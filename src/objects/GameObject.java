package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import mathematics.*;
import state.object.ObjectState;
import triggers.Trigger;

/**
 * Defines any object in the game
 * @author Nex
 *
 */
public class GameObject {

	//Attributes
	protected Vec position;
	protected Vec forward, right;
	protected double width, height;
	protected boolean visible;
	protected boolean solid;
	protected RectangularShape shape;
	protected BufferedImage image;
	protected Color color;
	protected Stack<ObjectState> stateStack;
	protected boolean triggerable;
	protected ArrayList<Trigger> triggers;

	/**
	 * Creates a basic GameObject with a position and size
	 * GameObject defaults to not running and not visible with a null state
	 * @param xx X Position in worldspace
	 * @param yy Y Position in worldspace
	 * @param w object width
	 * @param h object height
	 * @param fwd Forward vector, which direction is this gameobject facing.
	 */
	public GameObject(double xx, double yy, double w, double h, Vec fwd) {
		//Set designated attributes
		position = new Vec(xx, yy);

		//Set directional vectors
		forward = fwd;
		right = Vec.rotate(forward, Math.PI/2);


		width = w;
		height = h;
		
		//Initiaize state stack
		stateStack = new Stack<ObjectState>();

		//Set default attributes
		visible = false;

		shape = null;
		color = Color.black;

		image = null;

		triggerable = false;
		
		solid = true;
	}

	//Accessors
	/**
	 * Gets the position vector
	 * @return the position vector
	 */
	public Vec getPos(){
		return position;
	}

	/**
	 * Sets the position vector
	 * @param v New position vector
	 */
	public void setPos(Vec v){
		position = v;
	}

	/**
	 * Gets the X component of the position vector
	 * @return
	 */
	public double getXPos(){
		return position.getComponent(0);
	}

	/**
	 * Gets the Y component of the position {@link mathematics.Vec}
	 * @return the Y component of position {@link mathematics.Vec}
	 */
	public double getYPos(){
		return position.getComponent(1);
	}

	/**
	 * Gets the width of the gameobject's bounding box
	 * @return a double containing the width of the bounding box
	 */
	public double getWidth(){
		return width;
	}
	
	/**
	 * Assigns a new value to this gameObject's width
	 * If the new width is negative, this objects width is set to 0
	 * @param width The new width this object should have
	 */
	public void setWidth(double width){
		if(width <= 0) this.width = 0;
		else this.width = width;
	}

	/**
	 * Gets the height of the gameobjects bounding box
	 * @return the height of the bounding box
	 */
	public double getHeight(){
		return height;
	}
	
	/**
	 * Assigns a new value to this gameObject's Height
	 * If the new Height is negative, this objects Height is set to 0
	 * @param width The new Height this object should have
	 */
	public void setHeight(double height){
		if(height <= 0) this.height = 0;
		else this.height = height;
	}

	/**
	 * Gets a position vector representing the center of this object
	 * @return A vector holding the coordinates of the exact center of this object
	 */
	public Vec getCenter(){
		Vec centerVec = new Vec(2);
		centerVec.copy(position);
		centerVec.incrementComponent(0, width/2.0);
		centerVec.incrementComponent(1, height/2.0);
		return centerVec;
	}

	/**
	 * Gets the forward vector of the gameobject
	 * @return A vector representing the direction which this gameObject s facing
	 */
	public Vec getForward(){
		return forward;
	}

	/**
	 * Sets the forward vector of the gameObject
	 * @param v Vector representing a new forward vector.
	 */
	public void setForward(Vec v){
		forward = v;
		right = Vec.rotate(forward, Math.PI/2);
	}

	/**
	 * Gets the right vector
	 * @return The right vector.
	 */
	public Vec getRight(){
		return right;
	}

	/**
	 * pushes a {@link ObjectState} to the top
	 * Of this gameObject's stateStack
	 * @param newState State to attach to object
	 */
	public void pushState(ObjectState newState){
		//Push state onto stack
		stateStack.push(newState);

		//If not going into a null state
		if(newState != null){
			newState.setAttachedGameObject(this);
			newState.enter();
		}
	}
	
	/**
	 * Pops the current state off of the state stack.
	 * If the current state isn't null, it's exit method will be called.
	 */
	public void popState(){
		ObjectState poppedState;
		try{
			poppedState = stateStack.pop();
		}
		catch(EmptyStackException e){
			poppedState = null;
			System.out.println("No state to pop off of object.");
		}
		
		if(poppedState != null){
			poppedState.exit();
		}
	}

	/**
	 * Gets the current state of this gameObject
	 * @return The state on top of this gameObject's stateStack
	 */
	public ObjectState getCurrentState(){
		ObjectState returnState;
		try{
			returnState = stateStack.peek();
		}
		catch(EmptyStackException e){
			returnState = null;
			System.out.println("No state to get from object.");
		}
		
		return returnState;
	}

	/**
	 * Gets the visibility of this gameobject
	 * @return Whether or not the object is visible
	 */
	public boolean isVisible(){
		return visible;
	}

	/**
	 * Sets the visibility of this gameObject
	 * @param isVisible is the object visible
	 */
	public void setVisible(boolean isVisible){
		visible = isVisible;
	}

	/**
	 * Gets whethe or not this gameobject is running.
	 * @return Whether or not the gameobject is running.
	 */
	public boolean isRunning(){
		//return running;
		return getCurrentState() != null;
	}
	
	/**
	 * Gets whether or not this gameObject is solid.
	 * A solid gameObject will still be triggered upon collision, however it will not revert a movable game object
	 * to it's previous position.
	 * @return Whether this gameobject is solid
	 */
	public boolean isSolid(){
		return solid;
	}
	
	/**
	 * Sets whether or not this gameobject is solid.
	 * A solid gameObject will still be triggered upon collision, however it will not revert a movable game object
	 * to it's previous position.
	 * @param isSolid Whether this gameObject should be solid
	 */
	public void setSolid(boolean isSolid){
		solid = isSolid;
	}

	/**
	 * Sets the shape of the gameObject
	 * @param newShape The new shape
	 */
	public void setShape(RectangularShape newShape){
		//Set the shape
		shape = newShape;
		//if its not null, update it to my position
		if(shape != null){
			updateShape();
		}
	}

	/**
	 * Sets the image of the gameObject
	 * @param newImage The new image
	 */
	public void setImage(BufferedImage newImage){
		image = newImage;
	}

	/**
	 * Sets the color of the GameObject
	 * @param newColor The new color
	 */
	public void setColor(Color newColor){
		color = newColor;
	}

	/**
	 * Sets the shape and color of the gameobject
	 * @param newShape The new shape
	 * @param color The new color
	 */
	public void setShape(RectangularShape newShape, Color color)
	{
		setShape(newShape);
		setColor(color);
	}

	/**
	 * Gets whether this object is triggerable or not
	 * @return True if triggerable, false if not triggerable
	 */
	public boolean isTriggerable(){
		return triggerable;
	}

	/**
	 * Sets whether this object is triggerable or not.
	 * IF set to true the arrayList of triggers is initialized.
	 * This will clear the list of triggers.
	 * @param isTriggerable Whether or not this object should be triggerable
	 */
	public void setTriggerable(boolean isTriggerable){
		triggerable = isTriggerable;
		//If this object is triggerable, initialize its list of triggers
		if(triggerable){
			triggers = new ArrayList<Trigger>();
		}
	}

	/**
	 * Adds a trigger to this gameObject
	 * @param triggerToAdd The trigger being added
	 */
	public void addTrigger(Trigger triggerToAdd){
		triggers.add(triggerToAdd);
		triggerToAdd.setAttachedObj(this);
	}

	/**
	 * Removes a trigger from this gameObject
	 * @param triggerToRemove the trigger being removed
	 */
	public void removeTrigger(Trigger triggerToRemove){
		triggers.remove(triggerToRemove);
	}

	/**
	 * Gets the list of triggers attached to this object
	 * @return An arrayList of all triggers attached to this object
	 */
	public ArrayList<Trigger> getTriggers(){
		return new ArrayList<Trigger>(triggers);
	}

	/**
	 * Updates the current state of the gameObject if this object is running
	 */
	public void update(){
		if(isRunning()){
			getCurrentState().update();
		}
	}

	/**
	 * Draws the image of this gameObject if the object is visible
	 * IF there is no image, but the object is visible, this will 
	 * draw the shape of the gameobject.
	 * 
	 * If the gameobject is visible AND it is running, the current state's drawEffects method will also be called.
	 * @param g2d Graphics object to draw with
	 */
	public void draw(Graphics2D g2d){		

		//Save affine transformation
		AffineTransform savedState = new AffineTransform(g2d.getTransform());

		//Construct the local system
		AffineTransform localCoordinateSystem = constructLocalSystem(g2d.getTransform());

		//Set the affine transformation
		g2d.setTransform(localCoordinateSystem);

		if(visible){
			
			//If they have an image
			if(image != null){				
				//Note to self: drawImage does not use a position, width and height. Instead it uses a top left corner position and a bottom right corner position.
				g2d.drawImage(image, (int)(-1*(width/2)), (int)(-1*(height/2)), (int)width/2, (int)height/2, 0, 0, image.getWidth(), image.getHeight(), null);
			}
			else if(shape != null){
				//Set the color
				g2d.setColor(color);
				//Fill the shape
				g2d.fill(shape);
			}

			//If this obj is running
			if(isRunning()){
				//Draw it's state
				getCurrentState().drawEffects(g2d);
			}
		}

		//Revert back to saved coordinate system.
		g2d.setTransform(savedState);
	}

	/**
	 * Uses this objects position and forward vector
	 * To construct an affine transformation that, if applied, will set the current
	 * coordinate system to be centered on this gameObject and rotated in the direction of this gameObject's
	 * forward vector, while still translated to the position of the camera
	 * @param currentSystem An affineTransformation representing the current transformation stack on the renderer
	 * @return An affine transform representing this gameObject's current coordinate system.
	 */
	private AffineTransform constructLocalSystem(AffineTransform currentSystem){
		//Construct affine transformation
		AffineTransform transform = currentSystem;

		//translation the affine transformation to the center position of where this gameObject should be
		transform.translate(position.getComponent(0) + (width / 2), position.getComponent(1) + (height / 2));

		//Get angle of rotation
		double angle = forward.getAngle();
		//Rotate the affine transformation by the angle that this gameObject is rotated
		transform.rotate(angle);
		
		return transform;
	}

	/**
	 * Updates the shape to have it's center at 0, 0
	 * while adhering to this gameObject's width and height.
	 * 
	 * This method no longer positions the shape, instead it simply centers the object on the origin
	 * So that when constructLocalSystem() is called the object is translated and rotated to it's correct position.
	 */
	public void updateShape(){
		if(shape != null){
			//shape.setFrame(position.getComponent(0), position.getComponent(1), width, height);
			shape.setFrame(-width / 2, -height / 2, width, height);
		}
	}


	/**
	 * Checks if the bounding box of this obj is intersecting the bounding box of another obj
	 * @param obj GameObject to check with
	 * @return Whether this gameobject is intersecting with obj
	 */
	public boolean isColliding(GameObject obj){

		//If the left side of this is to the left  right side of obj and the right side of this is to the right of the left side of obj
		if(position.getComponent(0) < obj.position.getComponent(0) + obj.width && this.position.getComponent(0) + this.width > obj.position.getComponent(0)){

			//IF the top of this is higher than the bottom of obj and the bottom of this is further down than the top of obj
			if(position.getComponent(1) < obj.position.getComponent(1) + obj.height && this.position.getComponent(1) + this.height > obj.position.getComponent(1)){
				return true;
			}	
		}
		return false;
	}

	/**
	 * Checks whether or not the bounding box surrounding the shape contains a given point
	 * @param xx X position of the point
	 * @param yy Y Position of the point
	 * @return boolean indicating whether the point lies within this gameobject
	 */
	public boolean contains(double xx, double yy){
		return 
				xx < position.getComponent(0) + width && 
				xx > position.getComponent(0) && 
				yy < position.getComponent(1) + height && 
				yy > position.getComponent(1); 
	}

}
