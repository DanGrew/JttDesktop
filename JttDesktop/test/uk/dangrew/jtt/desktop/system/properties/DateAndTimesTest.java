/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.system.properties;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.TimeZone;

import org.junit.Test;

import uk.dangrew.jtt.desktop.system.properties.DateAndTimes;

/**
 * {@link DateAndTimes} test.
 */
public class DateAndTimesTest {

   @Test public void shouldSetDefaultSystemTimezone() {
      DateAndTimes.initialise();
      assertThat( TimeZone.getDefault(), is( DateAndTimes.UTC_TIMEZONE ) );
   }//End Method

}//End Class
