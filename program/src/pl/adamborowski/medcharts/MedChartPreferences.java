/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts;

import pl.adamborowski.utils.PreferencesManager;

/**
 *
 * @author test
 */
public class MedChartPreferences extends PreferencesManager {

    public static final String SHOW_HINT = "showHint";
    public static final int SHOW_HINT_OFF = 0;
    public static final int SHOW_HINT_CURRENT = 1;
    public static final int SHOW_HINT_ALL = 2;
    //
    public static final String HINT_POSITION = "hintPosition";
    public static final int HINT_POSITION_BOTTOM = 0;
    public static final int HINT_POSITION_VALUE = 1;
    public static final int HINT_POSITION_MOUSE = 2;
    public static final int HINT_POSITION_TOP = 3;
    //
    public static final String SAVE_ON_EXIT = "saveOnExit";
    public static final int SAVE_ON_EXIT_NO = 0;
    public static final int SAVE_ON_EXIT_YES = 1;
    public static final int SAVE_ON_EXIT_CONFIRM = 2;

    private final MedChart medChart;
    //
    public static final String HIDE_HOVERED_SELECTION = "hideHoveredSelection";

    public MedChartPreferences(MedChart medChart) {
        super("MedCharts");
        this.medChart = medChart;
        initProperty(SHOW_HINT, SHOW_HINT_ALL);
        initProperty(HINT_POSITION, HINT_POSITION_VALUE);
        initProperty(SAVE_ON_EXIT, SAVE_ON_EXIT_CONFIRM);
        initProperty(HIDE_HOVERED_SELECTION, false);
    }

    @Override
    protected void updateCallback() {
        medChart.repaint();
    }
}
