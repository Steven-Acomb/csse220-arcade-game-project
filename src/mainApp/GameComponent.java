package mainApp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JComponent;

import mainApp.GameObjects.GameObject;
import mainApp.GameObjects.Enemy.MeteorAlien;
import mainApp.GameObjects.Enemy.ProbeAlien;
import mainApp.GameObjects.Player.Player;
import mainApp.GameObjects.Terrain.Boundry;
import mainApp.GameObjects.Terrain.Platform;
import mainApp.GameObjects.Terrain.Rocket;
import mainApp.GameObjects.Resource.*;

@SuppressWarnings("serial")
public class GameComponent extends JComponent{
	private int resolutionScale;
    
    private ArrayList<GameObject> gameObjects;
    private boolean gameOver;
	private Player player;
    
    private String layout;
    public LevelReader reader;
    public Rocket rocket;
    
    private int currentScore;
    
    private boolean isFullscreen;
    private int currentScreenWidth;
    private int currentScreenHeight;
    private Dimension screenSize;
    private Dimension scaledJFrameSize;

    private boolean inDeveloperMode;
    private int frame;
    private double averageFPS;
    private double currentFPS;
    private long memory;

	public GameComponent(int resolutionScale,ArrayList<GameObject> gameObjects, boolean isFullscreen, Dimension screenSize, Dimension scaledJFrameSize) throws IllegalFileFormatException {
		this.resolutionScale = resolutionScale;
		this.gameObjects = gameObjects;
		this.isFullscreen = isFullscreen;
		this.screenSize = screenSize;
		this.scaledJFrameSize = scaledJFrameSize;
		this.reader = new LevelReader();
		this.readLayout();
	}

	public void levelUp() throws IllegalFileFormatException {
		this.reader.levelUp();
		readLayout();
	}
	
	public void levelDown() throws IllegalFileFormatException {
		this.reader.levelDown();
		readLayout();
	}
	
	public void readLayout() throws IllegalFileFormatException {
		this.layout = this.reader.runReadFiles();
		int row = 0;
		int column = 0;
		
		//inner
		this.gameObjects.add(new Boundry("bottomBoundry", 0, ArcadeGame.ZX_SPECTRUM_Y_RESOLUTION,false));
		this.gameObjects.add(new Boundry("topBoundry", 0, -32,false));
		this.gameObjects.add(new Boundry("leftBoundry", -32, 0,false));
		this.gameObjects.add(new Boundry("rightBoundry", ArcadeGame.ZX_SPECTRUM_X_RESOLUTION, 0,false));
		
		//outer
		this.gameObjects.add(new Boundry("topBoundry", 0, ArcadeGame.ZX_SPECTRUM_Y_RESOLUTION+32,true));
		this.gameObjects.add(new Boundry("bottomBoundry", 0, -64,true));
		this.gameObjects.add(new Boundry("rightBoundry", -64, -ArcadeGame.ZX_SPECTRUM_Y_RESOLUTION/2,true));
		this.gameObjects.add(new Boundry("leftBoundry", ArcadeGame.ZX_SPECTRUM_X_RESOLUTION+32, -ArcadeGame.ZX_SPECTRUM_Y_RESOLUTION/2,true));
		this.gameObjects.add(new Boundry("rightBoundry", -64, ArcadeGame.ZX_SPECTRUM_Y_RESOLUTION/2,true));
		this.gameObjects.add(new Boundry("leftBoundry", ArcadeGame.ZX_SPECTRUM_X_RESOLUTION+32, ArcadeGame.ZX_SPECTRUM_Y_RESOLUTION/2,true));
		
		for (int i = 0; i < layout.length(); i++) {
			char curChar = layout.charAt(i);
			if (curChar == 'P') {
				this.player = new Player(row, column);
				this.gameObjects.add(this.player);
				row += 8;
			}
			else if (curChar == '_') {
				this.gameObjects.add(new Platform("bottomPlatform", row, column));
				row += 8;
			}
			else if (curChar == '[') {
				this.gameObjects.add(new Platform("platformTopLeft", row, column));
				row += 48;
			}
			else if (curChar == '-') {
				this.gameObjects.add(new Platform("platformCenter", row, column));
				row += 32;
			}
			else if (curChar == ']') {
				this.gameObjects.add(new Platform("platformTopRight", row, column));
				row += 48;
			}
			else if (curChar == '1') {
				this.gameObjects.add(new ProbeAlien());
				row += 8;
			}
			else if (curChar == '2') {
				this.gameObjects.add(new MeteorAlien());
				row += 8;
			}
			else if (curChar =='R') {
				this.gameObjects.add(new RocketPiece(column));
//				this.gameObjects.add(new RocketPiece(column,"rocketMiddle"));
				row += 8;
			}
//			else if (curChar =='r') {
//				this.gameObjects.add(new RocketPiece(column,"rocketTop"));
//				row += 8;
//			}
			else if (curChar == 'F') {
				this.gameObjects.add(new Fuel(column));
				row += 8;
			}
			else if (curChar == 'L') {
				this.setRocket(new Rocket(row,column,Rocket.FUEL_ONLY_LEVEL_FIRST_STAGE));
				this.gameObjects.add(this.getRocket());
//				this.gameObjects.add(rocket);
				row+=8;
			}
			else if (curChar == 'T') {
				this.setRocket(new Rocket(row,column,Rocket.REBUILDING_LEVEL_FIRST_STAGE));
				this.gameObjects.add(this.getRocket());
				row+=8;
			}
			else if (curChar == '|') {
				column += 8;
				row = 0;
			}
			else if(curChar == '.')
				row += 8;
			else if(curChar == '?') {
				this.gameObjects.add(new GameObject(row,column) {
					@Override
					public void collideWith(GameObject other) {
						// TODO Auto-generated method stub
					}
				});
				row += 8;
			}
		}
	}

	public void updateGraphics(ArrayList<GameObject> gameObjects, int currentScore) {
		this.setGameObjects(gameObjects);
		this.setCurrentScore(currentScore);
		this.repaint();
	}

	public void updateGraphics(ArrayList<GameObject> gameObjects, int currentScore, int frame, double averageFPS,
			double currentFPS, long memory) {
		this.frame = frame;
		this.averageFPS = averageFPS;
		this.currentFPS = currentFPS;
		this.memory = memory;
		this.setGameObjects(gameObjects);
		this.setCurrentScore(currentScore);
		this.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, this.screenSize.width, this.screenSize.height);
		graphics.fillRect(0, 0, ArcadeGame.ZX_SPECTRUM_X_RESOLUTION*resolutionScale, ArcadeGame.ZX_SPECTRUM_Y_RESOLUTION*resolutionScale);
		
		Graphics2D graphics2 = (Graphics2D) graphics;
		int xOffset = (this.screenSize.width - ArcadeGame.ZX_SPECTRUM_X_RESOLUTION*resolutionScale)/2;
	    int yOffset = (this.screenSize.height - ArcadeGame.ZX_SPECTRUM_Y_RESOLUTION*resolutionScale)/2;
	    if(this.isFullscreen) {
	    	graphics2.translate(xOffset, yOffset);
	    }
	    
	    if(this.isGameOver()) {
	    	graphics2.setColor(Color.red);
	    	graphics2.fillRect(-xOffset, -yOffset, this.screenSize.width, this.screenSize.height);
//	    	graphics2.fillRect(0, 0, Jetpac.ZX_SPECTRUM_X_RESOLUTION*resolutionScale, Jetpac.ZX_SPECTRUM_Y_RESOLUTION*resolutionScale);
			
	    	graphics2.setColor(Color.white);
			Font font1 = new Font("Purisa", Font.BOLD, 40*resolutionScale);
		    FontMetrics metrics2 = graphics.getFontMetrics(font1);
		    String gameOver = "Game Over!";
		    graphics2.setFont(font1);
		    graphics2.drawString(gameOver,(ArcadeGame.ZX_SPECTRUM_X_RESOLUTION*this.resolutionScale - metrics2.stringWidth(gameOver))/2,2*metrics2.getHeight());
		    
		    Font font2 = new Font("Purisa", Font.BOLD, 12*resolutionScale);
		    FontMetrics metrics1 = graphics.getFontMetrics(font2);
		    String scoreboard = "Final Score: " + Integer.toString(this.getCurrentScore());
		    graphics2.setFont(font2);
		    graphics2.drawString(scoreboard,(ArcadeGame.ZX_SPECTRUM_X_RESOLUTION*this.resolutionScale - metrics1.stringWidth(scoreboard))/2,2*metrics1.getHeight()+2*metrics2.getHeight());
		    
	    }
	    else {
	    	for(GameObject obj : gameObjects)
	  			obj.drawOn(graphics2, resolutionScale);
	    	
	    	//Scoreboard
			graphics2.setColor(Color.WHITE);
			Font font = new Font("Purisa", Font.BOLD, 12*resolutionScale);
		    FontMetrics metrics = graphics.getFontMetrics(font);
		    String scoreboard = "Score: " + Integer.toString(this.getCurrentScore());
		    String lives = "Lives: " + Integer.toString(player.getLivesRemaining());
		    graphics2.setFont(font);
		    graphics2.drawString(scoreboard,(ArcadeGame.ZX_SPECTRUM_X_RESOLUTION*this.resolutionScale - metrics.stringWidth(scoreboard))/2,metrics.getHeight());
		    graphics2.drawString(lives,(ArcadeGame.ZX_SPECTRUM_X_RESOLUTION*this.resolutionScale - metrics.stringWidth(lives)-metrics.getHeight()),metrics.getHeight());
		    
		    //Developer Statistics
		    if(this.inDeveloperMode) {
		    	graphics2.setColor(Color.WHITE);
				Font fontDev = new Font("Purisa", Font.BOLD, 4*resolutionScale);
			    FontMetrics devMetrics = graphics.getFontMetrics(fontDev);
			    String devInfo_0 = " Frame: "+Integer.toString(this.frame);
			    String devInfo_1 = " CurrentFPS: "+Integer.toString((int)this.currentFPS);
			    String devInfo_2 = " AverageFPS: "+Integer.toString((int)this.averageFPS);
			    String devInfo_3 = " GameObject Count: "+Integer.toString((int)this.gameObjects.size());
			    String devInfo_4 = " Memory Used (Bytes): "+Long.toString(this.memory);
			    graphics2.setFont(fontDev);
			    graphics2.drawString(devInfo_0,0,3*yOffset/8);
			    graphics2.drawString(devInfo_1,0,3*yOffset/8+devMetrics.getHeight());
			    graphics2.drawString(devInfo_2,0,3*yOffset/8+2*devMetrics.getHeight());
			    graphics2.drawString(devInfo_3,0,3*yOffset/8+3*devMetrics.getHeight());
			    graphics2.drawString(devInfo_4,0,3*yOffset/8+4*devMetrics.getHeight());
		    }
		    else {
		    	//Window Boundries
			    graphics2.setColor(Color.black);
				graphics2.fillRect(-xOffset, -yOffset, xOffset, this.screenSize.height);
				graphics2.fillRect(ArcadeGame.ZX_SPECTRUM_X_RESOLUTION*this.resolutionScale, -yOffset, xOffset, this.screenSize.height);
				graphics2.fillRect(-xOffset, -yOffset, this.screenSize.width, yOffset);
				graphics2.fillRect(-xOffset, ArcadeGame.ZX_SPECTRUM_Y_RESOLUTION*resolutionScale, this.screenSize.width, yOffset);
		    }
	    }
	}	
	
	//////////////////////////////////////////////
	/////////// GETTERS AND SETTERS //////////////
	//////////////////////////////////////////////
	
	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}

	public void setGameObjects(ArrayList<GameObject> gameObjects) {
		this.gameObjects = gameObjects;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public int getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
	}
	
	public int getResolutionScale() {
		return resolutionScale;
	}

	public boolean isFullscreen() {
		return isFullscreen;
	}

	public void setFullscreen(boolean isFullscreen) {
		this.isFullscreen = isFullscreen;
	}

	public int getCurrentScreenWidth() {
		return currentScreenWidth;
	}

	public void setCurrentScreenWidth(int currentScreenWidth) {
		this.currentScreenWidth = currentScreenWidth;
	}

	public int getCurrentScreenHeight() {
		return currentScreenHeight;
	}

	public void setCurrentScreenHeight(int currentScreenHeight) {
		this.currentScreenHeight = currentScreenHeight;
	}

	public Dimension getScreenSize() {
		return screenSize;
	}

	public void setScreenSize(Dimension screenSize) {
		this.screenSize = screenSize;
	}

	public Dimension getScaledJFrameSize() {
		return scaledJFrameSize;
	}

	public void setScaledJFrameSize(Dimension scaledJFrameSize) {
		this.scaledJFrameSize = scaledJFrameSize;
	}

	public boolean isInDeveloperMode() {
		return inDeveloperMode;
	}

	public void setInDeveloperMode(boolean inDeveloperMode) {
		this.inDeveloperMode = inDeveloperMode;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Rocket getRocket() {
		return rocket;
	}

	public void setRocket(Rocket rocket) {
		this.rocket = rocket;
	}

}
