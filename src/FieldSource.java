
public class FieldSource {
	private int x, y;
	private double chargeMagnitude;
	public FieldSource(int x, int y, double chargeMagnitude){
		this.x = x;
		this.y = y;
		this.chargeMagnitude = chargeMagnitude;
		System.out.println(chargeMagnitude);
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public double getChargeMagnitude(){
		return Math.abs(chargeMagnitude);
	}
	public double getCharge(){
		return chargeMagnitude;
	}
}
