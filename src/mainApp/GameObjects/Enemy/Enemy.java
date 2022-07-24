package mainApp.GameObjects.Enemy;

import java.util.Random;

import mainApp.ArcadeGame;
import mainApp.GameObjects.GameObject;

public class Enemy extends GameObject {

	private static final int BOTTOM_PLATFORM_HEIGHT = 8;
	private boolean isLeft;
	private Random rng = new Random();
	private int angleDegrees;

	// Potential enemies:
			// from the game:
			// meteor - level 1
			// bouncer - level 2
			// rover - level 3
			// diver - level 4
			// saucer - level 5
			// from other media:
			// glider - conway's game of life
			// probe - terraria
	public Enemy() {
		super();
		this.appendSpritePath("Enemies");
		this.initSprite();
		this.spawn();
		this.setScoreValue(3);
	}
	
	public void spawn() {
		int enemyHeight = 16;
		int max = ArcadeGame.ZX_SPECTRUM_Y_RESOLUTION-enemyHeight-Enemy.BOTTOM_PLATFORM_HEIGHT;
		int min = 0;
		double x;
		double y = this.getRng().nextInt((max - min) + 1) + min;
		if(this.getRng().nextBoolean()) {
			x = -enemyHeight;
			this.setxVelocity(.5);
			this.setLeft(false);
			this.setAngleDegrees(0);
		}
		else {
			x = ArcadeGame.ZX_SPECTRUM_X_RESOLUTION;
			this.setxVelocity(-.5);
			this.setLeft(true);
			this.setAngleDegrees(180);
		}
		this.spawn(x,y);
	}

	@Override
	public void collideWith(GameObject other) {
		if(other.isHarmfulToEnemy()) {
			if(!other.isDissapatesPlayerLasers() && other.isCorporeal())
				this.setIfImpactsScoreOnDeletion(true);
			this.setToBeDeleted(true);
		}
	}
	
	//////////////////////////////////////////////
	/////////// GETTERS AND SETTERS //////////////
	//////////////////////////////////////////////

	public boolean isLeft() {
		return isLeft;
	}

	public void setLeft(boolean isLeft) {
		this.isLeft = isLeft;
	}

	public Random getRng() {
		return rng;
	}

	public void setRng(Random rng) {
		this.rng = rng;
	}
	
	public int getAngleDegrees() {
		return angleDegrees;
	}

	public void setAngleDegrees(int angleDegrees) {
		this.angleDegrees = angleDegrees;
	}
	
	public double getAngleRadians() {
		return Math.toRadians(this.angleDegrees);
	}

	public void setAngleRadians(double angleRadians) {
		this.angleDegrees = (int) Math.toDegrees(angleRadians);
	}
}
