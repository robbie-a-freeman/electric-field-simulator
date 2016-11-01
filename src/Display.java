import java.awt.Graphics;
import java.awt.Color;
import java.lang.Math;

import javax.swing.JPanel;


public class Display extends JPanel {

	/**
	 * Robbie Freeman Physics Project
	 */
	private static final long serialVersionUID = -4607251590105268195L;
	private int columns = 41; //graphing units
	private int rows = 31; //graphing units
	private int scale = 20; //pixel to unit scale, this is 5 meters
	private int margin = 70;
	private int height = scale * (rows + 1) + 2 * margin;
	private int width = scale * (columns + 1) + 2 * margin;
	private double k = 8.99 * Math.pow(10., 9.);
	private double Q1magnitude, Q2magnitude, Q3magnitude;
	private FieldSource Q1, Q2, Q3;

	public Display(double Q1magnitude, double Q2magnitude, double Q3magnitude)
	{
		this.Q1magnitude = Q1magnitude;
		this.Q2magnitude = Q2magnitude;
		this.Q3magnitude = Q3magnitude;
		Q1 = new FieldSource(margin + 10*scale, margin + 20*scale, this.Q1magnitude);
		Q2 = new FieldSource(margin + 30*scale, margin + 20*scale, this.Q2magnitude);
		Q3 = new FieldSource(margin + 20*scale, margin + 10*scale, this.Q3magnitude);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		this.setBackground(Color.WHITE);
		generateLines(g);
		g.setColor(Color.BLACK);
		g.drawOval(Q1.getX() - 20, Q1.getY() - 20, 40, 40);
		g.drawString("" + Math.round(Q1.getCharge() * Math.pow(10, 9)), Q1.getX() - 10, Q1.getY());
		g.drawOval(Q2.getX() - 20, Q2.getY() - 20, 40, 40);
		g.drawString("" + Math.round(Q2.getCharge() * Math.pow(10, 9)), Q2.getX() - 10, Q2.getY());
		g.drawOval(Q3.getX() - 20, Q3.getY() - 20, 40, 40);
		g.drawString("" + Math.round(Q3.getCharge() * Math.pow(10, 9)), Q3.getX() - 10, Q3.getY());
		for(int i = 0; i < rows; i++){ //rows
			for(int n = 0; n < columns; n++){ //columns
				placeVector(margin + scale * n, margin + scale * i, Q1, Q2, Q3, g);
			}
		}
		createVoltageLines(Q1, Q2, Q3, g);
	}
	private double toCoord(double coord, int type)
	{
		double coordinate;
		if(type == 0){ //Y coordinate
			coordinate = -1 * coord / 4 + 235 / 2 + 0.5;//70, 670 -> 100, -50
		}
		else{ //X coordinate
			coordinate = coord / 4 - 135/2 - 0.5; //70, 870 -> -50, 150
		}
		return coordinate;
	}
	private void generateLines(Graphics g)
	{
		g.setColor(new Color(200,200,200));
		for(int i = 0; i < columns; i++){ //vertical lines
			g.drawLine(margin + scale * i, margin, margin + scale * i,  height - margin - 2*scale);
		}
		for(int i = 0; i < rows; i++){ //horizontal lines
			g.drawLine(margin, margin + scale * i, width - margin - 2*scale,  margin + scale * i);
		}
	}
	private void placeVector(int particleX, int particleY, FieldSource Q1, FieldSource Q2, FieldSource Q3, Graphics g)
	{
		//get each field mag and dir, add fields in each direction, calculate angle, place vector

		double netY = getNet(getField(particleX, particleY, Q1, 0), getField(particleX, particleY, Q2, 0), getField(particleX, particleY, Q3, 0));
		double netX = getNet(getField(particleX, particleY, Q1, 1), getField(particleX, particleY, Q2, 1), getField(particleX, particleY, Q3, 1));
		double theta = getFinalAngle(netY, netX);
		double x = 6 * Math.cos(theta);
		double y = 6 * Math.sin(theta);
		
		double x1, y1, x2, y2;
		x1 = 0;
		x2 = 0;
		y1 = 0;
		y2 = 0;
		if(netY > 0 && netX > 0){
			x1 = particleX + x;
			y1 = particleY + y;
			x2 = particleX - x;
			y2 = particleY - y;
			drawArrows(g, x1, y1, theta, 1);
		}
		if(netY > 0 && netX < 0){
			x1 = particleX + x;
			y1 = particleY + y;
			x2 = particleX - x;
			y2 = particleY - y;
			drawArrows(g, x2, y2, theta, 2);
		}
		if(netY < 0 && netX < 0){

			x1 = particleX - x;
			y1 = particleY - y;
			x2 = particleX + x;
			y2 = particleY + y;
			drawArrows(g, x1, y1, theta, 3);
		}
		if(netY < 0 && netX > 0){

			x1 = particleX - x;
			y1 = particleY - y;
			x2 = particleX + x;
			y2 = particleY + y;
			drawArrows(g, x2, y2, theta, 4);
		}
		if(netY < 0 && netX == 0){

			x1 = particleX;
			y1 = particleY - 6;
			x2 = particleX;
			y2 = particleY + 6;
			drawArrows(g, x1, y1, theta, 5);
		}
		if(netY > 0 && netX == 0){

			x1 = particleX;
			y1 = particleY - 6;
			x2 = particleX;
			y2 = particleY + 6;
			drawArrows(g, x2, y2, theta, 6);
		}
		if(netY == 0 && netX > 0){

			x1 = particleX + 6;
			y1 = particleY;
			x2 = particleX - 6;
			y2 = particleY;
			drawArrows(g, x1, y1, theta, 7);
		}
		if(netY == 0 && netX < 0){

			x1 = particleX + 6;
			y1 = particleY;
			x2 = particleX - 6;
			y2 = particleY;
			drawArrows(g, x2, y2, theta, 8);
		}
		g.drawLine((int) x1, (int)y1, (int)x2, (int)y2);
	}
	private void drawArrows(Graphics g, double particleX, double particleY, double angle, int type)
	{
		double x2, y2;

		switch(type){
		case 1: 
			x2 = 2 * Math.cos(angle - 3 * Math.PI / 4) + particleX;
			y2 = 2 * Math.sin(angle - 3 * Math.PI / 4) + particleY;
			g.drawLine((int) particleX, (int) particleY, (int) x2, (int) y2);
			x2 = 2 * Math.cos(angle + 3 * Math.PI / 4) + particleX;
			y2 = 2 * Math.sin(angle + 3 * Math.PI / 4) + particleY;
			g.drawLine((int) particleX, (int) particleY, (int) x2, (int) y2);
			break;
		case 2:
			x2 = 2 * Math.cos(angle + Math.PI / 4) + particleX;
			y2 = 2 * Math.sin(angle + Math.PI / 4) + particleY;
			g.drawLine((int) particleX, (int) particleY, (int) x2, (int) y2);
			x2 = 2 * Math.cos(angle - Math.PI / 4) + particleX;
			y2 = 2 * Math.sin(angle - Math.PI / 4) + particleY;
			g.drawLine((int) particleX, (int) particleY, (int) x2, (int) y2);
			break;
		case 3:
			x2 = 2 * Math.cos(angle + Math.PI / 4) + particleX;
			y2 = 2 * Math.sin(angle + Math.PI / 4) + particleY;
			g.drawLine((int) particleX, (int) particleY, (int) x2, (int) y2);
			x2 = 2 * Math.cos(angle - Math.PI / 4) + particleX;
			y2 = 2 * Math.sin(angle - Math.PI / 4) + particleY;
			g.drawLine((int) particleX, (int) particleY, (int) x2, (int) y2);
			break;
		case 4:
			x2 = 2 * Math.cos(angle + 3 * Math.PI / 4) + particleX;
			y2 = 2 * Math.sin(angle + 3 * Math.PI / 4) + particleY;
			g.drawLine((int) particleX, (int) particleY, (int) x2, (int) y2);
			x2 = 2 * Math.cos(angle - 3 * Math.PI / 4) + particleX;
			y2 = 2 * Math.sin(angle - 3 * Math.PI / 4) + particleY;
			g.drawLine((int) particleX, (int) particleY, (int) x2, (int) y2);
			break;
		case 5:
			x2 = 2 * Math.cos(3 * Math.PI / 4) + particleX;
			y2 = 2 * Math.sin(3 * Math.PI / 4) + particleY;
			g.drawLine((int) particleX, (int) particleY, (int) x2, (int) y2);
			x2 = 2 * Math.cos(Math.PI / 4) + particleX;
			y2 = 2 * Math.sin(Math.PI / 4) + particleY;
			g.drawLine((int) particleX, (int) particleY, (int) x2, (int) y2);
			break;
		case 6:
			x2 = 2 * Math.cos(5 * Math.PI / 4) + particleX;
			y2 = 2 * Math.sin(5 * Math.PI / 4) + particleY;
			g.drawLine((int) particleX, (int) particleY, (int) x2, (int) y2);
			x2 = 2 * Math.cos(7 * Math.PI / 4) + particleX;
			y2 = 2 * Math.sin(7 * Math.PI / 4) + particleY;
			g.drawLine((int) particleX, (int) particleY, (int) x2, (int) y2);
			break;
		case 7:
			x2 = 2 * Math.cos(5 * Math.PI / 4) + particleX;
			y2 = 2 * Math.sin(5 * Math.PI / 4) + particleY;
			g.drawLine((int) particleX, (int) particleY, (int) x2, (int) y2);
			x2 = 2 * Math.cos(3 * Math.PI / 4) + particleX;
			y2 = 2 * Math.sin(3 * Math.PI / 4) + particleY;
			g.drawLine((int) particleX, (int) particleY, (int) x2, (int) y2);
			break;
		case 8:
			x2 = 2 * Math.cos(Math.PI / 4) + particleX;
			y2 = 2 * Math.sin(Math.PI / 4) + particleY;
			g.drawLine((int) particleX, (int) particleY, (int) x2, (int) y2);
			x2 = -2 * Math.cos(Math.PI / 4) + particleX;
			y2 = -2 * Math.sin(Math.PI / 4) + particleY;
			g.drawLine((int) particleX, (int) particleY, (int) x2, (int) y2);
			break;
		}
	}
	private double getFinalAngle(double EY, double EX)
	{
		double finalAngle;

		finalAngle = Math.atan(EY/EX);
		return finalAngle;
	}
	private double getField(int particleX, int particleY, FieldSource Q, int inputType) //input is 0 for Y, 1 for X
	{
		double finalE, constant;
		constant = (double) 3/2;

		if(inputType == 0){
			finalE = k * Q.getCharge() * (toCoord(Q.getY(), 0) - toCoord(particleY, 0))/ //accounts for flipped Y axis
					Math.pow(Math.pow(toCoord(Q.getX(), 1) - toCoord(particleX, 1), 2) + Math.pow(toCoord(Q.getY(), 0) - toCoord(particleY, 0), 2), constant);
		}
		else{
			finalE = -(k * Q.getCharge() * (toCoord(Q.getX(), 1) - toCoord(particleX, 1)))/
					Math.pow(Math.pow(toCoord(Q.getX(), 1) - toCoord(particleX, 1), 2) + Math.pow(toCoord(Q.getY(), 0) - toCoord(particleY, 0), 2), constant);
		}
		return finalE;
	}
	private double getNet(double E1, double E2, double E3)
	{
		double net;

		net = E1 + E2 + E3;
		return net;
	}
	private void createVoltageLines(FieldSource Q1, FieldSource Q2, FieldSource Q3, Graphics g)
	{
		double voltage1, voltage2, voltage3, voltageSum;
		int constant = 20;
		boolean labelExists = false;
		for(int y = margin; y < margin + rows*scale - scale; y++){
			for(int x = margin; x < margin + columns*scale - scale; x++){
				voltage1 = k * Q1.getCharge() / Math.sqrt(Math.pow(toCoord(x, 1) - toCoord(Q1.getX(), 1), 2) + Math.pow(toCoord(y, 0) - toCoord(Q1.getY(), 0), 2));
				voltage2 = k * Q2.getCharge() / Math.sqrt(Math.pow(toCoord(x, 1) - toCoord(Q2.getX(), 1), 2) + Math.pow(toCoord(y, 0) - toCoord(Q2.getY(), 0), 2));
				voltage3 = k * Q3.getCharge() / Math.sqrt(Math.pow(toCoord(x, 1) - toCoord(Q3.getX(), 1), 2) + Math.pow(toCoord(y, 0) - toCoord(Q3.getY(), 0), 2));
				voltageSum = Math.round(voltage1 + voltage2 + voltage3);
				if(distanceToCharge(x, y, Q1, Q2, Q3) > 11.5 && (voltageSum < -200 || voltageSum > 200) && (Math.abs(voltageSum % constant) <= 2 || Math.abs(voltageSum % constant) >= 18) && voltageSum < 200 && voltageSum > -370){
					g.drawLine(x, y, x, y);
				}
				else if(distanceToCharge(x, y, Q1, Q2, Q3) > 8.75 && voltageSum >= -200 && voltageSum % constant == 0 && voltageSum < 200){
					g.drawLine(x, y, x, y);
					if(voltageSum == 0 && !labelExists){
						g.drawString("0 V", x, y);
						labelExists = true;
					}
				}
			}
		}
	}
	private double distanceToCharge(int x, int y, FieldSource Q1, FieldSource Q2, FieldSource Q3){
		double distance1, distance2, distance3, distance;
		
		distance1 = Math.sqrt(Math.pow(toCoord(x, 1) - toCoord(Q1.getX(), 1), 2) + Math.pow(toCoord(y, 0) - toCoord(Q1.getY(), 0), 2));
		distance2 = Math.sqrt(Math.pow(toCoord(x, 1) - toCoord(Q2.getX(), 1), 2) + Math.pow(toCoord(y, 0) - toCoord(Q2.getY(), 0), 2));
		distance3 = Math.sqrt(Math.pow(toCoord(x, 1) - toCoord(Q3.getX(), 1), 2) + Math.pow(toCoord(y, 0) - toCoord(Q3.getY(), 0), 2));
		if(distance1 < distance2 && distance1 < distance3){
			distance = distance1;
		}
		else if(distance2 < distance1 && distance2 < distance3){
			distance = distance2;
		}
		else {
			distance = distance3;
		}
		return distance;
	}
}
