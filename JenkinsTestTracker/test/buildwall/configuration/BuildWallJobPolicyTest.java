/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration;

import org.hamcrest.Matchers;
import org.junit.Assert;
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
      Assert.assertThat( BuildWallJobPolicy.AlwaysShow.shouldShow( job ), Matchers.is( true ) );
   }//End Method
   
   @Test public void shouldDecideForNeverShow() {
      Assert.assertThat( BuildWallJobPolicy.NeverShow.shouldShow( job ), Matchers.is( false ) );
   }//End Method
   
   @Test public void shouldDecideForShowFailuresOnly() {
      job.lastBuildStatusProperty().set( BuildResultStatus.ABORTED );
      Assert.assertThat( BuildWallJobPolicy.OnlyShowFailures.shouldShow( job ), Matchers.is( false ) );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.FAILURE );
      Assert.assertThat( BuildWallJobPolicy.OnlyShowFailures.shouldShow( job ), Matchers.is( true ) );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.NOT_BUILT );
      Assert.assertThat( BuildWallJobPolicy.OnlyShowFailures.shouldShow( job ), Matchers.is( false ) );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      Assert.assertThat( BuildWallJobPolicy.OnlyShowFailures.shouldShow( job ), Matchers.is( false ) );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.UNKNOWN );
      Assert.assertThat( BuildWallJobPolicy.OnlyShowFailures.shouldShow( job ), Matchers.is( false ) );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.UNSTABLE );
      Assert.assertThat( BuildWallJobPolicy.OnlyShowFailures.shouldShow( job ), Matchers.is( true ) );
   }//End Method

}//End Class
