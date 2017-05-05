/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.time;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * The {@link TimestampFormatter} is responsible for providing common operations for formatting
 * timestamps.
 */
public class TimestampFormatter {
   
   /**
    * Method to format the given value with the given {@link DateTimeFormatter}.
    * @param value the timestamp.
    * @param formatter the {@link DateTimeFormatter}.
    * @return the formatted timestamp.
    */
   public String format( long value, DateTimeFormatter formatter ) {
      return new Timestamp( value )
               .toInstant()
               .atZone( ZoneId.of( "Europe/London" ) )
               .toLocalDateTime()
               .format( formatter );
   }//End Method

}//End Class
