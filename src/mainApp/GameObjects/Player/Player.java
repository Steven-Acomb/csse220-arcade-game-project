package mainApp.GameObjects.Player;

import java.awt.Dimension;
import mainApp.GameObjects.GameObject;
import mainApp.GameObjects.Projectile.PlayerLaser;
import mainApp.GameObjects.Projectile.Projectile;
import mainApp.GameObjects.Terrain.Rocket;

public class Player extends GameObject {
	private static final double MAX_INTER_SHOT_COOLDOWN = 10;
	private static final double INTER_SHOT_COOLDOWN_SPEED = 2;
	private static final double MAX_INVINCIBILITY_DURATION = 12;
	private static final double INVINCIBILITY_DECAY_RATE = 0.1;
	private static final double MOVE_SPEED_MULTIPLIER = 1.0;
	
	private boolean onShotCooldown;
	private boolean firedThisFrame;
	private double interShotCooldown;
	private int livesRemaining;
	private double invincibilityTimeRemaining;
	private boolean invincible;
	private boolean grounded;
	private boolean inShip;
	
	private PlayerState movementState = PlayerState.STANDING;
	private PlayerState lastState = PlayerState.STANDING;
	private boolean facingLeft = false;
	public static final Dimension PLAYER_DIMENSIONS = new Dimension(16, 24);
	
	public enum PlayerState {
		STANDING, WALKING, FLYING, IN_SHIP
	}

	public Player(int row, int column) {
		super();
		this.spawn(row, column);
		
		this.setDebugName("Player");
		this.appendSpritePath("Jetman");
		this.initSprite();
		this.updateSprite("Jetman-Standing-Right-Invincible");
		
		this.setHarmfulToEnemy(false);
		this.setHarmfulToPlayer(false);
		this.setDissapatesEnemyLasers(true);
		this.setDissapatesPlayerLasers(false);
		this.setCanPickUp(true);
		
		this.onShotCooldown = false;
		this.firedThisFrame = false;
		this.interShotCooldown = MAX_INTER_SHOT_COOLDOWN;
		this.livesRemaining = 3;
		this.invincibilityTimeRemaining = MAX_INVINCIBILITY_DURATION;
		this.setInvinciblity(true);		
	}
	
	public void moveX(int i) {
		if (i > 0)
			this.turnRight();
		else if (i < 0)
			this.turnLeft();
		this.setxVelocity(i*Player.MOVE_SPEED_MULTIPLIER);
	}

	public void moveY(int i) {
		this.setyVelocity(i*Player.MOVE_SPEED_MULTIPLIER);
	}

	@Override
	public void shoot() {
		if((!onShotCooldown)&&(!firedThisFrame)) {
			Projectile playerLaser = new PlayerLaser((int)this.getxPositionCurrent(), (int)this.getyPositionCurrent(),
					this.facingLeft);
			this.getChildObjects().add(playerLaser);
			this.firedThisFrame = true;
			this.interShotCooldown = MAX_INTER_SHOT_COOLDOWN;
		}
	}
	
	public void handleWeaponCooldowns() {
		if((this.firedThisFrame)&&(this.interShotCooldown >= Player.MAX_INTER_SHOT_COOLDOWN))
			this.onShotCooldown = true;
		else {
			this.interShotCooldown -= Player.INTER_SHOT_COOLDOWN_SPEED;
			if(this.interShotCooldown <= 0)
				this.onShotCooldown = false;
		}
		this.firedThisFrame = false;
	}
	
	public void handleInvincibility() {
		if(this.isInvincible()){
			double timeRemaining = this.getInvincibilityTimeRemaining();
			timeRemaining -= Player.INVINCIBILITY_DECAY_RATE;
			if(timeRemaining<=0) {
				this.setInvinciblity(false);
				this.setInvincibilityTimeRemaining(Player.MAX_INVINCIBILITY_DURATION);
			}
			else {
				this.setInvincibilityTimeRemaining(timeRemaining);
			}
		}
		else {
			this.setInvincibilityTimeRemaining(Player.MAX_INVINCIBILITY_DURATION);
		}
	}

	public void update() {
		this.handleWeaponCooldowns();
		this.handleInvincibility();
		this.setxPositionCurrent(this.getxPositionNext());
		this.setyPositionCurrent(this.getyPositionNext());
		this.determineSprite();
		if(this.livesRemaining<=0)
			this.setToBeDeleted(true);
	}

	public void determineSprite() {
//		PlayerState previousState = this.movementState;
		if (this.getyVelocity() < 0)
			this.setGrounded(false);
		if(this.inShip)
			this.movementState = PlayerState.IN_SHIP;
		else if (!this.isGrounded())
			this.movementState = PlayerState.FLYING;
		else if (Math.abs(this.getxVelocity()) > 0)
			this.movementState = PlayerState.WALKING;
		else
			this.movementState = PlayerState.STANDING;

		if (this.facingLeft) {
			switch (this.movementState) {
			case IN_SHIP:
					this.updateSprite("nothing");
				break;
			case STANDING:
				if(this.isInvincible())
					this.updateSprite("Jetman-Standing-Left-Invincible");
				else
					this.updateSprite("Jetman-Standing-Left");
				break;
			case WALKING:
				if(this.isInvincible())
					this.updateSprite("Jetman-Walking-Left-Invincible");
				else {
					this.updateSprite("Jetman-Walking-Left");
				}
				break;
			case FLYING:
				if(this.isInvincible())
					this.updateSprite("Jetman-Flying-Left-Invincible");
				else
					this.updateSprite("Jetman-Flying-Left");
				break;
			}
		} else {
			switch (this.movementState) {
			case IN_SHIP:
				this.updateSprite("nothing");
			break;
			case STANDING:
				if(this.isInvincible())
					this.updateSprite("Jetman-Standing-Right-Invincible");
				else
					this.updateSprite("Jetman-Standing-Right");
				break;
			case WALKING:
				if(this.isInvincible())
					this.updateSprite("Jetman-Walking-Right-Invincible");
				else
					this.updateSprite("Jetman-Walking-Right");
				break;
			case FLYING:
				if(this.isInvincible())
					this.updateSprite("Jetman-Flying-Right-Invincible");
				else
					this.updateSprite("Jetman-Flying-Right");
				break;
			}
		}
	}

	@Override
	public void collideWith(GameObject other) {
		
		if(other.isTargetForDroppedObjects()&&other.canPickUp()) {
			if(this.getyPositionCurrent()+this.getHeight()>other.getHeight()-Rocket.VISIBLE_HEIGHT) {
				this.setInShip(true);
			}
		}
		
		if(!this.invincible) {
			if(other.isHarmfulToPlayer()) {
				this.livesRemaining--;
				this.setInvinciblity(true);
			}
		}
		if(other.isCorporeal()) {
			
			double vX = this.getxVelocity();
			double vY = this.getyVelocity();
			double xC = this.getxPositionCurrent();
			double yC = this.getyPositionCurrent();
			double xN = this.getxPositionNext();
			double yN = this.getyPositionNext();
			double w = this.getWidth();
			double h = this.getHeight();
			
			double xO = other.getxPositionCurrent();
			double yO = other.getyPositionCurrent();
			double wO = other.getWidth();
			double hO = other.getHeight();
			
			if((((xN+w)>=(xO))&&((xC+w)<=(xO)))) {
				if(vX>0) //Detected collision RIGHT
					this.setxPositionNext(xO-w);
				else {
//					System.out.println("                    Detected clipping RIGHT");
				}
			}
			else if(((xN<=(xO+wO))&&(xC>=(xO+wO)))) {
				if(vX<0) //Detected collision LEFT
					this.setxPositionNext(xN = xO+wO);
				else {
//					System.out.println("                    Detected clipping LEFT");
				}
			}
			else {
//				System.out.println("                    No X-Axis Collision Detected");
			}
			
			if((((yN+h)>=(yO))&&((yC+h)<=(yO)))) {
				if(vY>0) { //Detected collision DOWN
					this.setyPositionNext(yO-h);
					this.setyVelocity(0);
					this.setGrounded(true);
				}
				else {
//					System.out.println("                    Detected clipping DOWN");
				}
			}
			else if(((yN<=(yO+hO))&&(yC>=(yO+hO)))) {
				if(vY<0) //Detected collision UP
					this.setyPositionNext(yO+hO);
				else {
//					System.out.println("                    Detected clipping UP");
				}
			}
			else {
//				System.out.println("                    No Y-Axis Collision Detected");
			}		
		}
	}
	
	//////////////////////////////////////////////
	/////////// GETTERS AND SETTERS //////////////
	//////////////////////////////////////////////
	
	public int getLivesRemaining() {
		return livesRemaining;
	}

	public void setLivesRemaining(int livesRemaining) {
		this.livesRemaining = livesRemaining;
	}
	
	public void turnLeft() {
		this.facingLeft = true;
	}

	public void turnRight() {
		this.facingLeft = false;
	}

	public void stand() {
		this.movementState = PlayerState.STANDING;
	}

	public void walk() {
		this.movementState = PlayerState.WALKING;
	}

	public void fly() {
		this.movementState = PlayerState.FLYING;
	}
	
	public boolean isInvincible() {
//		if(invincible) 
//			System.out.println("Player invincible!");
//		else
//			System.out.println("Player vulnerable.");
		return invincible;
	}

	public void setInvinciblity(boolean invincible) {
		this.invincible = invincible;
	}

	public double getInvincibilityTimeRemaining() {
		return invincibilityTimeRemaining;
	}

	public void setInvincibilityTimeRemaining(double invincibilityTimeRemaining) {
		this.invincibilityTimeRemaining = invincibilityTimeRemaining;
	}

	public boolean isGrounded() {
		return grounded;
	}

	public void setGrounded(boolean grounded) {
		this.grounded = grounded;
	}

	public PlayerState getLastState() {
		return lastState;
	}

	public void setLastState(PlayerState lastState) {
		this.lastState = lastState;
	}

	public boolean isInShip() {
		return inShip;
	}

	public void setInShip(boolean inShip) {
		this.inShip = inShip;
	}

}