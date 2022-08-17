public class Platform extends Terrain {
	
	public Platform(String platformName, int x, int y) {
		super(platformName);
		this.spawn(x,y);
		this.appendSpritePath("Platform");
		this.initSprite();
		this.initTerrain();
		this.setDissapatesEnemyLasers(true);
		this.setDissapatesPlayerLasers(true);
		this.setCorporeality(true);
	}
	
	//////////////////////////////////////////////
	/////////// GETTERS AND SETTERS //////////////
	//////////////////////////////////////////////

}
