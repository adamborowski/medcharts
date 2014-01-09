/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 *
 * @author test
 */
public class KeyboardPressingDispatcher implements ActionListener
{

    private Map<Integer, Boolean> pressingMap = new HashMap<>();
    private int numPressed = 0;
    private Timer timer;
    private KeyboardListener target;

    public boolean isKeyDown(int key)
    {
        return pressingMap.get(key);
    }

    public boolean isKeyUp(int key)
    {
        return !pressingMap.get(key);
    }

    public boolean containsKey(int key)
    {
        return pressingMap.containsKey(key);
    }

    public void addKey(int key)
    {
        pressingMap.put(key, false);
        bindingSource.getInputMap().put(KeyStroke.getKeyStroke(key, 0, false), "KPD_" + key + "_pressed");
        bindingSource.getInputMap().put(KeyStroke.getKeyStroke(key, 0, true), "KPD_" + key + "_released");
        bindingSource.getActionMap().put("KPD_" + key + "_pressed", new BindingAction(key, true));
        bindingSource.getActionMap().put("KPD_" + key + "_released", new BindingAction(key, false));
    }

    public void removeKey(int key)
    {
        if (isKeyDown(key))
        {
            numPressed--;
        }
        bindingSource.getInputMap().put(KeyStroke.getKeyStroke(key, 0, false), null);
        bindingSource.getInputMap().put(KeyStroke.getKeyStroke(key, 0, true), null);
        bindingSource.getActionMap().put("KPD_" + key + "_pressed", null);
        bindingSource.getActionMap().put("KPD_" + key + "_released", null);
    }

    public void setKeyboardListener(KeyboardListener target)
    {
        this.target = target;
    }

    public KeyboardPressingDispatcher(JComponent bindingSource, int delay)
    {
        this.bindingSource = bindingSource;
        timer = new Timer(delay, this);
    }
    private JComponent bindingSource;

    @Override
    public void actionPerformed(ActionEvent e)
    {
        //tiknięto timer, wyślij
        target.keyPressing(this);
    }

    private class BindingAction extends AbstractAction
    {

        private int key;
        private boolean isPressed;

        public BindingAction(int key, boolean isPressed)
        {
            this.key = key;
            this.isPressed = isPressed;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            pressingMap.put(key, isPressed);
            int oldNumPressed = numPressed;
            numPressed += isPressed ? 1 : -1;//modyfikuj licznik wciśniętych przycisków
            if (oldNumPressed == 0 && numPressed == 1)
            {
                //wciśnięto pierwszy klawisz:
                timer.start();
            } else if (oldNumPressed == 1 && numPressed == 0)
            {
                timer.stop();
            }
            if(isPressed){
                target.keyPressed(KeyboardPressingDispatcher.this);
            }else{
                target.keyReleased(KeyboardPressingDispatcher.this);
            }
        }
    }

    public interface KeyboardListener
    {

        void keyPressing(KeyboardPressingDispatcher kpd);
        void keyReleased(KeyboardPressingDispatcher kpd);
        void keyPressed(KeyboardPressingDispatcher kpd);
    }
}
