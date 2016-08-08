/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.dual;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map.Entry;
import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sun.javafx.application.PlatformImpl;

import javafx.event.EventHandler;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import uk.dangrew.jtt.buildwall.configuration.persistence.buildwall.BuildWallConfigurationSessions;
import uk.dangrew.jtt.buildwall.configuration.persistence.dualwall.DualWallConfigurationSessions;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallJobPolicy;
import uk.dangrew.jtt.buildwall.effects.flasher.ImageFlasherImplTest;
import uk.dangrew.jtt.buildwall.layout.GridWallImpl;
import uk.dangrew.jtt.buildwall.panel.type.JobPanelDescriptionProviders;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.environment.preferences.ConfigurationWindowController;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;
import uk.dangrew.jtt.styling.SystemStyling;
import uk.dangrew.jtt.utility.TestCommon;

/**
 * {@link DualBuildWallDisplayImpl} test.
 */
public class DualBuildWallDisplayImplTest {

   @Mock private ConfigurationWindowController windowController;
   @Mock private BuildWallConfigurationSessions sessions;
   @Mock private DualWallConfigurationSessions dualSessions;
   private JenkinsDatabase database;
   private DualBuildWallDisplayImpl systemUnderTest;

   private SystemConfiguration systemConfiguration;
   
   @BeforeClass public static void initialiseStylings(){
      SystemStyling.initialise();
   }//End Method
   
   @BeforeClass public static void initialisePlatform(){
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      
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
      
      systemConfiguration = new SystemConfiguration();
      
      systemUnderTest = new DualBuildWallDisplayImpl( database, systemConfiguration, windowController, sessions, dualSessions );
      
      systemConfiguration.getLeftConfiguration().jobPolicies().entrySet().forEach( entry -> entry.setValue( BuildWallJobPolicy.AlwaysShow ) );
      systemConfiguration.getRightConfiguration().jobPolicies().entrySet().forEach( entry -> entry.setValue( BuildWallJobPolicy.AlwaysShow ) );
      PlatformImpl.runAndWait( () -> {} );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      JavaFxInitializer.launchInWindow( () -> {
         systemUnderTest = new DualBuildWallDisplayImpl( database, systemConfiguration );
         systemUnderTest.setOnContextMenuRequested( new DualBuildWallContextMenuOpener( systemUnderTest ) );
         return systemUnderTest; 
      } );
      
      Image alertImage = new Image( ImageFlasherImplTest.class.getResourceAsStream( "alert-image.png" ) );
      systemUnderTest.imageFlasherConfiguration().imageProperty().set( alertImage );
      systemUnderTest.imageFlasherConfiguration().flashingSwitch().set( true );
      
      Thread.sleep( 4000 );
      
      PlatformImpl.runAndWait( () -> {
         systemUnderTest.hideRightWall();
      } );
          
      Thread.sleep( 4000 );

      PlatformImpl.runAndWait( () -> {
         systemConfiguration.getRightConfiguration().jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Default );
         for ( Entry< JenkinsJob, BuildWallJobPolicy > entry : systemConfiguration.getRightConfiguration().jobPolicies().entrySet() ) {
            entry.setValue( BuildWallJobPolicy.NeverShow );
         }
      } );
      
      PlatformImpl.runAndWait( () -> {
         systemConfiguration.getRightConfiguration().jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Detailed );
         for ( Entry< JenkinsJob, BuildWallJobPolicy > entry : systemConfiguration.getRightConfiguration().jobPolicies().entrySet() ) {
            entry.setValue( BuildWallJobPolicy.AlwaysShow );
         }
      } );
      
      Thread.sleep( 4000 );

      PlatformImpl.runAndWait( () -> {
         systemUnderTest.showRightWall();
      } );
      
      Thread.sleep( 4000 );

      PlatformImpl.runAndWait( () -> {
         systemConfiguration.getRightConfiguration().jobPolicies().entrySet()
            .forEach( entry -> entry.setValue( BuildWallJobPolicy.NeverShow ) );
      } );
      
      Thread.sleep( 4000 );

      PlatformImpl.runAndWait( () -> {
         systemConfiguration.getRightConfiguration().jobPolicies().entrySet()
            .forEach( entry -> entry.setValue( BuildWallJobPolicy.AlwaysShow ) );
      } );
      
      Thread.sleep( 4000 );

      PlatformImpl.runAndWait( () -> {
         systemConfiguration.getRightConfiguration().jobPolicies().entrySet()
            .forEach( entry -> entry.setValue( BuildWallJobPolicy.NeverShow ) );
      } );
      
      Thread.sleep( 4000 );

      PlatformImpl.runAndWait( () -> {
         systemConfiguration.getLeftConfiguration().jobPolicies().entrySet()
            .forEach( entry -> entry.setValue( BuildWallJobPolicy.NeverShow ) );
      } );
      
      Thread.sleep( 4000 );

      PlatformImpl.runAndWait( () -> {
         systemConfiguration.getRightConfiguration().jobPolicies().entrySet().iterator().next().setValue( BuildWallJobPolicy.AlwaysShow );
         systemConfiguration.getLeftConfiguration().jobPolicies().entrySet().iterator().next().setValue( BuildWallJobPolicy.AlwaysShow );
      } );
      
      Thread.sleep( 10000000 );
   }//End Method
   
   @Ignore
   @Test public void menuStressing() throws InterruptedException {
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      JavaFxInitializer.launchInWindow( () -> {
         systemUnderTest.setOnContextMenuRequested( new DualBuildWallContextMenuOpener( systemUnderTest ) );
         return systemUnderTest; 
      } );
      
      Random random = new Random();
      for ( int i = 0; i < 100000; i++ ) {
         EventHandler< ? super ContextMenuEvent > conextHandler = systemUnderTest.onContextMenuRequestedProperty().get();
      }
      
      Thread.sleep( 10000000 );
   }//End Method
   
   @Test public void shouldInitialseWithNoConfiguration(){
      assertThat( systemUnderTest.isConfigurationShowing(), is( false ) );
      assertThat( systemUnderTest.buildWallPane().getRight(), nullValue() );
   }//End Method
   
   @Test public void shouldEnsureConfigurationIsUpdatedInitiallyAndWhenDatabaseIsUpdatedForBothWallsIndividually(){
      assertThat( database.jenkinsJobs().size(), is( 15 ) );
      assertThat( systemConfiguration.getRightConfiguration().jobPolicies().size(), is( 15 ) );
      assertThat( systemConfiguration.getLeftConfiguration().jobPolicies().size(), is( 15 ) );
      
      JenkinsJob job = new JenkinsJobImpl( "something-else" );
      database.store( job );
      assertThat( systemConfiguration.getRightConfiguration().jobPolicies().containsKey( job ), is( true ) );
      assertThat( systemConfiguration.getLeftConfiguration().jobPolicies().containsKey( job ), is( true ) );
   }//End Method
   
   @Test public void shouldProvideGridWallsInSplitPaneForManualSplitting(){
      assertThat( systemUnderTest.buildWallPane().getCenter(), instanceOf( SplitPane.class ) );
      assertSplitPaneItems( systemUnderTest.leftGridWall(), systemUnderTest.rightGridWall() );
   }//End Method
   
   /**
    * Method to assert that the given items are in the {@link SplitPane}, and only these.
    * @param expectedWalls the {@link GridWallImpl}s expected.
    */
   private void assertSplitPaneItems( GridWallImpl... expectedWalls ){
      SplitPane split = ( SplitPane ) systemUnderTest.buildWallPane().getCenter();
      assertThat( split.getItems().size(), is( expectedWalls.length ) );
      assertThat( split.getItems(), contains( expectedWalls ) );
   }//End Method
   
   @Test public void shouldHideAndShowRightWall(){
      systemUnderTest.hideRightWall();
      assertThat( systemUnderTest.isRightWallShowing(), is( false ) );
      assertSplitPaneItems( systemUnderTest.leftGridWall() );
      systemUnderTest.showRightWall();
      assertThat( systemUnderTest.isRightWallShowing(), is( true ) );
      assertSplitPaneItems( systemUnderTest.leftGridWall(), systemUnderTest.rightGridWall() );
   }//End Method
   
   @Test public void shouldHideAndShowLeftWall(){
      systemUnderTest.hideLeftWall();
      assertThat( systemUnderTest.isLeftWallShowing(), is( false ) );
      assertSplitPaneItems( systemUnderTest.rightGridWall() );
      systemUnderTest.showLeftWall();
      assertThat( systemUnderTest.isLeftWallShowing(), is( true ) );
      assertSplitPaneItems( systemUnderTest.leftGridWall(), systemUnderTest.rightGridWall() );
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
   
   @Test public void shouldAutoHideAndAutoShowRightWall(){
      assertThat( systemUnderTest.isRightWallShowing(), is( true ) );
      systemConfiguration.getRightConfiguration().jobPolicies().entrySet().forEach( entry -> entry.setValue( BuildWallJobPolicy.NeverShow ) );
      PlatformImpl.runAndWait( () -> {} );
      assertThat( systemUnderTest.isRightWallShowing(), is( false ) );
      
      systemConfiguration.getRightConfiguration().jobPolicies().entrySet().iterator().next().setValue( BuildWallJobPolicy.AlwaysShow );
      PlatformImpl.runAndWait( () -> {} );
      assertThat( systemUnderTest.isRightWallShowing(), is( true ) );
   }//End Method
   
   @Test public void shouldLeftHideAndAutoShowLeftWall(){
      assertThat( systemUnderTest.isLeftWallShowing(), is( true ) );
      systemConfiguration.getLeftConfiguration().jobPolicies().entrySet().forEach( entry -> entry.setValue( BuildWallJobPolicy.NeverShow ) );
      PlatformImpl.runAndWait( () -> {} );
      assertThat( systemUnderTest.isLeftWallShowing(), is( false ) );
      
      systemConfiguration.getLeftConfiguration().jobPolicies().entrySet().iterator().next().setValue( BuildWallJobPolicy.AlwaysShow );
      PlatformImpl.runAndWait( () -> {} );
      assertThat( systemUnderTest.isLeftWallShowing(), is( true ) );
   }//End Method
   
   @Test public void shouldPreserveDividerLocationForLeft(){
      final double dividerLocation = 0.8;
      systemUnderTest.splitPane().setDividerPosition( 0, dividerLocation );
      
      systemConfiguration.getLeftConfiguration().jobPolicies().entrySet().forEach( entry -> entry.setValue( BuildWallJobPolicy.NeverShow ) );
      PlatformImpl.runAndWait( () -> {} );
      
      systemConfiguration.getLeftConfiguration().jobPolicies().entrySet().iterator().next().setValue( BuildWallJobPolicy.AlwaysShow );
      PlatformImpl.runAndWait( () -> {} );
      
      assertThat( systemUnderTest.splitPane().getDividerPositions()[ 0 ], closeTo( dividerLocation, TestCommon.precision() ) );
   }//End Method
   
   @Test public void shouldPreserveDividerLocationForRight(){
      final double dividerLocation = 0.8;
      systemUnderTest.splitPane().setDividerPosition( 0, dividerLocation );
      
      systemConfiguration.getRightConfiguration().jobPolicies().entrySet().forEach( entry -> entry.setValue( BuildWallJobPolicy.NeverShow ) );
      PlatformImpl.runAndWait( () -> {} );
      
      systemConfiguration.getRightConfiguration().jobPolicies().entrySet().iterator().next().setValue( BuildWallJobPolicy.AlwaysShow );
      PlatformImpl.runAndWait( () -> {} );
      
      assertThat( systemUnderTest.splitPane().getDividerPositions()[ 0 ], closeTo( dividerLocation, TestCommon.precision() ) );
   }//End Method
   
   @Test public void shouldBeAStackPaneWithFlasherOnTop(){
      assertThat( systemUnderTest.getChildren(), hasSize( 2 ) );
      assertThat( systemUnderTest.getChildren().get( 0 ), is( systemUnderTest.buildWallPane() ) );
      assertThat( systemUnderTest.buildWallPane().getCenter(), is( systemUnderTest.splitPane() ) );
      assertThat( systemUnderTest.getChildren().get( 1 ), is( systemUnderTest.imageFlasher() ) );
   }//End Method
   
   @Test public void shouldShowImageFlasherConfiguration(){
      assertThat( systemUnderTest.isConfigurationShowing(), is( false ) );
      systemUnderTest.showImageFlasherConfiguration();
      assertThat( systemUnderTest.isConfigurationShowing(), is( true ) );
      assertThat( systemUnderTest.buildWallPane().getRight(), is( notNullValue() ) );
   }//End Method
   
   @Test public void shouldStartFlasherWhenFailureHappens(){
      database.jenkinsJobs().get( 0 ).lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      assertThat( systemUnderTest.imageFlasherConfiguration().flashingSwitch().get(), is( false ) );
      database.jenkinsJobs().get( 0 ).lastBuildStatusProperty().set( BuildResultStatus.FAILURE );
      assertThat( systemUnderTest.imageFlasherConfiguration().flashingSwitch().get(), is( true ) );
   }//End Method
   
   @Test public void shouldAssociateConfigurationWindowController(){
      verify( windowController ).associateWithConfiguration( 
               systemConfiguration 
      );
   }//End Method
   
   @Test public void shouldRedirectShowConfigurationWindow(){
      systemUnderTest.showConfigurationWindow();
      verify( windowController ).showConfigurationWindow();
   }//End Method
   
   @Test public void shouldRedirectHideConfigurationWindow(){
      systemUnderTest.hideConfigurationWindow();
      verify( windowController ).hideConfigurationWindow();
   }//End Method
   
   @Test public void shouldRedirectIsShowingConfigurationWindow(){
      when( windowController.isConfigurationWindowShowing() ).thenReturn( false );
      assertThat( systemUnderTest.isConfigurationWindowShowing(), is( false ) );
      verify( windowController, times( 1 ) ).isConfigurationWindowShowing();
      
      when( windowController.isConfigurationWindowShowing() ).thenReturn( true );
      assertThat( systemUnderTest.isConfigurationWindowShowing(), is( true ) );
      verify( windowController, times( 2 ) ).isConfigurationWindowShowing();
      
      when( windowController.isConfigurationWindowShowing() ).thenReturn( false );
      assertThat( systemUnderTest.isConfigurationWindowShowing(), is( false ) );
      verify( windowController, times( 3 ) ).isConfigurationWindowShowing();
   }//End Method
   
}//End Class
