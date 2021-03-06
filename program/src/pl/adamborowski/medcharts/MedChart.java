/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.Beans;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.xml.bind.JAXBException;
import pl.adamborowski.largeplotter.io.MaskExporter;
import pl.adamborowski.medcharts.assembly.imporing.AssemblyImporter;
import pl.adamborowski.medcharts.assembly.imporing.AssemblyImporter.Mapping;
import pl.adamborowski.medcharts.assembly.imporing.IImportListener;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.medcharts.assembly.jaxb.Selection;
import pl.adamborowski.medcharts.assembly.jaxb.Serie;
import pl.adamborowski.medcharts.assembly.reading.IDataReader;
import pl.adamborowski.medcharts.renderers.RootRenderer;
import pl.adamborowski.medcharts.usercontrol.KeyboardController;
import pl.adamborowski.utils.PaintUtil;
import pl.adamborowski.utils.PreferencesManager;
import pl.adamborowski.utils.converters.IntegerToLongConverter;
import pl.adamborowski.utils.interfaces.IProgressListener;

/**
 *
 * @author test
 */
public class MedChart extends JPanel implements IImportListener<Mapping>, ActionListener
{

    private static final int MAX_PROGRESS = 4000;
    private final CardLayout cardsLayout;
    private final String CHARTS_CARD = "chartsCard";
    private final String INFO_CARD = "infoCard";
    private final String DESIGN_TIME_CARD = "designTimeCard";
    private List<RootRenderer> charts;
    private long xMin;
    private long xMax;
    private KeyboardController keyboardController;
    private JMenu visibilityMenu;
    private JMenu exportMenu;
    private Assembly assembly;
    private Viewport bottomLabelViewport;
    private RootRenderer bottomLabelViewRenderer;
    private Viewport topLabelViewport;
    private RootRenderer topLabelViewRenderer;
    private PreferencesManager preferences = new MedChartPreferences(this);
    private AssemblyImporter importer;

    public void selectAll()
    {
        for (Viewport viewport : viewports)
        {
            if (viewport.getSelectionController() != null)
            {
                viewport.getSelectionController().selection.selectAll();
                viewport.repaint();
            }
        }
    }

    public void deselectAll()
    {
        for (Viewport viewport : viewports)
        {
            if (viewport.getSelectionController() != null)
            {
                viewport.getSelectionController().selection.deselectAll();
                viewport.repaint();
            }
        }
    }

    public void joinSelected()
    {
        for (Viewport viewport : viewports)
        {
            if (viewport.getSelectionController() != null)
            {
                viewport.getSelectionController().joinSelected();
                viewport.repaint();
            }
        }
    }

    public void removeSelected()
    {

        for (Viewport viewport : viewports)
        {
            if (viewport.getSelectionController() != null)
            {
                for (SelectionItem selectedItem : viewport.getSelectionController().selection.getItems())
                {
                    viewport.getSelectionController().removeItem(selectedItem);
                    viewport.repaint();
                }
            }
        }
    }

    /**
     * Creates new form MedChart
     */
    public MedChart()
    {
        initComponents();
        infoProgressBar.setMaximum(MAX_PROGRESS);
        cardsLayout = (CardLayout) cards.getLayout();
        if (Beans.isDesignTime())
        {
            cardsLayout.show(cards, DESIGN_TIME_CARD);
        }
        keyboardController = new KeyboardController(this);
    }
    private File assemblyFile;
    public static final String PROP_ASSEMBLY = "assembly";

    /**
     * Get the value of assembly
     *
     * @return the value of assembly
     */
    public File getAssembly()
    {
        return assemblyFile;
    }

    /**
     * Set the value of assembly
     *
     * @param assembly new value of assembly
     */
    public void setAssembly(File assembly) throws JAXBException
    {
        File oldAssembly = this.assemblyFile;
        this.assemblyFile = assembly;
        openAssembly();
        firePropertyChange(PROP_ASSEMBLY, oldAssembly, assembly);
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
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        slider = new javax.swing.JSlider();
        cards = new javax.swing.JPanel();
        designTimePanel = new javax.swing.JPanel();
        designIcon = new javax.swing.JLabel();
        designMedChartsLabel = new javax.swing.JLabel();
        designAuthorLabel = new javax.swing.JLabel();
        desingWWWLabel = new javax.swing.JLabel();
        infoPanel = new javax.swing.JPanel();
        infoLabel = new javax.swing.JLabel();
        infoProgressBar = new javax.swing.JProgressBar();
        chartsPanel = new javax.swing.JPanel();

        setFocusCycleRoot(true);

        slider.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        slider.setForeground(new java.awt.Color(64, 109, 165));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${dataX}"), slider, org.jdesktop.beansbinding.BeanProperty.create("value"), "");
        binding.setConverter(new IntegerToLongConverter());
        bindingGroup.addBinding(binding);

        cards.setLayout(new java.awt.CardLayout());

        designTimePanel.setBackground(new java.awt.Color(254, 254, 254));

        designIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pl/adamborowski/medcharts/resources/Charts.png"))); // NOI18N

        designMedChartsLabel.setFont(new java.awt.Font("Arial", 3, 48)); // NOI18N
        designMedChartsLabel.setForeground(new java.awt.Color(182, 211, 103));
        designMedChartsLabel.setText("MedCharts");

        designAuthorLabel.setFont(new java.awt.Font("Arial", 3, 22)); // NOI18N
        designAuthorLabel.setForeground(new java.awt.Color(255, 180, 0));
        designAuthorLabel.setText("Adam Borowski 2013");

        desingWWWLabel.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        desingWWWLabel.setForeground(new java.awt.Color(186, 98, 99));
        desingWWWLabel.setText("www.adamborowski.pl");

        javax.swing.GroupLayout designTimePanelLayout = new javax.swing.GroupLayout(designTimePanel);
        designTimePanel.setLayout(designTimePanelLayout);
        designTimePanelLayout.setHorizontalGroup(
            designTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(designTimePanelLayout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(designIcon)
                .addGap(18, 18, 18)
                .addGroup(designTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(designMedChartsLabel)
                    .addComponent(designAuthorLabel)
                    .addComponent(desingWWWLabel))
                .addContainerGap(119, Short.MAX_VALUE))
        );
        designTimePanelLayout.setVerticalGroup(
            designTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(designTimePanelLayout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(designTimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(designIcon)
                    .addGroup(designTimePanelLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(designMedChartsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(designAuthorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(desingWWWLabel)))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        cards.add(designTimePanel, "designCard");

        infoLabel.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        infoLabel.setForeground(new java.awt.Color(67, 110, 144));
        infoLabel.setText("Trwa importowanie danych");

        javax.swing.GroupLayout infoPanelLayout = new javax.swing.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(infoProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(infoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 636, Short.MAX_VALUE))
                .addContainerGap())
        );
        infoPanelLayout.setVerticalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addComponent(infoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(infoProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 139, Short.MAX_VALUE))
        );

        cards.add(infoPanel, "infoCard");

        chartsPanel.setLayout(new javax.swing.BoxLayout(chartsPanel, javax.swing.BoxLayout.Y_AXIS));
        cards.add(chartsPanel, "chartsCard");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(slider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(cards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(slider, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(cards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cards;
    private javax.swing.JPanel chartsPanel;
    private javax.swing.JLabel designAuthorLabel;
    private javax.swing.JLabel designIcon;
    private javax.swing.JLabel designMedChartsLabel;
    private javax.swing.JPanel designTimePanel;
    private javax.swing.JLabel desingWWWLabel;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JProgressBar infoProgressBar;
    private javax.swing.JSlider slider;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    protected void openAssembly() throws JAXBException
    {
        importer = new AssemblyImporter(assemblyFile, this);
        this.assembly = importer.getAssembly();

        timer = new Timer(70, this);
        timer.setRepeats(false);
        timer.start(); // z opóźnieniem włączamy ekran z paskiem postępu.
//        importer.setForceDelete(false);//todo change to false
        importer.setForceDelete(false);//todo change to false
        importer.execute();
    }
    Timer timer;

    @Override
    public void progressChanged(float progress)
    {
        infoProgressBar.setValue((int) (progress * MAX_PROGRESS));
    }

    @Override
    public void done(Mapping result)
    {
        timer.stop();
        timer = null;

        cardsLayout.show(cards, CHARTS_CARD);
        showCharts(result);
    }
    private List<Viewport> viewports;

    private void showCharts(Mapping mapping)
    {
        chartsPanel.removeAll();
        viewports = new ArrayList<>(mapping.getAssembly().getSerie().size());
        charts = new LinkedList<>();
        for (Serie serie : mapping.getAssembly().getSerie())
        {
            Viewport viewport = new Viewport(this, serie, mapping);


            charts.add(viewport.getViewRenderer());

            viewports.add(viewport);
            chartsPanel.add(viewport);
        }
        bottomLabelViewport = new Viewport(this);
        bottomLabelViewRenderer = bottomLabelViewport.getViewRenderer();

        topLabelViewport = new Viewport(this);
        topLabelViewRenderer = topLabelViewport.getViewRenderer();
        topLabelViewRenderer.getHelpRenderer().setFlipLabel(true);

        final int labelHeight = 32;
        bottomLabelViewport.setMaximumSize(new Dimension(Integer.MAX_VALUE, labelHeight));
        bottomLabelViewport.setPreferredSize(new Dimension(400, labelHeight));

        topLabelViewport.setMaximumSize(new Dimension(Integer.MAX_VALUE, labelHeight));
        topLabelViewport.setPreferredSize(new Dimension(400, labelHeight));

        chartsPanel.add(bottomLabelViewport);
        chartsPanel.add(topLabelViewport, 0);
        //TODO UMIEŚCIĆ ROZSĄDNIE WYKRES Z LABELAMI
        //charts.add(labelViewport.getViewRenderer());
//        for (RootRenderer chart : map)
//        {
//            chart.configure(); //teraz każemy skonfigurować się rendererowi, bo
//            //wiemy, że reader jest dostępny
//            Viewport viewport = new Viewport(this, chart);
//            viewports.add(viewport);
//            chartsPanel.add(viewport);
//        }
        //chartsPanel.getComponents()[4].setVisible(false);
        chartsPanel.validate();
        calculateDataBounds();
        slider.setMinimum((int) xMin);
        slider.setMaximum((int) xMax);

        initVisibilityMenu(charts);
        initExportMenu();
        showAll();
    }

    public List<Viewport> getViewports()
    {
        return viewports;
    }

    private void calculateDataBounds()
    {
        xMin = charts.get(0).sp().getFirstTime();
        xMax = charts.get(0).sp().getLastTime();
        for (RootRenderer chart : charts)
        {
            if (xMin > chart.sp().getFirstTime())
            {
                xMin = chart.sp().getFirstTime();
            }
            if (xMax < chart.sp().getLastTime())
            {
                xMax = chart.sp().getLastTime();
            }
        }
    }

    @Override
    public void error(Exception ex)
    {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Problem z importowaniem", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        cardsLayout.show(cards, INFO_CARD);
    }

    public void showAll()
    {
        final float margin = 20;
        float displayWidth = chartsPanel.getWidth() - 2 * margin;
        float dataWidth = xMax - xMin;
        float scale = dataWidth / displayWidth;
        setScaleX(scale);
        setDataX((long) (xMin + dataWidth / 2));
        setDataY(0);
        setScaleY(1);
    }

    public void setScaleX(float scaleX)
    {
        for (RootRenderer chart : charts)
        {
            chart.setScaleX(scaleX);
        }
        bottomLabelViewRenderer.setScaleX(scaleX);
        topLabelViewRenderer.setScaleX(scaleX);
    }

    public float getScaleX()
    {
        if (charts == null)
        {
            return 0;
        }
        return charts.get(0).getScaleX();
    }

    public void setScaleY(float scaleY)
    {
        for (RootRenderer chart : charts)
        {
            chart.setScaleY(scaleY);
        }
        bottomLabelViewRenderer.setScaleY(scaleY);
        topLabelViewRenderer.setScaleY(scaleY);
        firePropertyChange("scaleY", null, scaleY);
    }

    public float getScaleY()
    {
        if (charts == null)
        {
            return 0;
        }
        return charts.get(0).getScaleY();
    }

    public void setDataX(long dataX)
    {
        if (charts != null)
        {
            for (RootRenderer chart : charts)
            {
                chart.setDataX(dataX);
            }
        }
        bottomLabelViewRenderer.setDataX(dataX);
        topLabelViewRenderer.setDataX(dataX);
        firePropertyChange("dataX", null, dataX);
    }

    public long getDataX()
    {
        if (charts == null)
        {
            return 0;
        }
        return charts.get(0).getDataX();
    }

    public void setDataY(float dataY)
    {
        if (charts != null)
        {
            for (RootRenderer chart : charts)
            {
                chart.setDataY(dataY);
            }
        }
        bottomLabelViewRenderer.setDataY(dataY);
        topLabelViewRenderer.setDataY(dataY);
        firePropertyChange("dataY", null, dataY);
    }

    public float getDataY()
    {
        if (charts == null)
        {
            return 0;
        }
        return charts.get(0).getDataY();
    }

    public void zoomOut()
    {
        setScaleX(getScaleX() * 2);
        setScaleY(getScaleY() * 2);
    }

    public void zoomIn()
    {
        setScaleX(getScaleX() / 2.0f);
        setScaleY(getScaleY() / 2.0f);
    }

    public void resetScaleY()
    {
        for (RootRenderer i : charts)
        {
            i.setAutoScaleY(true);
        }
    }

    public PreferencesManager getPreferences()
    {
        return preferences;
    }
    
    public void saveWorkProgress()
    {
        for(Viewport v:viewports)
        {
            if(v.getSelectionController()!=null)
            {
                v.getSelectionController().serialize();
            }
        }
    }

    public void setLayerVisibilityMenu(JMenu menu)
    {
        this.visibilityMenu = menu;
    }

    private void initVisibilityMenu(List<RootRenderer> charts)
    {
        if (visibilityMenu == null)
        {
            return;
        }
        visibilityMenu.removeAll();
        int i = 0;
        visibilityActions = new VisibilityAction[viewports.size()];
        for (Viewport v : viewports)
        {
            i++;
            JCheckBoxMenuItem m = new JCheckBoxMenuItem();
            m.setSelected(true);
            m.setText(i + ": " + v.getViewRenderer().getSerieRenderer().getBinding().getTitle());
            m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0 + i, KeyEvent.SHIFT_MASK));
            final VisibilityAction visibilityAction = new VisibilityAction(v, m);
            visibilityActions[i - 1] = visibilityAction;
            m.addActionListener(visibilityAction);
            visibilityMenu.add(m);

        }
    }
    VisibilityAction[] visibilityActions;

    public void setExportMenu(JMenu menu)
    {
        this.exportMenu = menu;
    }

    private void initExportMenu()
    {
        if (exportMenu == null)
        {
            return;
        }
        exportMenu.removeAll();
        int i = 0;
        for (Selection selection : assembly.getSelection())
        {
            i++;
            JMenuItem m = new JMenuItem(selection.getTitle());
            m.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0 + i, KeyEvent.ALT_MASK));
            //głupia metoda, ale znajdź viewport
            Viewport v = null;
            for (Viewport viewport : viewports)
            {
                if (viewport.getSelectionController() == null)
                {
                    continue;
                }
                if (viewport.getSelectionController().getBinding() == selection)
                {
                    v = viewport;
                    break;
                }
            }
            if (v != null)
            {
                m.addActionListener(new ExportAction(v, selection));
            } else
            {
                continue;
                //nie ma nikogo, kto by chciał wyświetlić!!
            }

            exportMenu.add(m);
        }
    }

    public AssemblyImporter getImporter()
    {
        return importer;
    }

    public void setHoveredViewport(Viewport viewport)
    {
        //nie będziemy hoverować, bo to przeszkadza trafiać w wykres
//        chartsPanel.remove(labelViewport);
//        int p=0;
//        
//        chartsPanel.add(labelViewport, viewports.indexOf(viewport)+1);
//        chartsPanel.validate();
    }

    public void showAllCharts()
    {

        for (VisibilityAction vp : visibilityActions)
        {
            vp.show();
        }

        chartsPanel.validate();
    }

    private class ExportAction implements ActionListener
    {

        private final Viewport viewport;
        private final Selection selection;
        private final SelectionController selectionController;

        public ExportAction(Viewport viewport, Selection selection)
        {
            this.viewport = viewport;
            this.selection = selection;
            selectionController = viewport.getSelectionController();
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            viewport.setBackground(PaintUtil.createColor(0xffffff, .6f));
//            JOptionPane.showMessageDialog(getRootPane(), "eksport zaznaczenia: \"" + selection.getTitle() + "\" : elementów " + SelectionController.getControllerForSelection(selection, null, null).getItems().size() + " (nie zaimplementowano jeszcze)");
            String filename=selection.getId()+"-export-"+new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(System.currentTimeMillis())+".txt";
            File exportFile=importer.getSourcePath().resolve("export").resolve(filename).toFile();
            exportFile.getParentFile().mkdirs();
            ExportWorker ew=new ExportWorker(exportFile, selectionController, viewport.getViewRenderer().getSerieRenderer().getReader());
            ew.execute();
        }
    }

    private class VisibilityAction implements ActionListener
    {

        private final Viewport viewport;
        private final JCheckBoxMenuItem menuItem;

        public VisibilityAction(Viewport viewport, JCheckBoxMenuItem menuItem)
        {
            this.viewport = viewport;
            this.menuItem = menuItem;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            viewport.setVisible(menuItem.isSelected());
        }

        public void show()
        {
            viewport.setVisible(true);

            menuItem.setSelected(true);
        }
    }

    class ExportWorker extends SwingWorker implements IProgressListener
    {

        private File exportFile;
        private final int maxProgress = 1000;
        private final SelectionController selectionController;
        private final IDataReader dataReader;

        public ExportWorker(File exportFile, SelectionController controller, IDataReader dataReader)
        {
            this.exportFile = exportFile;
            this.selectionController = controller;
            this.dataReader=dataReader;
            cardsLayout.show(cards, INFO_CARD);
            infoProgressBar.setMaximum(maxProgress);
            infoProgressBar.setValue(0);
            infoLabel.setText("Proszę czekać, trwa eksportowanie...");
        }

        @Override
        protected Object doInBackground() throws Exception
        {

            MaskExporter exporter = new MaskExporter(dataReader,selectionController);
            exporter.setListener(this);
            exporter.export(exportFile);
            return null;
        }

        @Override
        public void progressChanged(float value)
        {
            progress = value;
            publish();
        }
        private float progress;

        @Override
        protected void process(List chunks)
        {
            super.process(chunks); //To change body of generated methods, choose Tools | Templates.
            infoProgressBar.setValue((int) (progress * maxProgress));
        }

        @Override
        protected void done()
        {
            infoProgressBar.setValue(maxProgress);
            cardsLayout.show(cards,CHARTS_CARD);
//            importDialog.setVisible(false);
//            PlotterApplication.this.setEnabled(true);
            JOptionPane.showMessageDialog(getRootPane(), "Pomyślnie wyeksportowano do: \n" + exportFile.getAbsolutePath() + ".", "Wiadomość", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
