/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.franz.tb.db;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.List;

/**
 *
 * @author pfranz
 */
public class TestTextUtil {
   
   @Test
   public void testParseCsv()
   {
      List<String> data = TextUtil.parseCsv("\"col1\",\\\"col2,col3\\nline2");
      
      assertEquals(data.size(), 3);
      assertEquals(data.get(0), "col1");
      assertEquals(data.get(1), "\"col2");
      assertEquals(data.get(2), "col3\nline2");
      
      data = TextUtil.parseCsv("");
      assertEquals(data.size(), 0);

      data = TextUtil.parseCsv(null);
      assertEquals(data.size(), 0);
      
   }
   
   @Test
   public void testEncodeString()
   {
      String str = TextUtil.encodeString(", \n test");
      
      assertEquals (str, "\", \\n test\"");
      
      str = TextUtil.encodeString("test\t test");
      
      assertEquals (str, "test\\t test");

      str = TextUtil.encodeString("test\\t test");
      
      assertEquals (str, "test\\\\t test");
      
      str = TextUtil.encodeString("test\" test");
      
      assertEquals (str, "test\\\" test");
      
   }
}
