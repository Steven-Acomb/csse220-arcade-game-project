package mainApp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.Timer;

public class GameListener implements KeyListener, ActionListener{
	public static final String TICK_ACTION_COMMAND = "ADVANCE APPLICATION";
	public static final String PAUSE_ACTION_COMMAND = "PAUSE APPLICATION";
	public static final String UNPAUSE_ACTION_COMMAND = "UNPAUSE APPLICATION";
	public static final String EXIT_ACTION_COMMAND = "EXIT APPLICATION";
	private HashMap<Integer,Boolean> pressedKeyboard;
	private HashMap<Integer,Boolean> tappedKeyboard;
	private boolean doTickPassthrough = true;
	private Timer timer;
	private ActionListener tickListener;
	private int pauseKey = 0;
	private int exitKey = 0;
	private boolean pauseKeyDefined = false;
	private boolean exitKeyDefined = false;
	
	public GameListener(){
		this.pressedKeyboard = this.initKeyboard();
		this.tappedKeyboard = this.initKeyboard();
	}
	
	public GameListener(int delay, ActionListener tickListener){
		this.pressedKeyboard = this.initKeyboard();
		this.tappedKeyboard = this.initKeyboard();
		this.timer = new Timer(delay, this);
		this.tickListener = tickListener;
	}
	
	public void startTimer() {
		this.timer.start();
	}
	
	public void stopTimer() {
		this.timer.stop();
	}
	
	public void exit() {
		this.timer.stop();
		ActionEvent exitEvent = new ActionEvent(this, 0, EXIT_ACTION_COMMAND, System.currentTimeMillis(), 0);
		this.tickListener.actionPerformed(exitEvent);
	}
	
	public void togglePause() {
		if(this.doTickPassthrough) {
			ActionEvent pauseEvent = new ActionEvent(this, 0, PAUSE_ACTION_COMMAND, System.currentTimeMillis(), 0);
			this.tickListener.actionPerformed(pauseEvent);
		}
		else {
			ActionEvent unpauseEvent = new ActionEvent(this, 0, UNPAUSE_ACTION_COMMAND, System.currentTimeMillis(), 0);
			this.tickListener.actionPerformed(unpauseEvent);
		}
		this.doTickPassthrough = !this.doTickPassthrough;
	}
	
	public void resetTaps() {
		this.tappedKeyboard = this.initKeyboard();
	}
	
	public boolean isHeld(int key) {
		return pressedKeyboard.get(key);
	}
	
	public boolean wasTapped(int key) {
		return tappedKeyboard.get(key);
	}
	
	public void handleInitialPress(KeyEvent e) {
		pressedKeyboard.put(e.getKeyCode(),true);
		tappedKeyboard.put(e.getKeyCode(),true);
		
		//pause
		if(this.pauseKeyDefined &&(e.getKeyCode() == this.pauseKey))
			this.togglePause();
		
		//exit
		if(this.exitKeyDefined &&(e.getKeyCode() == this.exitKey))
			this.exit();
	}
	
	@Override
	public void keyPressed(KeyEvent e){
		for(Integer key : this.pressedKeyboard.keySet()) {
			if((e.getKeyCode() == key) && (pressedKeyboard.get(key) == false))
				this.handleInitialPress(e);
		}
    }
	
	@Override
	public void keyReleased(KeyEvent e) {
		for(Integer key : this.pressedKeyboard.keySet()) {
			if((e.getKeyCode() == key) && (pressedKeyboard.get(key) == true))
				pressedKeyboard.put(key,false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(doTickPassthrough) {
			ActionEvent tickEvent = new ActionEvent(this,e.getID(),TICK_ACTION_COMMAND,e.getWhen(), 0);
			this.tickListener.actionPerformed(tickEvent);
		}
	}
	
	private HashMap<Integer,Boolean> initKeyboard(){
		HashMap<Integer,Boolean> keyboard = new HashMap<Integer,Boolean>();
		keyboard.put(KeyEvent.VK_ENTER,false);
		keyboard.put(KeyEvent.VK_ENTER,false);
		keyboard.put(KeyEvent.VK_BACK_SPACE,false);
		keyboard.put(KeyEvent.VK_TAB,false);
		keyboard.put(KeyEvent.VK_CANCEL,false);
		keyboard.put(KeyEvent.VK_CLEAR,false);
		keyboard.put(KeyEvent.VK_SHIFT,false);
		keyboard.put(KeyEvent.VK_CONTROL,false);
		keyboard.put(KeyEvent.VK_ALT,false);
		keyboard.put(KeyEvent.VK_PAUSE,false);
		keyboard.put(KeyEvent.VK_CAPS_LOCK,false);
		keyboard.put(KeyEvent.VK_ESCAPE,false);
		keyboard.put(KeyEvent.VK_SPACE,false);
		keyboard.put(KeyEvent.VK_PAGE_UP,false);
		keyboard.put(KeyEvent.VK_PAGE_DOWN,false);
		keyboard.put(KeyEvent.VK_END,false);
		keyboard.put(KeyEvent.VK_HOME,false);
		keyboard.put(KeyEvent.VK_LEFT,false);
		keyboard.put(KeyEvent.VK_UP,false);
		keyboard.put(KeyEvent.VK_RIGHT,false);
		keyboard.put(KeyEvent.VK_DOWN,false);
		keyboard.put(KeyEvent.VK_COMMA,false);
		keyboard.put(KeyEvent.VK_MINUS,false);
		keyboard.put(KeyEvent.VK_PERIOD,false);
		keyboard.put(KeyEvent.VK_SLASH,false);
		keyboard.put(KeyEvent.VK_0,false);
		keyboard.put(KeyEvent.VK_1,false);
		keyboard.put(KeyEvent.VK_2,false);
		keyboard.put(KeyEvent.VK_3,false);
		keyboard.put(KeyEvent.VK_4,false);
		keyboard.put(KeyEvent.VK_5,false);
		keyboard.put(KeyEvent.VK_6,false);
		keyboard.put(KeyEvent.VK_7,false);
		keyboard.put(KeyEvent.VK_8,false);
		keyboard.put(KeyEvent.VK_9,false);
		keyboard.put(KeyEvent.VK_SEMICOLON,false);
		keyboard.put(KeyEvent.VK_EQUALS,false);
		keyboard.put(KeyEvent.VK_A,false);
		keyboard.put(KeyEvent.VK_B,false);
		keyboard.put(KeyEvent.VK_C,false);
		keyboard.put(KeyEvent.VK_D,false);
		keyboard.put(KeyEvent.VK_E,false);
		keyboard.put(KeyEvent.VK_F,false);
		keyboard.put(KeyEvent.VK_G,false);
		keyboard.put(KeyEvent.VK_H,false);
		keyboard.put(KeyEvent.VK_I,false);
		keyboard.put(KeyEvent.VK_J,false);
		keyboard.put(KeyEvent.VK_K,false);
		keyboard.put(KeyEvent.VK_L,false);
		keyboard.put(KeyEvent.VK_M,false);
		keyboard.put(KeyEvent.VK_N,false);
		keyboard.put(KeyEvent.VK_O,false);
		keyboard.put(KeyEvent.VK_P,false);
		keyboard.put(KeyEvent.VK_Q,false);
		keyboard.put(KeyEvent.VK_R,false);
		keyboard.put(KeyEvent.VK_S,false);
		keyboard.put(KeyEvent.VK_T,false);
		keyboard.put(KeyEvent.VK_U,false);
		keyboard.put(KeyEvent.VK_V,false);
		keyboard.put(KeyEvent.VK_W,false);
		keyboard.put(KeyEvent.VK_X,false);
		keyboard.put(KeyEvent.VK_Y,false);
		keyboard.put(KeyEvent.VK_Z,false);
		keyboard.put(KeyEvent.VK_OPEN_BRACKET,false);
		keyboard.put(KeyEvent.VK_BACK_SLASH,false);
		keyboard.put(KeyEvent.VK_CLOSE_BRACKET,false);
		keyboard.put(KeyEvent.VK_NUMPAD0,false);
		keyboard.put(KeyEvent.VK_NUMPAD1,false);
		keyboard.put(KeyEvent.VK_NUMPAD2,false);
		keyboard.put(KeyEvent.VK_NUMPAD3,false);
		keyboard.put(KeyEvent.VK_NUMPAD4,false);
		keyboard.put(KeyEvent.VK_NUMPAD5,false);
		keyboard.put(KeyEvent.VK_NUMPAD6,false);
		keyboard.put(KeyEvent.VK_NUMPAD7,false);
		keyboard.put(KeyEvent.VK_NUMPAD8,false);
		keyboard.put(KeyEvent.VK_NUMPAD9,false);
		keyboard.put(KeyEvent.VK_MULTIPLY,false);
		keyboard.put(KeyEvent.VK_ADD,false);
		keyboard.put(KeyEvent.VK_SEPARATER,false);
		keyboard.put(KeyEvent.VK_SEPARATOR,false);
		keyboard.put(KeyEvent.VK_SUBTRACT,false);
		keyboard.put(KeyEvent.VK_DECIMAL,false);
		keyboard.put(KeyEvent.VK_DIVIDE,false);
		keyboard.put(KeyEvent.VK_DELETE,false);
		keyboard.put(KeyEvent.VK_NUM_LOCK,false);
		keyboard.put(KeyEvent.VK_SCROLL_LOCK,false);
		keyboard.put(KeyEvent.VK_F1,false);
		keyboard.put(KeyEvent.VK_F2,false);
		keyboard.put(KeyEvent.VK_F3,false);
		keyboard.put(KeyEvent.VK_F4,false);
		keyboard.put(KeyEvent.VK_F5,false);
		keyboard.put(KeyEvent.VK_F6,false);
		keyboard.put(KeyEvent.VK_F7,false);
		keyboard.put(KeyEvent.VK_F8,false);
		keyboard.put(KeyEvent.VK_F9,false);
		keyboard.put(KeyEvent.VK_F10,false);
		keyboard.put(KeyEvent.VK_F11,false);
		keyboard.put(KeyEvent.VK_F12,false);
		keyboard.put(KeyEvent.VK_F13,false);
		keyboard.put(KeyEvent.VK_F14,false);
		keyboard.put(KeyEvent.VK_F15,false);
		keyboard.put(KeyEvent.VK_F16,false);
		keyboard.put(KeyEvent.VK_F17,false);
		keyboard.put(KeyEvent.VK_F18,false);
		keyboard.put(KeyEvent.VK_F19,false);
		keyboard.put(KeyEvent.VK_F20,false);
		keyboard.put(KeyEvent.VK_F21,false);
		keyboard.put(KeyEvent.VK_F22,false);
		keyboard.put(KeyEvent.VK_F23,false);
		keyboard.put(KeyEvent.VK_F24,false);
		keyboard.put(KeyEvent.VK_PRINTSCREEN,false);
		keyboard.put(KeyEvent.VK_INSERT,false);
		keyboard.put(KeyEvent.VK_HELP,false);
		keyboard.put(KeyEvent.VK_META,false);
		keyboard.put(KeyEvent.VK_BACK_QUOTE,false);
		keyboard.put(KeyEvent.VK_QUOTE,false);
		keyboard.put(KeyEvent.VK_KP_UP,false);
		keyboard.put(KeyEvent.VK_KP_DOWN,false);
		keyboard.put(KeyEvent.VK_KP_LEFT,false);
		keyboard.put(KeyEvent.VK_KP_RIGHT,false);
		keyboard.put(KeyEvent.VK_DEAD_GRAVE,false);
		keyboard.put(KeyEvent.VK_DEAD_ACUTE,false);
		keyboard.put(KeyEvent.VK_DEAD_CIRCUMFLEX,false);
		keyboard.put(KeyEvent.VK_DEAD_TILDE,false);
		keyboard.put(KeyEvent.VK_DEAD_MACRON,false);
		keyboard.put(KeyEvent.VK_DEAD_BREVE,false);
		keyboard.put(KeyEvent.VK_DEAD_ABOVEDOT,false);
		keyboard.put(KeyEvent.VK_DEAD_DIAERESIS,false);
		keyboard.put(KeyEvent.VK_DEAD_ABOVERING,false);
		keyboard.put(KeyEvent.VK_DEAD_DOUBLEACUTE,false);
		keyboard.put(KeyEvent.VK_DEAD_CARON,false);
		keyboard.put(KeyEvent.VK_DEAD_CEDILLA,false);
		keyboard.put(KeyEvent.VK_DEAD_OGONEK,false);
		keyboard.put(KeyEvent.VK_DEAD_IOTA,false);
		keyboard.put(KeyEvent.VK_DEAD_VOICED_SOUND,false);
		keyboard.put(KeyEvent.VK_DEAD_SEMIVOICED_SOUND,false);
		keyboard.put(KeyEvent.VK_AMPERSAND,false);
		keyboard.put(KeyEvent.VK_ASTERISK,false);
		keyboard.put(KeyEvent.VK_QUOTEDBL,false);
		keyboard.put(KeyEvent.VK_LESS,false);
		keyboard.put(KeyEvent.VK_GREATER,false);
		keyboard.put(KeyEvent.VK_BRACELEFT,false);
		keyboard.put(KeyEvent.VK_BRACERIGHT,false);
		keyboard.put(KeyEvent.VK_AT,false);
		keyboard.put(KeyEvent.VK_COLON,false);
		keyboard.put(KeyEvent.VK_CIRCUMFLEX,false);
		keyboard.put(KeyEvent.VK_DOLLAR,false);
		keyboard.put(KeyEvent.VK_EURO_SIGN,false);
		keyboard.put(KeyEvent.VK_EXCLAMATION_MARK,false);
		keyboard.put(KeyEvent.VK_INVERTED_EXCLAMATION_MARK,false);
		keyboard.put(KeyEvent.VK_LEFT_PARENTHESIS,false);
		keyboard.put(KeyEvent.VK_NUMBER_SIGN,false);
		keyboard.put(KeyEvent.VK_PLUS,false);
		keyboard.put(KeyEvent.VK_RIGHT_PARENTHESIS,false);
		keyboard.put(KeyEvent.VK_UNDERSCORE,false);
		keyboard.put(KeyEvent.VK_WINDOWS,false);
		keyboard.put(KeyEvent.VK_CONTEXT_MENU,false);
		keyboard.put(KeyEvent.VK_FINAL,false);
		keyboard.put(KeyEvent.VK_CONVERT,false);
		keyboard.put(KeyEvent.VK_NONCONVERT,false);
		keyboard.put(KeyEvent.VK_ACCEPT,false);
		keyboard.put(KeyEvent.VK_MODECHANGE,false);
		keyboard.put(KeyEvent.VK_KANA,false);
		keyboard.put(KeyEvent.VK_KANJI,false);
		keyboard.put(KeyEvent.VK_ALPHANUMERIC,false);
		keyboard.put(KeyEvent.VK_KATAKANA,false);
		keyboard.put(KeyEvent.VK_HIRAGANA,false);
		keyboard.put(KeyEvent.VK_FULL_WIDTH,false);
		keyboard.put(KeyEvent.VK_HALF_WIDTH,false);
		keyboard.put(KeyEvent.VK_ROMAN_CHARACTERS,false);
		keyboard.put(KeyEvent.VK_ALL_CANDIDATES,false);
		keyboard.put(KeyEvent.VK_PREVIOUS_CANDIDATE,false);
		keyboard.put(KeyEvent.VK_CODE_INPUT,false);
		keyboard.put(KeyEvent.VK_JAPANESE_KATAKANA,false);
		keyboard.put(KeyEvent.VK_JAPANESE_HIRAGANA,false);
		keyboard.put(KeyEvent.VK_JAPANESE_ROMAN,false);
		keyboard.put(KeyEvent.VK_KANA_LOCK,false);
		keyboard.put(KeyEvent.VK_INPUT_METHOD_ON_OFF,false);
		keyboard.put(KeyEvent.VK_CUT,false);
		keyboard.put(KeyEvent.VK_COPY,false);
		keyboard.put(KeyEvent.VK_PASTE,false);
		keyboard.put(KeyEvent.VK_UNDO,false);
		keyboard.put(KeyEvent.VK_AGAIN,false);
		keyboard.put(KeyEvent.VK_FIND,false);
		keyboard.put(KeyEvent.VK_PROPS,false);
		keyboard.put(KeyEvent.VK_STOP,false);
		keyboard.put(KeyEvent.VK_COMPOSE,false);
		keyboard.put(KeyEvent.VK_ALT_GRAPH,false);
		keyboard.put(KeyEvent.VK_BEGIN,false);
		keyboard.put(KeyEvent.VK_UNDEFINED,false);
		return keyboard;
	}
	
	//////////////////////////////////////////////
	/////////// GETTERS AND SETTERS //////////////
	//////////////////////////////////////////////
	
	public int getPauseKey() {
		return pauseKey;
	}

	public void setPauseKey(int pauseKey) {
		this.pauseKey = pauseKey;
		this.pauseKeyDefined = true;
	}

	public int getExitKey() {
		return exitKey;
	}

	public void setExitKey(int exitKey) {
		this.exitKey = exitKey;
		this.exitKeyDefined = true;
	}
}