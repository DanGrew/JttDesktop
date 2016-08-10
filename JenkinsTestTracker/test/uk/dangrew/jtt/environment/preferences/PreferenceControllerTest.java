/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.environment.preferences;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.scene.Node;
import javafx.scene.control.Label;
import uk.dangrew.jtt.configuration.content.ConfigurationTreeContent;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.configuration.tree.ConfigurationTreeItems;
import uk.dangrew.jtt.configuration.tree.ConfigurationTreePane;
import uk.dangrew.jtt.event.structure.Event;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.TestPlatformDecouplerImpl;

/**
 * {@link PreferenceController} test.
 */
public class PreferenceControllerTest {

   private SystemConfiguration configuration;
   @Mock private PreferenceWindowController controller;
   @Mock private ConfigurationTreeContent content;
   private PreferenceController systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      MockitoAnnotations.initMocks( this );
      configuration = new SystemConfiguration();
      systemUnderTest = new PreferenceController( controller, configuration, content );
   }//End Method

   @Test public void shouldAssociate(){
      ArgumentCaptor< ConfigurationTreePane > paneCaptor = ArgumentCaptor.forClass( ConfigurationTreePane.class );
      verify( controller ).associateWithConfiguration( paneCaptor.capture() );
      
      assertThat( paneCaptor.getValue(), is( notNullValue() ) );
      assertThat( paneCaptor.getValue().hasTree( systemUnderTest.tree() ), is( true ) );
      assertThat( paneCaptor.getValue().hasContent( content ), is( true ) );
   }//End Method
   
   @Test public void shouldTriggerOpeningOfWindowWhenEventReceived() {
      new PreferencesOpenEvent().fire( new Event<>( null, new PreferenceBehaviour( WindowPolicy.Open, null ) ) );
      verify( controller ).showConfigurationWindow();
   }//End Method
   
   @Test public void shouldTriggerClosingOfWindowWhenEventReceived() {
      new PreferencesOpenEvent().fire( new Event<>( null, new PreferenceBehaviour( WindowPolicy.Close, null ) ) );
      verify( controller ).hideConfigurationWindow();
   }//End Method
   
   @Test public void shouldIgnoreEventWithNoPolicy() {
      new PreferencesOpenEvent().fire( new Event<>( null, null ) );
      verify( controller, never() ).showConfigurationWindow();
      verify( controller, never() ).hideConfigurationWindow();
   }//End Method
   
   @Test public void shouldTriggerOpeningOfWindowWhenEventReceivedMultipleTimes() {
      PreferencesOpenEvent event = new PreferencesOpenEvent();
      event.fire( new Event<>( null, new PreferenceBehaviour( WindowPolicy.Open, null ) ) );
      event.fire( new Event<>( null, new PreferenceBehaviour( WindowPolicy.Open, null ) ) );
      verify( controller, times( 2 ) ).showConfigurationWindow();
   }//End Method
   
   @Test public void shouldTriggerOpeningOfWindowFromMultipleEventSources() {
      new PreferencesOpenEvent().fire( new Event<>( null, new PreferenceBehaviour( WindowPolicy.Open, null ) ) );
      new PreferencesOpenEvent().fire( new Event<>( null, new PreferenceBehaviour( WindowPolicy.Open, null ) ) );
      verify( controller, times( 2 ) ).showConfigurationWindow();
   }//End Method

   @Test public void shouldInstructContentToShowNewNodes() {
      Node titleNode = new Label();
      Node contentNode = new Label();
      
      systemUnderTest.displayContent( titleNode, contentNode );
      verify( content ).setContent( titleNode, contentNode );
   }//End Method   
   
   @Test public void shouldSelectInTreeWhenProvided(){
      new PreferencesOpenEvent().fire( new Event<>( null, new PreferenceBehaviour( WindowPolicy.Open, ConfigurationTreeItems.LeftWallRoot ) ) );
      assertThat( systemUnderTest.tree().isSelected( ConfigurationTreeItems.LeftWallRoot ), is( true ) );
   }//End Method
   
   @Test public void shouldNotSelectInTreeWhenNotProvided(){
      new PreferencesOpenEvent().fire( new Event<>( null, new PreferenceBehaviour( WindowPolicy.Open, ConfigurationTreeItems.LeftWallRoot ) ) );
      assertThat( systemUnderTest.tree().isSelected( ConfigurationTreeItems.LeftWallRoot ), is( true ) );
      
      new PreferencesOpenEvent().fire( new Event<>( null, new PreferenceBehaviour( WindowPolicy.Open, null ) ) );
      assertThat( systemUnderTest.tree().isSelected( ConfigurationTreeItems.LeftWallRoot ), is( true ) );
   }//End Method
}//End Class
