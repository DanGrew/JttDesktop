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

import uk.dangrew.jtt.configuration.content.ConfigurationTreeContent;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.environment.preferences.PreferenceBehaviour;
import uk.dangrew.jtt.environment.preferences.PreferenceController;
import uk.dangrew.jtt.environment.preferences.PreferencesOpenEvent;
import uk.dangrew.jtt.environment.preferences.WindowPolicy;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.model.event.structure.Event;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;

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
      JenkinsDatabase database = new TestJenkinsDatabaseImpl();
      tree = new ConfigurationTree( new PreferenceController( configuration, database ), database, configuration );
      systemUnderTest = new ConfigurationTreePane( tree, content );
   }//End Method
   
   @Ignore
   @Test public void manual() throws InterruptedException {
      new PreferencesOpenEvent().fire( new Event< PreferenceBehaviour >( 
               new PreferenceBehaviour( WindowPolicy.Open, ConfigurationTreeItems.DualWallProperties ) ) 
      );
      
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
   
   @Test public void shouldLayoutTreeAndContent(){
      assertThat( systemUnderTest.getLeft(), is( tree ) );
      assertThat( systemUnderTest.getCenter(), is( content ) );
   }//End Method
   
   @Test public void shouldAutoSelectFirstItemOnConstruction(){
      ConfigurationTree actualTree = ( ConfigurationTree ) systemUnderTest.getLeft();
      assertThat( actualTree, is( tree ) );
      assertThat( tree.getSelectionModel().getSelectedIndex(), is( 0 ) );
   }//End Method

}//End Class
