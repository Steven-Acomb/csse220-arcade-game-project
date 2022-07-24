package mainApp.GameObjects.Terrain;

import mainApp.GameObjects.GameObject;

public class Terrain extends GameObject {

	private String terrainName;
	
	public Terrain(String terrainName) {
		super();
		this.setTerrainName(terrainName);
		this.appendSpritePath("Terrain");
		this.initSprite();
	}
	
	public Terrain() {
		super();
		this.appendSpritePath("Terrain");
		this.initSprite();
	}
	
	public void initTerrain() {
		this.setDebugName(terrainName);
		this.updateSprite(terrainName);
	}
	
	@Override
	public void collideWith(GameObject other) {
		//
	}
	
	//////////////////////////////////////////////
	/////////// GETTERS AND SETTERS //////////////
	//////////////////////////////////////////////
	
	public String getTerrainName() {
		return terrainName;
	}

	public void setTerrainName(String terrainName) {
		this.terrainName = terrainName;
	}
}
