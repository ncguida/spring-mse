package com.hust.mse.utils;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类操作工具类
 *
 * @author ncguida
 * @date 2018/10/10
 */
public class ClassUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    public static ClassLoader getClasLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitialized, getClasLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class fail", e);
            throw new RuntimeException(e);
        }
        return cls;
    }

    public static Set<Class<?>> getClassSet(String packageName) {

        Set<Class<?>> classSet = new HashSet<>();

        try {

            Enumeration<URL> urls = getClasLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        String packagePath = url.getPath().replaceAll("%20", "");
                        addClass(classSet, packagePath, packageName);
                    } else if (protocol.equals("jar")) {
                        JarURLConnection urlConnection = (JarURLConnection)url.openConnection();
                        if (urlConnection != null) {
                            JarFile jarFile = urlConnection.getJarFile();
                            if (jarFile != null) {
                                Enumeration<JarEntry> entries = jarFile.entries();
                                while (entries.hasMoreElements()) {
                                    JarEntry jarEntry = entries.nextElement();
                                    String jarEntryName = jarEntry.getName();

                                    if (jarEntryName.endsWith(".class")) {

                                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf("."))
                                            .replaceAll("/", ".");

                                        doAddClass(classSet, className);

                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("get class set failed", e);
            throw new RuntimeException(e);
        }

        return classSet;
    }

    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isFile() && file.getName().endsWith(".class")) {
                    return true;
                }
                return file.isDirectory();
            }
        });

        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StringUtils.isNotBlank(packageName)) {
                    className = packageName + "." + className;
                }
                doAddClass(classSet, className);
            } else {
                String subPackagePath = fileName;
                if (StringUtils.isNotBlank(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (StringUtils.isNotBlank(subPackageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }

                addClass(classSet, subPackagePath, subPackageName);
            }

        }

    }

    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> clazz = loadClass(className, false);
        classSet.add(clazz);
    }

    public static void main(String[] args) {
        getClassSet("org.apache.commons.collections4");
    }

}
