package application;

public class Array2D {
	private double x,y;
	public Array2D() {
		this.x = 0;
		this.y = 0;
	}
	public Array2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public String toString() {
		return this.x + " " + this.y;
	}
}
