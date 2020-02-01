package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 * @author Owner
 */
public class ResourceFileHandler {
    public static List<String> listResourcePathsInJarFile(URL jarUrl)
    {
        List<String> resourcePaths = new ArrayList<>();
        if (!jarUrl.getProtocol().equals("jar")) {
            return resourcePaths;
        }
        
        // 定義ファイルがJAR内の場合
        String[] s = jarUrl.getPath().split(":");
        String path = s[s.length - 1].split("!")[0];
        File file = new File(path);
        JarFile jarFile;
        try {
            jarFile = new JarFile(file);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                resourcePaths.add(entry.getName());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return resourcePaths;
    }
    
    public static String loadResource(ClassLoader classLoader, String resourceFilePath) throws IOException {
        URL url = classLoader.getResource(resourceFilePath);
        if(url == null) {
            throw new IllegalArgumentException("リソースが見つからないか、呼出し側がリソースを取得する適切な特権を持っていません。resourceFilePath=" + resourceFilePath);
        }
        
        String resourceContent = null;
        if (url.getProtocol().equals("jar")) {
            // 定義ファイルがJAR内の場合
            for(String resourcePathInJarFile : listResourcePathsInJarFile(url)) {
                if (resourcePathInJarFile != null && resourcePathInJarFile.equals(resourceFilePath)) {
                    try (InputStream is = classLoader.getResourceAsStream(resourcePathInJarFile);) {
                        resourceContent = read(is);
                    }
                    break;
                }
            }
        } else {
            // 定義ファイルがJAR外の場合
            try (InputStream is = classLoader.getResourceAsStream(resourceFilePath);) {
                resourceContent = read(is);
            }
        }
        return resourceContent;
    }

    private static String read(InputStream is) {
        String readResult = "";
        try (InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
             BufferedReader br = new BufferedReader(isr);) 
        {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            readResult = sb.toString();
        } catch (IOException ex) {
            // 例外処理
            ex.printStackTrace();
        }
        return readResult;
    }

}
