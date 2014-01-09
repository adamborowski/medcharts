/*
 * To change this template, choose Tools | Templates
 * a
 @Override
 public Float convertForward(Integer value)
 {
 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
 }

 @Override
 public Integer convertReverse(Float value)
 {
 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
 }
 nd open the template in the editor.
 */
package pl.adamborowski.app;

import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import pl.adamborowski.medcharts.MedChartPreferences;
import pl.adamborowski.utils.PreferencesManager;
import pl.adamborowski.utils.interfaces.IProgressListener;
import pl.adamborowski.utils.Serialization;

/**
 *
 * @author test
 */
public final class PlotterApplication extends javax.swing.JFrame
{

    private static JComponent _rootPane;

    public static void showMessageDialog(String message, String title, int messageType)
    {
        JOptionPane.showMessageDialog(_rootPane, message, title, messageType);
    }

    /**
     * Creates new form PlotterApplication
     */
    public PlotterApplication()
    {
        initComponents();
        initSettings();

//        SystemUtil.showOnScreen(0, this);
        medChart.setLayerVisibilityMenu(visibilityMenu);
        medChart.setExportMenu(exportMenu);
        _rootPane = rootPane;
        setIconImage(Toolkit.getDefaultToolkit().getImage(PlotterApplication.class.getResource("resources/app.png")));
        initHelp();
        updateLastOpened(null);
        ///
        final File file = new File("/home/test/Dokumenty/projekty/LargePlot/assets/moj_assembly/Assembly.xml");
        if (file.exists())
        {
            open(file);
        }
    }

    /**
     *
     * @param assemblyFile jeśli null, to nie dodaje do listy
     */
    private void updateLastOpened(File assemblyFile)
    {
        final String REOPEN = "reopen";
        //najpierw dodaj folder do listy ostatnich
        Preferences prefs = Preferences.userRoot().node("opening");
        byte[] filePathsSource = prefs.getByteArray(REOPEN, null);
        ArrayList<String> filePaths;
        if (filePathsSource == null)
        {
            filePaths = new ArrayList<>();
        } else
        {
            try
            {
                filePaths = (ArrayList<String>) Serialization.bytesToObject(filePathsSource);
            } catch (ClassNotFoundException | IOException ex)
            {
                filePaths = new ArrayList<>();
            }
        }
        if (assemblyFile != null)
        {
            //file=null jesli tylko chcemy wyswietlic ostatnie elementy
            //dodaj nowy item do listy
            filePaths.add(assemblyFile.getAbsolutePath());
        }
        List l = filePaths.subList(
                filePaths.size() - Math.min(filePaths.size(), 9),
                filePaths.size());
        filePaths = new ArrayList<>(l);
        try
        {
            //zapisz
            prefs.putByteArray(REOPEN, Serialization.objectToBytes(filePaths));
        } catch (IOException ex)
        {
        }
        //dodaj item do naszego menu
        reopenMenu.removeAll();
        int c = 0;
        Collections.reverse(filePaths);
        for (final String i : filePaths)
        {
            c++;
            JMenuItem mi = new JMenuItem(c + ": " + i);
            mi.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_0 + c, java.awt.event.InputEvent.CTRL_MASK));
            mi.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    open(new File(i));
                }
            });
            reopenMenu.add(mi);
        }
    }

    private void open(File f)
    {
        if (medChart.getAssembly() != null)
        {
            if (!trySaveProgress())
            {
                return;
            }
        }
        try
        {
            medChart.setAssembly(f);
            updateLastOpened(f);
        } catch (Exception ex)
        {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage(), "Problem z importowaniem", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        help = new javax.swing.JDialog();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        helpLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        helpLabel2 = new javax.swing.JLabel();
        importDialog = new javax.swing.JDialog();
        importProgressBar = new javax.swing.JProgressBar();
        processLabel = new javax.swing.JLabel();
        labelOptionGroup = new javax.swing.ButtonGroup();
        medChart = new pl.adamborowski.medcharts.MedChart();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        reopenMenu = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        exportMenu = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        visibilityMenu = new javax.swing.JMenu();
        jCheckBoxMenuItem3 = new javax.swing.JCheckBoxMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        option_hint_all = new javax.swing.JRadioButtonMenuItem();
        option_hint_current = new javax.swing.JRadioButtonMenuItem();
        option_hint_off = new javax.swing.JRadioButtonMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenu6 = new javax.swing.JMenu();
        option_hint_value = new javax.swing.JRadioButtonMenuItem();
        option_hint_bottom = new javax.swing.JRadioButtonMenuItem();
        option_hint_mouse = new javax.swing.JRadioButtonMenuItem();
        jMenu7 = new javax.swing.JMenu();
        option_save = new javax.swing.JRadioButtonMenuItem();
        option_dontSave = new javax.swing.JRadioButtonMenuItem();
        option_confirmSave = new javax.swing.JRadioButtonMenuItem();
        option_hideHovered = new javax.swing.JCheckBoxMenuItem();
        jMenu2 = new javax.swing.JMenu();
        helpMenuItem = new javax.swing.JMenuItem();

        help.setTitle("Pomoc");
        help.setMinimumSize(new java.awt.Dimension(636, 476));
        help.setResizable(false);

        helpLabel1.setBackground(new java.awt.Color(204, 204, 204));
        helpLabel1.setText("<HTML><font color=\"#888888\"><PRE><b>Panning:</b>\n\t- Przeciąganie myszką z wciśniętym prawym przyciskiem myszy.\n\t\t+ SHIFT\t\ttylko poziomo\n\t\t+ CONTROL\t\ttylko pionowo\n\t- Kręcenie rolką myszy - przewijanie w poziomie.\n\t- Poruszanie się za pomocą klawiszy strzałek.\n<b>Zooming:</b>\n\t- Kręcenie rolką myszy z wciśniętymi klawiszami:\n\t\t+ SHIFT\t\ttylko w poziomie\n\t\t+ CONTROL\t\ttylko w pionie\n\t\t+ SHIFT&CONTROL\tw  obu kierunkach</PRE></font></HTML>");
        helpLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        helpLabel1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2261, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(25, Short.MAX_VALUE)
                    .addComponent(helpLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 527, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(1709, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 431, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(helpLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(228, Short.MAX_VALUE)))
        );

        jTabbedPane2.addTab("Nawigacja", jPanel1);

        helpLabel2.setBackground(new java.awt.Color(204, 204, 204));
        helpLabel2.setText("<HTML><pre><b>Nowy element:</b>\n\tAby narysować nowy obszar, należy wcisnąć lewy przycisk\n\tmyszy i rozciągnąć w poziomie prosokątny kształt zaznaczenia.\n\n<b>Zaznaczenie obszaru:</b>\n\tAby wybrać istniejący obszar, należy wcisnąć na nim lewy\n\tprzycisk myszy. Można zaznaczyć wiele obszarów rysując\n\tzaznaczenie lewym przyciskiem myszy z użyciem klawisza SHIFT.\n\tMożna przełączać zaznaczenie klikając lewym\n\tprzyciskiem myszy na obszarze z użyciem klawisza CONTROL.\n\n<b>Przemieszczanie obszaru:</b>\n\tAby przsunąć obszar w lewo lub w prawo, należy wcisnąć lewy\n\tprzycisk myszy i przeciągnąć w wybranym kierunku.\n\n<b>Zmiana rozmiaru obszaru:</b>\n\tAby zmienić położenie lewej lub prawej krawędzi obszaru\n\tzaznaczenia, należy wzkazać go kursorem, następnie wskazać\n\twybrany narożnik obszaru, po czym przeciągnąć w wybranym\n\tkierunku.\n\n<b>Usuwanie obszaru:</b>\n\tAby usunąć obszar, należy najpierw go zaznaczyć, następnie\n\twcisnąć przycisk DELETE na klawiaturze.</pre></html>");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2261, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap(31, Short.MAX_VALUE)
                    .addComponent(helpLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 2220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 431, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(helpLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 11, Short.MAX_VALUE)))
        );

        jTabbedPane2.addTab("Obszary eksportu", jPanel2);

        javax.swing.GroupLayout helpLayout = new javax.swing.GroupLayout(help.getContentPane());
        help.getContentPane().setLayout(helpLayout);
        helpLayout.setHorizontalGroup(
            helpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        helpLayout.setVerticalGroup(
            helpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );

        importDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        importDialog.setTitle("Importowanie danych");
        importDialog.setAlwaysOnTop(true);
        importDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        importDialog.setMinimumSize(new java.awt.Dimension(415, 122));
        importDialog.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        importDialog.setUndecorated(true);
        importDialog.setResizable(false);

        processLabel.setText("Proszę czekać, trwa importowanie...");

        javax.swing.GroupLayout importDialogLayout = new javax.swing.GroupLayout(importDialog.getContentPane());
        importDialog.getContentPane().setLayout(importDialogLayout);
        importDialogLayout.setHorizontalGroup(
            importDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, importDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(importProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(importDialogLayout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addComponent(processLabel)
                .addContainerGap(111, Short.MAX_VALUE))
        );
        importDialogLayout.setVerticalGroup(
            importDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(importDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(processLabel)
                .addGap(18, 18, 18)
                .addComponent(importProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);
        setMinimumSize(new java.awt.Dimension(400, 300));
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                formWindowClosing(evt);
            }
        });

        jMenu1.setText("Plik");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Otwórz");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        reopenMenu.setText("Otwórz ponownie");
        jMenu1.add(reopenMenu);

        jMenu5.setText("Importuj");

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setText("Maskę zaznaczenia");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem6);

        jMenu1.add(jMenu5);

        exportMenu.setText("Eksportuj");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Do folderu pliku źródłowego");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem2ActionPerformed(evt);
            }
        });
        exportMenu.add(jMenuItem2);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Do wybranego folderu");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem4ActionPerformed(evt);
            }
        });
        exportMenu.add(jMenuItem4);

        jMenu1.add(exportMenu);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Zakończ");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu8.setText("Edycja");

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setText("Zaznacz wszystkie obszary");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem8);

        jMenuItem14.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem14.setText("Odznacz wszystkie obszary");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem14);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        jMenuItem9.setText("Usuń zaznaczone obszary");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem9);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0));
        jMenuItem10.setText("Złącz zaznaczone obszary");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem10);

        jMenuBar1.add(jMenu8);

        jMenu9.setText("Nawigacja");

        jMenuItem12.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_EQUALS, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem12.setText("Powiększ");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem12);

        jMenuItem13.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_MINUS, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem13.setText("Pomniejsz");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem13);

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_HOME, 0));
        jMenuItem11.setText("Pokaż wszystko");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem11);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SPACE, 0));
        jMenuItem7.setText("Resetuj położenie i skalę w pionie");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem7);

        jMenuItem15.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_BACK_QUOTE, java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItem15.setText("Pokaż wszystkie wykresy");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem15);

        jMenuBar1.add(jMenu9);

        jMenu4.setText("Opcje");

        visibilityMenu.setText("Widoczność wykresów");

        jCheckBoxMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.SHIFT_MASK));
        jCheckBoxMenuItem3.setSelected(true);
        jCheckBoxMenuItem3.setText("1: Thorax");
        visibilityMenu.add(jCheckBoxMenuItem3);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItem5.setText("2: SpO2");
        visibilityMenu.add(jMenuItem5);

        jMenu4.add(visibilityMenu);

        jMenu3.setText("Wskazywanie wartości");

        option_hint_all.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, 0));
        option_hint_all.setSelected(true);
        option_hint_all.setText("Wszystkie wykresy");
        jMenu3.add(option_hint_all);

        option_hint_current.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.SHIFT_MASK));
        option_hint_current.setSelected(true);
        option_hint_current.setText("Tylko wskazywany wykres");
        jMenu3.add(option_hint_current);

        option_hint_off.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        option_hint_off.setSelected(true);
        option_hint_off.setText("Wyłączone");
        jMenu3.add(option_hint_off);
        jMenu3.add(jSeparator2);

        jMenu6.setText("Pozycja");

        option_hint_value.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, 0));
        option_hint_value.setSelected(true);
        option_hint_value.setText("Przy wartości");
        jMenu6.add(option_hint_value);

        option_hint_bottom.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.SHIFT_MASK));
        option_hint_bottom.setSelected(true);
        option_hint_bottom.setText("Na dole");
        jMenu6.add(option_hint_bottom);

        option_hint_mouse.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        option_hint_mouse.setSelected(true);
        option_hint_mouse.setText("Pod kursorem");
        jMenu6.add(option_hint_mouse);

        jMenu3.add(jMenu6);

        jMenu4.add(jMenu3);

        jMenu7.setText("Zapis stanu pracy");

        option_save.setSelected(true);
        option_save.setText("Zapisuj przy wyjściu");
        jMenu7.add(option_save);

        option_dontSave.setSelected(true);
        option_dontSave.setText("Nie zapisuj przy wyjściu");
        jMenu7.add(option_dontSave);

        option_confirmSave.setSelected(true);
        option_confirmSave.setText("Pytaj przed wyjściem");
        jMenu7.add(option_confirmSave);

        jMenu4.add(jMenu7);

        option_hideHovered.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, 0));
        option_hideHovered.setSelected(true);
        option_hideHovered.setText("Wyblaknij wskazywany obszar");
        jMenu4.add(option_hideHovered);

        jMenuBar1.add(jMenu4);

        jMenu2.setText("Pomoc");

        helpMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        helpMenuItem.setText("Obsługa");
        helpMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                helpMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(helpMenuItem);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(medChart, javax.swing.GroupLayout.DEFAULT_SIZE, 1172, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(medChart, javax.swing.GroupLayout.DEFAULT_SIZE, 877, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem1ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem1ActionPerformed
        WindowEvent closingEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closingEvent);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void helpMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_helpMenuItemActionPerformed
    {//GEN-HEADEREND:event_helpMenuItemActionPerformed
        help.setLocationRelativeTo(this);
        help.setVisible(true);
    }//GEN-LAST:event_helpMenuItemActionPerformed

    private void initHelp()
    {
        helpLabel1.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        helpLabel2.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
    }

    private void initSettings()
    {
        PreferencesManager p = medChart.getPreferences();
        p.addRadioButton(option_hint_all, MedChartPreferences.SHOW_HINT, MedChartPreferences.SHOW_HINT_ALL);
        p.addRadioButton(option_hint_off, MedChartPreferences.SHOW_HINT, MedChartPreferences.SHOW_HINT_OFF);
        p.addRadioButton(option_hint_current, MedChartPreferences.SHOW_HINT, MedChartPreferences.SHOW_HINT_CURRENT);
        //
        p.addRadioButton(option_hint_bottom, MedChartPreferences.HINT_POSITION, MedChartPreferences.HINT_POSITION_BOTTOM);
        p.addRadioButton(option_hint_value, MedChartPreferences.HINT_POSITION, MedChartPreferences.HINT_POSITION_VALUE);
        p.addRadioButton(option_hint_mouse, MedChartPreferences.HINT_POSITION, MedChartPreferences.HINT_POSITION_MOUSE);
        //
        p.addRadioButton(option_save, MedChartPreferences.SAVE_ON_EXIT, MedChartPreferences.SAVE_ON_EXIT_YES);
        p.addRadioButton(option_dontSave, MedChartPreferences.SAVE_ON_EXIT, MedChartPreferences.SAVE_ON_EXIT_NO);
        p.addRadioButton(option_confirmSave, MedChartPreferences.SAVE_ON_EXIT, MedChartPreferences.SAVE_ON_EXIT_CONFIRM);
        //
        p.addCheckBox(option_hideHovered, MedChartPreferences.HIDE_HOVERED_SELECTION);
    }
    final String DEFAULT_DIRECTORY = "defaultDirectory";
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem3ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem3ActionPerformed

        Preferences prefs = Preferences.userRoot().node("opening");
        String dirName = prefs.get(DEFAULT_DIRECTORY, System.getProperty("user.root"));
        JFileChooser jfc = new JFileChooser(dirName);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setFileFilter(new FileNameExtensionFilter("pliki Assembly (*.xml)", "xml"));
        jfc.setDialogTitle("Wybierz plik danych");
        if (jfc.showOpenDialog(rootPane) == JFileChooser.APPROVE_OPTION)
        {
            prefs.put(DEFAULT_DIRECTORY, jfc.getSelectedFile().getParent());
            final File file = jfc.getSelectedFile();
            open(file);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem2ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem2ActionPerformed
//        if (currentFile == null)
//        {
//            return;
//        }
//        String filename = currentFile.getName();
//        filename = filename.substring(0, filename.lastIndexOf('.'));
//        filename = filename + ".sel";
//        File selFile = new File(currentFile.getParent() + "/" + filename);
//        export(selFile);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem4ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem4ActionPerformed
//        if (currentFile == null)
//        {
//            return;
//        }
//        Preferences prefs = Preferences.userRoot().node("saving");
//        String dirName = prefs.get(DEFAULT_DIRECTORY, System.getProperty("user.root"));
//        JFileChooser jfc = new JFileChooser(dirName);
//        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        jfc.setFileFilter(new FileNameExtensionFilter("maska binarna (*.sel)", "sel"));
//        jfc.setDialogTitle("Wskaż miejsce zapisu pliku maski");
//        if (jfc.showSaveDialog(rootPane) == JFileChooser.APPROVE_OPTION)
//        {
//            prefs.put(DEFAULT_DIRECTORY, jfc.getSelectedFile().getParent());
//            final File file = jfc.getSelectedFile();
//            export(file);
//        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem6ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem6ActionPerformed
        JOptionPane.showMessageDialog(rootPane, "Opcja dostępna wkrótce.", "Błąd.", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem7ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem7ActionPerformed
//        renderer.setScaleY(1);
//        renderer.setDataY(0);
        medChart.resetScaleY();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem8ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem8ActionPerformed
        medChart.selectAll();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem9ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem9ActionPerformed
        medChart.removeSelected();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem10ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem10ActionPerformed
        medChart.joinSelected();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        medChart.showAll();
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        medChart.zoomOut();
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        medChart.zoomIn();
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem14ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem14ActionPerformed
        medChart.deselectAll();
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem15ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem15ActionPerformed
        medChart.showAllCharts();
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        if (!trySaveProgress())
        {
            return;
        }
        System.exit(0);


    }//GEN-LAST:event_formWindowClosing
    //<editor-fold defaultstate="collapsed" desc="Main">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
     * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {

        try
        {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());


        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(PlotterApplication.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new PlotterApplication().setVisible(true);
            }
        });
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="gui variables">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu exportMenu;
    private javax.swing.JDialog help;
    private javax.swing.JLabel helpLabel1;
    private javax.swing.JLabel helpLabel2;
    private javax.swing.JMenuItem helpMenuItem;
    private javax.swing.JDialog importDialog;
    private javax.swing.JProgressBar importProgressBar;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.ButtonGroup labelOptionGroup;
    private pl.adamborowski.medcharts.MedChart medChart;
    private javax.swing.JRadioButtonMenuItem option_confirmSave;
    private javax.swing.JRadioButtonMenuItem option_dontSave;
    private javax.swing.JCheckBoxMenuItem option_hideHovered;
    private javax.swing.JRadioButtonMenuItem option_hint_all;
    private javax.swing.JRadioButtonMenuItem option_hint_bottom;
    private javax.swing.JRadioButtonMenuItem option_hint_current;
    private javax.swing.JRadioButtonMenuItem option_hint_mouse;
    private javax.swing.JRadioButtonMenuItem option_hint_off;
    private javax.swing.JRadioButtonMenuItem option_hint_value;
    private javax.swing.JRadioButtonMenuItem option_save;
    private javax.swing.JLabel processLabel;
    private javax.swing.JMenu reopenMenu;
    private javax.swing.JMenu visibilityMenu;
    // End of variables declaration//GEN-END:variables
//</editor-fold>

    /**
     * czy użytkownik dokonał decyzji a nie anulował
     *
     * @return
     * @throws HeadlessException
     */
    boolean trySaveProgress() throws HeadlessException
    {
        int saveOnExit = (int) medChart.getPreferences().get(MedChartPreferences.SAVE_ON_EXIT);
        if (saveOnExit == MedChartPreferences.SAVE_ON_EXIT_CONFIRM)
        {
            int r = JOptionPane.showConfirmDialog(rootPane, "Czy zapisać aktualny stan pracy? Ustawienie tego okna możesz zmienić w menu Opcje->Zapis stanu pracy");
            if (r == JOptionPane.YES_OPTION)
            {
                medChart.saveWorkProgress();
            } else if (r == JOptionPane.CANCEL_OPTION)
            {
                return false;
            }
        } else if (saveOnExit == MedChartPreferences.SAVE_ON_EXIT_YES)
        {
            medChart.saveWorkProgress();

        }
        return true;
    }
}
