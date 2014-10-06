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
		reset = new JMenu("Reset");
		reset.addActionListener(menulistener);
		resetgame = new JMenuItem("Reset This Game");
		resetgame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK));
		resetgame.addActionListener(menulistener);
		reset.add(resetgame);
		change = new JMenuItem("Change Image");
		change.addActionListener(menulistener);
		reset.add(change);
		jmb.add(reset);
		close = new JMenuItem("Exit");
		close.addActionListener(menulistener);
		jmb.add(close);
		setJMenuBar(jmb);
		
		try{
			bf = ImageIO.read(new File(imagename));
		}catch(IOException e){
			JOptionPane.showMessageDialog(this, "Image File Not found");
		}
		game = new ImagePanel(bf);
		add(game);
		
		//Ensures Frame has enough space to hold all components 
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
					resetGame();
					break;
				case "Change Image":
					JFileChooser choose = new JFileChooser();
					if(choose.showOpenDialog(getRootPane()) == JFileChooser.APPROVE_OPTION){
						try{
							bf = ImageIO.read(choose.getSelectedFile());
							resetGame();
						}catch(IOException e){
							JOptionPane.showMessageDialog(getRootPane(), "Image File Not found");
						}
					}
					break;
				case "Exit":
					System.exit(0);
					break;
			}
		}
		
	}
	
	/**
	 * Resets the game with a random empty box.
	 */
	private void resetGame() {
		remove(game);
		game = new ImagePanel(bf,(int)(Math.random()*8),(int)(Math.random()*8));
		add(game);
		pack();
		validate();
	}

}
