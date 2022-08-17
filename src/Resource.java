public class Resource extends GameObject {

	private double spawnY;

	private double otherNextPosX;
	private double otherNextPosY;

	public Resource() {
		super();
		this.appendSpritePath("Resources");
		this.initSprite();
		this.setyVelocity(1);
		this.setSpawnY(0);
	}

	@Override
	public void collideWith(GameObject other) {}


	public double getSpawnY() {
		return spawnY;
	}

	public void setSpawnY(double spawnY) {
		this.spawnY = spawnY;
	}

	public double getOtherNextPosX() {
		return otherNextPosX;
	}

	public void setOtherNextPosX(double otherNextPosX) {
		this.otherNextPosX = otherNextPosX;
	}

	public double getOtherNextPosY() {
		return otherNextPosY;
	}

	public void setOtherNextPosY(double otherNextPosY) {
		this.otherNextPosY = otherNextPosY;
	}

	public void move() {
		if (this.isBeingCarried()) {
			this.setxPositionNext(this.getOtherNextPosX());
			this.setyPositionNext(this.getOtherNextPosY());
		} else {
			this.setxPositionNext(this.getxPositionCurrent() + this.getxVelocity());
			this.setyPositionNext(this.getyPositionCurrent() + this.getyVelocity());
		}
	}

}