/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.time;

import java.time.LocalDateTime;

/**
 * Implementation of {@link TimestampProvider} that simply provides {@link LocalDateTime#now()}.
 */
public final class DefaultTimestampProvider implements TimestampProvider {

   /**
    * {@inheritDoc}
    */
   @Override public final LocalDateTime get() {
      return LocalDateTime.now();
   }//End Method

}//End Class
