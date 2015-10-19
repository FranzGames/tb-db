/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.franz.tb.db;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pfranz
 */
public class TextQuery {

   List<Criteria> criterias = new ArrayList<Criteria>();
   private boolean matchAll;

   public TextQuery() {

   }

   public void addCriteria(Criteria crit) {
      criterias.add(crit);
   }

   /**
    * @return the matchAll
    */
   public boolean isMatchAll() {
      return matchAll;
   }

   /**
    * @param matchAll the matchAll to set
    */
   public void setMatchAll(boolean matchAll) {
      this.matchAll = matchAll;
   }

   public boolean match(TextTable table, TextRecord rec) {
      boolean result = matchAll;

      for (Criteria crit : criterias) {
         int colNo = table.getColumnNo(crit.getColumnName());
         String value = rec.getColumnData(colNo);

         boolean critResult = crit.match(value);

         if (critResult && !matchAll) {
            result = true;
            break;
         }

         if (!critResult && matchAll) {
            result = false;
            break;
         }

      }

      return result;
   }

   public static enum Operand {

      Equals, NotEquals, Contains, NotContains, LessThan, LessThanEquals, 
      GreaterThan, GreaterThanEquals
   }

   public static class Criteria {

      private String columnName;
      private TextQuery.Operand operand;
      private Object value;

      public Criteria(String column, TextQuery.Operand op, Object val) {
         this.columnName = column;
         this.operand = op;
         this.value = val;
      }

      public boolean match(Object columnValue) {
         Comparable compareValue = (Comparable)columnValue;
         int compare = 0;
         
         if (columnValue == null)
            compare = -1;
         else
         {
            compare = compareValue.compareTo(this.getValue());
         }

         switch (this.getOperand()) {
            case Equals:
               return compare == 0;

            case NotEquals:
               return compare != 0;

            case LessThan:
               return compare < 0;

            case LessThanEquals:
               return compare <= 0;
               
            case GreaterThan:
               return compare > 0;

            case GreaterThanEquals:
               return compare >= 0;
               
            case Contains:
               if (compareValue == null)
                  return false;
               
               return ((String)compareValue).contains((String)this.getValue());

            case NotContains:
               if (compareValue == null)
                  return true;
               
               return !((String)compareValue).contains((String)this.getValue());

            default:
               break;
         }

         return false;
      }

      /**
       * @return the columnName
       */
      public String getColumnName() {
         return columnName;
      }

      /**
       * @param columnName the columnName to set
       */
      public void setColumnName(String columnName) {
         this.columnName = columnName;
      }

      /**
       * @return the operand
       */
      public TextQuery.Operand getOperand() {
         return operand;
      }

      /**
       * @param operand the operand to set
       */
      public void setOperand(TextQuery.Operand operand) {
         this.operand = operand;
      }

      /**
       * @return the value
       */
      public Object getValue() {
         return value;
      }

      /**
       * @param value the value to set
       */
      public void setValue(Object value) {
         this.value = value;
      }
   }
}
