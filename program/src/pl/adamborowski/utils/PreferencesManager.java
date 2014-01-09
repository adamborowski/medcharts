/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;

/**
 *
 * @author test
 */
abstract public class PreferencesManager
{

    final private String id;
    private Map<String, Object> map = new HashMap<>();
    private Map<String, Object> defaultsMap = new HashMap<>();
    private Map<String, ArrayList<JCheckBoxMenuItem>> checkboxes = new HashMap<>();
    private Map<String, ArrayList<JRadioButtonMenuItem>> radiobuttons = new HashMap<>();
    private Map<JRadioButtonMenuItem, Integer> radioButtonsOptions = new HashMap<>();
    private Map<Object, String> propertyOfComponent = new HashMap<>();
    private final Preferences prefs;
    private final Set<String> notSaveable = new HashSet<>();
    Listener listener = new Listener();

    public PreferencesManager(String id)
    {
        this.id = id;
        prefs = Preferences.userRoot().node(id);
    }

    protected abstract void updateCallback();

    private void update(String property, Object value)
    {
        if (value instanceof Boolean)
        {
            ArrayList<JCheckBoxMenuItem> cb = checkboxes.get(property);
            if (cb != null)
            {
                for (JCheckBoxMenuItem c : cb)
                {
                    updateCheckBox(c, value);
                }
            }
        } else if (value instanceof Integer)
        {
            ArrayList<JRadioButtonMenuItem> rb = radiobuttons.get(property);
            if (rb != null)
            {
                for (JRadioButtonMenuItem r : rb)
                {
                    updateRadioButton(r, value);
                }
            }
        }
        updateCallback();
    }

    public void set(String property, Object value)
    {
        map.put(property, value);
        if (!notSaveable.contains(property))
        {
            try
            {
                //zapisz do preferences
                prefs.putByteArray(property, Serialization.objectToBytes(value));
            } catch (IOException ex)
            {
                throw new RuntimeException(ex.getMessage());
            }

        }
        update(property, value);
    }

    public void initProperty(String property, Object value)
    {
        initProperty(property, value, true);
    }

    public void initProperty(String property, Object value, boolean saveableProperty)
    {
        defaultsMap.put(property, value);
        if (!saveableProperty)
        {
            notSaveable.add(property);
        }
    }

    public Object get(String name)
    {
        if (!map.containsKey(name))
        {
            //pobierz z preferences
            byte[] objectBytes = prefs.getByteArray(name, null);
            Object objectInstance;
            if (!notSaveable.contains(name))
            {
                //jeśli jest zapisywalny to pobierz
                if (objectBytes == null)
                {
                    objectInstance = defaultsMap.get(name);
                    try
                    {
                        prefs.putByteArray(name, Serialization.objectToBytes(objectInstance));
                    } catch (IOException ex)
                    {
                        throw new RuntimeException(ex.getMessage());
                    }
                } else
                {
                    try
                    {
                        objectInstance = Serialization.bytesToObject(objectBytes);
                    } catch (IOException | ClassNotFoundException ex)
                    {

                        throw new RuntimeException(ex.getMessage());
                    }
                }
            } else
            {
                //jeśli niezapisywalny, to po prostu defaults
                objectInstance = defaultsMap.get(name);
            }
            map.put(name, objectInstance);
            return objectInstance;
        }
        return map.get(name);
    }

    public void addCheckBox(JCheckBoxMenuItem item, String property)
    {

        ArrayList<JCheckBoxMenuItem> items = checkboxes.get(property);
        if (items == null)
        {
            items = new ArrayList<>();
            checkboxes.put(property, items);
        }
        items.add(item);
        updateCheckBox(item, get(property));
        item.addActionListener(listener);
        propertyOfComponent.put(item, property);
    }

    public void addRadioButton(JRadioButtonMenuItem item, String property, int option)
    {

        ArrayList<JRadioButtonMenuItem> items = radiobuttons.get(property);
        if (items == null)
        {
            items = new ArrayList<>();
            radiobuttons.put(property, items);
        }
        items.add(item);
        radioButtonsOptions.put(item, option);
        updateRadioButton(item, get(property));
        item.addActionListener(listener);
        propertyOfComponent.put(item, property);
    }

    private void updateCheckBox(JCheckBoxMenuItem c, Object value)
    {
        c.setSelected((boolean) value);
    }

    private void updateRadioButton(JRadioButtonMenuItem r, Object value)
    {
        Integer option = radioButtonsOptions.get(r);
        r.setSelected(option.equals(value));
    }

    class Listener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() instanceof JCheckBoxMenuItem)
            {
                JCheckBoxMenuItem cb = (JCheckBoxMenuItem) e.getSource();
                boolean selected = cb.isSelected();
                String name = propertyOfComponent.get(cb);
                set(name, selected);
            } else if (e.getSource() instanceof JRadioButtonMenuItem)
            {
                JRadioButtonMenuItem rb = (JRadioButtonMenuItem) e.getSource();
                String name = propertyOfComponent.get(rb);
                int option=radioButtonsOptions.get(rb);
                set(name, option);
            }
        }
    }
}
