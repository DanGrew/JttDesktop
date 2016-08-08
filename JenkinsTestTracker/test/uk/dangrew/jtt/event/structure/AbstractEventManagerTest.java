/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.event.structure;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.utility.TestCommon;

/**
 * Abstract pattern for testing {@link EventManager} extensions.
 */
public abstract class AbstractEventManagerTest< SourceT, ValueT > {

   @Mock private EventSubscription< SourceT, ValueT > subscriber;
   @Mock private EventSubscription< SourceT, ValueT > subscriberB;
   @Mock private Event< SourceT, ValueT > notification;
   @Mock private Event< SourceT, ValueT > notification2;
   
   private EventManager< SourceT, ValueT > systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      systemUnderTest = constructSut();
   }//End Method
   
   /** 
    * Method to provide the system under test.
    * @return the {@link EventManager} being tested.
    */
   protected abstract EventManager< SourceT, ValueT > constructSut();
   
   /**
    * Method to add a mocked subscription.
    * @return the mocked {@link EventSubscription}.
    */
   private EventSubscription< SourceT, ValueT > newSubscription(){
      @SuppressWarnings("unchecked")//mocking 
      EventSubscription< SourceT, ValueT > subscription = mock( EventSubscription.class );
      
      constructSut().register( subscription );
      return subscription;
   }//End Method
   
   @Test public void shouldNotifySingleEvent() {
      EventSubscription< SourceT, ValueT > registered = newSubscription();
      
      Event< SourceT, ValueT > event = new Event<>( null, null );
      constructSut().fire( event );
      verify( registered ).notify( event );
   }//End Method
   
   @Test public void shouldNotifySingleEventMultipleTimes() {
      EventSubscription< SourceT, ValueT > registered = newSubscription();
      
      Event< SourceT, ValueT > event = new Event<>( null, null );
      constructSut().fire( event );
      constructSut().fire( event );
      verify( registered, times( 2 ) ).notify( event );
   }//End Method
   
   @Test public void shouldNotifyMultupleEvents() {
      EventSubscription< SourceT, ValueT > registered = newSubscription();
      
      Event< SourceT, ValueT > event = new Event<>( null, null );
      Event< SourceT, ValueT > event2 = new Event<>( null, null );
      
      constructSut().fire( event );
      constructSut().fire( event2 );
      
      verify( registered ).notify( event );
      verify( registered ).notify( event2 );
   }//End Method
   
   @Test public void shouldReceiveEventsInDifferentSubscriptions() {
      EventSubscription< SourceT, ValueT > registered = newSubscription();
      EventSubscription< SourceT, ValueT > registered2 = newSubscription();
      
      Event< SourceT, ValueT > event = new Event<>( null, null );
      Event< SourceT, ValueT > event2 = new Event<>( null, null );
      
      constructSut().fire( event );
      constructSut().fire( event2 );
      
      verify( registered ).notify( event );
      verify( registered ).notify( event2 );
      verify( registered2 ).notify( event );
      verify( registered2 ).notify( event2 );
   }//End Method
   
   @Test public void shouldNotPermitConcurrencyAcrossEvents(){
      TestCommon.assertConcurrencyIsNotAnIssue( 
               count -> constructSut().register( event -> {} ), 
               count -> constructSut().notify( new Event< SourceT, ValueT >( null, null ) ), 
               1000 
      );
   }//End Method

}//End Class
