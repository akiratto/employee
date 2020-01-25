package util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Owner
 */
public class UserEntityClassLoader extends ClassLoader implements Serializable {
    private List<File> clazzFiles;
    
    public UserEntityClassLoader(List<File> clazzFiles)
    {
        this.clazzFiles = clazzFiles;
    }
    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        for(File clazzFile : clazzFiles) {
            if(clazzFile.getName().replaceAll("\\.class", "").equals(name)) {
                byte[] data = loadClassData(clazzFile);
                return defineClass(name, data, 0, data.length);
            }
        }
        return super.findClass(name);
    }
    
    private byte[] loadClassData(File file) {
        byte[] data = new byte[(int) file.length()];
        try {
            InputStream input = new BufferedInputStream(new FileInputStream(file));
            try {
                input.read(data);
            } finally {
                input.close();
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return data;
    }
}
