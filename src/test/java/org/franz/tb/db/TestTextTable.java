/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.franz.tb.db;

import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 *
 * @author pfranz
 */
public class TestTextTable {
   
   @Test
   public void testCreateTable()
   {
      try {
         File tmpFile = File.createTempFile("table", "");
         
         tmpFile.delete();
         
         TextTable table = TextTable.createTable(tmpFile);
         
         assertTrue(table.isValid());
         
         table.addColumn("Name");
         table.addColumn("Email");
         table.addColumn("Id");
         
         TextRecord rec = table.createRecord();
         
         assertTrue(rec.getRecordFile().exists());
         
         rec.setColumnData(0, "John Smith");
         rec.setColumnData(1, "John.Smith@domain.com");
         rec.setColumnData(2, "jsmith");
         
         rec.write();
         
         TextRecord readRec = table.getRecord(rec.getRecordId());
         
         assertEquals (readRec.getColumnData(0), "John Smith");
         assertEquals (readRec.getColumnData(1), "John.Smith@domain.com");
         assertEquals (readRec.getColumnData(2), "jsmith");
         
         table.deleteTable();
      } catch (IOException ex) {
         fail (ex.getMessage());
         Logger.getLogger(TestTextTable.class.getName()).log(Level.SEVERE, null, ex);
      }
      
   }
   
}
