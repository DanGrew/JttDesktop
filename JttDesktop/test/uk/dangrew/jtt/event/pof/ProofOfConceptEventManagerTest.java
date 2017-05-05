/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.event.pof;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import uk.dangrew.jtt.event.structure.Event;
import uk.dangrew.jtt.event.structure.EventSubscription;

/**
 * {@link ProofOfConceptEventManager} test.
 */
public class ProofOfConceptEventManagerTest {

   @Test public void shouldProvideRegistrationAndNotification() {
      ProofOfConceptEventManager registeringEventManager = new ProofOfConceptEventManager();
      
      BooleanProperty received = new SimpleBooleanProperty( false );
      registeringEventManager.register( event -> received.set( true ) );
      
      ProofOfConceptEventManager firingEventManager = new ProofOfConceptEventManager();
      firingEventManager.fire( new Event<>( new EventValue() ) );
      
      assertThat( received.get(), is( true ) );
   }//End Method
   
   @Test public void shouldInstantiateMultipleManagersThatContributeToTheSameEventSubscriptions() {
      ProofOfConceptEventManager registeringEventManagerA = new ProofOfConceptEventManager();
      
      @SuppressWarnings("unchecked") //mocking 
      EventSubscription< EventValue > subscriptionA = mock( EventSubscription.class );
      registeringEventManagerA.register( subscriptionA );
      
      ProofOfConceptEventManager registeringEventManagerB = new ProofOfConceptEventManager();
      @SuppressWarnings("unchecked") //mocking 
      EventSubscription< EventValue > subscriptionB = mock( EventSubscription.class );
      registeringEventManagerB.register( subscriptionB );

      ProofOfConceptEventManager firingEventManagerA = new ProofOfConceptEventManager();
      Event< EventValue > eventA = new Event<>( new EventValue() );
      firingEventManagerA.fire( eventA );
      
      verify( subscriptionA ).notify( eventA );
      verify( subscriptionB ).notify( eventA );
      
      ProofOfConceptEventManager firingEventManagerB = new ProofOfConceptEventManager();
      Event< EventValue > eventB = new Event<>( new EventValue() );
      firingEventManagerB.fire( eventB );
      
      verify( subscriptionA ).notify( eventB );
      verify( subscriptionB ).notify( eventB );
   }//End Method

}//End Class
