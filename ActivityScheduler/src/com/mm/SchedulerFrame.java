package com.mm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

public class SchedulerFrame extends JFrame implements ActionListener{

	JPanel pnlNorth,pnlSouth;

	JLabel lblStartHour; 
	JTextField txtStartHour ;
	JLabel lblStartMin ; 
	JTextField txtStartMin ;
	JLabel lblLunchStartHour ;; 
	JTextField txtLunchStartTime ;
	JLabel lblStagMotivationStartHour ; 
	JTextField txtStagMotivStartHour ;
	JFileChooser jfc ;
	JButton btnOpenFile ;

	JTextArea taOutput ;;
	JButton sb ;;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	//All GUI construction
	public SchedulerFrame(){
//		super (new BorderLayout());

		
		//upper panel
		pnlNorth = new JPanel();

		lblStartHour = new JLabel("Start Hour:"); 
		txtStartHour = new JTextField(4);
		lblStartMin = new JLabel("Start Minute:"); 
		txtStartMin = new JTextField(4);
		 lblLunchStartHour = new JLabel("Lunch Start Time:"); 
		 txtLunchStartTime = new JTextField(4);
		lblStagMotivationStartHour = new JLabel("Stag Motivation Start Time:"); 
		 txtStagMotivStartHour = new JTextField(4);
		 jfc = new JFileChooser();
		 btnOpenFile  = new JButton("Upload a file...");
		 btnOpenFile.addActionListener(this);
		
		pnlNorth.add(lblStartHour); pnlNorth.add(txtStartHour);
		pnlNorth.add(lblStartMin);pnlNorth.add(txtStartMin);
		pnlNorth.add(lblLunchStartHour); pnlNorth.add(txtLunchStartTime);
		pnlNorth.add(lblStagMotivationStartHour); pnlNorth.add(txtStagMotivStartHour);
		pnlNorth.add(btnOpenFile);


//		//lower panel
		 pnlSouth = new JPanel();
		 taOutput = new JTextArea(30,40);
		taOutput.setWrapStyleWord(true);
		taOutput.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(taOutput);

		pnlSouth.add(scrollPane); 

		//add upper panel to frame
		this.add(pnlNorth,BorderLayout.NORTH);
		
		//add bottom panel to frame
		this.add(pnlSouth,BorderLayout.CENTER);
		
		this.setTitle("Organise your team's day!");

		this.setSize(900, 600);
		this.setLocationRelativeTo(null);
		this.setVisible(true);;
		this.setDefaultCloseOperation(SchedulerFrame.EXIT_ON_CLOSE);
		
	}
	@Override
	
public void actionPerformed(ActionEvent e) {
		
		try{
		Organiser o  = new Organiser();
		if(txtLunchStartTime.getText()!=null && !txtLunchStartTime.getText().equals(""))
			o.setLUNCH_START_TIME(Integer.parseInt(txtLunchStartTime.getText()));
		if(txtStagMotivStartHour.getText()!=null && !txtStagMotivStartHour.getText().equals(""))
			o.setSTAG_MOTIVIATION_START_TIME(Integer.parseInt(txtStagMotivStartHour.getText()));
		if(txtStartHour.getText() != null && !txtStartHour.getText().equals(""))
			o.setSTART_HOUR(Integer.parseInt(txtStartHour.getText()));
		if(txtStartMin.getText() !=null && !txtStartMin.getText().equals(""))
			o.setSTART_MINUTE(Integer.parseInt(txtStartMin.getText()));
	     //Handle open button action.
        if (e.getSource() == btnOpenFile) {
            int returnVal = jfc.showOpenDialog(SchedulerFrame.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();
                //This is where a real application would open the file.
                taOutput.setText(o.scheduler(file));
                taOutput.setEditable(false);
            } else {
                System.out.println("Open command cancelled by user.");
            }
         
        }
		}
		catch(Exception exc){
			taOutput.setText(exc.getMessage());
		}
	}

	static public void main(String[] a){
		SwingUtilities.invokeLater(() -> {
				new SchedulerFrame();
		});
	}
}
