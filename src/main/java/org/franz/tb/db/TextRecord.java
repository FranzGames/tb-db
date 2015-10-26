/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.franz.tb.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pfranz
 */
public class TextRecord {
   
   public static String getRecordIdFromFile(File file)
   {
      if (file == null)
         return null;
      
      int index = file.getName().lastIndexOf('.');
      
      if (index < 0)
         return file.getName();
      
      return file.getName().substring(0, index);
   }
   

   static String EMPTY_COLUMN = "";
   private File recordFile;
   private List<String> data = new ArrayList<String>();

   public TextRecord(File file) {
      this.recordFile = file;
   }

   /**
    * @return the recordFile
    */
   public File getRecordFile() {
      return recordFile;
   }
   
   public boolean delete()
   {
      return this.getRecordFile().delete();
   }
   
   public String getRecordId()
   {
      return getRecordIdFromFile(this.getRecordFile());
   }

   void fillRecord(int numOfColumns) {
      while (getData().size() < numOfColumns) {
         getData().add(EMPTY_COLUMN);
      }
   }

   public void read() throws IOException {
      BufferedReader reader = new BufferedReader(new FileReader(recordFile));
      String line = null;

      try {
         line = reader.readLine();
      } finally {
         if (reader != null) {
            reader.close();
         }
      }

      if (line != null) {
         List<String> cols = TextUtil.parseCsv(line);

         if (cols != null) {
            this.setData(cols);
         }
      }
   }

   public void write() throws IOException {
      PrintWriter writer = new PrintWriter(new FileWriter(this.getRecordFile()));

      try {
         writer.println(TextUtil.encodeCsv(this.getData()));
      } finally {
         if (writer != null) {
            writer.close();
         }
      }
   }

   public void setColumnData (int colNo, String value)
   {
      if (colNo < 0)
         return;
      
      while (this.getData().size() <= colNo)
      {
         this.getData().add(EMPTY_COLUMN);
      }
      
      this.getData().set(colNo, value);
   }
   
   public String getColumnData (int colNo)
   {
      if (colNo < 0 || colNo >= this.getData().size())
         return "";
      
      return this.getData().get(colNo);
   }
   
   public Object getValue (int colNo)
   {
      return this.getColumnData(colNo);
   }
   
   /**
    * @return the data
    */
   public List<String> getData() {
      return data;
   }

   /**
    * @param data the data to set
    */
   public void setData(List<String> data) {
      this.data = data;
   }

}
