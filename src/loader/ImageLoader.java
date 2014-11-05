package loader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageLoader extends Loader<BufferedImage>{

	/**
	 * Constructs a loader that loads pngs
	 */
	public ImageLoader() {
		super("./Assets/Images", ".png");
		
	}

	/**
	 * Loads all of the files with .png extension in ./Assets/Images directory
	 * Returning a hashmap with each image file as a value keyed by fileName (Not including .png or path)
	 */
	@Override
	public HashMap<String, BufferedImage> loadAll() {
		//Create hashmap of images
		HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
		
		//for each file in the directory which has the proper extension
		for(File f : getDirectory().listFiles(
			new FilenameFilter(){

				@Override
				public boolean accept(File arg0, String arg1) {
					return arg1.endsWith(ext);
				}
			}
		)){
			//Store filename
			String fileName = f.getName();
			//Create key
			String key = fileName.substring(0, fileName.length() - ext.length());
			//Declare image for hashmap
			BufferedImage val;
			
			try {
				val = ImageIO.read(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());
				val = null;
			}
			
			//If val isn't null, store it in hashmap
			if(val != null)
				images.put(key, val);
		}
		
		
		return images;
	}

}
