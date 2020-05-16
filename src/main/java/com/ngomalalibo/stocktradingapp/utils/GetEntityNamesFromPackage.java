package com.ngomalalibo.stocktradingapp.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class GetEntityNamesFromPackage
{
    private static final char PKG_SEPARATOR = '.';
    private static final char DIR_SEPARATOR = '/';
    private static final String CLASS_FILE_SUFFIX = ".class";
    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";
    
   /* public static Set<String> retrieveEntityNamesFromPackage(String scannedPackage)
    {
        //System.out.println("*** Retrieving entities from package ***");
        String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        System.out.println("scannedUrl = " + scannedUrl);
        if (scannedUrl == null)
        {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
        }
        
        File scannedDir = new File(scannedUrl.getFile());
        Set<String> classes = new HashSet<String>();
        System.out.println("Dir content -> " + scannedDir.listFiles());
        if (scannedDir.listFiles() != null)
        {
            for (File file : scannedDir.listFiles())
            {
                String name = file.getName();
                if (name.endsWith(CLASS_FILE_SUFFIX))
                {
                    int endIndex = name.length() - CLASS_FILE_SUFFIX.length();
                    name = name.substring(0, endIndex);
                    classes.add(name);
                }
                classes.add(name);
                //classes.addAll(findInDirectory(file, scannedPackage));
            }
            Stream<String> sorted = classes.stream().sorted();
            sorted.forEach(log::info);
            //System.out.println(d);
            sorted.forEach(classes::add);
        }
        
        return classes;
    }*/
    
    public static Set<String> retrieveEntityNamesFromPackage(String scannedPackage)
    {
        Set<String> entityNames = new HashSet<>();
        entityNames.add("ActivityLog");
        entityNames.add("Client");
        entityNames.add("ClientAccount");
        entityNames.add("ClientPortFolio");
        entityNames.add("ClientTransaction");
        entityNames.add("Note");
        entityNames.add("User");
        
        
        return entityNames;
    }
    
    
    public static void main(String[] args)
    {
        Set<String> entities = retrieveEntityNamesFromPackage("com.pc.weblibrarian.entities");
        entities.forEach(log::info);
    }
}
