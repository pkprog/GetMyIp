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
    public static final String DEFAULT_FILE = "application.properties";
    public static String pathToFolderWithFile;

    public static void init(String pathToFolderWithFile) {
        AppPropertiesLoader.pathToFolderWithFile = pathToFolderWithFile;
    }

    public static Properties load() {
        try {
            if (TypeUtils.notEmpty(pathToFolderWithFile)) {
                return loadFromFile(pathToFolderWithFile);
            } else {
                return loadFromResource(DEFAULT_FILE);
            }
        } catch (URISyntaxException |IOException e) {
            e.printStackTrace();
            throw new ApplicationException("Can not load properties from local file:" + e.getMessage(), e);
        }
    }

    private static Properties loadFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            Properties props = new Properties();
            props.load(fis);
            return props;
        }
    }

    private static Properties loadFromResource(String filePath) throws IOException, URISyntaxException {
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
