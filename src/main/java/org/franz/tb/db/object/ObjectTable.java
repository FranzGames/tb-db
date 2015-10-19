/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.franz.tb.db.object;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.franz.tb.db.TextRecord;
import org.franz.tb.db.TextTable;
import org.franz.tb.db.TextTableDefinition;

/**
 *
 * @author pfranz
 */
public class ObjectTable extends TextTable {

   static ObjectTable openTable(File directory) throws IOException {
      ObjectTable table = new ObjectTable(directory);

      table.loadDefinition();

      return table;
   }

   static ObjectTable createTable(File directory) throws IOException {
      if (!directory.mkdirs()) {
         return null;
      }

      ObjectTable table = new ObjectTable(directory);

      table.setupTable();

      return table;
   }
   
   protected ObjectTable (File directory)
   {
      super (directory);
   }
   
   @Override
   protected TextTableDefinition createTableDefinitionObject (File file)
   {
      return new ObjectTableDefinition (file);
   }
   
   @Override
   protected TextRecord createRecordObject (File file)
   {
      ObjectRecord rec = new ObjectRecord(file);
      
      rec.setDefinition((ObjectTableDefinition)this.getTableDefinition());
      
      return rec;
   }
   
   @Override
   protected void setupTable() throws IOException
   {
      super.setupTable();
   }

   @Override
   public void addColumn(String name) {
      this.addColumn(name, String.class);
   }
   
   public void addColumn(String name, Class classType) {
      ObjectTableDefinition def = (ObjectTableDefinition) this.getTableDefinition();
      
      def.addColumn(name, classType);
      
      try {
         this.writeDefinition();
      } catch (IOException ex) {
         Logger.getLogger(TextTable.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
