package Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import Enums.KeyName;

public class Controls implements KeyListener {

    private Map<Integer, KeyName> keyMappings = new HashMap<Integer, KeyName>();
    private Map<KeyName, Boolean> actionStates = new HashMap<KeyName, Boolean>();

    public Controls(int id) {
        if(id == 1){
            keyMappings.put(KeyEvent.VK_SPACE, KeyName.Pause);
            keyMappings.put(KeyEvent.VK_W, KeyName.Rotate);
            keyMappings.put(KeyEvent.VK_A, KeyName.Left);
            keyMappings.put(KeyEvent.VK_D, KeyName.Right);
            keyMappings.put(KeyEvent.VK_S, KeyName.Drop);
    
        }
        else if (id == 2){
            keyMappings.put(KeyEvent.VK_SPACE, KeyName.Pause);
            keyMappings.put(KeyEvent.VK_U, KeyName.Rotate);
            keyMappings.put(KeyEvent.VK_H, KeyName.Left);
            keyMappings.put(KeyEvent.VK_K, KeyName.Right);
            keyMappings.put(KeyEvent.VK_J, KeyName.Drop);
        }
        
        actionStates.put(KeyName.Left, false);
        actionStates.put(KeyName.Right, false);
        actionStates.put(KeyName.Rotate, false);
        actionStates.put(KeyName.Drop, false);
        actionStates.put(KeyName.Pause, false);
    }

    public boolean getKeyDown(KeyName key) {
        return actionStates.put(key, false);
    }

    public boolean getKey(KeyName key) {
        return actionStates.get(key);
    }
    
    public void press(KeyName key){
        actionStates.put(key, true);
    }

    public void keyPressed(KeyEvent keyEvent) {
        if(keyMappings.containsKey(keyEvent.getKeyCode())){
            if (actionStates.containsKey(keyMappings.get(keyEvent.getKeyCode()))) {
                actionStates.put(keyMappings.get(keyEvent.getKeyCode()), true);
            }
        }
    }

    public void keyReleased(KeyEvent keyEvent) {
        if(keyMappings.containsKey(keyEvent.getKeyCode())){
            if (actionStates.containsKey(keyMappings.get(keyEvent.getKeyCode()))) {
                actionStates.put(keyMappings.get(keyEvent.getKeyCode()), false);
            }
        }
    }

    public void keyTyped(KeyEvent e) {
    }
}