package com.mycompany.a3;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

import com.codename1.charts.util.ColorUtil;
import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.TextField;
import com.codename1.ui.util.UITimer;
import com.mycompany.fixedObjects.Base;
import com.mycompany.fixedObjects.EnergyStation;
import com.mycompany.interfaces.ICollider;
import com.mycompany.interfaces.IIterator;
import com.mycompany.movableObjects.Drone;
import com.mycompany.movableObjects.NonPlayerRobot;
import com.mycompany.movableObjects.Robot;
import com.mycompany.strategies.ChaseRobot;
import com.mycompany.strategies.NextBase;
import com.mycompany.movableObjects.PlayerRobot;
import com.codename1.ui.Command;

public class GameWorld extends Observable {
	private GameCollection objects;
	private int clockTime;
	private int remainingLives;
	private boolean sound;
	private int height;
	private int width;
	private Sound prWithDroneSound;
	private Sound prWithBaseSound;
	private Sound prWithEsSound;
	private Sound prWithNprSound;
	private Sound gameSound;
	private Sound alarm;
	public Random rand = new Random();

	public GameWorld() {
		this.objects = new GameCollection();
		this.clockTime = 0;
		this.remainingLives = 3;
		this.sound = false;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void createSounds() {
		prWithBaseSound = new Sound("yeet.wav");
		alarm = new Sound("alarm.wav");
	}

	public void changeStrategy() {
		IIterator iter = objects.getIterator();
		NonPlayerRobot npr = null;
		while (iter.hasNext()) {
			GameObject obj = iter.getNext();
			if (obj instanceof NonPlayerRobot) {
				npr = (NonPlayerRobot) obj;
				if (npr.getStrategy() == "ChaseRobot") {
					npr.setStrategy(new NextBase());
					npr.invokeStrategy(this, npr, 10);
				} else {
					npr.setStrategy(new ChaseRobot());
					npr.invokeStrategy(this, npr, 10);
				}
				System.out.println(npr);
			}
		}
	}

	public PlayerRobot getRobot() {
		IIterator iter = objects.getIterator();
		PlayerRobot pr = null;
		while (iter.hasNext()) {
			GameObject obj = iter.getNext();
			if (obj instanceof PlayerRobot) {
				pr = ((PlayerRobot) obj);
			}
		}
		return pr;
	}

	public GameCollection getObjects() {
		return objects;
	}

	public int getTime() {
		return clockTime;
	}

	public int getLivesLeft() {
		return remainingLives;
	}

	public void toggleSound() {
		this.sound = !sound;
		setChanged();
		notifyObservers(this);
	}

	public String getSound() {
		if (this.sound == true) {
			return "ON";
		} else {
			return "OFF";
		}
	}

	public void exitGame() {
		System.exit(0);
	}

	// TODO adjust acceleration according to energy level and damage
	public void accelerate() {
		System.out.println("Accelerating");
		IIterator iter = objects.getIterator();
		while (iter.hasNext()) {
			GameObject obj = iter.getNext();
			if (obj instanceof PlayerRobot) {
				PlayerRobot robot = (PlayerRobot) obj;
				if (robot.getSpeed() + 3 > robot.getMaximumSpeed()) {
					robot.setSpeed(robot.getMaximumSpeed());
				} else { // otherwise increment speed by 5
					robot.setSpeed(robot.getSpeed() + 3);
				}
			}
		}
		setChanged();
		notifyObservers(this);
	}

	public void brake() {
		prWithBaseSound.play();
		alarm.play();
		System.out.println("Breaking");
		IIterator iter = objects.getIterator();
		while (iter.hasNext()) {
			GameObject obj = iter.getNext();
			if (obj instanceof PlayerRobot) {
				PlayerRobot robot = (PlayerRobot) obj;
				if (robot.getSpeed() - 5 < 0) {
					robot.setSpeed(0);
				} else {
					robot.setSpeed(robot.getSpeed() - 5);
				}
			}
		}
		setChanged();
		notifyObservers(this);
	}

	public void steerLeft() {
		System.out.println("Steering Left");

		IIterator iter = objects.getIterator();
		while (iter.hasNext()) {
			GameObject obj = iter.getNext();
			if (obj instanceof PlayerRobot) {
				PlayerRobot robot = (PlayerRobot) obj;
				if (robot.getSpeed() > 0) {
					if (robot.getSteeringDirection() - 1 < -40) { // check if steering left will lead to less than -40
						robot.turn(-40);
					} else { // otherwise decrease steering direction by 5
						robot.turn(robot.getSteeringDirection() - 1);
					}
				}
				System.out.println("Steering Direction: " + robot.getSteeringDirection());
			}
		}
		setChanged();
		notifyObservers(this);
	}

	public void steerRight() {
		System.out.println("Steering Right");
		// changing steering direction of the robot 5 degree to the right. It changes
		// the direction, not the heading
		IIterator iter = objects.getIterator();
		while (iter.hasNext()) {
			GameObject obj = iter.getNext();
			if (obj instanceof PlayerRobot) {
				PlayerRobot robot = (PlayerRobot) obj;
				if (robot.getSpeed() > 0) {
					if (robot.getSteeringDirection() + 1 > 40) { // check if steering right will lead to more than 40
						// degrees
						robot.turn(40);
					} else { // otherwise decrease steering direction by 5
						robot.turn(robot.getSteeringDirection() + 1);
					}
				}
			}
		}
	}

	public void robotCollision(PlayerRobot pr, NonPlayerRobot npr) {
		if (pr.getDamageLevel() + 5 > pr.getMaxDamageLevel()) {
			pr.setDamageLevel(pr.getMaxDamageLevel());
		} else {
			pr.setDamageLevel(pr.getDamageLevel() + 5);
			pr.setColor(pr.getColor() + 30); // Make Color Brighter
			pr.setMaximumSpeed(pr.getMaximumSpeed() - 5);
		}

		if (npr.getDamageLevel() + 5 > npr.getMaxDamageLevel()) {
			npr.setDamageLevel(npr.getMaxDamageLevel());
		} else {
			npr.setDamageLevel(npr.getDamageLevel() + 5);
			npr.setMaximumSpeed(npr.getMaximumSpeed() - 5);
		}

		setChanged();
		notifyObservers(this);
	}

	public void droneColission(Robot robot) {
		if (robot.getDamageLevel() + 5 > robot.getMaxDamageLevel()) {
			robot.setDamageLevel(robot.getMaxDamageLevel());
		} else {
			robot.setDamageLevel(robot.getDamageLevel() + 2);
			robot.setColor(robot.getColor() + 30); // Make Color Brighter
			robot.setMaximumSpeed(robot.getMaximumSpeed() - 5);
		}

		setChanged();
		notifyObservers(this);
	}

	public void esCollision(EnergyStation es, Robot robot) {
		if (robot.getEnergyLevel() + es.getCapacity() > 100) {
			robot.setEnergyLevel(robot.getMaxEnergyLevel());
		} else {
			robot.setEnergyLevel(robot.getEnergyLevel() + es.getCapacity()); // recharghe energy level
		}
		es.drain(); // set energy station capacity to 0;
		es.setColor(es.getColor() + 30); // faint color
		objects.add(new EnergyStation(rand.nextInt(200 - 50) + 50, rand.nextInt(this.width - 200),
				rand.nextInt(this.height - 200)));
		robot.setMaximumSpeed(robot.getMaximumSpeed()); // set maximum speed back to normal

		setChanged();
		notifyObservers(this);
	}

	public void baseCollide(Robot robot, Base base) {
		if (base.getSequenceNumber() == robot.getLastBaseReached() + 1) {
			robot.setBase(robot.getLastBaseReached() + 1);
			System.out.println("Reached base" + base.getSequenceNumber());
			if (base.getSequenceNumber() == 4) {
				if (robot instanceof PlayerRobot) {
					System.out.println("Game Over! You won");

				} else if (robot instanceof NonPlayerRobot) {
					System.out.println("Game Over! You won");
					robot.setSpeed(0);
				}
			}

		}
		setChanged();
		notifyObservers(this);
	}

	public void tickClock(UITimer timer) {
		this.clockTime++;
		IIterator iter = objects.getIterator();
		while (iter.hasNext()) {
			GameObject obj = iter.getNext();
			if (obj instanceof MovableObject && !(obj instanceof NonPlayerRobot)) {
				MovableObject movObj = (MovableObject) obj;
				movObj.move(width, height, 10);
			}
			if (obj instanceof NonPlayerRobot) {
				NonPlayerRobot npr = (NonPlayerRobot) obj;
				npr.move(this, npr, width, height, 20);
			}
			if (obj instanceof PlayerRobot) {
				PlayerRobot pr = (PlayerRobot) obj;
				if (pr.getEnergyLevel() <= 0) {
					System.out.println("Game over, you ran out of energy! Line319");
					if (this.remainingLives - 1 < 0) {
						System.out.println("Game over, you ran out of lives! Line 321");
						timer.cancel();
						// exitGame();
					}
					this.remainingLives--;
					double oldLocationX = pr.getLocationX();
					double oldLocationY = pr.getLocationY();
					int oldSize = pr.getSize();
					objects.remove(pr);
					pr = PlayerRobot.getPlayerRobot(oldSize, oldLocationX, oldLocationY);
					objects.add(pr);
				}

			}
		}
		iter = objects.getIterator();
		while (iter.hasNext()) {
			ICollider objA = (ICollider) iter.getNext();
			IIterator iter2 = objects.getIterator();
			while (iter2.hasNext()) {
				ICollider objB = (ICollider) iter2.getNext();
				if (objB != objA && objA.collidesWith(objB)) {
					objA.handleCollision(objB, this);
				}
			}
		}
		setChanged();
		notifyObservers(this);
	}

	public void init() {

		PlayerRobot playerRobot = PlayerRobot.getPlayerRobot(80, 50, 50);
		objects.add(new Base(60, 250, 300, 1));
		objects.add(new Base(60, 300, 1000, 2));
		objects.add(new Base(60, 1100, 100, 3));
		objects.add(new Base(60, 1100, 800, 4));
		// objects.add(new Drone(50, rand.nextInt(this.width - 100),
		// rand.nextInt(this.height - 100), rand.nextInt(359),
		// 5));
		objects.add(new Drone(50, rand.nextInt(this.width), rand.nextInt(height),
				rand.nextInt(359), 5));
		objects.add(new EnergyStation(rand.nextInt(200 - 50) + 50, rand.nextInt(this.width - 200),
				rand.nextInt(this.height - 200)));
		objects.add(new EnergyStation(rand.nextInt(200 - 50) + 50, rand.nextInt(this.width - 200),
				rand.nextInt(this.height - 200)));
		objects.add(playerRobot);
		// objects.add(new NonPlayerRobot(80, 30, 80, new NextBase()));
		// objects.add(new NonPlayerRobot(80, 150, 80, new NextBase()));
		// objects.add(new NonPlayerRobot(80, 390, 80, new NextBase()));
	}
}
