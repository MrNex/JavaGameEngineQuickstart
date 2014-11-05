package loader;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import engine.Engine;
import buffer.CollisionBuffer;
import objects.GameObject;
import state.object.*;
import triggers.*;
import levels.Level;
import mathematics.Vec;

public abstract class LevelLoader extends Loader<Level> {

	public LevelLoader() {
		super("./Assets/Levels", ".png");
	}

	@Override
	public HashMap<String, Level> loadAll() {
		//Create hashmap of levels
		HashMap<String, Level> levels = new HashMap<String, Level>();
		
		//for each file in the directory which has the proper extension
		for(File f : getDirectory().listFiles(
			new FilenameFilter(){

				@Override
				public boolean accept(File arg0, String arg1) {
					return arg1.endsWith(ext);
				}
					
			}
		)){
			//Store the filename
			String fileName = f.getName();
			//Create key for hashmap
			String key = fileName.substring(0, fileName.length() - ext.length());
			
			//Declare image to load level from
			BufferedImage levelPicture;
			
			//Try to loadimage
			try {
				levelPicture = ImageIO.read(f);
			} catch(IOException e) {
				//If shit goes down, print error
				e.printStackTrace();
				System.out.println(e.getMessage());
				levelPicture = null;
				
				//Move to next file
				continue;
			}
			
			//Create level
			Level thisLevel = new Level();
			
			//Get level width and height and num pixels
			int width = levelPicture.getWidth();
			int height = levelPicture.getHeight();
			
						
			//Set variables to help build game objects out of pixels
			boolean spanningObject = false;
			int indexObjStart = 0;
			Color previousColor = Color.white;
			
			//For each row of pixels
			for(int i = 0; i < height; i++){
				//For each column of pixels
				for(int j = 0; j < width; j++){
					Color c = new Color(levelPicture.getRGB(i, j));
					
					//If the color is white
					if(c.equals(Color.white)){
						if(!previousColor.equals(Color.white)){
							//Create object
							thisLevel.addObject(makeObject(i, indexObjStart, j, previousColor));
						}
					}
					else if(!previousColor.equals(c)){
						if(!previousColor.equals(Color.white)){
							thisLevel.addObject(makeObject(i, indexObjStart, j, previousColor));
						}
						
						indexObjStart =j;
						
					}	
					previousColor = c;
				}
				if(!previousColor.equals(Color.white)){
					thisLevel.addObject(makeObject(i, indexObjStart, width, previousColor));
				}
				previousColor = Color.white;
			}
			//Optimize the level
			thisLevel.optimize();
			
			//Add level to hashmap
			levels.put(key, thisLevel);
		}
		
		return levels;
	}
	
	/**
	 * 
	 * @param pixelRow Row of image object is in
	 * @param startIndex Pixel column of image that this object color started
	 * @param pixelColumn Pixel column of image that this object color ends
	 * @param objColor The object color
	 * @return A ready-to-go gameObject:
	 */
	private GameObject makeObject(int pixelRow, int startIndex, int pixelColumn, Color objColor){
		//Get obj xPos
		double x = startIndex * 20;
		double y = pixelRow * 20;
		
		//Get dimensions
		double w = (pixelColumn - startIndex) * 20;
		double h = 20;
		
		//Create object
		GameObject obj = makeWall(x, y, w, h, new Vec(1, 0), objColor);
		
		//Add any special effects to object in game-specific implementation of level loader
		mutateObject(obj, objColor);
		
		/*
		 * This part of object creation will now be handled by the mutateObject method
		 * 
		if(objColor.equals(Color.blue)){

		}
		else if(objColor.equals(Color.green)){

		}
		else if(objColor.equals(Color.red)){

		}
		else if(objColor.equals(Color.yellow)){

		}
		else if(objColor.equals(Color.cyan)){

		}
		else if(objColor.equals(new Color(255, 165, 0, 255))){

		}
		*/
		
		return obj;
	}
	
	/**
	 * Builds a basic wall gameobject
	 * @param x position in world
	 * @param y position in world
	 * @param w width of rect to make
	 * @param h height of rect to make
	 * @param Forward vector (1, 0) if not rotated.
	 * @return A gameobject representing  wall
	 */
	private GameObject makeWall(double x, double y, double w, double h, Vec v, Color c){
		GameObject obj = new GameObject(x,y,w,h,v);
		obj.setShape(new Rectangle2D.Double());
		obj.setVisible(true);
		obj.setColor(c);
		return obj;
	}
	
	/**
	 * Defines a ruleset by which objects should be created based upon color when loaded
	 * Through LevelLoader from a png
	 * @param obj Current basic object instance (Just a wall)
	 * @param c Color of object in level image
	 */
	abstract protected void mutateObject(GameObject obj, Color c);
	

}
