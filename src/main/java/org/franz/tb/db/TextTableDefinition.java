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
public class TextTableDefinition {
   
   private List<TextColumnDefinition> columns = new ArrayList<TextColumnDefinition>();
   File definitionFile;

   public TextTableDefinition(File file)
   {
      this.definitionFile = file;
   }
   
   protected TextColumnDefinition createDefinitionObject()
   {
      return new TextColumnDefinition();
   }
   
   void setColumns(List<TextColumnDefinition> cols)
   {
      this.columns = cols;
   }
   
   List<TextColumnDefinition> getColumns()
   {
      return this.columns;
   }
   
   public int getNumOfColumns()
   {
      return this.getColumns().size();
   }
   
   public String getColumnName(int columnNo) {
      if (columnNo < 0 || columnNo >= this.getColumns().size()) {
         return null;
      }

      return this.getColumns().get(columnNo).getName();
   }
   
   public TextColumnDefinition getColumnDefinition(int columnNo) {
      if (columnNo < 0 || columnNo >= this.getColumns().size()) {
         return null;
      }

      return this.getColumns().get(columnNo);
   }
   
   
   public int getColumnNo (String name)
   {
      for (int i = 0; i < this.getNumOfColumns(); i++)
      {
         if (name.equals(this.columns.get(i).getName()))
            return i;
      }
      
      return -1;
   }
   
   public void addColumn (String name)
   {
      TextColumnDefinition colDef = this.createDefinitionObject();

      colDef.setName(name);
      
      this.columns.add(colDef);
   }
   
   protected void addColumn (TextColumnDefinition coldef)
   {
      this.columns.add(coldef);
   }
   
   protected void load() throws IOException {
      BufferedReader reader = new BufferedReader(new FileReader(this.definitionFile));
      String line;
      List<TextColumnDefinition> cols = new ArrayList<TextColumnDefinition>();

      try {
         while ((line = reader.readLine()) != null) {
            TextColumnDefinition colDef = this.createDefinitionObject();
            
            colDef.processLine(line);
            cols.add(colDef);
         }

         this.setColumns(cols);
      } finally {
         reader.close();
      }
   }

   protected void write() throws IOException {
      PrintWriter writer = new PrintWriter(new FileWriter(this.definitionFile));

      try {
         for (TextColumnDefinition col : this.getColumns()) {
            writer.println(col.createLine());
         }
      } finally {
         writer.close();
      }
   }
}
