/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.time;

import java.time.Instant;

/**
 * Implementation of {@link TimestampProvider} that simply provides {@link LocalDateTime#now()}.
 */
public final class DefaultInstantProvider implements InstantProvider {

   /**
    * {@inheritDoc}
    */
   @Override public final Long get() {
      return Instant.now().getEpochSecond();
   }//End Method

}//End Class
