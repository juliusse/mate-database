package info.seltenheim.services.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import play.Play;

/**
 * 
 * @author Julius This implementation saves all files into the folder that is
 *         specified in services.filesystem.location
 */
@Profile("localFS")
@Component
public class LocalFileSystemService implements FileSystemService {

    private final File basePath;
    private static final Random random = new Random();

    public LocalFileSystemService() throws IOException {
        final String basePathFromConf = Play.application().configuration().getString("info.seltenheim.mate.files");
        basePath = new File(basePathFromConf != null ? basePathFromConf : "uploaded");
        if (!basePath.exists()) {
            FileUtils.forceMkdir(basePath);
        }

    }

    @Override
    public String saveImage(byte[] imageBytes, String filename) throws IOException {
        final File file = new File(basePath, filename);
        FileUtils.writeByteArrayToFile(file, imageBytes);
        return filename;
    }

    @Override
    public InputStream getImageAsStream(String path) throws FileNotFoundException {
        final File file = new File(basePath, path);
        if (!file.exists()) {
            throw new FileNotFoundException("File '" + file.getAbsolutePath() + "' not found!");
        }

        return new FileInputStream(file);
    }

    @Override
    public File getImageAsFile(String path) throws FileNotFoundException {
        final File file = new File(basePath, path);
        if (!file.exists()) {
            throw new FileNotFoundException("File '" + file.getAbsolutePath() + "' not found!");
        }

        return file;
    }

}
