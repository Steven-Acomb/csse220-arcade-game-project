package mainApp.GameObjects.Enemy;

import mainApp.GameObjects.Projectile.ProbeLaser;
import mainApp.GameObjects.Projectile.Projectile;

public class ProbeAlien extends Enemy {

	public ProbeAlien() {
		super();
		this.appendSpritePath("Probe");
		this.initSprite();
		this.setDebugName("Probe");
		this.setDissapatesEnemyLasers(false);
		this.setDissapatesPlayerLasers(true);
		
		if(this.isLeft())
			this.updateSprite("Probe-180");
		else
			this.updateSprite("Probe-0");
	}
	
	public void randomlyShoot() {
		int max = 50;
		int min = 1;
		int x = this.getRng().nextInt((max - min) + 1) + min;
		if(x == 1)
			this.shoot();
	}
	
	@Override
	public void update() {
		super.update();
		this.randomlyShoot();
	}
	
	@Override
	public void shoot() {
		Projectile probeLaser = new ProbeLaser((int)this.getxPositionCurrent(),(int)this.getyPositionCurrent(),this.isLeft());
		this.getChildObjects().add(probeLaser);
	}
	
	//////////////////////////////////////////////
	/////////// GETTERS AND SETTERS //////////////
	//////////////////////////////////////////////

}