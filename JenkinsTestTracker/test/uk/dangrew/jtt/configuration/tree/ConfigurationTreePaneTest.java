/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.tree;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.configuration.content.ConfigurationTreeContent;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.PlatformDecouplerImpl;

/**
 * {@link ConfigurationTreePane} test.
 */
public class ConfigurationTreePaneTest {

   private SystemConfiguration systemConfiguration;
   private ConfigurationTreePane systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      
      systemConfiguration = new SystemConfiguration();
      
      systemUnderTest = new ConfigurationTreePane( systemConfiguration );
   }//End Method
   
   @Ignore
   @Test public void manual() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> systemUnderTest );
      
      Thread.sleep( 1000000 );
   }//End Method
   
   @Test public void shouldDisplayTreeAndContentAndCommunicateBetweenThem(){
      ConfigurationTree left = ( ConfigurationTree ) systemUnderTest.getLeft();
      ScrollPane center = ( ScrollPane ) systemUnderTest.getCenter();
      ConfigurationTreeContent content = ( ConfigurationTreeContent ) center.getContent();
      
      BorderPane contentArea = ( BorderPane ) content.getCenter();
      Node initialCentre = contentArea.getCenter();
      
      left.getSelectionModel().select( 1 );
      assertThat( contentArea.getCenter(), is( not( initialCentre ) ) );
   }//End Method
   
   @Test public void shouldWrapContentInVerticalScrollerWithFixedWidth(){
      ScrollPane center = ( ScrollPane ) systemUnderTest.getCenter();
      
      assertThat( center.isFitToWidth(), is( true ) );
      assertThat( center.getHbarPolicy(), is( ScrollBarPolicy.NEVER ) );
   }//End Method
   
   @Test public void shouldAutoSelectFirstItemOnConstruction(){
      ConfigurationTree tree = ( ConfigurationTree ) systemUnderTest.getLeft();
      assertThat( tree.getSelectionModel().getSelectedIndex(), is( 0 ) );
   }//End Method

}//End Class
