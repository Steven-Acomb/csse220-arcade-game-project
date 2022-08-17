import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class SpriteReader {

	private BufferedImage sprite;

	public SpriteReader(){};

	/** Initializes the sprite from the file provided in the constructor
	 * @return BufferedImage sprite
	 */
	public BufferedImage initSprite(String imageName) {
		
		InputStream imageStream = this.getClass().getClassLoader().getResourceAsStream(imageName + ".png");
		BufferedImage image;
		try {
			image = ImageIO.read(imageStream);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		this.sprite = image;
		return sprite;
		
	}

	/**
	 * Forces the sprite to change with a a new BufferedImage instead of reading a file
	 * @param sprite
	 */
	
	public void updateSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}
	
}
