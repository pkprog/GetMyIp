package ru.pk.gmi;

import ru.pk.gmi.exceptions.ApplicationException;
import ru.pk.gmi.utils.TypeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class AppPropertiesLoader {
    public static final String FILE = "application.properties";
    public static String pathToFolderOfFile;

    public static void init(String pathToFolderOfFile) {
        AppPropertiesLoader.pathToFolderOfFile = pathToFolderOfFile;
    }

    public static Properties load() {
        String pathFile = TypeUtils.notEmpty(pathToFolderOfFile) ?
                (pathToFolderOfFile + "/" + FILE) : (FILE);
        try {
            return loadFromFile(pathFile);
        } catch (URISyntaxException |IOException e) {
            e.printStackTrace();
            throw new ApplicationException("Can not load properties from local file:" + e.getMessage(), e);
        }
    }

    private static Properties loadFromFile(String filePath) throws IOException, URISyntaxException {
        URL url = AppPropertiesLoader.class.getClassLoader().getResource(filePath);
        if (url == null) {
            throw new ApplicationException("Not found file-resource:" + filePath);
        }

        URI uri = url.toURI();

        try (FileInputStream fis = new FileInputStream(new File(uri))) {
            Properties props = new Properties();
            props.load(fis);
            return props;
        }

    }

}
