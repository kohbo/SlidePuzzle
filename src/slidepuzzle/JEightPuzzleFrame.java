package slidepuzzle;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * Main Application Window
 * This class builds the JFrame, adds the menu bar, and adds
 * the game window. The inner class acts as a listener for the menu, 
 * while the panel itself handles all game logic originating from 
 * the puzzle.
 * 
 * Expands on the assignment and adds the ability to quickly change
 * the source image. Plan was also to add a popup panel that could
 * should the original source image for help, but I felt it was too much.
 */
@SuppressWarnings("serial")
public class JEightPuzzleFrame extends JFrame {
	Insets inset;
	JMenuBar jmb;
	JMenu reset;
	JMenuItem resetgame, change, close;
	MenuListener menulistener = new MenuListener();
	BufferedImage bf;
	ImagePanel game;
	
	JEightPuzzleFrame(String title, String imagename){
		super(title);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		jmb = new JMenuBar();
		reset = new JMenu("Options");
		reset.addActionListener(menulistener);
		resetgame = new JMenuItem("Reset This Game");
		resetgame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK));
		//added the accelerator just to test it out, left it in because it seemed useful
		resetgame.addActionListener(menulistener);
		reset.add(resetgame);
		change = new JMenuItem("Change Image");
		change.addActionListener(menulistener);
		reset.add(change);
		jmb.add(reset);
		setJMenuBar(jmb);
		
		try{
			bf = ImageIO.read(new File(imagename));
		}catch(IOException e){
			JOptionPane.showMessageDialog(this, "Image File Not found");
		}
		game = new ImagePanel(bf);
		add(game);
		
		//Ensures Frame has enough space to hold all components, including borders
		pack();
		inset = getInsets();
		setSize(inset.left + inset.right + bf.getWidth(), 
				jmb.getHeight()+ inset.top + inset.bottom + bf.getHeight());
		
	}
	
	/**
	 * Inner class that handles Menu events and changes the game
	 * accordingly.
	 */
	private class MenuListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
			System.out.println(event.getActionCommand());
			switch(event.getActionCommand()){
				case "Reset This Game":
					game.resetGame();
					break;
				case "Change Image":
					JFileChooser choose = new JFileChooser();
					if(choose.showOpenDialog(getRootPane()) == JFileChooser.APPROVE_OPTION){
						try{
							bf = ImageIO.read(choose.getSelectedFile());
							remove(game);
							game = new ImagePanel(bf,(int)(Math.random()*2),(int)(Math.random()*2));
							add(game);
							pack();
							validate();
						}catch(IOException e){
							JOptionPane.showMessageDialog(getRootPane(), "Image File Not found");
						}
					}
					break;
			}
		}
		
	}
}
