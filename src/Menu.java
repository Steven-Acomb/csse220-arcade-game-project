import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.Timer;
import javax.swing.JComponent;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Menu extends JComponent{
	private int resolutionScale;
	private JFrame menuFrame;
	private Game currentGame;
	private Timer menuTimer;
	private int currentScreenWidth;
	private int currentScreenHeight;
	private MenuListener menuListener;
	private boolean menuOpen = false;
	private boolean isFullscreen = false;
	private double hue = 0.0;
	private GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
	private Dimension screenSize;
	private Dimension scaledContentPlane;
	private Dimension scaledJFrameSize;
	private boolean showingControls;
	
	public Menu(Dimension screenSize, Dimension scaledContentPlane, Dimension scaledJFrameSize, int resolutionScale) {
		this.screenSize = screenSize;
		this.scaledContentPlane = scaledContentPlane;
		this.scaledJFrameSize = scaledJFrameSize;
		this.resolutionScale = resolutionScale;
		this.currentScreenWidth = this.scaledJFrameSize.width;
		this.currentScreenHeight = this.scaledJFrameSize.height;
		this.menuListener = new MenuListener(this);
		this.initMenuFrame();
	}

	/**
	 * ensures: sets the menu JFrame to fullscreen and resizes elements appropriately.
	 * @param args unused
	 */
	public void setFullScreen() {
		menuFrame.dispose();
		menuFrame.setSize(screenSize);
		menuFrame.setUndecorated(true);
		menuFrame.setTitle("Main Menu (Full Screen)");
		this.setIsFullscreen(true);
		this.currentScreenWidth = this.screenSize.width;
		this.currentScreenHeight = this.screenSize.height;
		device.setFullScreenWindow(menuFrame);
        menuFrame.setVisible(true);
	}
	
	/**
	 * ensures: sets the menu JFrame to windowed mode and resizes elements appropriately.
	 * @param args unused
	 */
	public void setWindowed() {
		 device.setFullScreenWindow(null);
		 menuFrame.dispose();
         menuFrame.setUndecorated(false);
         menuFrame.setResizable(false);
         menuFrame.setTitle("Main Menu");
         this.setIsFullscreen(false);
         menuFrame.setSize(scaledJFrameSize);
         this.currentScreenWidth = this.scaledJFrameSize.width;
         this.currentScreenHeight = this.scaledJFrameSize.height;
         menuFrame.setVisible(true);
	}	

	/**
	 * ensures: creates the Menu JFrame with a timer, key listener, and Game object.
	 * @param args unused
	 */
	public void initMenu() throws IllegalFileFormatException {
		this.menuListener = new MenuListener(this);
		this.currentGame = new Game(screenSize, scaledJFrameSize, this.resolutionScale, this.isFullscreen);
		this.addKeyListener(menuListener);
        this.setFocusable(true);
        this.menuTimer = new Timer(ArcadeGame.DELAY,menuListener);
		menuTimer.start();
	    menuFrame.setVisible(true);
	}
	
	/**
	 * ensures: starts this Menu's "Game" object and stops the menu.
	 * @param args unused
	 */
	public void startGame() {
		this.currentGame.start();
		this.stopMenu();
	}
	
	/**
	 * ensures: closes this Menu's JFrame and stops the timer.
	 * @param args unused
	 */
	public void stopMenu() {
		this.setFocusable(false);
		this.menuFrame.setVisible(false);
		this.menuFrame.dispose();
		this.setMenuOpen(false);
		this.menuTimer.stop();
	}
	
	/**
	 * ensures: creates and opens this Menu's JFrame.
	 * @param args unused
	 */
	public void initMenuFrame() {
		this.menuFrame = new JFrame();
		this.menuFrame.setSize(this.scaledJFrameSize);
		this.menuFrame.setResizable(false);
		this.menuFrame.setTitle("Main Menu");
		this.menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.add(this);
        this.setMenuOpen(true);
	}
	
	
	/**
	 * ensures: Draws the menu graphics to its JFrame.
	 * @param graphics is the graphics object of the JFrame this menu is a component of.
	 */
	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D graphics2 = (Graphics2D) graphics;
		
		if(this.isFullscreen()) {
			graphics2.setColor(Color.green);
			graphics2.fillRect(0, 0, this.screenSize.width, this.screenSize.height);
			
			graphics2.setColor(Color.getHSBColor((float)hue/360,(float)1.0,(float)1.0));
			graphics2.fillRect(1, 1, this.screenSize.width-2, this.screenSize.height-2);
		}
		else {
			graphics2.setColor(Color.magenta);
			graphics2.fillRect(0, 0, this.scaledContentPlane.width-0, this.scaledContentPlane.height-0);
			
			graphics2.setColor(Color.getHSBColor((float)hue/360,(float)1.0,(float)1.0));
			graphics2.fillRect(1, 1, this.scaledContentPlane.width-2-0, this.scaledContentPlane.height-2-0);
		}
		
		graphics2.setColor(Color.WHITE);
		Font font = new Font("Purisa", Font.BOLD, 60*resolutionScale);
	    FontMetrics metrics = graphics.getFontMetrics(font);
	    String gameName = "Jetman";
	    
	    //instruction text
	    Font font2 = new Font("Purisa", Font.PLAIN, 16*resolutionScale);
	    Font font3 = new Font("Purisa", Font.PLAIN, 12*resolutionScale);
	    FontMetrics metrics2 = graphics.getFontMetrics(font2);
	    FontMetrics metrics3 = graphics.getFontMetrics(font3);
	    String m1_2 = "press f to toggle fullscreen,";
	    String m1_3 = "press space to begin game,";
	    String m1_4 = "press c to see more controls.";
	    
	    //more controls text
	    String m1_9 = "A: shoot lasers";
	    String m1_10 = "F: toggle fullscreen";
	    String m1_5 = " \\ : enable/disable dev mode";
	    String m1_6 = "U: advance to the next level";
	    String m1_7 = "D: regress to previous level";
	    String m1_8 = "press c to close this dialogue.";
	    
	    if(showingControls) {
	    	graphics2.setFont(font3);
	    	graphics2.drawString(m1_9,metrics3.getHeight()/2,1*metrics3.getHeight());
	    	graphics2.drawString(m1_10,metrics3.getHeight()/2,2*metrics3.getHeight());
	    	graphics2.drawString(m1_5,metrics3.getHeight()/2,3*metrics3.getHeight());
		    graphics2.drawString(m1_6,metrics3.getHeight()/2,4*metrics3.getHeight());
		    graphics2.drawString(m1_7,metrics3.getHeight()/2,5*metrics3.getHeight());
		    graphics2.drawString(m1_8,metrics3.getHeight()/2,this.currentScreenHeight-1*metrics3.getHeight());
	    }
	    else {
	    	graphics2.setFont(font);
	    	graphics2.drawString(gameName,(this.currentScreenWidth - metrics.stringWidth(gameName))/2,metrics.getHeight());
	    	graphics2.setFont(font2);
	    	graphics2.drawString(m1_2,(this.currentScreenWidth - metrics2.stringWidth(m1_2))/2,metrics.getHeight()+2*metrics2.getHeight());
		    graphics2.drawString(m1_3,(this.currentScreenWidth - metrics2.stringWidth(m1_3))/2,metrics.getHeight()+3*metrics2.getHeight());
		    graphics2.drawString(m1_4,(this.currentScreenWidth - metrics2.stringWidth(m1_4))/2,metrics.getHeight()+4*metrics2.getHeight());
	    }
	}
	
	/**
	 * ensures: rotates the hue angle to strobe the menu title font color
	 * @param args unused
	 */
	public void update() {
		this.hue++;
		this.repaint();
	}

	public boolean isFullscreen() {
		return isFullscreen;
	}

	public void setIsFullscreen(boolean isFullscreen) {
		this.isFullscreen = isFullscreen;		
	}

	public boolean isMenuOpen() {
		return menuOpen;
	}

	public void setMenuOpen(boolean menuOpen) {
		this.menuOpen = menuOpen;
	}

	public int getCurrentScreenHeight() {
		return currentScreenHeight;
	}
	
	public int getCurrentScreenWidth() {
		return currentScreenHeight;
	}
	
	public void updateGame() {
		this.currentGame.update();
	}
	
	public boolean gameRunning() {
		return currentGame.getIsRunning();
	}	
	
	public int getResolutionScale() {
		return resolutionScale;
	}

	public void setResolutionScale(int resolutionScale) {
		this.resolutionScale = resolutionScale;
	}

	public boolean isShowingControls() {
		return showingControls;
	}

	public void setShowingControls(boolean showingControls) {
		this.showingControls = showingControls;
	}
}