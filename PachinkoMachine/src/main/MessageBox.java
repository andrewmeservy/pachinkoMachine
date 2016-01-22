package main;

import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageBox implements Runnable
{
	public int slot;
	public boolean isShot = false;
	
    public MessageBox() {}
    
    public void run()
    {
    	JPanel panel = new JPanel();
    	
    	JButton shoot = new JButton("SHOOT");
    	
    	JSlider slotPos = new JSlider();
    	slotPos.setMinimum(0);
    	slotPos.setMaximum(PachinkoV2.NUM_OF_SLOTS);
    	
    	slotPos.setMinorTickSpacing(1);
        slotPos.setMajorTickSpacing(5);
        slotPos.setPaintTicks(true);
        slotPos.setPaintLabels(true);
    	
    	slotPos.setValue(0);
    	
    	panel.add(slotPos);
    	panel.add(shoot);
    	
    	shoot.addActionListener(new ActionListener()
    	{
			public void actionPerformed(ActionEvent e)
			{
    			isShot = true;
			}
    	});
    	
    	slotPos.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				slot = slotPos.getValue();
			}
		});
    	
    	JOptionPane.showOptionDialog(null, panel,"Pachinko", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);
    }
}
