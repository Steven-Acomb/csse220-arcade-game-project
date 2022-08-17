import java.util.Random;

public class Gemstone extends Resource{
	
	private Random rand = new Random();
	private static final int FRAMES_IN_ANIMATION = 4;
	private int spriteStage;
	private int framesPerSpriteStage;
	private int currentSpriteStageFrame;

	public Gemstone() {
		super();
		this.appendSpritePath("ScoreResource");
		this.appendSpritePath("Gemstone");
		this.initSprite();
		this.setSpriteStage(rand.nextInt(Gemstone.FRAMES_IN_ANIMATION));
		this.initValue();
		this.advanceSprite();
		this.spawn();
	}
	
	public void spawn() {
		int max = ArcadeGame.ZX_SPECTRUM_X_RESOLUTION-this.getWidth();
		int min = 0;
		int x = rand.nextInt((max - min) + 1) + min;
		int y = 0-this.getHeight();
		this.setyVelocity(1);
		this.spawn(x,y);
	}
	
	public void initValue() {
		int scoreMultiplier = rand.nextInt((7 - 3) + 1) + 3;
		this.setFramesPerSpriteStage((2*5-scoreMultiplier));
		this.setScoreValue(5*scoreMultiplier);
		this.setCurrentSpriteStageFrame(0);
	}
	
	public void advanceSprite() {
		currentSpriteStageFrame = (currentSpriteStageFrame+1)%framesPerSpriteStage;
		if(currentSpriteStageFrame+1 == framesPerSpriteStage) {
			spriteStage = (spriteStage+1)%Gemstone.FRAMES_IN_ANIMATION;
			String spriteName = "gemstone-"+Integer.toString(spriteStage);
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
		if(other.isCorporeal()) {
			if(this.getyPositionNext()>this.getHeight()) {
				this.setyVelocity(0);
			}
		}
		if (other.canPickUp()) {
			this.setIfImpactsScoreOnDeletion(true);
			this.setToBeDeleted(true);
		}
	}

	public int getSpriteStage() {
		return spriteStage;
	}

	public void setSpriteStage(int spriteStage) {
		this.spriteStage = spriteStage;
	}

	public int getFramesPerSpriteStage() {
		return framesPerSpriteStage;
	}

	public void setFramesPerSpriteStage(int framesPerSpriteStage) {
		this.framesPerSpriteStage = framesPerSpriteStage;
	}

	public int getCurrentSpriteStageFrame() {
		return currentSpriteStageFrame;
	}

	public void setCurrentSpriteStageFrame(int currentSpriteStageFrame) {
		this.currentSpriteStageFrame = currentSpriteStageFrame;
	}

}
