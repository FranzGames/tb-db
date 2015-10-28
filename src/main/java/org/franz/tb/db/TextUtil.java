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
public class TextUtil {

   static final int NORMAL_STATE = 0;
   static final int ESCAPE_STATE = 1;

   public static List<String> parseCsv(String csvLine) {
      List<String> data = new ArrayList<String>();
      StringBuilder builder = new StringBuilder();
      int state = 0;
      boolean isQuoted = false;

      if (csvLine == null || csvLine.length() == 0) {
         return data;
      }

      for (int i = 0; i < csvLine.length(); i++) {
         char ch = csvLine.charAt(i);

         if (state == ESCAPE_STATE) {
            switch (ch) {
               case '"':
                  builder.append('"');
                  break;
               case 'r':
                  builder.append('\r');
                  break;
               case 'n':
                  builder.append('\n');
                  break;
               case 't':
                  builder.append('\t');
                  break;
               default:
                  builder.append(ch);
                  break;
            }

            state = NORMAL_STATE;
         } else if (ch == '"' && builder.length() == 0) {
            isQuoted = true;
            continue;
         } else if (ch == '\\') {
            state = ESCAPE_STATE;
         } else if (ch == '"' && isQuoted) {
            isQuoted = false;
         } else if (ch == ',') {
            if (isQuoted) {
               builder.append(ch);
            } else {
               data.add(builder.toString());
               builder.setLength(0);
               builder.trimToSize();
            }
         } else {
            builder.append(ch);
         }
      }

      data.add(builder.toString());

      return data;
   }

   public static String encodeCsv(List<String> data) {
      StringBuilder builder = new StringBuilder();

      for (int i = 0; i < data.size(); i++) {
         if (i != 0) {
            builder.append(',');
         }

         builder.append(encodeString(data.get(i)));
      }

      return builder.toString();
   }

   public static String encodeString(String str) {
      StringBuilder builder = new StringBuilder();
      boolean isQuoted = str.indexOf(',') >= 0;

      if (isQuoted) {
         builder.append('"');
      }

      for (int i = 0; i < str.length(); i++) {
         char ch = str.charAt(i);

         switch (ch) {
            case '"':
               builder.append("\\\"");
               break;
            case '\n':
               builder.append("\\n");
               break;
            case '\r':
               builder.append("\\r");
               break;
            case '\t':
               builder.append("\\t");
               break;
            case '\\':
               builder.append("\\\\");
               break;
            default:
               builder.append(ch);
               break;
         }
      }

      if (isQuoted) {
         builder.append('"');
      }

      return builder.toString();
   }

}
