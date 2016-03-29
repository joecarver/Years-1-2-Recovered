import javax.swing.*;

/*
 * Help window opens and adds text but is not really readable - all one line of text
 * did not have time to fix this as its not a massive problem
 */

@SuppressWarnings("serial")
public class HelpWindow extends JFrame 
{
	public HelpWindow(String title)
	{
		super(title);
	}
	
	public void init()
	{
		JPanel text = new JPanel();
		this.setContentPane(text);
		JTextPane a = new JTextPane();
		String h = "The Mandelbrot set is displayed in the left panel. Click on any point in the Mandelbrot set" +
						" to display the corresponding point in the Julia set on the  right hand side panel. " +
						"Alternatively click the 'Update Live' box to enable hover-over display of Julia set. " +
						"When on click to view mode, individual sets can be set as favourites, which can be selected " +
						"from the drop-down box and displayed using the Display button.";
		a.setText(h);
		text.add(a);
		this.setSize(300, 400);
		this.setVisible(true);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
}