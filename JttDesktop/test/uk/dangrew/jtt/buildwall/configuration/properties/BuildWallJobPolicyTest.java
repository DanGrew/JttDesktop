/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.properties;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.utility.TestCommon;

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
      job.setBuildStatus( BuildResultStatus.ABORTED );
      assertThat( BuildWallJobPolicy.OnlyShowFailures.shouldShow( job ), is( true ) );
      
      job.setBuildStatus( BuildResultStatus.FAILURE );
      assertThat( BuildWallJobPolicy.OnlyShowFailures.shouldShow( job ), is( true ) );
      
      job.setBuildStatus( BuildResultStatus.NOT_BUILT );
      assertThat( BuildWallJobPolicy.OnlyShowFailures.shouldShow( job ), is( false ) );
      
      job.setBuildStatus( BuildResultStatus.SUCCESS );
      assertThat( BuildWallJobPolicy.OnlyShowFailures.shouldShow( job ), is( false ) );
      
      job.setBuildStatus( BuildResultStatus.UNKNOWN );
      assertThat( BuildWallJobPolicy.OnlyShowFailures.shouldShow( job ), is( false ) );
      
      job.setBuildStatus( BuildResultStatus.UNSTABLE );
      assertThat( BuildWallJobPolicy.OnlyShowFailures.shouldShow( job ), is( true ) );
   }//End Method
   
   @Test public void shouldDecideForShowPassingOnly() {
      job.setBuildStatus( BuildResultStatus.ABORTED );
      assertThat( BuildWallJobPolicy.OnlyShowPassing.shouldShow( job ), is( false ) );
      
      job.setBuildStatus( BuildResultStatus.FAILURE );
      assertThat( BuildWallJobPolicy.OnlyShowPassing.shouldShow( job ), is( false ) );
      
      job.setBuildStatus( BuildResultStatus.NOT_BUILT );
      assertThat( BuildWallJobPolicy.OnlyShowPassing.shouldShow( job ), is( false ) );
      
      job.setBuildStatus( BuildResultStatus.SUCCESS );
      assertThat( BuildWallJobPolicy.OnlyShowPassing.shouldShow( job ), is( true ) );
      
      job.setBuildStatus( BuildResultStatus.UNKNOWN );
      assertThat( BuildWallJobPolicy.OnlyShowPassing.shouldShow( job ), is( false ) );
      
      job.setBuildStatus( BuildResultStatus.UNSTABLE );
      assertThat( BuildWallJobPolicy.OnlyShowPassing.shouldShow( job ), is( false ) );
   }//End Method
   
   @Test public void shouldDecideForBuildingOnlyOnly() {
      job.buildStateProperty().set( BuildState.Built );
      assertThat( BuildWallJobPolicy.OnlyShowBuilding.shouldShow( job ), is( false ) );
      
      job.buildStateProperty().set( BuildState.Building );
      assertThat( BuildWallJobPolicy.OnlyShowBuilding.shouldShow( job ), is( true ) );
   }//End Method

}//End Class
