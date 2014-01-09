/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.medcharts.assembly.imporing;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import javax.xml.bind.JAXBException;
import pl.adamborowski.medcharts.assembly.AssemblyIO;
import pl.adamborowski.medcharts.assembly.jaxb.Assembly;
import pl.adamborowski.medcharts.assembly.reading.IDataReader;
import pl.adamborowski.medcharts.renderers.RootRenderer;
import pl.adamborowski.medcharts.renderers.OverlayRenderer;
import pl.adamborowski.utils.interfaces.IProgressListener;

/**
 *
 * @author test
 */
public class AssemblyImporter extends SwingWorker<AssemblyImporter.Mapping, ImporterBase> implements IProgressListener
{

    private final Assembly assembly;
    private final IImportListener guiListener;
    private long overallSize = 0;
    private long importedSize = 0;
    private long importingSize;
    private boolean forceImport = true;
    private final Path sourcePath;
    private final Path binPath;
    private boolean currentIsImporting = false;

    public boolean isForceDelete()
    {
        return forceImport;
    }

    public void setForceDelete(boolean forceDelete)
    {
        this.forceImport = forceDelete;
    }

    public AssemblyImporter(File assemblyFile, IImportListener guiListener) throws JAXBException
    {
        this(assemblyFile, guiListener, assemblyFile.toPath().resolveSibling("bin-cache"));
    }

    public AssemblyImporter(File assemblyFile, IImportListener guiListener, Path binPath) throws JAXBException
    {

        this.assembly = AssemblyIO.readAssembly(assemblyFile, binPath);
        this.sourcePath = assemblyFile.toPath().getParent();
        this.binPath = binPath;
        File binDir = binPath.toFile();
        if (!binDir.exists())
        {
            binDir.mkdir();
        }
        //bardzo ważne, aby liczenie plików działało
        this.guiListener = guiListener;

    }

    public Assembly getAssembly()
    {
        return assembly;
    }

    public Path getSourcePath()
    {
        return sourcePath;
    }

    /**
     * powiadomienie workera, że teraz pracuje się nad tym procesem
     *
     * @param importer
     */
    private void notifyImporting(ImporterBase importer)
    {

        currentIsImporting = mustImporterImporting(importer);
        if (currentIsImporting)
        {
            importingSize = importer.getComplexityRatio();
        } else
        {
            importingSize = 0;
        }
        System.out.println("Current file: " + importer.getSourceFile().getName());
    }

    private void notifyImported(ImporterBase importer)
    {
        if (mustImporterImporting(importer))
        {
            importedSize += importer.getComplexityRatio();
        }
        importingSize = 0;
        currentIsImporting = false;
        lastProgressReported = 0;
        publish();
    }

    @Override
    protected void process(List<ImporterBase> imported)
    {
        if (currentIsImporting)
        {
            guiListener.progressChanged(Math.min(1, ((importedSize + lastProgressReported * importingSize) / overallSize)));
        }
    }

    @Override
    protected void done()
    {
        try
        {
            guiListener.done(get());
        } catch (InterruptedException | ExecutionException ex)
        {
            Logger.getLogger(AssemblyImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean mustImporterImporting(ImporterBase imp)
    {
        return forceImport || !imp.isCacheValid();
    }

    private void addImporterToProgressIfImporting(ImporterBase importer)
    {
        if (mustImporterImporting(importer))
        {
            overallSize += importer.getComplexityRatio();
        }
    }

    @Override
    protected Mapping doInBackground() throws Exception
    {
        Thread.currentThread().setName("AssemblyImporter");
        long startTime = System.currentTimeMillis();
        LinkedList<ImporterBase> queue = null;
        Map<Object, IDataReader> map = new HashMap<>();
        try
        {
            /*
             *umieścić wszystkie importery w w kolejce oczekiwania
             */
            queue = new LinkedList<>();
            for (Assembly.Serie serie : assembly.getSerie())
            {
                MainImporter mainImporter = new MainImporter(this, serie);
//                RootRenderer viewRenderer = new RootRenderer(mainImporter.getReader());
//                viewRenderer.setBinding(serie);
//                viewRenderer.initialize();
                //skojarz obiekt jaxb z readerem
                map.put(serie, mainImporter.getReader());
                queue.add(mainImporter);
                addImporterToProgressIfImporting(mainImporter);
                for (Assembly.Serie.Overlay overlay : serie.getOverlay())
                {
                    OverlayImporter overlayImporter = new OverlayImporter(this, overlay, mainImporter);
//                    OverlayRenderer overlayRenderer = new OverlayRenderer(overlayImporter.getReader());
//                    overlayRenderer.setBinding(overlay);
//                    overlayRenderer.initialize();
                    queue.add(overlayImporter);
                    //skojarz jaxb obiekt z tym readererm
                    map.put(overlay, overlayImporter.getReader());
//                    viewRenderer.getSerieRenderer().setOverlayRenderer(overlayRenderer);
                    addImporterToProgressIfImporting(overlayImporter);
                }
            }
        } catch (FileNotFoundException ex)
        {
            guiListener.error(ex);
        } catch (Exception ex)
        {
            guiListener.error(ex);
        }

        //teraz importuj po kolei
        for (ImporterBase importer : queue)
        {
            notifyImporting(importer);
            importer.doImport(forceImport);
            notifyImported(importer);
        }
        System.out.println("Czas importowania: " + (System.currentTimeMillis() - startTime));
        return new Mapping(map);
    }
    private float lastProgressReported;

    @Override
    public final void progressChanged(float progress)
    {
        //jakiś importer powiedziła nam o swoim postępie
        lastProgressReported = progress; //zapamiętajmy ten postęp
        publish(); // powiadommy swinga że chcemy aktualizować

    }

    public Path getBinPath()
    {
        return binPath;
    }

    public File getSourceFile(String fileName)
    {
        return getSourcePath().resolve(fileName).toFile();
    }
    
    public final class Mapping{
        private final Map<Object, IDataReader> map;

        public Mapping(Map<Object, IDataReader> map)
        {
            this.map = map;
        }
        public Assembly getAssembly()
        {
            return assembly;
        }
        public IDataReader readerFor(Object object){
            return map.get(object);
        }
    }
}
