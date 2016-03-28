/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.panel.type;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import buildwall.configuration.BuildWallConfiguration;
import buildwall.configuration.BuildWallConfigurationImpl;
import buildwall.panel.description.DefaultJobPanelDescriptionImpl;
import buildwall.panel.description.JobPanelDescriptionBaseImpl;
import buildwall.panel.description.SimpleJobPanelDescriptionImpl;
import graphics.DecoupledPlatformImpl;
import graphics.JavaFxInitializer;
import graphics.TestPlatformDecouplerImpl;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;

/**
 * {@link JobPanelDescriptionProviders}
 */
public class JobPanelDescriptionProvidersTest {
   
   private BuildWallConfiguration configuration;
   private JenkinsJob job;
   
   @BeforeClass public static void initialisePlatform(){
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      JavaFxInitializer.startPlatform();
   }//End Method
   
   @Before public void initialiseEnvironment(){
      configuration = new BuildWallConfigurationImpl();
      job = new JenkinsJobImpl( "some job" );
   }//End Method

   @Test public void simpleShouldProvideSimple() {
      JobPanelDescriptionBaseImpl description = JobPanelDescriptionProviders.Simple.constructJobDescriptionPanel( configuration, job );
      assertThat( description, notNullValue() );
      assertThat( description, instanceOf( SimpleJobPanelDescriptionImpl.class ) );
   }//End Method
   
   @Test public void defaultShouldProvideDefault() {
      JobPanelDescriptionBaseImpl description = JobPanelDescriptionProviders.Default.constructJobDescriptionPanel( configuration, job );
      assertThat( description, notNullValue() );
      assertThat( description, instanceOf( DefaultJobPanelDescriptionImpl.class ) );
   }//End Method

}//End Class
