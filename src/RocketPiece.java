import java.util.Random;

public class RocketPiece extends Resource {
	private Random rand = new Random();
//	private RocketTier rocketTier = RocketTier.bottom;
//	private RocketTier rocketTier = RocketTier.middle;

	public enum RocketTier {
		bottom, middle, top;
	}

	
//	public RocketPiece(double xPosition, String spriteName) {
//		super();
//		this.appendSpritePath("ObjectiveResource");
//		this.initSprite();
//		this.updateSprite(spriteName);
//		this.spawn(xPosition, this.getSpawnY());
//		this.setTargetForDroppedObjects(true);
//		this.setCanBeCarried(true);
//	}
	
	public RocketPiece(String spriteName) {
		super();
		this.appendSpritePath("ObjectiveResource");
		this.initSprite();
		this.updateSprite(spriteName);
		this.spawn(rand.nextInt(ArcadeGame.ZX_SPECTRUM_X_RESOLUTION - this.getSprite().getWidth()), this.getSpawnY());
		this.setTargetForDroppedObjects(true);
		this.setCanBeCarried(true);
		this.setScoreValue(100);
	}
	
	public RocketPiece(double xPosition) {
		super();
		this.appendSpritePath("ObjectiveResource");
		this.initSprite();
//		this.updateSprite("rocketBottom");
		this.updateSprite("rocketMiddle");
		this.spawn(xPosition, this.getSpawnY());
		this.setTargetForDroppedObjects(true);
		this.setCanBeCarried(true);
		this.setScoreValue(100);
//		this.setCorporeality(true);
	}

//	public void respawn() {
////		this.setxPositionCurrent(rand.nextInt(0, Jetpac.ZX_SPECTRUM_X_RESOLUTION - this.getSprite().getWidth()));
////		this.setxPositionNext();
////		this.setyPositionCurrent(this.getSpawnY());
////		this.setyPositionNext();
//		this.spawn(rand.nextInt(0, Jetpac.ZX_SPECTRUM_X_RESOLUTION - this.getSprite().getWidth()), this.getSpawnY());
//		this.setCanBeCarried(true);
//		this.setCorporeality(true);
//		
//		if (this.rocketTier == RocketTier.bottom) {
//			this.rocketTier = RocketTier.middle;
//			this.updateSprite("rocketMiddle");
//		} else if (this.rocketTier == RocketTier.middle) {
//			this.rocketTier = RocketTier.top;
//			this.updateSprite("rocketTop");
//		} else if (this.rocketTier == RocketTier.top) {
//			this.updateSprite("fuel");
//			return;
//		}
//	}

	public void collideWith(GameObject other) {

		if (other.canPickUp()) {
			if (this.isBeingCarried()) {
				this.setOtherNextPosX(other.getxPositionNext());
				this.setOtherNextPosY(other.getyPositionNext());
			}
			else if (this.isCanBeCarried() && !this.isBeingCarried()) {
				this.setBeingCarried(true);
			}

		} 
//		else if (other.isCorporeal() && !this.isCorporeal()) {
//			if (this.getyPositionCurrent() >= Jetpac.ZX_SPECTRUM_Y_RESOLUTION && !this.isCanBeCarried()) {
//				
//				this.respawn();
//				
//			}
//		}

		else if (other.isCorporeal()) {
			double vY = this.getyVelocity();
			double yC = this.getyPositionCurrent();
			double yN = this.getyPositionNext();
			double h = this.getHeight();

			double yO = other.getyPositionCurrent();
			double hO = other.getHeight();

			if ((((yN + h) >= (yO)) && ((yC + h) <= (yO)))) {
				if (vY > 0) // Detected collision DOWN
					this.setyPositionNext(yO - h);

			} else if (((yN <= (yO + hO)) && (yC >= (yO + hO)))) {
				if (vY < 0) // Detected collision UP
					this.setyPositionNext(yO + hO);
			}
		} else if (other.isTargetForDroppedObjects() && this.isBeingCarried()) {
			this.setxPositionNext(other.getxPositionCurrent());
			this.setBeingCarried(false);
//			this.setCorporeality(false);
		}
	}
}
