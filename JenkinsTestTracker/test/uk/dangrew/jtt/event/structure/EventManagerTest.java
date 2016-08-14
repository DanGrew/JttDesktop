/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.event.structure;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import uk.dangrew.jtt.graphics.JavaFxInitializer;

/**
 * {@link EventManager} test.
 */
public class EventManagerTest {

   @Mock private EventSubscription< EventManagerTest, Object > subscriber;
   @Mock private EventSubscription< EventManagerTest, Object > subscriberB;
   @Mock private Event< EventManagerTest, Object > notification;
   @Mock private Event< EventManagerTest, Object > notification2;
   
   @Spy private ReentrantLock lock;
   private Set< EventSubscription< EventManagerTest, Object > > subscriptions;
   private EventManager< EventManagerTest, Object > systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      subscriptions = spy( new LinkedHashSet<>() );
      systemUnderTest = new EventManager<>( subscriptions, lock );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullSubscriptions(){
      new EventManager<>( null, lock );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullLock(){
      new EventManager<>( subscriptions, null );
   }//End Method
   
   @SuppressWarnings("unchecked") //contains method varargs
   @Test public void shouldRegisterSubscriber() {
      systemUnderTest.register( subscriber );
      assertThat( subscriptions, contains( subscriber ) );
   }//End Method
   
   @Test public void shouldUnregisterSubscriber() {
      systemUnderTest.register( subscriber );
      systemUnderTest.unregister( subscriber );
      assertThat( subscriptions, hasSize( 0 ) );
   }//End Method
   
   @Test public void shouldNotifyAllSubscribers(){
      systemUnderTest.register( subscriber );
      systemUnderTest.register( subscriberB );
      
      systemUnderTest.fire( notification );
      verify( subscriber ).notify( notification );
      verify( subscriberB ).notify( notification );
      
      systemUnderTest.fire( notification2 );
      verify( subscriber ).notify( notification2 );
      verify( subscriber ).notify( notification2 );
   }//End Method
   
   @Test public void shouldNotPermitAddIfLockIsHeld(){
      systemUnderTest.register( subscriber );
      InOrder locking = inOrder( lock, subscriptions );
      locking.verify( lock ).lock();
      locking.verify( subscriptions ).add( subscriber );
      locking.verify( lock ).unlock();
   }//End Method
   
   @Test public void shouldNotPermitRemoveIfLockIsHeld(){
      systemUnderTest.unregister( subscriber );
      InOrder locking = inOrder( lock, subscriptions );
      locking.verify( lock ).lock();
      locking.verify( subscriptions ).remove( subscriber );
      locking.verify( lock ).unlock();
   }//End Method
   
   @Test public void shouldNotPermitNotifyIfLockIsHeldAndShouldBlockUntilAllNotified(){
      systemUnderTest.register( subscriber );
      systemUnderTest.register( subscriberB );
      systemUnderTest.notify( notification );
      
      InOrder locking = inOrder( lock, subscriber, subscriberB );
      locking.verify( lock ).lock();
      locking.verify( subscriber ).notify( notification );
      locking.verify( subscriberB ).notify( notification );
      locking.verify( lock ).unlock();
   }//End Method
   
   @Test public void shouldClearAllSubscriptions(){
      systemUnderTest.register( subscriber );
      systemUnderTest.clearAllSubscriptions();
      systemUnderTest.register( subscriberB );
      
      systemUnderTest.notify( notification );
      
      InOrder locking = inOrder( lock, subscriber, subscriberB );
      locking.verify( lock ).lock();
      locking.verify( subscriber, never() ).notify( notification );
      locking.verify( subscriberB, times( 1 ) ).notify( notification );
      locking.verify( lock ).unlock();
   }//End Method

}//End Class
