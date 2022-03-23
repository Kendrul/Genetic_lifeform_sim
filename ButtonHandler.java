

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ButtonHandler implements ActionListener{
	
	public void actionPerformed(ActionEvent e) {
		//start button
       if (e.getSource() == Starter.getStartButton())
       {
    	   if (Starter.isStart() == true) Starter.setStart(false);
    	   else Starter.setStart(true);
       }
	}

}
