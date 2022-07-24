package mainApp.GameObjects.Projectile;

import mainApp.GameObjects.GameObject;

public class ProbeLaser extends Projectile{
	
	private static final double SPEED = 2;
	private static final int PROBE_GUN_HEIGHT_OFFSET = 8;
	
	public ProbeLaser(double xPosition, double yPosition, boolean isLeft) {
		super(xPosition+((isLeft) ? -4 : 12), yPosition+ProbeLaser.PROBE_GUN_HEIGHT_OFFSET, isLeft);
		this.setHarmfulToEnemy(false);
		this.setHarmfulToPlayer(true);
		this.setDebugName("ProbeLaser");
		this.appendSpritePath("ProbeLaser");
		this.initSprite();
		if(isLeft)
			this.updateSprite("180-1");
		else
			this.updateSprite("0-1");
		
		this.setxVelocity(this.getxVelocity()*ProbeLaser.SPEED);
	}
	
	@Override
	public void collideWith(GameObject other) {
		if(other.isDissapatesEnemyLasers())
			this.setToBeDeleted(true);
	}
}
