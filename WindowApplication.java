import java.awt.*;
import java.awt.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.event.*;
import java.util.*;
import java.io.*;

public class WindowApplication extends JFrame 
{
	protected JButton buttonSave;
	protected JButton buttonReset;
	
	//0 is red 1 is green 2 is blue
	protected JButton[] minusButtons;
	protected JButton[] plusButtons;
	protected JTextField red;
	protected JTextField green;
	protected JTextField blue;
	protected int r, g, b;
	protected Vector<Integer> colorNum;
	protected Vector<String> colorName;
	protected String curColorName;
	 
	protected JList colorList;
	protected ColorComponent colorBox;
	
	private int winWidth;
	private int winHeight;
	
	public static void main (String argv []) 
	{
		new WindowApplication("Color Sampler");
	}

	//Constructor
	public WindowApplication(String title) 
	{
		super(title);		// call constructor of base class
		
		//instantiating arrays
		red = new JTextField("");
		green = new JTextField("");
		blue = new JTextField("");
		minusButtons = new JButton[3];
		plusButtons = new JButton[3];
		
		//Instantiate components
		colorBox = new ColorComponent();
		colorList = new JList();
		
		for (int i = 0; i < minusButtons.length; i++){
			minusButtons[i] = new JButton();
			minusButtons[i].setText("-");
			minusButtons[i].addActionListener(new ActionHandler());
		}
		
		for (int i = 0; i < minusButtons.length; i++){
			plusButtons[i]= new JButton();
			plusButtons[i].setText("+");
			plusButtons[i].addActionListener(new ActionHandler());
		}
		
		winWidth = 350;
		winHeight = 150;
		setSize(winWidth, winHeight);
		
		//add components and add their listers to the content pane
		addWindowListener(new WindowDestroyer());  
		
		getContentPane().setLayout(new GridLayout(4, 7));
		
		//adding buttons and labels
		getContentPane().add(new JLabel("Red:"));
		red.addActionListener(new ActionHandler());
		getContentPane().add(red);
		getContentPane().add(minusButtons[0]);
		getContentPane().add(plusButtons[0]);
		
		getContentPane().add(new JLabel("Green:"));
		green.addActionListener(new ActionHandler());
		getContentPane().add(green);
		getContentPane().add(minusButtons[1]);
		getContentPane().add(plusButtons[1]);
		
		getContentPane().add(new JLabel("Blue:"));
		blue.addActionListener(new ActionHandler());
		getContentPane().add(blue);
		getContentPane().add(minusButtons[2]);
		getContentPane().add(plusButtons[2]);
		
		buttonSave = new JButton("Save");
		buttonSave.addActionListener(new ActionHandler());
		getContentPane().add(buttonSave);
		
		buttonReset = new JButton("Reset");
		buttonReset.addActionListener(new ActionHandler());
		getContentPane().add(buttonReset);
		
		
		colorList.addListSelectionListener(new ListHandler());
		getContentPane().add(new JScrollPane(colorList));
		colorList.setSelectedIndex(0);
		getContentPane().add(colorBox);
		//After adding all buttons, labels, and listeners, make sure they are visible
		setVisible(true);
		
	}
	
	private class ListHandler implements ListSelectionListener{
		public void valueChanged(ListSelectionEvent e)
		{
		  if ( e.getSource() == colorList )
		     if ( !e.getValueIsAdjusting() )
		     {
		    	 //Handle list selection
		    	 int i = colorList.getSelectedIndex();
		    	 String s = (String) colorList.getSelectedValue();
		    	 curColorName = s;
		    	 r = colorNum.get(i*3 + 0);
		    	 g = colorNum.get(i*3 + 1);
		    	 b = colorNum.get(i*3 + 2);
		    	 red.setText(Integer.toString(r));
		    	 green.setText(Integer.toString(g));
		    	 blue.setText(Integer.toString(b));
				System.out.println("Position " + i + " selected: " +s);
		    }
		  	//Draw the new buffer
		  	repaint();
		}
	}
	
	// Define window adapter                                       
	private class WindowDestroyer extends WindowAdapter 
	{   

		/*
		 * Read from the files the name of a color and its RGB values
		 */
		public void windowOpened(WindowEvent e){
			//Try to read from file if it exist
			try {
				Scanner file = new Scanner(new BufferedReader(new FileReader("Color.txt")));
				colorName = new Vector<String>();
				colorNum = new Vector<Integer>();
				
				while (file.hasNext()){
					colorName.add(file.next());
					colorNum.add(file.nextInt());
					colorNum.add(file.nextInt());
					colorNum.add(file.nextInt());
					
				}
				
				colorList.setListData(colorName);
				/*
				 * Initialize the the color component with the first value from file
				 */
				curColorName = colorName.get(0);
				r = colorNum.get(0);
				g = colorNum.get(1);
				b = colorNum.get(2);
				red.setText(Integer.toString(r));
				green.setText(Integer.toString(g));
				blue.setText(Integer.toString(b));
				
				file.close();
				System.out.println("File open success");
			} catch (FileNotFoundException ex){
				System.out.println("File didn't open");
				
			}
		}
		
		// implement only the function that you want
		public void windowClosing(WindowEvent e) 
		{    
			/*
			 *Write to file the update RGB values 
			 */
			try {
				PrintWriter file = new PrintWriter("Color.txt");
			    ListIterator tList = colorNum.listIterator();
				for (int i = 0; i < colorName.size(); i++){
					file.print(colorName.get(i) + " ");
					file.print(tList.next() + " ");
					file.print(tList.next() + " ");
					file.println(tList.next() + " ");
				}
			
				file.close();
				System.out.println("File writing successful");
			} catch (Exception ex){
				System.out.println("fail to write to file");
				
			}
			System.exit(0);  
		} 
		
	} 
	
	
	// Define action listener                                       
		private class ActionHandler implements ActionListener 
		{      
			public void actionPerformed(ActionEvent e)
			{
				/*
				 * RGB are stored in 1D array
				 * Increment the index accordingly to get to those values
				 */
				if ( e.getSource() == buttonSave )
				{
					//Updates the RGB vector but doesn't write to file
					setTitle("Color Sampler");
					int i = colorName.indexOf(curColorName);
					colorNum.set(i * 3 + 0, r);
					colorNum.set(i * 3 + 1, g);
					colorNum.set(i * 3 + 2, b);
				}
				//Reset RGB text field from RGB vector
				else if ( e.getSource() == buttonReset ){
					System.out.println("Reset works");
					setTitle("Color Sampler");
					int i = colorList.getSelectedIndex();
					if (i < 0){
						i = 0;
					}
					System.out.println(i);
					curColorName = colorName.get(i);
					r = colorNum.get(i * 3 + 0);
					g = colorNum.get(i * 3 + 1);
					b = colorNum.get(i * 3 + 2);
					red.setText(Integer.toString(r));
					green.setText(Integer.toString(g));
					blue.setText(Integer.toString(b));
				}
				/*
				 * Modify RGB components of the color 
				 * Update the text fields
				 * and check if those updates are within bounds
				 */
				else if (e.getSource() == minusButtons[0]){
					System.out.println("Minus works");
					setTitle("Color Sampler*");
					if (r>0)
						r-=5;
					updateText();

				}
				else if (e.getSource() == minusButtons[1]){
					System.out.println("Minus works");
					setTitle("Color Sampler*");
					if (g>0)
						g-=5;
					updateText();

				}
				else if (e.getSource() == minusButtons[2]){
					System.out.println("Minus works");
					setTitle("Color Sampler*");
					if (b>0)
						b-=5;
					updateText();

				}
				else if (e.getSource() == plusButtons[0]){
					System.out.println("Plus works");
					setTitle("Color Sampler*");
					if (r<255)
						r+=5;
					updateText();

				}

				else if (e.getSource() == plusButtons[1]){
					System.out.println("Plus works");
					setTitle("Color Sampler*");
					if (g<255)
						g+=5;
					updateText();

				}
				else if (e.getSource() == plusButtons[2]){
					System.out.println("Plus works");
					setTitle("Color Sampler*");
					if (b<255)
						b+=5;
					updateText();
				}
				else if (e.getSource() == red){
					System.out.println("TextField can have listeners too");
					int i = Integer.parseInt(red.getText());
					if (i <= 255 && i >= 0){
						r = i;
					}
					else {
						red.setText(Integer.toString(r));
					}
				}
				else if (e.getSource() == green){
					int i = Integer.parseInt(green.getText());
					if (i <= 255 && i >= 0){
						g = i;
					}
					else {
						green.setText(Integer.toString(g));
					}
				}
				else if (e.getSource() == blue){
					int i = Integer.parseInt(blue.getText());
					if (i <= 255 && i >= 0){
						b = i;
					}
					else {
						blue.setText(Integer.toString(b));
					}
				}
				//Update visual color component when values have updated
				repaint();
			}
			//updates text fields
			private void updateText(){
				red.setText(Integer.toString(r));
				green.setText(Integer.toString(g));
				blue.setText(Integer.toString(b));
			}
		}
		
		
		//Draws Color component
		private class ColorComponent extends JComponent
		{
			public void paint(Graphics grap)
			{
				
				Color c = new Color(r, g, b);
				grap.setColor(c);
				grap.fillRect(1, 1, red.getWidth(), red.getHeight());
			}
		}
	
} 
