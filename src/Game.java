/***
 * This menu screen will appear when the game is started, user is able to choose between level 1 to 5. Returns to main menu 
 * if game is won/lost
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class Game{
	public JFrame frame; // initial frame
	JPanel panel = new JPanel();
	boolean begingame;
	String[] Strings = { "Level 1", "Level 2", "Level 3", "Level 4", "Level 5" }; // items in the dropdown menu
	String lvl = "Level 1";
	Gameloop loop = new Gameloop();
    Thread t = new Thread(loop);
	
	public static void main(String[] args) throws IOException {
		// make frame
		Game game = new Game();
		game.makeScreen();
	}
	
	public void makeScreen(){
		// initialize the frame
		frame = new JFrame("Pacman");
		frame.setVisible(true);
		frame.setSize(500,200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// initialize panel, and the "play button"
		JPanel panel = new JPanel();
		frame.add(panel);
		JButton play = new JButton("Play Pacman");
		panel.add(play);
		
		//add actionListener, start the game when button is pressed
		play.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
	              t.start();}
			});
		// initialize the combobox 
		JComboBox LeveList = new JComboBox(Strings);
		LeveList.setSelectedIndex(0);
		panel.add(LeveList);
		// combobox selection changed
		LeveList.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  lvl = Strings[LeveList.getSelectedIndex()];
			  } 
			} );
		frame.revalidate();
		
	}
	
	public void end(){
		loop = new Gameloop();
	    t = new Thread(loop);
	    makeScreen();
	}
	
	public class Gameloop implements Runnable {

	    private boolean keepRunning = true;

	    @Override
	    public void run() {
	        if (keepRunning) {
	        	frame.dispose();
				Level level = new Level();
				try {
					level.initialize(lvl);
					level.mainloop();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					level.frame.dispose();
				    end();
				} catch (IOException b) {
					b.printStackTrace();
				}
	        }
	    }

	}
}


