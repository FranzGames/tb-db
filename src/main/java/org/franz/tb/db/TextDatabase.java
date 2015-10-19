/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.franz.tb.db;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pfranz
 */
public class TextDatabase {
   public static TextDatabase openDatabase (File directory) throws IOException
   {
      if (!directory.exists())
      {
         throw new IOException ("Directory "+directory+" does not exist");
      }
      
      return new TextDatabase (directory);
   }

   public static TextDatabase createDatabase (File directory) throws IOException
   {
      if (!directory.mkdirs())
      {
         throw new IOException ("Unable to create directory "+directory);
      }
      
      return openDatabase(directory);
   }
   
   private File databaseDirectory;
   
   protected TextDatabase (File directory)
   {
      this.databaseDirectory = directory;
   }
   
   public List<String> listOfTables ()
   {
    List<String> list = new ArrayList<String> ();
    File[] dirs = getDatabaseDirectory().listFiles(new TableFilter());
    
    for (File dir : dirs)
    {
       list.add(dir.getName());
    }
    
    return list;
   }
   
   public File getTableFile(String tableName)
   {
      return new File (this.getDatabaseDirectory().getPath()+File.separator+tableName);
   }
   
   public TextTable openTable (String table) throws IOException
   {
      return TextTable.openTable(getTableFile (table));
   }
   
   public TextTable createTable (String table) throws IOException
   {
      return TextTable.createTable(getTableFile (table));
   }

   /**
    * @return the databaseDirectory
    */
   public File getDatabaseDirectory() {
      return databaseDirectory;
   }
   
   static class TableFilter implements FileFilter
   {

      @Override
      public boolean accept(File pathname) {
        if (pathname.isDirectory())
        {
           return true;
        }
        
        return false;
      }
   }
}
