/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import model.jobs.BuildResultStatus;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import utility.TestCommon;

/**
 * {@link BuildWallJobPolicy} test.
 */
public class BuildWallJobPolicyTest {
   
   private JenkinsJob job;
   
   @Before public void initialiseSystemUnderTest(){
      job = new JenkinsJobImpl( "anything" );
   }//End Method
   
   @Test public void shouldValueOfForNames(){
      TestCommon.assertEnumNameWithValueOf( BuildWallJobPolicy.class );
   }//End Method
   
   @Test public void shouldValueOfForToString(){
      TestCommon.assertEnumToStringWithValueOf( BuildWallJobPolicy.class );
   }//End Method

   @Test public void shouldDecideForAlwaysShow() {
      assertThat( BuildWallJobPolicy.AlwaysShow.shouldShow( job ), is( true ) );
   }//End Method
   
   @Test public void shouldDecideForNeverShow() {
      assertThat( BuildWallJobPolicy.NeverShow.shouldShow( job ), is( false ) );
   }//End Method
   
   @Test public void shouldDecideForShowFailuresOnly() {
      job.lastBuildStatusProperty().set( BuildResultStatus.ABORTED );
      assertThat( BuildWallJobPolicy.OnlyShowFailures.shouldShow( job ), is( true ) );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.FAILURE );
      assertThat( BuildWallJobPolicy.OnlyShowFailures.shouldShow( job ), is( true ) );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.NOT_BUILT );
      assertThat( BuildWallJobPolicy.OnlyShowFailures.shouldShow( job ), is( false ) );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      assertThat( BuildWallJobPolicy.OnlyShowFailures.shouldShow( job ), is( false ) );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.UNKNOWN );
      assertThat( BuildWallJobPolicy.OnlyShowFailures.shouldShow( job ), is( false ) );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.UNSTABLE );
      assertThat( BuildWallJobPolicy.OnlyShowFailures.shouldShow( job ), is( true ) );
   }//End Method
   
   @Test public void shouldDecideForShowPassingOnly() {
      job.lastBuildStatusProperty().set( BuildResultStatus.ABORTED );
      assertThat( BuildWallJobPolicy.OnlyShowPassing.shouldShow( job ), is( false ) );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.FAILURE );
      assertThat( BuildWallJobPolicy.OnlyShowPassing.shouldShow( job ), is( false ) );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.NOT_BUILT );
      assertThat( BuildWallJobPolicy.OnlyShowPassing.shouldShow( job ), is( false ) );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      assertThat( BuildWallJobPolicy.OnlyShowPassing.shouldShow( job ), is( true ) );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.UNKNOWN );
      assertThat( BuildWallJobPolicy.OnlyShowPassing.shouldShow( job ), is( false ) );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.UNSTABLE );
      assertThat( BuildWallJobPolicy.OnlyShowPassing.shouldShow( job ), is( false ) );
   }//End Method

}//End Class
