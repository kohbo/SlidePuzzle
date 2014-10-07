package slidepuzzle;

import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Extends JButton and allows an integer id to be controlled
 * and tied to each button.
 */
@SuppressWarnings("serial")
public class ImageButton extends JButton {
	private int id;
	ImageButton(Icon icon){
		super(icon);
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id; 
	}
}
