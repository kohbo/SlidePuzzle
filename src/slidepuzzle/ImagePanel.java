package slidepuzzle;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This class holds the game pieces and handles all
 * inner game logic. The game is initiated the same way 
 * way every time for this assignment, but randomizes
 * itself every next try.
 */
@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
	GridLayout gl;
	GameListener listener;
	BufferedImage sourceImage;
	int sHeight;
	int sWidth;
	private int[][] seed = {{8,0,1}, 	//Seed array acts as a cipher to discern where an image
							{4,5,2},	//slice will be placed by the builtButtonArray method.
							{3,6,7}};	//Example: seed[0][0] is 8. This means the block at at
										//the top left of the screen will be the 9th image slice (0-8).
	ImageButton[][] buttons;
	
	/**
	 * @param sourceImage Image being used in the puzzle
	 * 
	 * Empty block defaults to 0,0 using this constructor.
	 */
	ImagePanel(BufferedImage sourceImage){
		this(sourceImage,0,0);
	}
	
	/**
	 * @param sourceImage Image being used in the puzzle
	 * @param empty Accepts what block will be empty for this puzzle round
	 */
	ImagePanel(BufferedImage sourceImage, int emptyx, int emptyy){
		gl = new GridLayout(3,3,0,0);
		setLayout(gl);
		listener = new GameListener();
		this.sourceImage = sourceImage;
		this.sHeight = sourceImage.getHeight();
		this.sWidth = sourceImage.getWidth();
		
		buildButtonArray(emptyx, emptyy);
	}

	/**
	 * @param emptyx x-coordinate for the empty button
	 * @param emptyy y-coordinate for the empty button
	 * 
	 * Builds the array of buttons and adds them to the JPanel, using the
	 * seed array as a guide. Each button is registered to listener that
	 * handles input and calls the checkWin method to check if player
	 * has won.
	 */
	private void buildButtonArray(int emptyx, int emptyy) {
		ImageButton[] buttonstemp = new ImageButton[9];
		buttons = new ImageButton[3][3];
		int loc = 0;
		
		for(int y = 0; y < sHeight; y+=(sHeight/3)){
			for(int x = 0; x < sWidth; x+=(sWidth/3)){
				
				buttonstemp[loc] = new ImageButton(getImageSlice(x,y));
				buttonstemp[loc].setPreferredSize(new Dimension(sWidth/3, sHeight/3));
				buttonstemp[loc].setMargin(new Insets(0,0,0,0));
				buttonstemp[loc].addActionListener(listener);
				loc++;
			}
		}
		
		loc = 0;
		for(int y=0; y<3;y++){
			for(int x = 0; x < 3; x++){
				buttons[x][y] = buttonstemp[seed[y][x]];
				buttons[x][y].setId(seed[y][x]);
				buttons[x][y].setActionCommand(String.valueOf(loc++));
				add(buttons[x][y]);
			}
		}
		buttons[emptyx][emptyy].setVisible(false);
	}
	
	/**
	 * @param buttonid
	 * 
	 * Handles the switching of buttons. Utilizes a try...catch block to
	 * help handle when looking for an empty box that doesn't exist in a
	 * minimal amount of code. 
	 */
	private void switchNeighbor(int buttonid){
		ImageButton temp;
		int locx = buttonid%3;
		int locy = buttonid/3;
		int empty = 99;
		System.out.println("Clicked (" + locx + " : " + locy + ")");
		
		for(int x = -1; x<2 && empty==99; x++){
			try{
				if(!(buttons[locx+x][locy].isVisible())){
					empty = locy*3+(locx+x);
					System.out.println("Empty Position: " + empty);
					temp = buttons[locx][locy];
					buttons[locx][locy] = buttons[locx+x][locy];
					buttons[locx][locy].setActionCommand(String.valueOf(empty));
					buttons[locx+x][locy] = temp;
					buttons[locx+x][locy].setActionCommand(String.valueOf(empty));
				}
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}
		for(int y = -1; y<2 && empty==99; y++){
			try{
				if(!(buttons[locx][locy+y].isVisible())){
					empty = (locy+y)*3+(locx);
					System.out.println("Empty Position: " + empty);
					temp = buttons[locx][locy];
					buttons[locx][locy] = buttons[locx][locy+y];
					buttons[locx][locy].setActionCommand(String.valueOf(empty));
					buttons[locx][locy+y] = temp;
					buttons[locx][locy+y].setActionCommand(String.valueOf(empty));
				}
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}
		
		if(empty != 99){
			updateGame();
		}
	}
	
	
	/**
	 * @param xloc x-coordinate to begin slicing source image
	 * @param yloc y-coordinate to begin slicing source image
	 * @return icon sliced image icon
	 * 
	 * Takes a x and y coordinate and returns an image slice that covers to 1/9 the
	 * area of the original image.
	 */
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
	
	/**
	 * Returns true if the player has won, false if not.
	 * 
	 * Checks the order of the button ids and compares them to a
	 * predefined order.
	 */
	boolean checkWin(){
		int[] winList = {0,1,2,3,4,5,6,7,8};
		int[] idList = new int[9];
		System.out.println("winList\tidList");
		for(int x=0; x<idList.length; x++){
			idList[x] = buttons[x%3][x/3].getId();
			System.out.println(winList[x] + "\t" + idList[x]);
			if(winList[x] != idList[x]){
				return false;
			}
		}
		return true;
	}
	
	
	void resetGame(){
		removeAll();
		buildButtonArray((int)(Math.random()*2),(int)(Math.random()*2));
		repaint();
	}
	
	void updateGame(){
		removeAll();
		for(int y=0; y<3;y++){
			for(int x = 0; x < 3; x++){
				add(buttons[x][y]);
			}
		}
		validate();
	}
	
	
	/**
	 * Handles click events and calls the necessary methods
	 * to handle winning the game.
	 */
	class GameListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			switchNeighbor(Integer.valueOf(e.getActionCommand()));
			if(checkWin()){
				while(JOptionPane.showConfirmDialog(null, "Click OK to reset the game!", 
						"Winner", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION);
				resetGame();
			}
		}
	}
}
