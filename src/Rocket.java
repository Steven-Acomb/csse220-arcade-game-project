public class Rocket extends Terrain {
	private static final int FRAMES_IN_ANIMATION = 3;
	public static final int REBUILDING_LEVEL_FIRST_STAGE = 0;
	public static final int FUEL_ONLY_LEVEL_FIRST_STAGE = 2;
	public static final int FINAL_STAGE = 26;
	public static final int FRAMES_PER_ANIMATION_UPDATE = 2;
	public static final int VISIBLE_HEIGHT = 48;
	
	public static final int EASY_DIFFICULTY_FUEL_MULTIPLIER = 6;
//	public static final int MEDIUM_DIFFICULTY_FUEL_MULTIPLIER = 4;
//	public static final int HARD_DIFFICULTY_FUEL_MULTIPLIER = 3;
	
	private int stage;
	private boolean built;
	private boolean refueled;
	private boolean clear;
	private boolean ready;
	private boolean loadIn;
	private String spriteName;
	private int currentAnimationFrame;
	private int framesSinceAnimationUpdate;
	

	public Rocket(double x, double y, int initialStage) {
		super();
		this.setLoadIn(true);
		this.setClear(false);
		this.setReady(false);
		this.spawn(x, y-ArcadeGame.ZX_SPECTRUM_Y_RESOLUTION+8);
		this.appendSpritePath("Rocket");
		this.initSprite();
		this.currentAnimationFrame = 0;
		this.setTargetForDroppedObjects(true);
		this.framesSinceAnimationUpdate = 0;
		
		if(initialStage>=Rocket.FINAL_STAGE) {
			this.setBuilt(true);
			this.setRefueled(true);
			this.setStage(Rocket.FINAL_STAGE);
		}
		else if(initialStage>=Rocket.FUEL_ONLY_LEVEL_FIRST_STAGE) {
			this.setBuilt(true);
			this.setStage(Rocket.FUEL_ONLY_LEVEL_FIRST_STAGE);
		}
		else
			this.setStage(Rocket.REBUILDING_LEVEL_FIRST_STAGE);
		this.setSpriteForStage();
		this.spawn(x, y - ArcadeGame.ZX_SPECTRUM_Y_RESOLUTION + 8 - this.getHeight());
	}
	
	public void setSpriteForStage() {
		String currentSprite = "rocketStage"+Integer.toString(this.getStage());
		if(this.getStage() == Rocket.FINAL_STAGE)
			currentSprite = currentSprite + "_" + Integer.toString(this.getCurrentAnimationFrame());
		this.updateSprite(currentSprite);
	}
	
	public void advanceSprite() {
		framesSinceAnimationUpdate = (framesSinceAnimationUpdate+1)%Rocket.FRAMES_PER_ANIMATION_UPDATE;
		if(framesSinceAnimationUpdate==0) {
			currentAnimationFrame = ((currentAnimationFrame+1)%Rocket.FRAMES_IN_ANIMATION);
			this.setSpriteForStage();
		}
	}
	
	public void advanceStage() {
		if(!this.isRefueled()) {
			if(this.isBuilt())
				this.stage += Rocket.EASY_DIFFICULTY_FUEL_MULTIPLIER;
			else
				this.stage++;
			this.setSpriteForStage();
			this.updateCompletionStatus();
		}
	}
	
	public void updateCompletionStatus() {
		if(this.stage>=Rocket.FUEL_ONLY_LEVEL_FIRST_STAGE)
			this.setBuilt(true);
		if(this.stage>=Rocket.FINAL_STAGE) {
			this.setRefueled(true);
			this.setCanPickUp(true);
		}
	}
	
	@Override
	public void update() {
		super.update();
		if (this.isLoadIn()) {
			this.setyVelocity(1);
			if (this.getyPositionNext() > -10) {
				this.setyVelocity(0);
				this.setLoadIn(false);
			}
		}
		else if (this.getStage() == Rocket.FINAL_STAGE) {
			this.advanceSprite();
			if (this.isReady()) {
				this.setyVelocity(-3);
				if (this.getyPositionCurrent() + this.getHeight() < 0) {
					this.setClear(true);
			}

			}
		}
	}
	
	@Override
	public void collideWith(GameObject other) {
		if (other.getyPositionNext() > this.getHeight() - 32 && other.isCanBeCarried()) {
			this.advanceStage();
			other.setCanBeCarried(false);
			other.setIfImpactsScoreOnDeletion(true);
			other.setToBeDeleted(true);
			if (!this.isBuilt())
				this.getChildObjects().add(new RocketPiece("rocketTop"));
			else if (!this.isRefueled()) {
				this.getChildObjects().add(new Fuel());
		}
			
			
			}
		else if (this.isRefueled() && other.canPickUp()) {
//			this.setReady(true);
		}
	}

	//////////////////////////////////////////////
	/////////// GETTERS AND SETTERS //////////////
	//////////////////////////////////////////////
	
	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public boolean isBuilt() {
		return built;
	}

	public void setBuilt(boolean built) {
		this.built = built;
	}

	public boolean isRefueled() {
		return refueled;
	}

	public void setRefueled(boolean refueled) {
		this.refueled = refueled;
	}

	public String getSpriteName() {
		return spriteName;
	}

	public void setSpriteName(String spriteName) {
		this.spriteName = spriteName;
	}

	public int getCurrentAnimationFrame() {
		return currentAnimationFrame;
	}

	public void setCurrentAnimationFrame(int currentAnimationFrame) {
		this.currentAnimationFrame = currentAnimationFrame;
	}

	public boolean isClear() {
		return clear;
	}

	public void setClear(boolean clear) {
		this.clear = clear;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public boolean isLoadIn() {
		return loadIn;
	}

	public void setLoadIn(boolean loadIn) {
		this.loadIn = loadIn;
	}
	
}
