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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author pfranz
 */
public class TextIndex {

   File indexFile;
   private List<IndexEntry> entries = new ArrayList<IndexEntry>();
   List<String> columns = new ArrayList<String>();
   
   public static String getIndexFilename (List<String> columns)
   {
      StringBuilder builder = new StringBuilder();
      
      for (int i = 0; i < columns.size(); i++)
      {
         if (i > 0)
         {
            builder.append("-");
         }
         
         builder.append(columns.get(i));
      }
      
      builder.append(".txt");
      
      return builder.toString();
   }

   public TextIndex(File file) {
      this.indexFile = file;

      extractColumnNames();
   }

   private final void extractColumnNames() {
      List<String> cols = new ArrayList<String>();
      String filename = indexFile.getName();

      if (!filename.endsWith(".txt")) {
         return;
      }

      String colNames = filename.substring(0, filename.length() - 4);
      StringTokenizer tokenizer = new StringTokenizer(colNames, "-");

      while (tokenizer.hasMoreTokens()) {
         cols.add(tokenizer.nextToken());
      }

      this.columns = cols;
   }

   public void addEntry(TextRecord rec, TextTable table) {
      List<String> values = new ArrayList<String>();

      if (rec == null || table == null) {
         return;
      }

      for (String col : this.columns) {
         int index = table.getColumnNo(col);
         String value = null;

         if (index < 0) {
            value = "";
         } else {
            value = rec.getColumnData(index);
         }

         values.add(value);
      }

      IndexEntry entry = new IndexEntry(values, rec.getRecordId());

      this.entries.add(entry);
   }

   public void sort() {
      Collections.sort(entries, new CompareIndexEntry());
   }

   static class CompareIndexEntry implements Comparator<IndexEntry> {

      @Override
      public int compare(IndexEntry o1, IndexEntry o2) {
         if (o1 == null && o2 == null) {
            return 0;
         } else if (o1 == null) {
            return -1;
         } else if (o2 == null) {
            return 1;
         } else {
            List<String> values1 = o1.getKeyValues();
            List<String> values2 = o2.getKeyValues();

            for (int i = 0; i < values1.size(); i++) {
               String value1 = values1.get(i);
               String value2 = values2.get(i);

               if (value1 == null && value2 == null) {
                  return 0;
               } else if (value1 == null) {
                  return -1;
               } else if (value2 == null) {
                  return 1;
               } else {
                  int c = value1.compareTo(value2);

                  if (c != 0) {
                     return c;
                  }
               }
            }
         }

         return 0;
      }

   }

   public void read() throws IOException {
      BufferedReader reader = new BufferedReader(new FileReader(this.indexFile));
      List<IndexEntry> ents = new ArrayList<IndexEntry>();
      String line = null;

      try {
         while ((line = reader.readLine()) != null) {
            IndexEntry entry = new IndexEntry();

            entry.processLine(line);
            ents.add(entry);
         }
      } finally {
         reader.close();
      }

      this.entries = ents;
   }

   public void write() throws IOException {
      PrintWriter writer = new PrintWriter(new FileWriter(this.indexFile));

      try {
         for (IndexEntry entry : this.entries) {
            writer.println(entry.getLine());
         }
      } finally {
         writer.close();
      }
   }

   /**
    * @return the recordIds
    */
   public List<String> getRecordIds() {
      List<String> recordIds = new ArrayList<String>();

      for (IndexEntry entry : this.entries) {
         recordIds.add(entry.getRecordId());
      }

      return recordIds;
   }
   
   public void deleteRecordId (String recId)
   {
      if (recId == null)
         return;
      
      for (int i = 0; i < this.entries.size(); i++)
      {
         if (this.entries.get(i).getRecordId().equals(recId))
         {
            this.entries.remove(i);
            break;
         }
      }
   }

   public static class IndexEntry {

      private List<String> keyValues = new ArrayList<String>();
      private String recordId;

      public IndexEntry() {
      }

      public IndexEntry(List<String> values, String recId) {
         this.keyValues = values;
         this.recordId = recId;
      }

      /**
       * @return the keyValues
       */
      public List<String> getKeyValues() {
         return keyValues;
      }

      /**
       * @param keyValues the keyValues to set
       */
      public void setKeyValues(List<String> keyValues) {
         this.keyValues = keyValues;
      }

      /**
       * @return the recordId
       */
      public String getRecordId() {
         return recordId;
      }

      /**
       * @param recordId the recordId to set
       */
      public void setRecordId(String recordId) {
         this.recordId = recordId;
      }

      public void processLine(String str) {
         List<String> data = TextUtil.parseCsv(str);

         if (data.isEmpty()) {
            return;
         }

         // Remove the last column as that is the record id
         this.setRecordId(data.get(data.size() - 1));
         data.remove(data.size() - 1);

         this.setKeyValues(data);
      }

      public String getLine() {
         return TextUtil.encodeCsv(this.getKeyValues())+","+this.getRecordId();
      }
   }
}
