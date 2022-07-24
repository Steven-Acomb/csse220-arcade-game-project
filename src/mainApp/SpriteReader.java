package mainApp;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteReader {

	private String folderName;
	private File file;
	private BufferedImage sprite;
	/**
	 * Initializes the reader with a specific folder and file of the image we are reading
	 * @param folderName
	 */
	
	public SpriteReader(String folderName) {
		this.folderName = folderName;
	}
	/** Initializes the sprite from the file provided in the constructor
	 * 
	 * @return BufferedImage sprite
	 */
	public BufferedImage initSprite(String imageName) {
		
		try {
			String pathToFile = folderName + imageName + ".png";
			this.file = new File(pathToFile);
			//System.out.println(this.file.getAbsolutePath());
			BufferedImage image = ImageIO.read(file);
			this.sprite = image;
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		return sprite;
		
	}
	/**Used to change the active sprite. Sets the old folder and file names
	 * to the new ones taken in, but if the fields are Null then the old values are used instead
	 * 
	 * 
	 * @param folderName of new image, if same set null
	 * @param imageName of new image, if same set null
	 * @return New sprite image from new address
	 */
	public BufferedImage updateSprite(String folderName, String imageName) {
		if (folderName != null ) {
		this.folderName = folderName;
		}
		if (imageName != null) {
		initSprite(imageName);
		}
		return initSprite(imageName);
	}
	/**
	 * Forces the sprite to change with a a new BufferedImage instead of reading a file
	 * @param sprite
	 */
	
	public void updateSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}
	
}
