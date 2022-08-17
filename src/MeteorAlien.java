public class MeteorAlien extends Enemy {
	
	public final String[] meteorColors = new String[]{"00eeee", "ee00ee", "eeee00", "00ee00"};
	public final int[] angleOffsets = new int[]{0,30,-30};
	private String colorHex;
	private double moveSpeed = 1;

	public MeteorAlien() {
		super();
		this.appendSpritePath("Meteor");
		this.initSprite();
		this.setDebugName("Meteor");
		this.setHarmfulToEnemy(false);
		this.setHarmfulToPlayer(true);
		
		int max = this.meteorColors.length-1;
		int min = 0;
		this.colorHex = meteorColors[this.getRng().nextInt((max - min) + 1) + min];
		max = this.angleOffsets.length-1;
		min = 0;

		int newAngle = (360+this.getAngleDegrees() - angleOffsets[this.getRng().nextInt((max - min) + 1) + min])%360;
		this.setAngleDegrees(newAngle);
		this.setxVelocity(this.moveSpeed*Math.cos(this.getAngleRadians()));
		this.setyVelocity(this.moveSpeed*Math.sin(-1*this.getAngleRadians()));
		this.setSpriteForAngle();
	}
	
	public void setSpriteForAngle() {
		String spriteName = this.colorHex+"-"+Integer.toString(this.getAngleDegrees());
		this.updateSprite(spriteName);
	}
		
	@Override
	public void collideWith(GameObject other) {
		if(other.isHarmfulToEnemy()) {
			if(!other.isDissapatesPlayerLasers() && other.isCorporeal())
				this.setIfImpactsScoreOnDeletion(true);
			this.setToBeDeleted(true);
		}
		
if(other.isCorporeal() && other.isDissapatesPlayerLasers()) {
			
			double vX = this.getxVelocity();
			double vY = this.getyVelocity();
			double xC = this.getxPositionCurrent();
			double yC = this.getyPositionCurrent();
			double xN = this.getxPositionNext();
			double yN = this.getyPositionNext();
			double w = this.getWidth();
			double h = this.getHeight();
			
			double xO = other.getxPositionCurrent();
			double yO = other.getyPositionCurrent();
			double wO = other.getWidth();
			double hO = other.getHeight();
			
			int deg = this.getAngleDegrees();
			
			if((((xN+w)>=(xO))&&((xC+w)<=(xO)))) {
				if(vX>0) { //Detected collision RIGHT
					this.setxPositionNext(xO-w);
					this.setxVelocity(-vX);
					if(deg == 30)
						this.setAngleDegrees(150);
					if(deg == 0)
						this.setAngleDegrees(180);
					if(deg == 330)
						this.setAngleDegrees(210);	
				}
			}
			else if(((xN<=(xO+wO))&&(xC>=(xO+wO)))) {
				if(vX<0) { //Detected collision LEFT
					this.setxPositionNext(xN = xO+wO);
					this.setxVelocity(-vX);
					if(deg == 150)
						this.setAngleDegrees(30);
					if(deg == 180)
						this.setAngleDegrees(0);
					if(deg == 210)
						this.setAngleDegrees(330);
				}
			}
			
			if((((yN+h)>=(yO))&&((yC+h)<=(yO)))) {
				if(vY>0) { //Detected collision DOWN
					this.setyPositionNext(yO-h);
					this.setyVelocity(-vY);
					this.setAngleDegrees(360-deg);
				}
			}
			else if(((yN<=(yO+hO))&&(yC>=(yO+hO)))) {
				if(vY<0) { //Detected collision UP
					this.setyPositionNext(yO+hO);
					this.setyVelocity(-vY);
					this.setAngleDegrees(360-deg);
				}
			}
			this.setSpriteForAngle();
		}
	}
	
	//////////////////////////////////////////////
	/////////// GETTERS AND SETTERS //////////////
	//////////////////////////////////////////////

}