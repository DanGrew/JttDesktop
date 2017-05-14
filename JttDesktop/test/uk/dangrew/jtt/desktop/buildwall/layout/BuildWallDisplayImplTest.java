/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.layout;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import uk.dangrew.jtt.desktop.buildwall.configuration.components.BuildWallConfigurationPanelImpl;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.desktop.styling.SystemStyling;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;
import uk.dangrew.sd.graphics.launch.TestApplication;

/**
 * {@link BuildWallDisplayImpl} test.
 */
public class BuildWallDisplayImplTest {

   private JenkinsDatabase database;
   private BuildWallDisplayImpl systemUnderTest;
   
   @BeforeClass public static void initialiseStylings(){
      SystemStyling.initialise();
   }//End Method
   
   @BeforeClass public static void initialisePlatform(){
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      database = new TestJenkinsDatabaseImpl();
      database.store( new JenkinsJobImpl( "Project Build" ) );
      database.store( new JenkinsJobImpl( "Subset A" ) );
      database.store( new JenkinsJobImpl( "Subset B" ) );
      database.store( new JenkinsJobImpl( "Subset C" ) );
      database.store( new JenkinsJobImpl( "Units (Legacy)" ) );
      database.store( new JenkinsJobImpl( "Long Tests" ) );
      database.store( new JenkinsJobImpl( "Capacity Tests" ) );
      database.store( new JenkinsJobImpl( "Integration Tests" ) );
      database.store( new JenkinsJobImpl( "Configurable Build1" ) );
      database.store( new JenkinsJobImpl( "Configurable Build2" ) );
      database.store( new JenkinsJobImpl( "Configurable Build3" ) );
      database.store( new JenkinsJobImpl( "Configurable Build4" ) );
      database.store( new JenkinsJobImpl( "Configurable Build5" ) );
      database.store( new JenkinsJobImpl( "Configurable Build6" ) );
      database.store( new JenkinsJobImpl( "Configurable Build7" ) );
      
      TestApplication.startPlatform();
      systemUnderTest = new BuildWallDisplayImpl( database );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      for ( int i = 0; i < 40; i++ ) {
         database.store( new JenkinsJobImpl( "" + i ) );
      }
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      TestApplication.launch( () -> {
         systemUnderTest.toggleConfiguration();
         return systemUnderTest; 
      } );
      
      Thread.sleep( 10000000 );
   }//End Method
   
   @Test public void shouldDisplayGridWallImpl() {
      Assert.assertTrue( systemUnderTest.getCenter() instanceof GridWallImpl );
   }//End Method
   
   @Test public void shouldInitiallyProvideNoConfiguration() {
      Assert.assertNull( systemUnderTest.getRight() );
   }//End Method
   
   @Test public void shouldToggleConfiguration() {
      Assert.assertNull( systemUnderTest.getRight() );
      systemUnderTest.toggleConfiguration();
      
      Node configuration = systemUnderTest.getRight();
      Assert.assertNotNull( configuration );
      assertThat( configuration, instanceOf( ScrollPane.class ) );
      ScrollPane scrollPane = ( ScrollPane ) configuration;
      assertThat( scrollPane.getContent(), instanceOf( BuildWallConfigurationPanelImpl.class ) );
      assertThat( systemUnderTest.hasConfigurationTurnedOn(), is( true ) );
      
      systemUnderTest.toggleConfiguration();
      Assert.assertNull( systemUnderTest.getRight() );
      assertThat( systemUnderTest.hasConfigurationTurnedOn(), is( false ) );
      
      systemUnderTest.toggleConfiguration();
      Assert.assertEquals( scrollPane, systemUnderTest.getRight() );
      assertThat( systemUnderTest.hasConfigurationTurnedOn(), is( true ) );
   }//End Method
   
   @Test public void shouldEnsureConfigurationIsUpdatedInitiallyAndWhenDatabaseIsUpdated(){
      assertThat( database.jenkinsJobs().size(), is( 15 ) );
      assertThat( systemUnderTest.configuration().jobPolicies().size(), is( 15 ) );
      
      JenkinsJob job = new JenkinsJobImpl( "something-else" );
      database.store( job );
      assertThat( systemUnderTest.configuration().jobPolicies().containsKey( job ), is( true ) );
   }//End Method
   
}//End Class
