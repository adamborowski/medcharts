/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.assembly.reading;

import java.io.IOException;
import pl.adamborowski.medcharts.assembly.data.DataSequence;
import pl.adamborowski.medcharts.renderers.SpaceManager;

/**
 *
 * @author test
 */
public interface IDataReader<TRETURNDATA>
{
    void initialize() throws IOException;
    long getStart();
    long getEnd();
    DataSequence getSequence();
    TRETURNDATA getData(SpaceManager sp, int fromPixel, int toPixel) throws IOException;
}
