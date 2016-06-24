/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.tree;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.configuration.content.ConfigurationTreeContent;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.PlatformDecouplerImpl;

/**
 * {@link ConfigurationTreePane} test.
 */
public class ConfigurationTreePaneTest {

   private ConfigurationTreePane systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      
      systemUnderTest = new ConfigurationTreePane();
   }//End Method
   
   @Ignore
   @Test public void manual() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> new ConfigurationTreePane() );
      
      Thread.sleep( 1000000 );
   }//End Method
   
   @Test public void shouldUseSplitPaneAtDefaultPosition(){
      assertThat( systemUnderTest.getCenter(), is( instanceOf( SplitPane.class ) ) );
      SplitPane split = ( SplitPane ) systemUnderTest.getCenter();
      assertThat( split.getDividerPositions(), is( new double[]{ ConfigurationTreePane.DEFAULT_DIVIDER_POSITION } ) );
   }//End Method
   
   @Test public void shouldDisplayTreeAndContentAndCommunicateBetweenThem(){
      SplitPane split = ( SplitPane ) systemUnderTest.getCenter();
      
      ConfigurationTree left = ( ConfigurationTree ) split.getItems().get( 0 );
      ConfigurationTreeContent right = ( ConfigurationTreeContent ) split.getItems().get( 1 );
      
      BorderPane contentArea = ( BorderPane ) right.getCenter();
      assertThat( contentArea.getCenter(), is( nullValue() ) );
      
      left.getSelectionModel().select( 0 );
      assertThat( contentArea.getCenter(), is( notNullValue() ) );
   }//End Method

}//End Class
