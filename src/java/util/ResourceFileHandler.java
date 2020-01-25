/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;

/**
 *
 * @author Owner
 */
public class ResourceFileHandler {
//private static void loadMessages(ClassLoader classLoader) {
//        URL url = classLoader.getResource(MESSAGES_DIR);
//        if (url == null) {
//            // 例外処理
//        } else {
//            if (url.getProtocol().equals("jar")) {
//                // 定義ファイルがJAR内の場合
//                String[] s = url.getPath().split(":");
//                String path = s[s.length - 1].split("!")[0];
//                File f = new File(path);
//                JarFile jarFile;
//                try {
//                    jarFile = new JarFile(f);
//                    Enumeration<JarEntry> entries = jarFile.entries();
//                    while (entries.hasMoreElements()) {
//                        JarEntry entry = entries.nextElement();
//                        String name = entry.getName();
//                        if (name != null && name.startsWith(MESSAGES_DIR)) {
//                            try (InputStream is = classLoader.getResourceAsStream(name);) {
//                                read(is);
//                            }
//                        }
//                    }
//                } catch (IOException ex) {
//                    //例外処理
//                }
//            } else {
//                // 定義ファイルがJAR外の場合
//                File dir = new File(url.getPath());
//                String[] fileNames = dir.list();
//                for (String fileName : fileNames) {
//
//                    try (InputStream is = classLoader.getResourceAsStream(MESSAGES_DIR + fileName);) {
//                        read(is);
//                    } catch (IOException ex) {
//                        // 例外処理
//                    }
//                }
//            }
//        }
//    }
//
//    private static void read(InputStream is) {
//        try (InputStreamReader isr = new InputStreamReader(is);
//                BufferedReader br = new BufferedReader(isr);) {
//            // 読み込み処理はちゃんと書く。。。
//            String line = null;
//            while ((line = br.readLine()) != null) {
//                System.out.println(line);
//            }
//        } catch (IOException ex) {
//            // 例外処理
//        }
//    }

}
