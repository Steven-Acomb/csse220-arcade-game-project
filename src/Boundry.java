public class Boundry extends Terrain {
	
	public Boundry(String boundryName, int x, int y, boolean isOuter) {
		super(boundryName);
		this.spawn(x,y);
		this.appendSpritePath("Boundry");
		this.initSprite();
		this.initTerrain();
		this.setDissapatesEnemyLasers(isOuter);
		this.setDissapatesPlayerLasers(isOuter);
		this.setCorporeality(!isOuter);
		this.setHarmfulToEnemy(isOuter);
	}
	
	//////////////////////////////////////////////
	/////////// GETTERS AND SETTERS //////////////
	//////////////////////////////////////////////

}
