/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.event.structure;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.function.Supplier;

import org.mockito.ArgumentCaptor;

/**
 * {@link EventAssertions} provides common methods for asserting that prove the {@link EventManager}
 * mechanism works and is followed.
 */
public class EventAssertions {

   /**
    * Method to assert that an event is raised and received by an {@link EventSubscription}.
    * @param eventProvider the {@link Supplier} of the {@link EventManager} to register on.
    * @param eventFirer the process of firing the event.
    * @param expectedSource the expected SourceT.
    * @param expectedValue the expected ValueT.
    */
   public static < ValueT > void assertEventRaised( 
            Supplier< EventManager< ValueT > > eventProvider,
            Runnable eventFirer,
            ValueT expectedValue
   ){
      @SuppressWarnings("unchecked")//mocking 
      EventSubscription< ValueT > registration = mock( EventSubscription.class );
      eventProvider.get().register( registration );
      
      @SuppressWarnings("deprecation")//forClass does not compile 
      ArgumentCaptor< Event< ValueT > > eventCaptor = new ArgumentCaptor<>();
      
      eventFirer.run();
      verify( registration ).notify( eventCaptor.capture() );
      
      if ( expectedValue == null ) {
         assertThat( eventCaptor.getValue().getValue(), is( nullValue() ) );
      } else {
         assertThat( eventCaptor.getValue().getValue(), is( expectedValue ) );
      }
   }//End Method
   
}//End Class
