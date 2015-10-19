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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pfranz
 */
public class TextTable {

   static TextTable openTable(File directory) throws IOException {
      TextTable table = new TextTable(directory);

      table.loadDefinition();

      return table;
   }

   static TextTable createTable(File directory) throws IOException {
      if (!directory.mkdirs()) {
         return null;
      }

      TextTable table = new TextTable(directory);

      table.setupTable();

      return table;
   }

   private final File tableDirectory;
   private TextTableDefinition definition;
   Random rand = new Random(System.currentTimeMillis());

   protected TextTable(File root) {
      this.tableDirectory = root;
   }

   public boolean isValid() {
      if (!getRecordsDirectory().exists() || !getRecordsDirectory().isDirectory()) {
         return false;
      }

      if (!getIndexesDirectory().exists() || !getIndexesDirectory().isDirectory()) {
         return false;
      }

      return true;
   }

   public void deleteTable() throws IOException {
      File[] files = this.getRecordsDirectory().listFiles();

      for (File file : files) {
         if (!file.delete()) {
            throw new IOException("Unable to delete file " + file.getPath());
         }
      }

      files = this.getIndexesDirectory().listFiles();

      for (File file : files) {
         if (!file.delete()) {
            throw new IOException("Unable to delete file " + file.getPath());
         }
      }

      files = this.getTableDirectory().listFiles();

      for (File file : files) {
         if (!file.delete()) {
            throw new IOException("Unable to delete file/directory " + file.getPath());
         }
      }

      if (!this.getTableDirectory().delete()) {
         throw new IOException("Unable to delete directory " + this.getTableDirectory().getPath());
      }

   }

   public File getRecordsDirectory() {
      return new File(this.getTableDirectory() + File.separator + "records");
   }

   public File getIndexesDirectory() {
      return new File(this.getTableDirectory() + File.separator + "indexes");
   }

   public File getDefinitionFile() {
      return new File(this.getTableDirectory() + File.separator + "columns.txt");
   }

   public File getRecordFile(String recordId) {
      return new File(getRecordsDirectory() + File.separator + recordId + ".csv");
   }

   protected String generateRecordId() {
      return "" + System.currentTimeMillis() + "-" + rand.nextLong();
   }

   protected File generateRecordFile() throws IOException {
      String recId = this.generateRecordId();
      File file = getRecordFile(recId);
      Random localRand = new Random(System.currentTimeMillis() + rand.nextInt());

      while (!file.createNewFile()) {
         file = getRecordFile(recId + "-" + localRand.nextInt());
      }

      return file;
   }

   protected void setupTable() throws IOException {
      File recordDir = this.getRecordsDirectory();

      if (!recordDir.mkdir()) {
         throw new IOException("Unable to create directory " + recordDir);
      }

      File indexesDir = this.getIndexesDirectory();

      if (!indexesDir.mkdir()) {
         throw new IOException("Unable to create directory " + indexesDir);
      }

      definition = this.createTableDefinitionObject(this.getDefinitionFile());

      this.writeDefinition();
   }

   protected TextTableDefinition createTableDefinitionObject(File file) {
      return new TextTableDefinition(file);
   }

   public TextTableDefinition getTableDefinition() {
      return this.definition;
   }

   protected void loadDefinition() throws IOException {
      TextTableDefinition def = createTableDefinitionObject(getDefinitionFile());

      def.load();

      definition = def;
   }

   protected void writeDefinition() throws IOException {
      definition.write();
   }

   /**
    * @return the tableDirectory
    */
   public File getTableDirectory() {
      return tableDirectory;
   }

   public void addColumn(String columnName) {

      if (columnName == null) {
         throw new NullPointerException("Column Nane is null");
      }

      if (columnName.indexOf('-') >= 0) {
         throw new IllegalArgumentException("Column Name contains the '-' character.");
      }

      this.definition.addColumn(columnName);

      try {
         this.writeDefinition();
      } catch (IOException ex) {
         Logger.getLogger(TextTable.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public int getColumnNo(String columnName) {
      return this.definition.getColumnNo(columnName);
   }

   public String getColumnName(int columnNo) {
      return this.getTableDefinition().getColumnName(columnNo);
   }

   public int getNumOfColumns() {
      return this.getTableDefinition().getNumOfColumns();
   }

   protected TextRecord createRecordObject(File file) {
      return new TextRecord(file);
   }

   public TextRecord createRecord() throws IOException {
      File genFile = generateRecordFile();
      TextRecord rec = createRecordObject(genFile);

      rec.fillRecord(this.getNumOfColumns());
      rec.write();

      return rec;
   }

   public TextRecord getRecord(String recordId) throws IOException {
      TextRecord rec = createRecordObject(getRecordFile(recordId));

      rec.read();
      rec.fillRecord(this.getNumOfColumns());

      return rec;
   }

   public boolean deleteRecord(String recordId) throws IOException {
      TextRecord rec = createRecordObject(getRecordFile(recordId));

      return rec.delete();
   }

   public TextRecord findRecord(TextQuery query) {
      return findRecord(query, null);
   }

   public TextRecord findRecord(TextQuery query, TextIndex index) {
      List<String> recs = index != null ? index.getRecordIds() : listOfRecordIds();

      for (String recId : recs) {
         try {
            TextRecord rec = this.getRecord(recId);

            if (query == null) {
               return rec;
            } else if (query.match(this, rec)) {
               return rec;
            }
         } catch (IOException ex) {
            Logger.getLogger(TextTable.class.getName()).log(Level.SEVERE, null, ex);
         }
      }

      return null;
   }

   public List<TextRecord> findRecords(TextQuery query) {
      return findRecords(query, null);
   }

   public List<TextRecord> findRecords(TextQuery query, TextIndex index) {
      List<TextRecord> result = new ArrayList<TextRecord>();
      List<String> recs = index != null ? index.getRecordIds() : listOfRecordIds();

      for (String recId : recs) {
         try {
            TextRecord rec = this.getRecord(recId);

            if (query == null) {
               result.add(rec);
            } else if (query.match(this, rec)) {
               result.add(rec);
            }
         } catch (IOException ex) {
            Logger.getLogger(TextTable.class.getName()).log(Level.SEVERE, null, ex);
         }
      }

      return result;
   }

   public List<String> listOfRecordIds() {
      List<String> list = new ArrayList<String>();
      File[] files = this.getRecordsDirectory().listFiles(new RecordsFilter());

      for (File file : files) {

         list.add(TextRecord.getRecordIdFromFile(file));
      }

      return list;
   }

   public TextIndex createIndex(List<String> columns) throws IOException {
      File file = new File(this.getIndexesDirectory() + File.separator
         + TextIndex.getIndexFilename(columns));

      TextIndex index = new TextIndex(file);

      List<String> recIds = listOfRecordIds();

      for (String recId : recIds) {
         TextRecord rec = this.getRecord(recId);

         index.addEntry(rec, this);
      }

      index.write();

      return index;
   }

   public TextIndex getIndex(List<String> columns) throws IOException {
      File file = new File(this.getIndexesDirectory() + File.separator
         + TextIndex.getIndexFilename(columns));

      TextIndex index = new TextIndex(file);

      index.read();

      return index;
   }

   static class RecordsFilter implements FileFilter {

      @Override
      public boolean accept(File pathname) {
         if (pathname.isFile() && pathname.getName().endsWith(".csv")) {
            return true;
         }

         return false;
      }
   }

}
