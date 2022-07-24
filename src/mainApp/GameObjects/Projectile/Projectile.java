package mainApp.GameObjects.Projectile;

import mainApp.GameObjects.GameObject;

import java.lang.Math;

public class Projectile extends GameObject {
	
	private double angleDegrees;

	public Projectile(double xPosition, double yPosition, boolean isLeft) {
		super();
		this.spawn(xPosition,yPosition);
		this.appendSpritePath("Projectiles");
		this.initSprite();
		if (isLeft) {
			this.setxVelocity(-1);
			this.setAngleDegrees(180);
		}
		else {
			this.setxVelocity(1);
			this.setAngleDegrees(0);
		}
	}

	@Override
	public void collideWith(GameObject other) {
		// TODO Auto-generated method stub
	}

	public double getAngleDegrees() {
		return angleDegrees;
	}

	public void setAngleDegrees(double angleDegrees) {
		this.angleDegrees = angleDegrees;
	}
	
	public double getAngleRadians() {
		return Math.toRadians(this.angleDegrees);
	}

	public void setAngleRadians(double angleRadians) {
		this.angleDegrees = Math.toDegrees(angleRadians);
	}
}
