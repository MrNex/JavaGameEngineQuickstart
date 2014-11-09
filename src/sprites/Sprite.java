package sprites;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

import engine.Engine;
import engine.manager.SpriteManager;

/**
 * Class defines a (Possibly animated) image of a gameObject
 * Sprites are drawn from spriteSheets which can have multiple rows of animations
 * Each column is a frame of the row that contains it
 * Each row in itself is it's own animation
 * @author Nex
 *
 */
public class Sprite {

	//Attributes
	private BufferedImage spriteSheet;
	private Queue <AnimationInstruction> animationQueue;
	private int numRows;
	private int[] numColumns;
	private int currentRow, currentColumn;
	private int frameWidth, frameHeight;
	private int frameXPos, frameYPos;
	boolean repeating;
	
	
	/**
	 * Constructs a Sprite
	 * @param spriteSheet Image to draw frames from
	 * @param numRows Number of rows of animations
	 * @param numColumns Number of columns / frames in each row / animation
	 * @param frameWidth Width of each frame
	 * @param frameHeight Height of each frame
	 */
	public Sprite(BufferedImage spriteSheet, int numRows, int[] numColumns, int frameWidth, int frameHeight) {
		this.spriteSheet = spriteSheet;
		this.numRows = numRows;
		this.numColumns = numColumns;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		currentRow = 0;
		currentColumn = 0;
		repeating = false;
		
		animationQueue = new LinkedList<AnimationInstruction>();
		
		setFrame();
	}
	
	/**
	 * Sets the position of the frame based on the current
	 * Row and column of spritesheet being drawn
	 */
	private void setFrame(){
		frameXPos = currentColumn * frameWidth;
		frameYPos = currentRow * frameHeight;
	}
	
	
	/**
	 * Queues an animation to be played
	 * 
	 * Creates a set of {@link AnimationInstruction}s to be added to the animationQueue
	 * And enqueues the set of instructions
	 * 
	 * @param row The row of the animation to play
	 * @param repeat Should this animation repeat so long as no others are queued
	 */
	public void queueAnimation(int row, boolean repeat){
		//Create instruction set
		animationQueue.add(new AnimationInstruction(row, repeat));
	}
	
	
	/**
	 * Updates the sprite
	 * 
	 * Get the change in frames since last update from {@link SpriteManager}
	 * Increment currentColumn by change in frames
	 * If the final column of this animation has already been reached and there is something in the animationQueue
	 * 		Start next set of {@link AnimationInstruction}s in animationQueue
	 * If there is nothing in the animationQueue, and this animation is set to repeat
	 * 		Back to column 1 of current animation
	 * If this animation is NOT set to repeat and there is nothing left in the animationQueue
	 * 		Do nothing. Keep displaying the current frame
	 * 
	 * Finally, call setFrame to move frame to (possibly) new image
	 */
	public void update(){
		//Get reference to sprite manager
		SpriteManager manager = (SpriteManager)Engine.currentInstance.getManager(Engine.Managers.SPRITEMANAGER);
		
		//Get number of frames that passed
		double exactDF = manager.getDeltaFrames();
		if(exactDF >= 1){
			int dF = 0;

			dF = (int) Math.floor(exactDF);
			manager.flagDeltaFramesReset();
			
			//If the current column is the last column
			if(currentColumn == numColumns[currentRow] - 1){
				//If there is another set of drawing instructions ready
				if(animationQueue.size() > 0){				
					//Dequeue next set of instructions
					AnimationInstruction instructionSet = animationQueue.poll();
					//Load next animation
					currentRow = instructionSet.animationRowIndex;
					repeating = instructionSet.repeatAnimation;
					
					//Set column to 0 to start animation from beginning
					currentColumn = 0;
				}
				//Else there is nothing queued, if the current animation is set to repeat
				else if(repeating){
					//Set column to 0 to start animation from beginning
					currentColumn = 0;
				}
			}
			//Else not at last column of animation yet
			else{
				//If the current column + the change in frames is still not greater than the last column
				if(currentColumn + dF < numColumns[currentRow]){
					//Increment the current column by the number of frame changes
					currentColumn += dF;
				}
				//Else, the current column + the changes in frames results in an out of bounds frame
				else{
					//Set current column to last column
					currentColumn = numColumns[currentRow] - 1;
				}
			}
			
			//Finally, once frame variables are ready, set the frame
			setFrame();
			
			System.out.println(currentColumn);		
		}
		
	}
	
	/**
	 * Draws the current frame of this sprite
	 * @param g2d Reference to renderer to draw sprite with
	 */
	public void draw(Graphics2D g2d, int xPos, int yPos, int width, int height){
		//Draw the spriteSheet
		g2d.drawImage(spriteSheet,
				xPos, yPos, xPos + width, yPos + height, 
				frameXPos, frameYPos, frameXPos + frameWidth, frameYPos + frameHeight, null);
	}

}
