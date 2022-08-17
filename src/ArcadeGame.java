import javax.swing.JFrame;

import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Toolkit;

import java.io.File;

/**
 * Class: ArcadeGame
 * @author S22_A_M01
 * <br>Purpose: Top level class for CSSE220 Project containing main method 
 * <br>Restrictions: None
 */
public class ArcadeGame {
	public static final int ZX_SPECTRUM_X_RESOLUTION = 256;
	public static final int ZX_SPECTRUM_Y_RESOLUTION = 192;
	public static final int DELAY = 10;

	public static final File f = new File(ArcadeGame.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	
	private Toolkit toolkit;
	private Dimension screenSize;
	private Dimension scaledContentPlane;
	private Dimension scaledJFrameSize;
	private int resolutionScale;
	private Insets insets;
	private Menu mainMenu;
	
	/**
	 * ensures: creates the application with automatically determined graphics settings
	 */
	public ArcadeGame() {
		this.toolkit = Toolkit.getDefaultToolkit();
		this.screenSize = toolkit.getScreenSize();
		this.insets = dummyInsets(screenSize);
		this.resolutionScale = this.determineResolutionScale(insets, screenSize, ZX_SPECTRUM_X_RESOLUTION, ZX_SPECTRUM_Y_RESOLUTION);
		this.scaledContentPlane = determineScaledContentPlane(ZX_SPECTRUM_X_RESOLUTION, ZX_SPECTRUM_Y_RESOLUTION, resolutionScale);
		this.scaledJFrameSize = this.determineScaledJFrameSize(insets, ZX_SPECTRUM_X_RESOLUTION, ZX_SPECTRUM_Y_RESOLUTION, resolutionScale);		
	}	

	/**
	 * ensures: returns the default system insets for a full screen decorated window, regardless of host system.
	 */
	public Insets dummyInsets() {
		return dummyInsets(Toolkit.getDefaultToolkit().getScreenSize());
	}

	/**
	 * ensures: returns the default system insets for a full screen decorated window, regardless of host system. 
	 * @param screenSize is the screen size returned from Toolkit.getDefaultToolkit().getScreenSize();
	 */
	public Insets dummyInsets(Dimension screenSize) {
		JFrame test = new JFrame();
        test.setSize(screenSize);
        test.setTitle("Test");
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.pack();
        Insets insets = test.getInsets();
        test.dispose();
        return insets;
	}

	/**
	 * ensures: returns the number of pixels wide each "pixel" of the original application
	 * will be at the maximum integer multiple of the original resolution which the screen can display
	 * @param insets contains the dimensions of the window dressing elements on each edge of the window
	 * @param screenSize is the size of the screen displaying the window
	 * @param originalXResolution is the width of the original application in pixels
	 * @param originalYResolution is the height of the original application in pixels
	 */
	private int determineResolutionScale(Insets insets, Dimension screenSize, int originalXResolution, int originalYResolution){
		int maxContentWidth = screenSize.width-(insets.left+insets.right);
		int maxContentHeight = screenSize.height-(insets.top+insets.bottom);
		return Math.min(maxContentWidth/originalXResolution,maxContentHeight/originalYResolution);
	}

	/**
	 * ensures: returns the size of the window which wll actually display game content to the player
	 * @param originalXResolution is the width of the original application in pixels
	 * @param originalYResolution is the height of the original application in pixels
	 * @param resolutionScale is the multiple the original resolution is being scaled by
	 */
	private Dimension determineScaledContentPlane(int originalXResolution, int originalYResolution, int resolutionScale){
		int scaledContentWidth = originalXResolution*resolutionScale;
		int scaledContentHeight = originalYResolution*resolutionScale;
		return new Dimension(scaledContentWidth,scaledContentHeight);
	}

	/**
	 * ensures: returns the size of the entire window on the system running the game, including window dressing like insets
	 * @param insets contains the dimensions of the window dressing elements on each edge of the window
	 * @param originalXResolution is the width of the original application in pixels
	 * @param originalYResolution is the height of the original application in pixels
	 * @param resolutionScale is the multiple the original resolution is being scaled by
	 */
	private Dimension determineScaledJFrameSize(Insets insets, int originalXResolution, int originalYResolution, int resolutionScale){
		int scaledScreenWidth = originalXResolution*resolutionScale + insets.left + insets.right;
		int scaledScreenHeight = originalYResolution*resolutionScale + insets.top + insets.bottom;
		return new Dimension(scaledScreenWidth,scaledScreenHeight);
	}

	/**
	 * ensures: runs the application
	 * @throws IllegalFileFormatException if the level being loaded by the player is formatted incorrectly
	 */
	public void runApp() throws IllegalFileFormatException {
		this.mainMenu = new Menu(this.screenSize,scaledContentPlane,this.scaledJFrameSize, this.resolutionScale);
		this.mainMenu.initMenu();
	}

	/**
	 * ensures: creates and runs the application
	 * @param args unused
	 * @throws IllegalFileFormatException if the level being loaded by the player is formatted incorrectly
	 */
	public static void main(String[] args) throws IllegalFileFormatException {
 		ArcadeGame arcadeGame = new ArcadeGame();
		System.out.println(f.getAbsolutePath());
		arcadeGame.runApp();
	}

}