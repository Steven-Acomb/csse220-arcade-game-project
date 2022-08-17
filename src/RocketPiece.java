import java.util.Random;

public class RocketPiece extends Resource {
	private Random rand = new Random();

	public enum RocketTier {
		bottom, middle, top;
	}
	
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
		this.updateSprite("rocketMiddle");
		this.spawn(xPosition, this.getSpawnY());
		this.setTargetForDroppedObjects(true);
		this.setCanBeCarried(true);
		this.setScoreValue(100);
	}

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
		}
	}
}
