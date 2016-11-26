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
import static org.hamcrest.Matchers.nullValue;
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
import uk.dangrew.jtt.buildwall.configuration.components.DualBuildWallDescriptionPanel;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.environment.preferences.PreferenceController;
import uk.dangrew.jtt.graphics.JavaFxInitializer;

/**
 * {@link DualBuildWallRootItem} test.
 */
public class DualBuildWallRootItemTest {

   @Mock private PreferenceController controller;
   @Captor private ArgumentCaptor< Node > contentTitleCaptor;
   @Captor private ArgumentCaptor< Node > contentCaptor;
   
   private DualBuildWallRootItem systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new DualBuildWallRootItem( controller );
   }//End Method
   
   @Test public void shouldHandleClickByInstructingController() {
      systemUnderTest.handleBeingSelected();
      verify( controller ).displayContent( contentTitleCaptor.capture(), contentCaptor.capture() );
      
      assertThat( contentTitleCaptor.getValue(), is( instanceOf( SimpleConfigurationTitle.class ) ) );
      SimpleConfigurationTitle title = ( SimpleConfigurationTitle ) contentTitleCaptor.getValue();
      assertThat( title.getTitle(), is( DualBuildWallRootItem.TITLE ) );
      assertThat( title.getDescription(), is( nullValue() ) );
      
      assertThat( contentCaptor.getValue(), is( instanceOf( ScrollPane.class ) ) );
      ScrollPane scroller = ( ScrollPane ) contentCaptor.getValue();
      assertThat( scroller.getContent(), is( instanceOf( DualBuildWallDescriptionPanel.class ) ) );
   }//End Method
   
   @Test public void shouldProvideToStringUsingName(){
      assertThat( systemUnderTest.toString(), is( DualBuildWallRootItem.NAME ) );
   }//End Method

}//End Class
