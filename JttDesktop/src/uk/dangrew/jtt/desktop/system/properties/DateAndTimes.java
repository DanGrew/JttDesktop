/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.system.properties;

import java.util.TimeZone;

/**
 * {@link DateAndTimes} is responsible for configuring the system for date and
 * time properties.
 */
public class DateAndTimes {
   
   static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone( "UTC" );

   /** Constructor to hide public.**/
   private DateAndTimes(){
      //Do nothing - hide public constructor.
   }//End Constructor
   
   /** Method to initialise the properties the system should use.**/
   public static void initialise() {
      TimeZone.setDefault( UTC_TIMEZONE );
   }//End Method

}//End Class
