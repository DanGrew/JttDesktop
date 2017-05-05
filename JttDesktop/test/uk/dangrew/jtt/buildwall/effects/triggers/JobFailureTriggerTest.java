/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.effects.triggers;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.buildwall.effects.flasher.control.ImageFlasherControls;
import uk.dangrew.jtt.buildwall.effects.flasher.control.ImageFlasherControlsImpl;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.TestJenkinsDatabaseImpl;

/**
 * {@link JobFailureTrigger} test.
 */
public class JobFailureTriggerTest {
   
   private JenkinsJob firstJob;
   private JenkinsJob secondJob;
   private JenkinsJob thirdJob;
   private JenkinsDatabase database;
   
   private ImageFlasherControls imageFlasherControls;
   private JobFailureTrigger systemUnderTest;

   @Before public void initialiseSystemUnderTest(){
      database = new TestJenkinsDatabaseImpl();
      firstJob = new JenkinsJobImpl( "first" );
      secondJob = new JenkinsJobImpl( "second" );
      thirdJob = new JenkinsJobImpl( "third" );
      database.store( firstJob );
      database.store( secondJob );
      database.store( thirdJob );
      database.jenkinsJobs().forEach( job -> job.setBuildStatus( BuildResultStatus.SUCCESS ) );
      
      imageFlasherControls = new ImageFlasherControlsImpl();
      systemUnderTest = new JobFailureTrigger( database, imageFlasherControls );
   }//End Method
   
   @Test public void whenAnyJobFailsSwitchShouldBeFlipped() {
      assertThat( imageFlasherControls.flashingSwitch().get(), is( false ) );
      firstJob.setBuildStatus( BuildResultStatus.FAILURE );
      assertThat( imageFlasherControls.flashingSwitch().get(), is( true ) );
      
      imageFlasherControls.flashingSwitch().set( false );
      secondJob.setBuildStatus( BuildResultStatus.ABORTED );
      assertThat( imageFlasherControls.flashingSwitch().get(), is( true ) );
      
      imageFlasherControls.flashingSwitch().set( false );
      thirdJob.setBuildStatus( BuildResultStatus.UNSTABLE );
      assertThat( imageFlasherControls.flashingSwitch().get(), is( true ) );
   }//End Method
   
   @Test public void whenAnyJobFailsAgainSwitchShouldNotBeFlipped() {
      assertThat( imageFlasherControls.flashingSwitch().get(), is( false ) );
      firstJob.setBuildStatus( BuildResultStatus.FAILURE );
      assertThat( imageFlasherControls.flashingSwitch().get(), is( true ) );
      
      imageFlasherControls.flashingSwitch().set( false );
      firstJob.setBuildStatus( BuildResultStatus.FAILURE );
      assertThat( imageFlasherControls.flashingSwitch().get(), is( false ) );
   }//End Method

}//End Class
