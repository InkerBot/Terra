package com.dfsek.terra.addons.loading;

import com.dfsek.terra.addons.addon.TerraAddon;
import com.dfsek.terra.addons.annotations.Addon;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AddonClassLoader extends URLClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }

    public AddonClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public AddonClassLoader(URL[] urls) {
        super(urls);
    }

    @SuppressWarnings("unchecked")
    public static Set<Class<? extends TerraAddon>> fetchAddonClasses(File file) throws IOException {
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> entries = jarFile.entries();

        AddonClassLoader loader = new AddonClassLoader(new URL[] {file.toURI().toURL()}, AddonClassLoader.class.getClassLoader());

        Set<Class<? extends TerraAddon>> set = new HashSet<>();
        while(entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();

            if(entry.isDirectory() || !entry.getName().endsWith(".class")) continue;
            String className = entry.getName().substring(0, entry.getName().length() - 6).replace('/', '.');

            try {
                Class<?> clazz = loader.loadClass(className);

                Addon addon = clazz.getAnnotation(Addon.class);

                if(addon == null) continue;

                if(!TerraAddon.class.isAssignableFrom(clazz)) throw new IllegalArgumentException("Addon class must extend TerraAddon.");

                set.add((Class<? extends TerraAddon>) clazz);
            } catch(ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return set;
    }
}
