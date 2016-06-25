/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.panel.type;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.buildwall.panel.description.DefaultJobPanelDescriptionImpl;
import uk.dangrew.jtt.buildwall.panel.description.DetailedJobPanelDescriptionImpl;
import uk.dangrew.jtt.buildwall.panel.description.JobPanelDescriptionBaseImpl;
import uk.dangrew.jtt.buildwall.panel.description.SimpleJobPanelDescriptionImpl;
import uk.dangrew.jtt.buildwall.panel.type.JobPanelDescriptionProviders;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;

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
   
   @Test public void detailedShouldProvideDetailed() {
      JobPanelDescriptionBaseImpl description = JobPanelDescriptionProviders.Detailed.constructJobDescriptionPanel( configuration, job );
      assertThat( description, notNullValue() );
      assertThat( description, instanceOf( DetailedJobPanelDescriptionImpl.class ) );
   }//End Method

}//End Class
