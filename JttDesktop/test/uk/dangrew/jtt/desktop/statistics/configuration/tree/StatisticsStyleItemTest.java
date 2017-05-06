/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.configuration.tree;

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
import uk.dangrew.jtt.desktop.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.desktop.environment.preferences.PreferenceController;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;
import uk.dangrew.jtt.desktop.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.desktop.statistics.configuration.components.StatisticsStylePanel;
import uk.dangrew.jtt.desktop.statistics.configuration.tree.StatisticsStyleItem;

public class StatisticsStyleItemTest {

   private StatisticsConfiguration configuration;
   @Mock private PreferenceController controller;
   @Captor private ArgumentCaptor< Node > contentTitleCaptor;
   @Captor private ArgumentCaptor< Node > contentCaptor;
   
   private StatisticsStyleItem systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      MockitoAnnotations.initMocks( this );
      configuration = new StatisticsConfiguration();
      systemUnderTest = new StatisticsStyleItem( controller, configuration );
   }//End Method
   
   @Test public void shouldHandleClickByInstructingController() {
      systemUnderTest.handleBeingSelected();
      verify( controller ).displayContent( contentTitleCaptor.capture(), contentCaptor.capture() );
      
      assertThat( contentTitleCaptor.getValue(), is( instanceOf( SimpleConfigurationTitle.class ) ) );
      SimpleConfigurationTitle title = ( SimpleConfigurationTitle ) contentTitleCaptor.getValue();
      assertThat( title.getTitle(), is( StatisticsStyleItem.TITLE ) );
      assertThat( title.getDescription(), is( StatisticsStyleItem.DESCRIPTION ) );
      
      assertThat( contentCaptor.getValue(), is( instanceOf( ScrollPane.class ) ) );
      ScrollPane scroller = ( ScrollPane ) contentCaptor.getValue();
      assertThat( scroller.getContent(), is( instanceOf( StatisticsStylePanel.class ) ) );
      
      StatisticsStylePanel panel = ( StatisticsStylePanel ) scroller.getContent();
      assertThat( panel.isAssociatedWith( configuration ), is( true ) );
   }//End Method
   
   @Test public void shouldProvideToStringUsingName(){
      assertThat( systemUnderTest.toString(), is( StatisticsStyleItem.NAME ) );
   }//End Method
   
   @Test public void shouldBeAssociatedWithConfiguration(){
      assertThat( systemUnderTest.isAssociatedWith( configuration ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new Object() ), is( false ) );
   }//End Method

}//End Class
