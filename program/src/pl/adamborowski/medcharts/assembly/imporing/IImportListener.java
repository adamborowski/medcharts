/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.assembly.imporing;

/**
 *
 * @author test
 */
public interface IImportListener<TResult>
{

    void progressChanged(float progress);

    public void done(TResult resutl);
    public void error(Exception ex);
}
