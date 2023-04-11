package com.mycompany.fixedObjects;

import com.codename1.charts.models.Point;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.mycompany.a3.FixedObject;

public class EnergyStation extends FixedObject {

	private int capacity;

	/**
	 * @param size      - the size of the object
	 * @param locationX - location of the object on the X axis
	 * @param locationY - location of the object on the Y axis
	 * @param color     - color of the object
	 **/
	public EnergyStation(int size, double locationX, double locationY) {
		super(size, locationX, locationY, ColorUtil.YELLOW);
		this.capacity = size;
	}

	@Override
	public void draw(Graphics g, Point p) {
		g.setColor(ColorUtil.BLACK);
		// g.drawRect((int) (p.getX() + this.getLocationX()), (int) (p.getY() +
		// this.getLocationY()), this.getSize(),
		// this.getSize());
		// g.fillArc((int) (p.getX() + this.getLocationX()), (int) (p.getY() +
		// this.getLocationY()),
		// 100, 100, 0, 360);
		g.fillArc((int) (p.getX() + this.getLocationX()), (int) (p.getY() +
				this.getLocationY()), this.getSize(), this.getSize(), 0, 360);
	}

	/**
	 * @return capacity of the given station
	 **/
	public int getCapacity() {
		return capacity;
	}

	/**
	 * 
	 * @param amount - new energy station capacity
	 *               Set capacity to 0. No setter method as to not allow setting a
	 *               capacity that is not proportional to size
	 */
	public void drain() {
		this.capacity = 0;
	}

	@Override
	public String toString() {
		return "loc=" + Math.round(this.getLocationX() * 10.0) / 10.0 + ","
				+ Math.round(this.getLocationY() * 10.0) / 10.0 + " color=["
				+ this.getColor() + "] size=" + this.getSize() + " capacity="
				+ this.getCapacity();
	}
}
