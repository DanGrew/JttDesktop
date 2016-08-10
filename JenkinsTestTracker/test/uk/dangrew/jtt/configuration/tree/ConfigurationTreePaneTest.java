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
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import uk.dangrew.jtt.configuration.content.ConfigurationTreeContent;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.environment.preferences.PreferenceController;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.PlatformDecouplerImpl;

/**
 * {@link ConfigurationTreePane} test.
 */
public class ConfigurationTreePaneTest {

   private ConfigurationTree tree;
   private ConfigurationTreeContent content;
   private ConfigurationTreePane systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      
      content = new ConfigurationTreeContent();
      SystemConfiguration configuration = new SystemConfiguration(); 
      tree = new ConfigurationTree( new PreferenceController( configuration ), configuration );
      systemUnderTest = new ConfigurationTreePane( tree, content );
   }//End Method
   
   @Ignore
   @Test public void manual() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> systemUnderTest );
      
      systemUnderTest.tree().select( ConfigurationTreeItems.SystemVersion );
      Thread.sleep( 1000 );
      
      systemUnderTest.tree().select( ConfigurationTreeItems.DualWallProperties );
      Thread.sleep( 1000 );
      
      systemUnderTest.tree().select( ConfigurationTreeItems.DualWallRoot );
      Thread.sleep( 1000 );
      
      systemUnderTest.tree().select( ConfigurationTreeItems.LeftWallRoot );
      Thread.sleep( 1000 );
      
      systemUnderTest.tree().select( ConfigurationTreeItems.LeftDimension );
      Thread.sleep( 1000 );
      
      systemUnderTest.tree().select( ConfigurationTreeItems.LeftJobPolicies );
      Thread.sleep( 1000 );
      
      systemUnderTest.tree().select( ConfigurationTreeItems.LeftFonts );
      Thread.sleep( 1000 );
      
      systemUnderTest.tree().select( ConfigurationTreeItems.LeftColours );
      Thread.sleep( 1000 );
      
      systemUnderTest.tree().select( ConfigurationTreeItems.RightWallRoot );
      Thread.sleep( 1000 );
      
      systemUnderTest.tree().select( ConfigurationTreeItems.RightDimension );
      Thread.sleep( 1000 );
      
      systemUnderTest.tree().select( ConfigurationTreeItems.RightJobPolicies );
      Thread.sleep( 1000 );
      
      systemUnderTest.tree().select( ConfigurationTreeItems.RightFonts );
      Thread.sleep( 1000 );
      
      systemUnderTest.tree().select( ConfigurationTreeItems.RightColours );
      Thread.sleep( 1000 );
      
      
      Thread.sleep( 1000000 );
   }//End Method
   
   @Test public void shouldWrapContentInVerticalScrollerWithFixedWidth(){
      ScrollPane center = ( ScrollPane ) systemUnderTest.getCenter();
      
      assertThat( center.isFitToWidth(), is( true ) );
      assertThat( center.getHbarPolicy(), is( ScrollBarPolicy.NEVER ) );
   }//End Method
   
   @Test public void shouldAutoSelectFirstItemOnConstruction(){
      ConfigurationTree actualTree = ( ConfigurationTree ) systemUnderTest.getLeft();
      assertThat( actualTree, is( tree ) );
      assertThat( tree.getSelectionModel().getSelectedIndex(), is( 0 ) );
   }//End Method

}//End Class
