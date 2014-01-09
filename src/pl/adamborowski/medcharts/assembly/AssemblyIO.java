/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.assembly;

import java.io.File;
import java.nio.file.Path;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author test
 */
public class AssemblyIO
{

    public static pl.adamborowski.medcharts.assembly.jaxb.Assembly readAssembly(File xmlFile, Path binPath) throws JAXBException
    {
        JAXBContext context = JAXBContext.newInstance("pl.adamborowski.medcharts.assembly.jaxb");
        Unmarshaller u = context.createUnmarshaller();
        pl.adamborowski.medcharts.assembly.jaxb.Assembly assembly = (pl.adamborowski.medcharts.assembly.jaxb.Assembly) u.unmarshal(xmlFile);
        return assembly;
    }
}
