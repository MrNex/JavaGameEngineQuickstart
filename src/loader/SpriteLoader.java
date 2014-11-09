package loader;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import sprites.Sprite;


/**
 * SpriteLoader defines a class which converts BufferedImages
 * To Sprites by loading their respective Sprite blueprints and defining
 * each animation / image to spec.
 * @author Nex
 *
 */
public class SpriteLoader extends Loader<Sprite>{

	/**
	 * Constructs a spriteLoader
	 * Spriteloaders load from the Assets/Animation directory
	 * SpriteLoaders only load .sbp files.
	 */
	public SpriteLoader() {
		super("./Assets/Animation", ".sbp");
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * Creates an {@link ImageLoader} to load all images to a map
	 * Creates a new map of sprites
	 * Gets the array of valid files from the load directory
	 * Gets the contents of each line in each file in an arraylist
	 * Sends the contents of the blueprint, along with the respective {@link BufferedImage}
	 * to the ParseSprite function
	 * Puts the value returned by ParseSprite in a SpriteMap keyed by it's filename (excluding extension)
	 */
	@Override
	public HashMap<String, Sprite> loadAll() {
		//First get a hashmap of all images
		ImageLoader loader = new ImageLoader();
		HashMap<String, BufferedImage> imageMap = loader.loadAll();
		
		//Create a hashmap of sprites
		HashMap<String, Sprite> spriteMap = new HashMap<String, Sprite>();
		
		//Declare a bufferedReader to read file from
		BufferedReader inputStream = null;
		
		//Create an arraylist of lines in each file
		ArrayList<String> lines = new ArrayList<String>();
		
		//For each file in the array of valid files for this loader
		for(File f : getValidFiles()){
			//Clear the list of lines from previous file
			lines.clear();
			String imgKey;
			
			//Point the buffered reader to the file
			try {
				inputStream = new BufferedReader(new FileReader(f));
				
				//First store the image key this file is defining as a sprite
				String imgName = f.getName();
				//imgKey is the name without the file extension
				imgKey = imgName.substring(0, imgName.length() - ext.length());
				
				//Read the next line
				String line;
				while((line = inputStream.readLine()) != null){
					//Add it to the arraylist of lines
					lines.add(line);
				}
				
				//Close the input stream
				inputStream.close();
				
			} catch (FileNotFoundException e) {
				// If opening the stream breaks, print stack trace, file that caused
				//It to break and the exception message.
				e.printStackTrace();
				System.out.println("Failed to load sbp:" + f.getName() + ", the file was not found.");
				System.out.println(e.getMessage());
				//Then continue looping through files
				continue;
			} catch (IOException e) {
				// If opening the stream breaks, print stack trace, file that caused
				//It to break and the exception message.
				e.printStackTrace();
				System.out.println("Failed to load sbp:" + f.getName() + ", the file contains unexpected formatting.");
				System.out.println(e.getMessage());
				//Then continue looping through files
				continue;
			} 
			
			//Once all the lines have been read we must parse them and create a sprite
			Sprite s = parseBlueprint(imageMap.get(imgKey), lines);
			
			//Add sprite to the map
			spriteMap.put(imgKey, s);
		}
		
		
		return spriteMap;
	}
	
	
	/**
	 * Constructs a sprite from the loaded lines of a sbp (spriteBlueprint) file
	 * @param imageFile The image which contains the spriteSheet
	 * @param blueprintContents An arraylist of the lines of text inside of an sbp file
	 * @return A sprite built to the specifications of the blueprint contents sent
	 */
	Sprite parseBlueprint(BufferedImage imageFile, ArrayList<String> blueprintContents){
		//First line of blueprint content contains frame width and height
		String[] dimensions = blueprintContents.get(0).split(" ", 2);
		int frameWidth = Integer.parseInt(dimensions[0]);
		int frameHeight = Integer.parseInt(dimensions[1]);
		
		//Second line contains the number of rows this sheet has
		int numRows = Integer.parseInt(blueprintContents.get(1));
		
		//Every proceeding line contains the number of columns that each row has
		int[] numColumns = new int[numRows];
		
		for(int i = 0; i < numRows; i++){
			numColumns[i] = Integer.parseInt(blueprintContents.get(i + 2));
		}
		
		//Create and return sprite
		return new Sprite(imageFile, numRows, numColumns, frameWidth, frameHeight);
	}

}
