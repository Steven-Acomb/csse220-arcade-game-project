import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.lang.System;
import javax.swing.JFrame;

public class Game {
	private GameComponent currentGame;
	private int width;
	private int height;
	private int resolutionScale;
	private JFrame gameFrame;
    
    private boolean isPaused = false;
    private boolean isRunning = false;
    private GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
    private GameListener gameListener;
    private ArrayList<GameObject> gameObjects;
    
    private int frame = 0;
    private long startTime;
    private long lastTime;
    private double averageFPS;
    private double currentFPS;
    private Random rand = new Random();
    private int currentScore;
    
    private boolean isFullscreen;
    private int currentScreenWidth;
    private int currentScreenHeight;
    private Dimension screenSize;
    private Dimension scaledJFrameSize;
    
    private boolean inDeveloperMode;
    private Runtime runtime = Runtime.getRuntime();
    private long memory;
    
    class GameTickListener implements ActionListener{
    	private Game game;
    	public GameTickListener(Game game) {
    		this.game = game;
    	}
    	@Override
		public void actionPerformed(ActionEvent e) {
    		if(e.getActionCommand().equals(GameListener.TICK_ACTION_COMMAND)) {
    			game.update();
    		}
    		else if(e.getActionCommand().equals(GameListener.PAUSE_ACTION_COMMAND)) {
//    			System.out.println("Game paused.");
    		}
    		else if(e.getActionCommand().equals(GameListener.UNPAUSE_ACTION_COMMAND)) {
//    			System.out.println("Game unpaused.");
    		}
    		else if(e.getActionCommand().equals(GameListener.EXIT_ACTION_COMMAND)) {
    			game.exit();
    		}
    		else {
    			System.out.println("ERROR: ActionCommand " + e.getActionCommand() + " is not defined for this application.");
    		}
		}
    }
    
    public Game(Dimension screenSize, Dimension scaledJFrameSize, int resolutionScale, boolean isFullscreen) throws IllegalFileFormatException {
    	this.resolutionScale = resolutionScale;
    	this.screenSize = screenSize;
    	this.scaledJFrameSize = scaledJFrameSize;
    	this.isFullscreen = isFullscreen;
    	if(isFullscreen) {
    		this.width = scaledJFrameSize.width;
        	this.height = scaledJFrameSize.height;
    	}
    	else {
    		this.width = screenSize.width;
        	this.height = screenSize.height;
    	}
    	this.inDeveloperMode = false;
    	ActionListener gameTickListener = new GameTickListener(this);
    	this.gameListener = new GameListener(ArcadeGame.DELAY,gameTickListener);
    	this.gameListener.setPauseKey(KeyEvent.VK_P);
    	this.gameListener.setExitKey(KeyEvent.VK_X);
    	this.gameObjects = new ArrayList<GameObject>();
    	this.currentGame = new GameComponent(resolutionScale, this.gameObjects,this.isFullscreen, screenSize, scaledJFrameSize);
    }
    
    /**
	 * ensures: creates the game JFrame with a key listener and a timer, then opens it.
	 * @param args unused
	 */
	public void start() {
		this.setCurrentScore(0);
		this.setIsRunning(true);
    	this.gameFrame = new JFrame();
    	this.gameFrame.setSize(this.width,this.height);
    	this.gameFrame.setResizable(false);
    	this.gameFrame.setTitle("Jetpac");
		this.gameFrame.add(this.currentGame);
		this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.toggleFullscreen();
		this.toggleFullscreen();
		this.currentGame.addKeyListener(gameListener);
		this.startTime = System.currentTimeMillis();
		this.lastTime = this.startTime;
		this.gameListener.startTimer();
		this.gameFrame.setVisible(true);
		this.currentGame.setFocusable(true);
		try {
			this.currentGame.readLayout();
		} catch (IllegalFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
  	
  	/**
	 * ensures: closes the game JFrame and stops its timer
	 * @param args unused
	 */
  	public void exit() {
  		this.setIsRunning(false);
  		this.gameFrame.setVisible(false);
  		this.gameFrame.dispose();
  	}
  	
  	/**
	 * ensures: handles the logic of key presses and updates graphics components.
	 * @param args unused
	 */
  	public void update() {
  		this.checkForWin();
		this.handlePlayerInput();
  		this.spawnChildObjects();
  		this.calculateNextPositions();
  		this.checkForCollisions();
  		this.handleRandomSpawns();
		this.updateGameObjects();
		this.updateGraphics();
		this.checkForGameOver();
		this.frame++;
		this.memory = runtime.totalMemory() - runtime.freeMemory();
		this.calculateFPS();
	}
  	
  	public void checkForWin() {
  		if(currentGame.getPlayer().isInShip() && !currentGame.getRocket().isClear())
  			this.currentGame.rocket.setReady(true);
  		if(currentGame.getRocket().isClear())
  			this.levelUp();
  	}
  	
  	public void calculateFPS() {
  		double window = 100;
  		if((this.frame)%window == 0) {
  			long nextTime = System.currentTimeMillis();
  			double elapsedTime = (((double)(nextTime - lastTime))/(1000.0));
  			this.currentFPS = ((window)/(elapsedTime));
  			this.lastTime = nextTime;
  			double totalTime = (((double)(nextTime - startTime))/(1000.0));
  			this.averageFPS = ((this.frame)/(totalTime));
  		}
  	}
  	
  	/**
	 * ensures: Enacts the appropriate behavior based on currently held down keys.
	 * @param args unused
	 */
  	public void handlePlayerInput() {
  		//handle player movement
  		if(gameListener.isHeld(KeyEvent.VK_RIGHT))
			currentGame.getPlayer().moveX(1);
		else if(gameListener.isHeld(KeyEvent.VK_LEFT))
			currentGame.getPlayer().moveX(-1);
		else
			currentGame.getPlayer().moveX(0);
		if(gameListener.isHeld(KeyEvent.VK_UP))
			currentGame.getPlayer().moveY(-1);
		else
			currentGame.getPlayer().moveY(1);
		
		//shoot
		if(gameListener.isHeld(KeyEvent.VK_A))
			this.currentGame.getPlayer().shoot();
//			this.demoShooting();
		
		//advance level
		if(gameListener.wasTapped(KeyEvent.VK_U))
			this.levelUp();
		
		//regress level
		if(gameListener.wasTapped(KeyEvent.VK_D))
			this.levelDown();
		
		//toggle fullscreen
		if(gameListener.wasTapped(KeyEvent.VK_F))
			this.toggleFullscreen();
		
		if(gameListener.wasTapped(KeyEvent.VK_BACK_SLASH))
			this.toggleDeveloperMode();
		
		gameListener.resetTaps();
  	}

	public void spawnChildObjects() {
		ArrayList<GameObject> allChildren = new ArrayList<GameObject>();
		for(GameObject obj : gameObjects) {
			ArrayList<GameObject> children = obj.getChildObjects();
			for(GameObject child : children)
				allChildren.add(child);
			obj.setChildObjects(new ArrayList<GameObject>());
		}
		for(GameObject newChild : allChildren)
			gameObjects.add(newChild);
	}
  	
  	/**
	 * ensures: Runs the respective collideWith method of any GameObjects which would visually overlap if drawn.
	 * @param args unused
	 */
  	public void checkForCollisions() {
		for(GameObject obj1 : gameObjects) {
			for(GameObject obj2 : gameObjects) {
				if((obj1 != obj2)&&(obj1.overlapsWith(obj2)))
						 obj1.collideWith(obj2);
			}
  		}
  	}
  	
  	/**
	 * ensures: Runs the respective move method of all GameObjects.
	 * @param args unused
	 */
  	public void calculateNextPositions() {
  		for(GameObject obj : gameObjects)
  			obj.move();
  	}
  	
  	
  	public void handleRandomSpawns(){
  		if(rand.nextInt(720)==0)
  			gameObjects.add(new Gemstone());
  		if(rand.nextInt(256)==0)
  			gameObjects.add(new ProbeAlien());
  		if(rand.nextInt(64)==0)
  			gameObjects.add(new MeteorAlien());
  	}
  	
  	/**
	 * ensures: Runs the respective update method of all GameObjects.
	 * @param args unused
	 */
  	public void updateGameObjects() {
  		ArrayList<Integer> indecesForRemoval = new ArrayList<Integer>();
  		for(int i = 0; i<this.gameObjects.size();i++) {
  			this.gameObjects.get(i).update();
  			if(this.gameObjects.get(i).isToBeDeleted()) {
  				indecesForRemoval.add(i);
  				if(this.gameObjects.get(i).doesImpactScoreOnDeletion())
  					this.currentScore += this.gameObjects.get(i).getScoreValue();
  			}
  		}
  		for(int i = 0; i<indecesForRemoval.size();i++)
  			this.gameObjects.remove(indecesForRemoval.get(i)-i);
  		runtime.gc();
  	}
  	
  	/**
	 * ensures: passes the user message to advance to the next level on to the game's GameComponent
	 * (TEMPORARY, next to be refactored as I continue to move away from GameComponent.)
	 * @param args unused
	 */
  	public void levelUp() {
		try {
			this.gameObjects.clear();
			this.currentGame.levelUp();
		} catch (IllegalFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
  	/**
	 * ensures: passes the user message to regress to the previous level on to the game's GameComponent
	 * (TEMPORARY, next to be refactored as I continue to move away from GameComponent.)
	 * @param args unused
	 */
	public void levelDown() {
		try {
			this.gameObjects.clear();
			this.currentGame.levelDown();
		} catch (IllegalFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void demoShooting() {
		for(GameObject obj : gameObjects)
  			obj.shoot();
	}
	
	public void updateGraphics() {
		if(this.inDeveloperMode)
			this.currentGame.updateGraphics(gameObjects,currentScore,this.frame,this.averageFPS,this.currentFPS,this.memory);
		else
			this.currentGame.updateGraphics(gameObjects,currentScore);
	}
	
	public void debugGameObjects() {
		System.out.println("Number of GameObjects: " + this.gameObjects.size());
    	for(GameObject obj : gameObjects)
    		System.out.println(obj.getDebugName());
	}
	
	public void checkForGameOver() {
		if(this.currentGame.getPlayer().isToBeDeleted()){
			this.currentGame.setGameOver(true);
			for(GameObject obj : this.gameObjects)
				obj.setToBeDeleted(true);
			this.updateGameObjects();
			this.updateGraphics();
			this.gameListener.stopTimer();
		}
	}
	
	private void toggleFullscreen() {
		if(this.isFullscreen())
			this.setWindowed();
    	else
    		this.setFullScreen();
	}
	
	private void toggleDeveloperMode() {
		this.setInDeveloperMode(!this.isInDeveloperMode());
	}
	
	/**
	 * ensures: sets the game JFrame to fullscreen and resizes elements appropriately.
	 * @param args unused
	 */
	public void setFullScreen() {		
		gameFrame.dispose();
		gameFrame.setSize(screenSize);
		gameFrame.setUndecorated(true);
		gameFrame.setTitle("Jetpac (Full Screen)");
		this.setIsFullscreen(true);
		this.currentScreenWidth = this.screenSize.width;
		this.currentScreenHeight = this.screenSize.height;
		device.setFullScreenWindow(gameFrame);
		gameFrame.setVisible(true);
	}
	
	/**
	 * ensures: sets the game JFrame to windowed mode and resizes elements appropriately.
	 * @param args unused
	 */
	public void setWindowed() {
		this.currentGame.setFullscreen(true);
		device.setFullScreenWindow(null);
		gameFrame.dispose();
		gameFrame.setUndecorated(false);
		gameFrame.setResizable(false);
		gameFrame.setTitle("Jetpac");
        this.setIsFullscreen(false);
        gameFrame.setSize(scaledJFrameSize);
        this.currentScreenWidth = this.scaledJFrameSize.width;
        this.currentScreenHeight = this.scaledJFrameSize.height;
        gameFrame.setVisible(true);
	}	
  	
	//////////////////////////////////////////////
	/////////// GETTERS AND SETTERS //////////////
	//////////////////////////////////////////////
	
	public boolean getIsPaused() {
		return isPaused;
	}

	public void setIsPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}
	
	public GameComponent getCurrentGame() {
		return this.currentGame;
	}

	public boolean getIsRunning() {
		return isRunning;
	}

	public void setIsRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public GraphicsDevice getDevice() {
		return device;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getResolutionScale() {
		return resolutionScale;
	}

	public void setResolutionScale(int resolutionScale) {
		this.resolutionScale = resolutionScale;
	}

	public double getAverageFPS() {
		return averageFPS;
	}

	public void setAverageFPS(double averageFPS) {
		this.averageFPS = averageFPS;
	}

	public double getCurrentFPS() {
		return currentFPS;
	}

	public void setCurrentFPS(double currentFPS) {
		this.currentFPS = currentFPS;
	}

	public Random getRand() {
		return rand;
	}

	public void setRand(Random rand) {
		this.rand = rand;
	}

	public int getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
	}

	public boolean isFullscreen() {
		return isFullscreen;
	}

	public void setIsFullscreen(boolean isFullscreen) {
		this.isFullscreen = isFullscreen;
		this.currentGame.setFullscreen(isFullscreen);
	}

	public int getCurrentScreenWidth() {
		return currentScreenWidth;
	}

	public void setCurrentScreenWidth(int currentScreenWidth) {
		this.currentScreenWidth = currentScreenWidth;
		this.currentGame.setCurrentScreenWidth(currentScreenWidth);
	}

	public int getCurrentScreenHeight() {
		return currentScreenHeight;
	}

	public void setCurrentScreenHeight(int currentScreenHeight) {
		this.currentScreenHeight = currentScreenHeight;
		this.currentGame.setCurrentScreenHeight(currentScreenHeight);
	}

	public boolean isInDeveloperMode() {
		return inDeveloperMode;
	}

	public void setInDeveloperMode(boolean inDeveloperMode) {
		this.inDeveloperMode = inDeveloperMode;
		this.currentGame.setInDeveloperMode(inDeveloperMode);
	}
}
