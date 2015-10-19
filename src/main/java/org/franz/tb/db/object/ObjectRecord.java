/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.franz.tb.db.object;

import java.io.File;
import java.util.Date;
import org.franz.tb.db.TextRecord;

/**
 *
 * @author pfranz
 */
public class ObjectRecord extends TextRecord {
   
   ObjectTableDefinition def;

   public ObjectRecord(File file) {
      super(file);
   }
   
   public void setDefinition (ObjectTableDefinition d)
   {
      def = d;
   }

   public ObjectTableDefinition getDefinition() {
      return def;
   }

   protected int getColumnNo(String name) {
      return getDefinition().getColumnNo(name);
   }

   public void setValue(String name, Object value) {
      int colNo = getColumnNo(name);

      if (colNo < 0) {
         throw new IllegalArgumentException("Column " + name + " is not part of the data");
      }

      if (value == null) {
         throw new NullPointerException("Column " + name + " can not be null");
      }

      this.setColumnData(colNo, getDefinition().convertToString(name, value));
   }

   public boolean isColumn(String name) {
      return getColumnNo(name) >= 0;
   }

   public Object getValue(String column) {
      int colNo = getColumnNo(column);

      if (colNo < 0) {
         throw new IllegalArgumentException("Column " + column + " is not part of the data");
      }

      if (this.getColumnData(colNo) == null) {
         return getDefinition().getDefaultValue(column);
      }

      return getDefinition().convertFromString(column, this.getColumnData(colNo));
   }

   public int getInteger(String column) {
      return ((Integer) getValue(column)).intValue();
   }

   public double getDouble(String column) {
      return ((Double) getValue(column)).doubleValue();
   }

   public long getLong(String column) {
      return ((Long) getValue(column)).longValue();
   }

   public boolean getBoolean(String column) {
      return ((Boolean) getValue(column)).booleanValue();
   }

   public Date getDate(String column) {
      return (Date) getValue(column);
   }

   public String getString(String column) {
      return getValue(column).toString();
   }

   public void setValue(String column, int value) {
      setValue(column, new Integer(value));
   }

   public void setValue(String column, double value) {
      setValue(column, new Double(value));
   }

   public void setValue(String column, long value) {
      setValue(column, new Long(value));
   }

   public void setValue(String column, boolean value) {
      setValue(column, Boolean.valueOf(value));
   }

   public void setValue(String column, String value) {
      setValue(column, (Object) value);
   }

   public void setValue(String column, Date value) {
      setValue(column, (Object) value);
   }

   public void setColumn(String column, String value) {
      setValue(column, getDefinition().convertFromString(column, value));
   }

}
