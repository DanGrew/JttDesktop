/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.panel.type;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.desktop.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.desktop.buildwall.configuration.theme.BuildWallThemeImpl;
import uk.dangrew.jtt.desktop.buildwall.panel.description.DefaultJobPanelDescriptionImpl;
import uk.dangrew.jtt.desktop.buildwall.panel.description.DetailedJobPanelDescriptionImpl;
import uk.dangrew.jtt.desktop.buildwall.panel.description.JobPanelDescriptionBaseImpl;
import uk.dangrew.jtt.desktop.buildwall.panel.description.SimpleJobPanelDescriptionImpl;
import uk.dangrew.jtt.desktop.buildwall.panel.type.JobPanelDescriptionProviders;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;
import uk.dangrew.jtt.desktop.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;

/**
 * {@link JobPanelDescriptionProviders}
 */
public class JobPanelDescriptionProvidersTest {
   
   private BuildWallTheme theme;
   private BuildWallConfiguration configuration;
   private JenkinsJob job;
   
   @BeforeClass public static void initialisePlatform(){
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      JavaFxInitializer.startPlatform();
   }//End Method
   
   @Before public void initialiseEnvironment(){
      configuration = new BuildWallConfigurationImpl();
      theme = new BuildWallThemeImpl( "Test" );
      job = new JenkinsJobImpl( "some job" );
   }//End Method

   @Test public void simpleShouldProvideSimple() {
      JobPanelDescriptionBaseImpl description = JobPanelDescriptionProviders.Simple.constructJobDescriptionPanel( configuration, theme, job );
      assertThat( description, notNullValue() );
      assertThat( description, instanceOf( SimpleJobPanelDescriptionImpl.class ) );
   }//End Method
   
   @Test public void defaultShouldProvideDefault() {
      JobPanelDescriptionBaseImpl description = JobPanelDescriptionProviders.Default.constructJobDescriptionPanel( configuration, theme, job );
      assertThat( description, notNullValue() );
      assertThat( description, instanceOf( DefaultJobPanelDescriptionImpl.class ) );
   }//End Method
   
   @Test public void detailedShouldProvideDetailed() {
      JobPanelDescriptionBaseImpl description = JobPanelDescriptionProviders.Detailed.constructJobDescriptionPanel( configuration, theme, job );
      assertThat( description, notNullValue() );
      assertThat( description, instanceOf( DetailedJobPanelDescriptionImpl.class ) );
   }//End Method

}//End Class
