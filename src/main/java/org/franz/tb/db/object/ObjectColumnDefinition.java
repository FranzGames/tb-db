/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.franz.tb.db.object;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.franz.tb.db.TextColumnDefinition;

/**
 *
 * @author pfranz
 */
public class ObjectColumnDefinition extends TextColumnDefinition {

   private Class classType;
   private boolean isKey;
   static final Map<String,String> classNameToType;
   static final Map<String,String> typeToClassName;
   
   static {
      classNameToType = new HashMap<String, String>();
      
      classNameToType.put("java.util.Date", "Date");
      classNameToType.put("java.lang.String", "String");
      classNameToType.put("java.lang.Double", "Double");
      classNameToType.put("java.lang.Integer", "Integer");
      
      typeToClassName = new HashMap<String, String>();
      
      Iterator iter = classNameToType.entrySet().iterator();
      
      while (iter.hasNext())
      {
         Map.Entry entry = (Map.Entry)iter.next();
         String key = (String) entry.getKey();
         String value = classNameToType.get(key);
         
         typeToClassName.put(value, key);
      }
   }

   public ObjectColumnDefinition() {
      super();
   }

   public ObjectColumnDefinition(String n) {
      super(n);
   }

   public ObjectColumnDefinition(String name, Class type) {
      this(name, type, false);
   }

   public ObjectColumnDefinition(String name, Class type, boolean isKey) {
      this(name);
      classType = type;
      this.isKey = isKey;
   }

   public Class getClassType() {
      return classType;
   }

   public boolean isKey() {
      return isKey;
   }
   
   public static String convertClassNameToType (String className)
      {
      return classNameToType.get(className);
      }
   
   public static String convertTypeToClassName (String type)
      {
      return typeToClassName.get(type);
      }
   

   @Override
   public void processLine(String str) {
      int index = str.indexOf("=");

      if (index < 0) {
         throw new IllegalArgumentException("The line needs an equals");
      }

      this.setName(str.substring(0, index));

      String type = str.substring(index + 1);

      if (type.endsWith("*")) {
         setIsKey(true);
         type = type.substring(0, type.length() - 1);
      }

      try {
         setClassType(Class.forName(convertTypeToClassName(type)));
      } catch (Throwable t) {
         throw new IllegalArgumentException("Invalid Class " + type);
      }
   }

   @Override
   public String createLine() {
      StringBuilder buf = new StringBuilder();

      buf.append(this.getName());
      buf.append("=");
      buf.append(convertClassNameToType(classType.getName()));

      if (isKey) {
         buf.append("*");
      }

      return buf.toString();
   }

   /**
    * @param classType the classType to set
    */
   public void setClassType(Class classType) {
      this.classType = classType;
   }

   /**
    * @param isKey the isKey to set
    */
   public void setIsKey(boolean isKey) {
      this.isKey = isKey;
   }

}
