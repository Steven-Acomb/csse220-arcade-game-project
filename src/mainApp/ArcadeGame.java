package mainApp;

import javax.swing.JFrame;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Toolkit;

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
	
	private Dimension screenSize;
	private Dimension scaledContentPlane;
	private Dimension scaledJFrameSize;
	private int resolutionScale;
	private Insets insets;
	private Menu mainMenu;
	
	public ArcadeGame() {
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.insets = dummyInsets();
		
		int maxContentWidth = screenSize.width-(insets.left+insets.right);
		int maxContentHeight = screenSize.height-(insets.top+insets.bottom);
		this.resolutionScale = Math.min(maxContentWidth/ZX_SPECTRUM_X_RESOLUTION,maxContentHeight/ZX_SPECTRUM_Y_RESOLUTION);
		
		int scaledContentWidth = ZX_SPECTRUM_X_RESOLUTION*this.resolutionScale;
		int scaledContentHeight = ZX_SPECTRUM_Y_RESOLUTION*this.resolutionScale;
		this.scaledContentPlane = new Dimension(scaledContentWidth,scaledContentHeight);
		
		int scaledScreenWidth = ZX_SPECTRUM_X_RESOLUTION*this.resolutionScale + insets.left + insets.right + 0;
		int scaledScreenHeight = ZX_SPECTRUM_Y_RESOLUTION*this.resolutionScale + insets.top + insets.bottom + 0;
		this.scaledJFrameSize = new Dimension(scaledScreenWidth,scaledScreenHeight);
		
	}
	
	/**
	 * ensures: runs the application
	 * @param args unused
	 * @throws IllegalFileFormatException 
	 */
	public void runApp() throws IllegalFileFormatException {
		this.mainMenu = new Menu(this.screenSize,scaledContentPlane,this.scaledJFrameSize, this.resolutionScale);
		this.mainMenu.initMenu();
	}
	
	/**
	 * ensures: returns the default system insets for a full screen decorated window, regardless of host system. 
	 * @param args unused 
	 */
	public Insets dummyInsets() {
		JFrame test = new JFrame();
        test.setSize(this.screenSize);
        test.setTitle("Test");
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.pack();
        Insets insets = test.getInsets();
        test.dispose();
        return insets;
	}

	/**
	 * ensures: runs the application
	 * @param args unused
	 * @throws IllegalFileFormatException 
	 */
	public static void main(String[] args) throws IllegalFileFormatException {
 		ArcadeGame arcadeGame = new ArcadeGame();
		arcadeGame.runApp();
	} // main

}