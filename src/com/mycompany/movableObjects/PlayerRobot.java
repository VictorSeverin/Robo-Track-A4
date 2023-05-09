package com.mycompany.movableObjects;

import com.codename1.ui.geom.Point;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.mycompany.a3.Game;
import com.mycompany.a3.GameWorld;
import com.mycompany.fixedObjects.Base;
import com.mycompany.fixedObjects.EnergyStation;
import com.mycompany.interfaces.IColection;
import com.mycompany.interfaces.ICollider;

public class PlayerRobot extends Robot {
	private static PlayerRobot playerRobot;
	public int width;
	public int height;
	public GameWorld gw;
	private Point topLeft, topRight, bottomLeft, bottomRight;

	private PlayerRobot(int size, double locationX, double locationY) {
		super(size, locationX, locationY, 0, 255, 0, 0);
		topLeft = new Point(-size / 2, size / 2);
		topRight = new Point(size / 2, size / 2);
		bottomLeft = new Point(-size / 2, -size / 2);
		bottomRight = new Point(size / 2, -size / 2);
		translate((float) locationX, (float) locationY);
	}

	public void move(int width, int height, int elapsedTime) {
		// super.setHeading(this.getHeading() + this.getSteeringDirection());
		if (this.getEnergyLevel() > 0 && this.getDamageLevel() < this.getMaxDamageLevel() && this.getSpeed() > 0) {
			this.setEnergyLevel(this.getEnergyLevel() - this.getEnergyConsumptionRate());
			float dx = (float) Math.cos(Math.toRadians(90 - (float) getHeading())) * this.getSpeed();
			float dy = (float) Math.sin(Math.toRadians(90 - (float) getHeading())) * this.getSpeed();

			// apply the displacement vector to the robot's position
			translate(dx, dy);
		}
	}

	public void respawn() {
		this.setColor(255, 0, 0);
		this.setEnergyLevel(10000);
		this.setMaximumSpeed(10);
		this.setDamageLevel(0);
	}

	@Override
	public void draw(Graphics g, Point p, Point pCmpRelScrn) {
		g.setColor(ColorUtil.rgb(this.getRed(), this.getGreen(), this.getBlue()));
		Transform gXform = Transform.makeIdentity();
		g.getTransform(gXform);
		Transform copy = gXform.copy();
		gXform.translate(pCmpRelScrn.getX(), pCmpRelScrn.getY());
		gXform.translate(getTranslate().getTranslateX(),
				getTranslate().getTranslateY());
		gXform.concatenate(getRotation());
		gXform.scale(getScale().getScaleX(), getScale().getScaleY());
		gXform.translate(-pCmpRelScrn.getX(), -pCmpRelScrn.getY());
		g.setTransform(gXform);
		g.fillRect(p.getX(),
				p.getY(), this.getSize(), this.getSize());
		g.setTransform(copy);

	}

	public static PlayerRobot getPlayerRobot(int size, double locationX, double locationY) {
		if (playerRobot == null) {
			playerRobot = new PlayerRobot(size, locationX, locationY);
		}
		return playerRobot;
	}
}
