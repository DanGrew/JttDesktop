/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.time;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import org.junit.Before;
import org.junit.Test;

public class TimestampFormatterTest {

   private DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern( "dd/MM/yy" ).appendLiteral( "-" ).appendPattern( "HH:mm:ss" ).toFormatter();
   
   private TimestampFormatter systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new TimestampFormatter();
   }//End Method

   @Test public void shouldConvertTimestampIntoFormattedStringWithLocal() {
      assertThat( systemUnderTest.format( 123456789, DATE_TIME_FORMATTER ), is( "02/01/70-11:17:36" ) );
   }//End Method
   
}//End Class
