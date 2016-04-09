/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.dual;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import buildwall.layout.GridWallImpl;
import buildwall.panel.type.JobPanelDescriptionProviders;
import graphics.DecoupledPlatformImpl;
import graphics.JavaFxInitializer;
import graphics.PlatformDecouplerImpl;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import model.jobs.BuildResultStatus;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import model.users.JenkinsUserImpl;
import storage.database.JenkinsDatabase;
import storage.database.JenkinsDatabaseImpl;
import styling.SystemStyling;

/**
 * {@link DualBuildWallDisplayImpl} test.
 */
public class DualBuildWallDisplayImplTest {

   private JenkinsDatabase database;
   private DualBuildWallDisplayImpl systemUnderTest;
   
   @BeforeClass public static void initialiseStylings(){
      SystemStyling.initialise();
   }//End Method
   
   @BeforeClass public static void initialisePlatform(){
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      database = new JenkinsDatabaseImpl();
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
      
      Random random = new Random(); 
      for ( JenkinsJob job : database.jenkinsJobs() ) {
         int userCount = random.nextInt( 20 );
         for ( int i = 0; i < userCount; i++ ) {
            job.lastBuildStatusProperty().set( BuildResultStatus.values()[ i %BuildResultStatus.values().length ] );
            job.culprits().add( new JenkinsUserImpl( "User " + userCount + "_" + i ) );
         }
      }
      
      JavaFxInitializer.startPlatform();
      systemUnderTest = new DualBuildWallDisplayImpl( database );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      JavaFxInitializer.launchInWindow( () -> {
         systemUnderTest.showRightConfiguration();
         systemUnderTest.setOnContextMenuRequested( new DualBuildWallContextMenuOpener( systemUnderTest ) );
         return systemUnderTest; 
      } );
      
      Thread.sleep( 10000000 );
   }//End Method
   
   @Test public void shouldInitialseWithNoConfiguration(){
      assertThat( systemUnderTest.isConfigurationShowing(), is( false ) );
      assertThat( systemUnderTest.getRight(), nullValue() );
   }//End Method
   
   @Test public void shouldShowConfigurationPanelsIndividuallyAlwaysInsideScroller(){
      systemUnderTest.showRightConfiguration();
      assertThat( systemUnderTest.rightConfigurationPanel(), notNullValue() );
      assertThat( systemUnderTest.isConfigurationShowing(), is( true ) );
      assertThat( systemUnderTest.getRight(), instanceOf( ScrollPane.class ) );
      ScrollPane scroller = ( ScrollPane ) systemUnderTest.getRight();
      assertThat( scroller.getContent(), is( systemUnderTest.rightConfigurationPanel() ) );
      
      systemUnderTest.showLeftConfiguration();
      assertThat( systemUnderTest.leftConfigurationPanel(), notNullValue() );
      assertThat( systemUnderTest.isConfigurationShowing(), is( true ) );
      assertThat( systemUnderTest.getRight(), instanceOf( ScrollPane.class ) );
      scroller = ( ScrollPane ) systemUnderTest.getRight();
      assertThat( scroller.getContent(), is( systemUnderTest.leftConfigurationPanel() ) );
   }//End Method
   
   @Test public void shouldEnsureConfigurationIsUpdatedInitiallyAndWhenDatabaseIsUpdatedForBothWallsIndividually(){
      assertThat( database.jenkinsJobs().size(), is( 15 ) );
      assertThat( systemUnderTest.rightConfiguration().jobPolicies().size(), is( 15 ) );
      assertThat( systemUnderTest.leftConfiguration().jobPolicies().size(), is( 15 ) );
      
      JenkinsJob job = new JenkinsJobImpl( "something-else" );
      database.store( job );
      assertThat( systemUnderTest.rightConfiguration().jobPolicies().containsKey( job ), is( true ) );
      assertThat( systemUnderTest.leftConfiguration().jobPolicies().containsKey( job ), is( true ) );
   }//End Method
   
   @Test public void shouldProvideGridWallsInSplitPaneForManualSplitting(){
      assertThat( systemUnderTest.getCenter(), instanceOf( SplitPane.class ) );
      assertSplitPaneItems( systemUnderTest.leftGridWall(), systemUnderTest.rightGridWall() );
   }//End Method
   
   /**
    * Method to assert that the given items are in the {@link SplitPane}, and only these.
    * @param expectedWalls the {@link GridWallImpl}s expected.
    */
   private void assertSplitPaneItems( GridWallImpl... expectedWalls ){
      SplitPane split = ( SplitPane ) systemUnderTest.getCenter();
      assertThat( split.getItems().size(), is( expectedWalls.length ) );
      assertThat( split.getItems(), contains( expectedWalls ) );
   }//End Method
   
   @Test public void shouldHideAndShowRightWall(){
      systemUnderTest.hideRightWall();
      assertSplitPaneItems( systemUnderTest.leftGridWall() );
      systemUnderTest.showRightWall();
      assertSplitPaneItems( systemUnderTest.leftGridWall(), systemUnderTest.rightGridWall() );
   }//End Method
   
   @Test public void shouldHideAndShowLeftWall(){
      systemUnderTest.hideLeftWall();
      assertSplitPaneItems( systemUnderTest.rightGridWall() );
      systemUnderTest.showLeftWall();
      assertSplitPaneItems( systemUnderTest.leftGridWall(), systemUnderTest.rightGridWall() );
   }//End Method

   @Test public void shouldHideConfigIfWallHiddenRightWall(){
      systemUnderTest.showRightConfiguration();
      assertThat( systemUnderTest.isConfigurationShowing(), is( true ) );
      systemUnderTest.hideRightWall();
      assertThat( systemUnderTest.isConfigurationShowing(), is( false ) );
      
      systemUnderTest.showRightWall();
      assertThat( systemUnderTest.isConfigurationShowing(), is( false ) );
      
      systemUnderTest.showLeftConfiguration();
      systemUnderTest.hideRightWall();
      assertThat( systemUnderTest.isConfigurationShowing(), is( true ) );
   }//End Method

   @Test public void shouldHideConfigIfWallHiddenLeftWall(){
      systemUnderTest.showLeftConfiguration();
      assertThat( systemUnderTest.isConfigurationShowing(), is( true ) );
      systemUnderTest.hideLeftWall();
      assertThat( systemUnderTest.isConfigurationShowing(), is( false ) );
      
      systemUnderTest.showLeftWall();
      assertThat( systemUnderTest.isConfigurationShowing(), is( false ) );
      
      systemUnderTest.showRightConfiguration();
      systemUnderTest.hideLeftWall();
      assertThat( systemUnderTest.isConfigurationShowing(), is( true ) );
   }//End Method
   
   @Test public void multipleShowsShouldNotMultipleAdd(){
      assertSplitPaneItems( systemUnderTest.leftGridWall(), systemUnderTest.rightGridWall() );
      systemUnderTest.showRightWall();
      systemUnderTest.showRightWall();
      systemUnderTest.showRightWall();
      systemUnderTest.showLeftWall();
      systemUnderTest.showLeftWall();
      systemUnderTest.showLeftWall();
      assertSplitPaneItems( systemUnderTest.leftGridWall(), systemUnderTest.rightGridWall() );
   }//End Method
   
   @Test public void shouldUseContextMenuOpenerOnEntireSut(){
      systemUnderTest.initialiseContextMenu();
      assertThat( systemUnderTest.getOnContextMenuRequested(), instanceOf( DualBuildWallContextMenuOpener.class ) );
   }//End Method
   
   @Test public void leftConfigurationShouldUseDefaults(){
      assertThat( systemUnderTest.leftConfiguration().numberOfColumns().get(), is( 1 ) );
      assertThat( systemUnderTest.leftConfiguration().jobPanelDescriptionProvider().get(), is( JobPanelDescriptionProviders.Simple ) );
   }//End Method
   
   @Test public void rightConfigurationShouldUseDefaults(){
      assertThat( systemUnderTest.rightConfiguration().jobPanelDescriptionProvider().get(), is( JobPanelDescriptionProviders.Detailed ) );
   }//End Method
   
}//End Class
