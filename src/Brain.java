import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Brain extends JFrame{

	public static void main(String[] args) {
		JFrame f = new JFrame("Vectors of an Electric Field");
		JOptionPane.showMessageDialog(f, "You are using the final (planned) version of Robbie Freeman's Electric Field Generator.\n" + "The coordinate grid is (-50, 150) and it's in meters.\n" + "Note: the program was initially built for the magnitudes 200, -100, and -500 nC.");
		String Q1 = JOptionPane.showInputDialog("Please enter the first charge's magnitude (in nanometers, at (0,0))");
		String Q2 = JOptionPane.showInputDialog("Please enter the second charge's magnitude (in nanometers, at (100,0))");
		String Q3 = JOptionPane.showInputDialog("Please enter the third charge's magnitude (in nanometers, at (50,50))");
		double Q1num = Double.parseDouble(Q1) / Math.pow(10, 9);
		double Q2num = Double.parseDouble(Q2) / Math.pow(10, 9);
		double Q3num = Double.parseDouble(Q3) / Math.pow(10, 9);
		Display d = new Display(Q1num, Q2num, Q3num);
		f.add(d);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(1000, 800);
		f.setVisible(true);
	}

}
