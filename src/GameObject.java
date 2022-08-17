import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;	

public abstract class GameObject {

	private double xPositionCurrent;
	private double yPositionCurrent;

	private double xPositionNext;
	private double yPositionNext;

	private double xVelocity;
	private double yVelocity;

	private int width;
	private int height;

	private String spritePath;
	private SpriteReader spriteReader;
	private BufferedImage sprite;
	private String debugName;

	private boolean harmfulToPlayer;
	private boolean harmfulToEnemy;
	private boolean dissapatesPlayerLasers;
	private boolean dissapatesEnemyLasers;
	private boolean isCorporeal;
	private boolean toBeDeleted;
	private boolean canPickUp;
	private boolean isTargetForDroppedObjects;
	private boolean isBeingCarried;
	private boolean canBeCarried;

	private boolean impactsScoreOnDeletion;
	private int scoreValue;

	private ArrayList<GameObject> childObjects = new ArrayList<GameObject>();

	public GameObject() {
		this.setSpritePath("Assets" + File.separator);
		this.initSprite();
	}
	
	public GameObject(double row, double column) {
		this.spawn(row, column);
		this.setSpritePath("Assets" + File.separator);
		this.initSprite();
	}

	public void spawn(double row, double column) {
		this.setxPositionCurrent(row);
		this.setyPositionCurrent(column);
		this.setxPositionNext(row);
		this.setyPositionNext(column);
	}

	public void shoot() {
		//
	}

	public void initSprite() {
		this.setSpriteReader(new SpriteReader(this.getSpritePath()));
		this.updateSprite("default");
	}

	public void appendSpritePath(String subDirectory) {
		this.setSpritePath(this.getSpritePath() + subDirectory + File.separator);
	}

	public void updateSprite(String spriteName) {
		this.setSprite(this.getSpriteReader().initSprite(spriteName));
		this.setWidth(this.getSprite().getWidth());
		this.setHeight(this.getSprite().getHeight());
	}

//	public abstract void update(); // TODO For J: Implement this in Enemy, Resource, Projectile, and Player.

	// Implemented by Steve
	public void update() {
		if (!this.isToBeDeleted()) {
			this.setxPositionCurrent(this.getxPositionNext());
			this.setyPositionCurrent(this.getyPositionNext());
			if (this.isToBeDeleted())
				this.prepForDeletion();
		}
	}

//	public abstract void move(); // TODO For J: Implement this in Enemy, Resource, Projectile, and Player.

	// Implemented by Steve.
	public void move() {
		this.setxPositionNext(this.getxPositionCurrent() + this.getxVelocity());
		this.setyPositionNext(this.getyPositionCurrent() + this.getyVelocity());
	}

//	public abstract boolean overlapsWith(GameObject other); // TODO For J: Implement this in Enemy, Resource, Projectile, and Player.

	// Implemented by Steve.
	public boolean overlapsWith(GameObject other) {
		double x = this.getxPositionCurrent();
		double y = this.getyPositionCurrent();
		int w = this.getWidth();
		int h = this.getHeight();

		double xO = other.getxPositionCurrent();
		double yO = other.getyPositionCurrent();
		int wO = other.getWidth();
		int hO = other.getHeight();

		boolean xOverlap = false;
		boolean yOverlap = false;

//			System.out.println("        Does "+this.getClass().toString()+" collide with " + other.getClass().toString() + "?");
//			
//			System.out.println("x = "+x+", y = "+y+", w = "+w+", h = "+h);
//			System.out.println("xO = "+xO+", yO = "+yO+", wO = "+wO+", hO = "+hO);
//			System.out.println("cX = "+cX+", cY = "+cY);
//			System.out.println("cXO = "+cXO+", cYO = "+cYO);
//			System.out.println("xRange = "+xRange+", yRange = "+yRange);

//		boolean leftEdgeOverlapping = false;
//		boolean rightEdgeOverlapping = false;
//		boolean topEdgeOverlapping = false;
//		boolean bottomEdgeOverlapping = false;
//		
//		if((xO<=x)&&(x<=xO+wO)) {
//			leftEdgeOverlapping = true;
//		}
//		if((xO<=x+w)&&(x+w<=xO+wO)) {
//			rightEdgeOverlapping = true;
//		}
		if ((xO <= x + w) && (x <= xO + wO)) {
			xOverlap = true;
//			System.out.println("x axis aligned");
		}

//		if((yO<=y)&&(y<=yO+hO)) {
//			topEdgeOverlapping = true;
//		}
//		if((yO<=y+h)&&(y+h<=yO+hO)) {
//			bottomEdgeOverlapping = true;
//		}
		if ((yO <= y + h) && (y <= yO + hO)) {
			yOverlap = true;
//			System.out.println("y axis aligned");
		}

		boolean result = (xOverlap && yOverlap);
//			System.out.println("            result = " + result);
		return result;
	}

//	@SuppressWarnings("null")
	public void prepForDeletion() {
		this.xPositionCurrent = 0;
		this.yPositionCurrent = 0;
		this.xPositionNext = 0;
		this.yPositionNext = 0;
		this.xVelocity = 0;
		this.yVelocity = 0;
		this.harmfulToPlayer = false;
		this.harmfulToEnemy = false;
		this.dissapatesPlayerLasers = false;
		this.dissapatesEnemyLasers = false;
		this.isCorporeal = false;
		this.childObjects = new ArrayList<GameObject>();
		this.width = 0;
		this.height = 0;
		this.setSpritePath("Assets" + File.separator);
		this.updateSprite("nothing");
	}

//	public abstract void drawOn(Graphics2D graphics2, int resolutionScale); // TODO For J: Implement this in Enemy, Resource, Projectile, and Player.	

	// implemented by Steve
	public void drawOn(Graphics2D graphics2, int resolutionScale) {
		Image scaledSprite = this.getSprite().getScaledInstance(this.getWidth() * resolutionScale,
				this.getHeight() * resolutionScale, 0);
		graphics2.drawImage(scaledSprite, (int) this.getxPositionCurrent() * resolutionScale,
				(int) this.getyPositionCurrent() * resolutionScale, null);
	}

	public abstract void collideWith(GameObject other);

	//////////////////////////////////////////////
	/////////// GETTERS AND SETTERS //////////////
	//////////////////////////////////////////////

	public double getxPositionCurrent() {
		return xPositionCurrent;
	}

	public void setxPositionCurrent(double xPosition) {
		this.xPositionCurrent = xPosition;
	}

	public double getyPositionCurrent() {
		return yPositionCurrent;
	}

	public void setyPositionCurrent(double yPosition) {
		this.yPositionCurrent = yPosition;
	}

	public double getxPositionNext() {
		return xPositionNext;
	}

	public void setxPositionNext(double xPositionNext) {
		this.xPositionNext = xPositionNext;
	}

	public double getyPositionNext() {
		return yPositionNext;
	}

	public void setyPositionNext(double yPosition) {
		this.yPositionNext = yPosition;
	}

	public double getxVelocity() {
		return xVelocity;
	}

	public void setxVelocity(double xVelocity) {
		this.xVelocity = xVelocity;
	}

	public double getyVelocity() {
		return yVelocity;
	}

	public void setyVelocity(double yVelocity) {
		this.yVelocity = yVelocity;
	}

	public boolean isHarmfulToPlayer() {
		return harmfulToPlayer;
	}

	public void setHarmfulToPlayer(boolean harmfulToPlayer) {
		this.harmfulToPlayer = harmfulToPlayer;
	}

	public boolean isHarmfulToEnemy() {
		return harmfulToEnemy;
	}

	public void setHarmfulToEnemy(boolean harmfulToEnemy) {
		this.harmfulToEnemy = harmfulToEnemy;
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

	public boolean isDissapatesPlayerLasers() {
		return dissapatesPlayerLasers;
	}

	public void setDissapatesPlayerLasers(boolean dissapatesPlayerLasers) {
		this.dissapatesPlayerLasers = dissapatesPlayerLasers;
	}

	public boolean isDissapatesEnemyLasers() {
		return dissapatesEnemyLasers;
	}

	public void setDissapatesEnemyLasers(boolean dissapatesEnemyLasers) {
		this.dissapatesEnemyLasers = dissapatesEnemyLasers;
	}

	public boolean isToBeDeleted() {
		return toBeDeleted;
	}

	public void setToBeDeleted(boolean toBeDeleted) {
		this.toBeDeleted = toBeDeleted;
	}

	public boolean isCorporeal() {
		return isCorporeal;
	}

	public void setCorporeality(boolean isCorporeal) {
		this.isCorporeal = isCorporeal;
	}

	public ArrayList<GameObject> getChildObjects() {
		return childObjects;
	}

	public void setChildObjects(ArrayList<GameObject> childObjects) {
		this.childObjects = childObjects;
	}

	public SpriteReader getSpriteReader() {
		return spriteReader;
	}

	public void setSpriteReader(SpriteReader spriteReader) {
		this.spriteReader = spriteReader;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}

	public BufferedImage getSprite() {
		return sprite;
	}

	public String getSpritePath() {
		return spritePath;
	}

	public void setSpritePath(String spritePath) {
		this.spritePath = spritePath;
	}

	public String getDebugName() {
		return debugName;
	}

	public void setDebugName(String debugName) {
		this.debugName = debugName;
	}

	public boolean canPickUp() {
		return canPickUp;
	}

	public void setCanPickUp(boolean canPickUp) {
		this.canPickUp = canPickUp;
	}

	public boolean doesImpactScoreOnDeletion() {
		return impactsScoreOnDeletion;
	}

	public void setIfImpactsScoreOnDeletion(boolean impactsScoreOnDeletion) {
		this.impactsScoreOnDeletion = impactsScoreOnDeletion;
	}

	public int getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(int scoreValue) {
		this.scoreValue = scoreValue;
	}
	
	public boolean isTargetForDroppedObjects() {
		return isTargetForDroppedObjects;
	}

	public void setTargetForDroppedObjects(boolean isTarget) {
		this.isTargetForDroppedObjects = isTarget;
	}
	
	public boolean isBeingCarried() {
		return isBeingCarried;
	}

	public void setBeingCarried(boolean isCarried) {
		this.isBeingCarried = isCarried;
	}

	public boolean isCanBeCarried() {
		return canBeCarried;
	}

	public void setCanBeCarried(boolean canCarry) {
		this.canBeCarried = canCarry;
	}

}
