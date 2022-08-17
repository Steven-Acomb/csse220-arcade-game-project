import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MenuListener implements KeyListener, ActionListener{
	private Menu menu;
	public MenuListener(Menu menu){
		this.menu = menu;
	}

	@Override
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
        	menu.startGame();
        }
        if(e.getKeyCode() == KeyEvent.VK_F) {
        	if(menu.isFullscreen())
        		menu.setWindowed();
        	else
        		menu.setFullScreen();
        }
        if(e.getKeyCode() == KeyEvent.VK_C) {
        	menu.setShowingControls(!menu.isShowingControls());
        }
        if(e.getKeyCode() == KeyEvent.VK_X) {
        	menu.stopMenu();
        }
    }

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		menu.update();
	}	
}