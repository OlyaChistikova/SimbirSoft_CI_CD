package helpers;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс-синглтон для загрузки и предоставления свойств из config.properties.
 */
public class PropertyProvider {
    private static PropertyProvider instance;
    private static final String filePath = "config.properties";
    @Getter
    private final Properties properties = new Properties();

    /**
     * Метод для получения экземпляра класса PropertyProvider.
     *
     * @return единственный экземпляр PropertyProvider.
     */
    public static PropertyProvider getInstance(){
        if (instance == null){
            synchronized (PropertyProvider.class){
                if (instance == null){
                    instance = new PropertyProvider();
                }
            }
        }
        return instance;
    }

    /**
     * Приватный конструктор, который загружает свойства из config.properties.
     */
    private PropertyProvider(){
        try(InputStream propertiesInputStream =
                    getClass().getClassLoader().getResourceAsStream(filePath))
        {
            if (propertiesInputStream == null){
                throw new IOException("Properties file not found: " + filePath);
            }
            properties.load(propertiesInputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from " + filePath, e);
        }
    }

    /**
     * Статический метод для получения значения свойства по ключу.
     *
     * @param key ключ, по которому запрашивается значение свойства.
     * @return значение свойства, соответствующее переданному ключу.
     */
    public static String getProperty(String key){ return getInstance().properties.getProperty(key);}
}