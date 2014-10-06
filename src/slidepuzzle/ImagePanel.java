package slidepuzzle;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
	GridLayout gl;
	GameListener gamelisten;
	BufferedImage sourceImage;
	int sHeight;
	int sWidth;
	int loc = 0; //variable to assist in setting a location for the image slices
				 //compared to the seed
	private int[] seed = {8,0,1,4,5,2,3,6,7};
	JButton[] buttons, buttonpos;
	
	ImagePanel(BufferedImage sourceImage){
		this(sourceImage,8);
	}
	
	/**
	 * @param sourceImage Image being used in the puzzle
	 * @param empty Accepts what block will be empty for this puzzle round
	 */
	ImagePanel(BufferedImage sourceImage, int empty){
		gl = new GridLayout(3,3,0,0);
		gamelisten = new GameListener();
		setLayout(gl);
		this.sourceImage = sourceImage;
		this.sHeight = sourceImage.getHeight();
		this.sWidth = sourceImage.getWidth();
		buttonpos = new JButton[9];
		buttons = new JButton[9];
		
		
		for(int y = 0; y < sHeight; y+=(sHeight/3)){
			for(int x = 0; x < sWidth; x+=(sWidth/3)){
				
				buttons[loc] = new JButton(getImageSlice(x,y));
				buttons[loc].setPreferredSize(new Dimension(sWidth/3, sHeight/3));
				buttons[loc].setMargin(new Insets(0,0,0,0));
				buttons[loc].addActionListener(gamelisten);
				loc++;
			}
		}
		buttons[empty].setVisible(false);
		for(int x = 0; x<buttons.length; x++){
			buttons[seed[x]].setActionCommand(String.valueOf(x));
			add(buttons[seed[x]]);
		}
	}
	
	private ImageIcon getImageSlice(int xloc, int yloc){
		ImageIcon icon = new ImageIcon();
		int height = sourceImage.getHeight()/3;
		int width = sourceImage.getWidth()/3;
		BufferedImage slice = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		
		//copies pixels to image slice
		for(int y=0;y<height;y++){
			for(int x=0;x<width;x++){
				slice.setRGB(x,y, sourceImage.getRGB(x+xloc, y+yloc));
			}
		}

		icon.setImage(slice);
		
		return icon;
	}
	
	public int emptyNeighbor(int buttonPressed){
		
		return 99;
	}
	
	private class GameListener implements ActionListener{
		JButton btntemp;
		
		@Override
		public void actionPerformed(ActionEvent ae) {
			// TODO Auto-generated method stub
			System.out.print(ae.getActionCommand());
			System.out.println(" " + buttons[seed[Integer.valueOf(ae.getActionCommand())-1]].isVisible());
			btntemp = buttons[seed[Integer.valueOf(ae.getActionCommand())-1]];
			buttons[seed[Integer.valueOf(ae.getActionCommand())-1]] 
					= buttons[seed[Integer.valueOf(ae.getActionCommand())]];
			buttons[seed[Integer.valueOf(ae.getActionCommand())]] = btntemp;
			for(int x=0; x<buttons.length;x++){
				remove(buttons[seed[x]]);
				add(buttons[seed[x]]);
			}
			validate();
		}
		
	}
	
}
