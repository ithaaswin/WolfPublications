package com.ncsu.wolfpub.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesReader {

	public static Map<String, String> readProperties(String fileName) {
        Properties properties = new Properties();
        java.net.URL url = ClassLoader.getSystemResource(fileName);

        try  {
            properties.load(url.openStream());
        } catch (FileNotFoundException fie) {
            fie.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> props = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            props.put(key, properties.getProperty(key));
            
        }
        return props;        
    }

}
