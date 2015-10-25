/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.franz.tb.db.object;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.franz.tb.db.TextColumnDefinition;
import org.franz.tb.db.TextTableDefinition;

/**
 *
 * @author pfranz
 */
public class ObjectTableDefinition extends TextTableDefinition {
   public static final HashMap stringConvertMap = new HashMap();
   public static final HashMap defaultMap = new HashMap();
   

   static {
      defaultMap.put(Long.class, new Long(0l));
      defaultMap.put(Integer.class, new Integer(0));
      defaultMap.put(Double.class, new Double(0.0));
      defaultMap.put(Byte.class, new Byte((byte) 0));
      defaultMap.put(Character.class, new Character(' '));
      defaultMap.put(Float.class, new Float(0));
      defaultMap.put(String.class, new String());
      defaultMap.put(Short.class, new Short((short) 0));
      defaultMap.put(Boolean.class, Boolean.valueOf(false));
      defaultMap.put(Date.class, new Date());

      stringConvertMap.put(String.class, new StringConvert());
      stringConvertMap.put(Long.class, new LongConvert());
      stringConvertMap.put(Double.class, new DoubleConvert());
      stringConvertMap.put(Integer.class, new IntegerConvert());
      stringConvertMap.put(Date.class, new DateConvert());
      stringConvertMap.put(Boolean.class, new BooleanConvert());
   }
   
   public ObjectTableDefinition(File file) {
      super(file);
   }

   interface ConvertFromToString {
      String convertTo(Object obj);
      Object convertFrom(String str);
   }

   static class StringConvert implements ConvertFromToString {

      @Override
      public Object convertFrom(String str) {
         return str;
      }

      @Override
      public String convertTo(Object obj) {
         return (String) obj;
      }
   }

   static SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 

   static class DateConvert implements ConvertFromToString {

      @Override
      public Object convertFrom(String str) {
         try {
            return dateFormat.parse(str);
         } catch (Throwable t) {
            return new Date();
         }
      }

      @Override
      public String convertTo(Object obj) {
         if (obj == null)
            return null;
         
         return dateFormat.format(obj);
      }
   }

   static class BooleanConvert implements ConvertFromToString {

      @Override
      public Object convertFrom(String str) {
         try {
            return Boolean.valueOf(str);
         } catch (Throwable t) {
            return Boolean.valueOf(false);
         }
      }

      @Override
      public String convertTo(Object obj) {
         if (obj == null)
            return null;
         
         return obj.toString();
      }
   }

   static class LongConvert implements ConvertFromToString {

      @Override
      public Object convertFrom(String str) {
         try {
            return Long.valueOf(str);
         } catch (Throwable t) {
            return new Long(0);
         }
      }

      @Override
      public String convertTo(Object obj) {
         if (obj == null)
            return null;
         
         return obj.toString();
      }
   }

   static class IntegerConvert implements ConvertFromToString {

      @Override
      public Object convertFrom(String str) {
         try {
            return Integer.valueOf(str);
         } catch (Throwable t) {
            return new Integer(0);
         }
      }

      @Override
      public String convertTo(Object obj) {
         if (obj == null)
            return null;
         
         return obj.toString();
      }
   }

   static class DoubleConvert implements ConvertFromToString {

      @Override
      public Object convertFrom(String str) {
         try {
            return Double.valueOf(str);
         } catch (Throwable t) {
            return new Double(0.0);
         }
      }

      @Override
      public String convertTo(Object obj) {
         if (obj == null)
            return null;
         
         return obj.toString();
      }
   }
   
   public String convertToString (String columnName, Object value)
      {
      int index = this.getColumnNo(columnName);
      
      if (index < 0)
         return null;
      
      ObjectColumnDefinition colDef = (ObjectColumnDefinition) getColumnDefinition (index);
      
      ConvertFromToString convertor = (ConvertFromToString) stringConvertMap.get(colDef.getClassType());

      if (convertor != null)
          return convertor.convertTo(value);
      
      return null;
      }
   
   
   public Object convertFromString (String columnName, String value)
      {
      int index = this.getColumnNo(columnName);
      
      if (index < 0)
         return null;
      
      ObjectColumnDefinition colDef = (ObjectColumnDefinition) getColumnDefinition (index);
      
      ConvertFromToString convertor = (ConvertFromToString) stringConvertMap.get(colDef.getClassType());

      if (convertor != null)
          return convertor.convertFrom(value);
      
      return null;
      }
   
   public final Object getDefaultValue(String columnName)
      {
      int index = this.getColumnNo(columnName);
      
      if (index < 0)
         return null;
      
      ObjectColumnDefinition colDef = (ObjectColumnDefinition) getColumnDefinition (index);
      
      return defaultMap.get(colDef.getClassType());
      }

   @Override
   protected TextColumnDefinition createDefinitionObject() {
      return new ObjectColumnDefinition();
   }

   public void addColumn(String name, Class classType) {
      ObjectColumnDefinition colDef = (ObjectColumnDefinition) this.createDefinitionObject();

      colDef.setName(name);
      colDef.setClassType(classType);

      this.addColumn(colDef);
   }
}
