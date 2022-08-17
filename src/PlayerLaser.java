import java.util.Random;

public class PlayerLaser extends Projectile{
	
	private Random rand = new Random();
	private static final int FRAMES_IN_ANIMATION = 7;
	private static final int FRAMES_PER_SPRITE_STAGE = 3;
	private static final int PLAYER_GUN_HEIGHT_OFFSET = 11;
	private static final int PLAYER_WIDTH = 16;
	private static final double SPEED = 10/3;
	public final String[] laserColors = new String[]{"00ffff", "ff00ff", "ffffff"};
	public final int[] laserLengths = new int[]{38, 52, 66, 80, 90, 101, 108};
	private String colorHex;
	private String spriteName;
	private int spriteStage;
	private int currentSpriteStageFrame;
	
	
	public PlayerLaser(double xPosition, double yPosition, boolean isLeft) {
		super(xPosition+PlayerLaser.PLAYER_WIDTH*((isLeft) ? -2 : 1), yPosition+PlayerLaser.PLAYER_GUN_HEIGHT_OFFSET, isLeft);
		this.setxVelocity(this.getxVelocity()*PlayerLaser.SPEED);
		this.setHarmfulToEnemy(true);
		this.setHarmfulToPlayer(false);
		this.setDebugName("PlayerLaser");
		this.appendSpritePath("PlayerLaser");
		this.initSprite();
		this.spawn();
	}
	
	public void spawn() {
		int max = this.laserColors.length-1;
		int min = 0;
		this.colorHex = laserColors[rand.nextInt((max - min) + 1) + min];
		this.spriteStage = 0;
		this.spriteName = this.colorHex +"-"+ Integer.toString((int)this.getAngleDegrees())+"-"+Integer.toString(laserLengths[spriteStage]);
		this.updateSprite(spriteName);
	}
	
	public void advanceSprite() {
		currentSpriteStageFrame = (currentSpriteStageFrame+1)%PlayerLaser.FRAMES_PER_SPRITE_STAGE;
		if((currentSpriteStageFrame+1 == PlayerLaser.FRAMES_PER_SPRITE_STAGE)&&(spriteStage+1<PlayerLaser.FRAMES_IN_ANIMATION)) {
			spriteStage = (spriteStage+1)%PlayerLaser.FRAMES_IN_ANIMATION;
			this.spriteName = this.colorHex +"-"+ Integer.toString((int)this.getAngleDegrees())+"-"+Integer.toString(laserLengths[spriteStage]);
			this.updateSprite(spriteName);
		}
	}
	
	@Override
	public void update() {
		super.update();
		this.advanceSprite();
	}
	
	@Override
	public void collideWith(GameObject other) {
		if(other.isDissapatesPlayerLasers())
			this.setToBeDeleted(true);
	}
}
