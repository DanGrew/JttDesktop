/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.tree.item;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import uk.dangrew.jtt.buildwall.configuration.components.sound.SoundConfigurationPanel;
import uk.dangrew.jtt.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.environment.preferences.PreferenceController;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.TestPlatformDecouplerImpl;

public class SoundsTreeItemTest {

   private SoundConfiguration configuration;
   @Mock private PreferenceController controller;
   @Captor private ArgumentCaptor< Node > contentTitleCaptor;
   @Captor private ArgumentCaptor< Node > contentCaptor;
   
   private SoundsTreeItem systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      configuration = new SoundConfiguration();
      systemUnderTest = new SoundsTreeItem( configuration, controller );
   }//End Method
   
   @Test public void shouldHandleClickByInstructingController() {
      systemUnderTest.handleBeingSelected();
      verify( controller ).displayContent( contentTitleCaptor.capture(), contentCaptor.capture() );
      
      assertThat( contentTitleCaptor.getValue(), is( instanceOf( SimpleConfigurationTitle.class ) ) );
      SimpleConfigurationTitle title = ( SimpleConfigurationTitle ) contentTitleCaptor.getValue();
      assertThat( title.getTitle(), is( SoundsTreeItem.TITLE ) );
      assertThat( title.getDescription(), is( SoundsTreeItem.DESCRIPTION ) );
      
      assertThat( contentCaptor.getValue(), is( instanceOf( ScrollPane.class ) ) );
      ScrollPane scroller = ( ScrollPane ) contentCaptor.getValue();
      assertThat( scroller.getContent(), is( instanceOf( SoundConfigurationPanel.class ) ) );
      
      SoundConfigurationPanel panel = ( SoundConfigurationPanel ) scroller.getContent();
      assertThat( panel.isAssociatedWith( configuration ), is( true ) );
   }//End Method
   
   @Test public void shouldProvideToStringUsingName(){
      assertThat( systemUnderTest.toString(), is( SoundsTreeItem.NAME ) );
   }//End Method
   
   @Test public void shouldNotBeAssociatedWithAnything(){
      assertThat( systemUnderTest.isAssociatedWith( new Object() ), is( false ) );
   }//End Method

}//End Class
