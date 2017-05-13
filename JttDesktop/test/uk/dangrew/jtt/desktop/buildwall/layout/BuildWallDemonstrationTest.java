/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.layout;

import static org.mockito.Mockito.mock;

import java.util.Timer;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uk.dangrew.jtt.connection.api.sources.ExternalApi;
import uk.dangrew.jtt.desktop.api.handling.live.LiveStateFetcher;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallJobPolicy;
import uk.dangrew.jtt.desktop.buildwall.layout.BuildWallDisplayImpl;
import uk.dangrew.jtt.desktop.buildwall.panel.JobBuildSimulator;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;
import uk.dangrew.jtt.desktop.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.desktop.styling.SystemStyling;
import uk.dangrew.jtt.desktop.synchronisation.time.JobUpdater;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.SystemWideJenkinsDatabaseImpl;

/**
 * Provides a demonstration of the {@link BuildWallDisplayImpl} showing various
 * behaviour of jobs graphically.
 */
public class BuildWallDemonstrationTest {
   
   private JenkinsJob coreProject;
   private JenkinsJob graphicalProject;
   private JenkinsJob extensionProject;
   private JenkinsJob v102;
   private JenkinsJob v103;
   private JenkinsJob invisibleArchitecture;
   private JenkinsJob invisibleValidation;
   private JenkinsJob invisibleEventSystem;

   @Before public void initialiseSystemUnderTest(){
      JenkinsDatabase database = new SystemWideJenkinsDatabaseImpl().get();
      new JobUpdater( new LiveStateFetcher( 
               mock( ExternalApi.class ) 
      ), new Timer(), 5000L );
      
      coreProject = new JenkinsJobImpl( "Core Project" );
      graphicalProject = new JenkinsJobImpl( "Graphics" );
      extensionProject = new JenkinsJobImpl( "Extension" );
      v102 = new JenkinsJobImpl( "1.0.2" );
      v103 = new JenkinsJobImpl( "1.0.3" );
      invisibleArchitecture = new JenkinsJobImpl( "Architecture" );
      invisibleValidation = new JenkinsJobImpl( "Validation" );
      invisibleEventSystem = new JenkinsJobImpl( "Event System" );
      
      database.store( coreProject );
      database.store( graphicalProject );
      database.store( extensionProject );
      database.store( v102 );
      database.store( v103 );
      database.store( invisibleArchitecture );
      database.store( invisibleValidation );
      database.store( invisibleEventSystem );
      
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      SystemStyling.initialise();
      JavaFxInitializer.launchInWindow( () -> {
         BuildWallDisplayImpl display = new BuildWallDisplayImpl( database );
         BuildWallConfiguration configuration = display.configuration();
         configuration.jobPolicies().put( invisibleArchitecture, BuildWallJobPolicy.OnlyShowFailures );
         configuration.jobPolicies().put( invisibleValidation, BuildWallJobPolicy.OnlyShowFailures );
         configuration.jobPolicies().put( invisibleEventSystem, BuildWallJobPolicy.OnlyShowFailures );
         return display;
      } );
   }//End Method
   
   @Ignore
   @Test public void demonstration() throws InterruptedException {
      JobBuildSimulator.simulateConcurrentBuilding( coreProject, BuildResultStatus.FAILURE, BuildResultStatus.FAILURE, 100, 10000, 100 );
      JobBuildSimulator.simulateConcurrentBuilding( graphicalProject, BuildResultStatus.SUCCESS, BuildResultStatus.SUCCESS, 45, 20000, 100 );
      JobBuildSimulator.simulateConcurrentBuilding( extensionProject, BuildResultStatus.ABORTED, BuildResultStatus.ABORTED, 33, 2000, 100 );
      JobBuildSimulator.simulateConcurrentBuilding( v102, BuildResultStatus.UNSTABLE, BuildResultStatus.UNSTABLE, 3, 25000, 100 );
      JobBuildSimulator.simulateConcurrentBuilding( v103, BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS, 1, 25000, 100 );
      
      JobBuildSimulator.simulateConcurrentBuilding( invisibleArchitecture, BuildResultStatus.SUCCESS, BuildResultStatus.FAILURE, 1, 5000, 100 );
      JobBuildSimulator.simulateConcurrentBuilding( invisibleValidation, BuildResultStatus.SUCCESS, BuildResultStatus.UNSTABLE, 1, 8000, 100 );
      JobBuildSimulator.simulateConcurrentBuilding( invisibleEventSystem, BuildResultStatus.SUCCESS, BuildResultStatus.UNSTABLE, 1, 11000, 100 );
      
      Thread.sleep( 13000 );
      
      JobBuildSimulator.simulateConcurrentBuilding( invisibleArchitecture, BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS, 1, 5000, 100 );
      JobBuildSimulator.simulateConcurrentBuilding( invisibleValidation, BuildResultStatus.UNSTABLE, BuildResultStatus.SUCCESS, 1, 8000, 100 );
      JobBuildSimulator.simulateConcurrentBuilding( invisibleEventSystem, BuildResultStatus.UNSTABLE, BuildResultStatus.SUCCESS, 1, 11000, 100 );
      
      Thread.sleep( 10000000 );
   }//End Method

}//End Class
