/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.environment.preferences;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.event.structure.Event;

/**
 * {@link PreferenceOpener} test.
 */
public class PreferenceOpenerTest {

   @Mock private SystemConfiguration configuration;
   @Mock private PreferenceWindowController controller;
   private PreferenceOpener systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new PreferenceOpener( controller, configuration );
   }//End Method

   @Test public void shouldAssociate(){
      verify( controller ).associateWithConfiguration( configuration );
   }//End Method
   
   @Test public void shouldTriggerOpeningOfWindowWhenEventReceived() {
      new PreferencesOpenEvent().fire( new Event<>( null, WindowPolicy.Open ) );
      verify( controller ).showConfigurationWindow();
   }//End Method
   
   @Test public void shouldTriggerClosingOfWindowWhenEventReceived() {
      new PreferencesOpenEvent().fire( new Event<>( null, WindowPolicy.Close ) );
      verify( controller ).hideConfigurationWindow();
   }//End Method
   
   @Test public void shouldIgnoreEventWithNoPolicy() {
      new PreferencesOpenEvent().fire( new Event<>( null, null ) );
      verify( controller, never() ).showConfigurationWindow();
      verify( controller, never() ).hideConfigurationWindow();
   }//End Method
   
   @Test public void shouldTriggerOpeningOfWindowWhenEventReceivedMultipleTimes() {
      PreferencesOpenEvent event = new PreferencesOpenEvent();
      event.fire( new Event<>( null, WindowPolicy.Open ) );
      event.fire( new Event<>( null, WindowPolicy.Open ) );
      verify( controller, times( 2 ) ).showConfigurationWindow();
   }//End Method
   
   @Test public void shouldTriggerOpeningOfWindowFromMultipleEventSources() {
      new PreferencesOpenEvent().fire( new Event<>( null, WindowPolicy.Open ) );
      new PreferencesOpenEvent().fire( new Event<>( null, WindowPolicy.Open ) );
      verify( controller, times( 2 ) ).showConfigurationWindow();
   }//End Method

}//End Class
