package com.mycompany.fixedObjects;

import java.util.Random;

import com.codename1.charts.models.Point;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.mycompany.a3.FixedObject;

public class Base extends FixedObject {
	private int sequenceNumber;

	/**
	 * @param size      - the size of the object
	 * @param locationX - location of the object on the X axis
	 * @param locationY - location of the object on the Y axis
	 * @param color     - color of the object specifically
	 **/
	public Base(int size, double locationX, double locationY, int sequenceNumber) {
		super(size, locationX, locationY, ColorUtil.CYAN);
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @return the sequence number of the given base
	 **/
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	@Override
	public void draw(Graphics g, Point p) {
		g.setColor(ColorUtil.CYAN);
		int[] xCoordinates = { (int) this.getLocationX() - 50, (int) this.getLocationX(),
				(int) this.getLocationX() + 50 };
		int[] yCoordinates = { (int) this.getLocationY() - 50, (int) this.getLocationY(),
				(int) this.getLocationY() - 50 };
		g.fillPolygon(xCoordinates, yCoordinates, 3);
	}

	@Override
	public void setColor(int color) {
	}

	@Override
	public String toString() {
		return "loc=" + Math.round(this.getLocationX() * 10.0) / 10.0 + ","
				+ Math.round(this.getLocationY() * 10.0) / 10.0 + " color=["
				+ this.getColor()
				+ " size="
				+ this.getSize() + " seqNum= " + this.sequenceNumber;
	}
}
