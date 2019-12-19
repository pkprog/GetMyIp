package ru.pk.gmi;

import ru.pk.gmi.exceptions.ApplicationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class AppPropertiesLoader {
    public static final String FILE = "application.properties.local";

    public static Properties load() {
        try {
            return loadFromFile();
        } catch (URISyntaxException |IOException e) {
            e.printStackTrace();
            throw new ApplicationException("Can not load properties from local file:" + e.getMessage(), e);
        }
    }

    private static Properties loadFromFile() throws IOException, URISyntaxException {
        URL url = AppPropertiesLoader.class.getClassLoader().getResource(FILE);
        if (url == null) {
            throw new ApplicationException("Not found file-resource " + FILE);
        }

        URI uri = url.toURI();

        try (FileInputStream fis = new FileInputStream(new File(uri))) {
            Properties props = new Properties();
            props.load(fis);
            return props;
        }

    }

}
