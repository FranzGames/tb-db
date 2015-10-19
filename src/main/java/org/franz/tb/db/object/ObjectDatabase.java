/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.franz.tb.db.object;

import java.io.File;
import java.io.IOException;
import org.franz.tb.db.TextDatabase;
import org.franz.tb.db.TextTable;

/**
 *
 * @author pfranz
 */
public class ObjectDatabase extends TextDatabase {
   
   public static ObjectDatabase openDatabase (File directory) throws IOException
   {
      if (!directory.exists())
      {
         throw new IOException ("Directory "+directory+" does not exist");
      }
      
      return new ObjectDatabase (directory);
   }

   public static ObjectDatabase createDatabase (File directory) throws IOException
   {
      if (!directory.mkdirs())
      {
         throw new IOException ("Unable to create directory "+directory);
      }
      
      return openDatabase(directory);
   }
   
   private File databaseDirectory;
   
   protected ObjectDatabase (File directory)
   {
      super(directory);
   }
   
   
   public TextTable openTable (String table) throws IOException
   {
      return ObjectTable.openTable(this.getTableFile(table));
   }
   
   public TextTable createTable (String table) throws IOException
   {
      return ObjectTable.createTable(this.getTableFile(table));
   }
   
   
}
