/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.franz.tb.db;

/**
 *
 * @author pfranz
 */
public class TextColumnDefinition {
   
   private String name;
   
   public TextColumnDefinition()
   {
      
   }
   
   public TextColumnDefinition (String n)
   {
      this.name = n;
   }

   /**
    * @return the name
    */
   public String getName() {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(String name) {
      this.name = name;
   }
   
   public void processLine (String line)
   {
      name = line;
   }
   
   public String createLine ()
   {
      return name;
   }
}
