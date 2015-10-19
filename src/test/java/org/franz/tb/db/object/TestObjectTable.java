/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.franz.tb.db.object;

import org.franz.tb.db.object.ObjectRecord;
import org.franz.tb.db.object.ObjectTable;
import java.io.File;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.franz.tb.db.TestTextTable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author pfranz
 */
public class TestObjectTable {
   static SimpleDateFormat dateFormatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");   
   
  @Test
   public void testCreateTable()
   {
      try {
         File tmpFile = File.createTempFile("table", "");
         
         tmpFile.delete();
         
         ObjectTable table = ObjectTable.createTable(tmpFile);
         java.util.Date dt = new java.util.Date();
         String strDate = dateFormatter.format(dt);
         
         dt = dateFormatter.parse(strDate, new ParsePosition(0));
         
         
         assertTrue(table.isValid());
         
         table.addColumn("Name");
         table.addColumn("Email");
         table.addColumn("Id", Integer.class);
         table.addColumn("start", java.util.Date.class);
         
         ObjectRecord rec = (ObjectRecord)table.createRecord();
         
         assertTrue(rec.getRecordFile().exists());
         
         rec.setValue("Name", "John Smith");
         rec.setValue("Email", "John.Smith@domain.com");
         rec.setValue("Id", 100);
         rec.setValue("start", dt);
         
         rec.write();
         

         ObjectRecord readRec = (ObjectRecord)table.getRecord(rec.getRecordId());
         
         String cmpDate = dateFormatter.format(readRec.getValue("start"));
         
         assertEquals (readRec.getValue("Name"), "John Smith");
         assertEquals (readRec.getValue("Email"), "John.Smith@domain.com");
         assertEquals (readRec.getValue("Id"), 100);
         assertEquals (cmpDate, strDate);
         
         table.deleteTable();
      } catch (IOException ex) {
         fail (ex.getMessage());
         Logger.getLogger(TestTextTable.class.getName()).log(Level.SEVERE, null, ex);
      }
      
   }   
}
