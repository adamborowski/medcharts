/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Path;

/**
 *
 * @author test
 */
public class FileUtil
{

    @SuppressWarnings(
    {
        "null", "ConstantConditions"
    })
    public static long countLines(File file) throws IOException
    {
        try
        (LineNumberReader lnr = new LineNumberReader(new FileReader(file))) {
            lnr.skip(Long.MAX_VALUE);
            return lnr.getLineNumber();
        } catch (IOException ex)
        {
            throw ex;
        }
    }
    /**
     * Zamiana np z Wieslaw_jakis.txt na bin-cache/Wieslaw_jakis.bin
     * @param source
     * @param path
     * @param extension
     * @return 
     */
    public static File mapSourceToTarget(File source, Path path, String extension)
    {
        return path.resolve(getName(source)+"."+extension).toFile();
    }
    public static String getName(File file)
    {
        return file.getName().substring(0, file.getName().lastIndexOf('.'));
    }
    public static String getExtension(File file)
    {
        return file.getName().substring(file.getName().lastIndexOf('.')+1);
    }
}
