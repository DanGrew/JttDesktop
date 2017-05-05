/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.handling.live;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.json.JSONObject;
import org.junit.Test;

import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.SystemWideJenkinsDatabaseImpl;
import uk.dangrew.jtt.utility.TestCommon;

public class JobDetailsParserTest {

   @Test public void shouldParseSampleDataIntoDatabase() {
      String parsed = TestCommon.readFileIntoString( getClass(), "job-details-sample.txt" );
      JSONObject object = new JSONObject( parsed );
      
      JenkinsDatabase database = new SystemWideJenkinsDatabaseImpl().get();
      new JobDetailsParser( new JobDetailsModel() ).parse( object );
      
      assertThat( database.getJenkinsNode( "Dans Machine" ), is( notNullValue() ) );
      assertThat( database.getJenkinsNode( "Noones Machine" ), is( notNullValue() ) );
      assertThat( database.getJenkinsUser( "Dan Grew" ), is( notNullValue() ) );
      
      JenkinsJob dansProject = database.getJenkinsJob( "Dans Project" );
      assertThat( dansProject.buildStateProperty().get(), is( BuildState.Built ) );
      assertThat( dansProject.culprits().get( 0 ), is( database.getJenkinsUser( "Dan Grew" ) ) );
      assertThat( dansProject.currentBuildTimeProperty().get(), is( 0L ) );
      assertThat( dansProject.buildTimestampProperty().get(), is( 1481218445438L ) );
      assertThat( dansProject.expectedBuildTimeProperty().get(), is( 11149L ) );
      assertThat( dansProject.failingTestCases(), is( empty() ) );
      assertThat( dansProject.getBuildNumber(), is( 15 ) );
      assertThat( dansProject.getBuildStatus(), is( BuildResultStatus.SUCCESS ) );
      assertThat( dansProject.builtOnProperty().get(), is( database.getJenkinsNode( "Noones Machine" ) ) );
      assertThat( dansProject.testFailureCount().get(), is( 0 ) );
      assertThat( dansProject.testSkipCount().get(), is( 0 ) );
      assertThat( dansProject.testTotalCount().get(), is( 0 ) );
      assertThat( dansProject.totalBuildTimeProperty().get(), is( 12975L ) );
      
      JenkinsJob jeffsProject = database.getJenkinsJob( "Jeffs Project" );
      assertThat( jeffsProject.buildStateProperty().get(), is( BuildState.Built ) );
      assertThat( jeffsProject.culprits().get( 0 ), is( database.getJenkinsUser( "Dan Grew" ) ) );
      assertThat( jeffsProject.currentBuildTimeProperty().get(), is( 0L ) );
      assertThat( jeffsProject.buildTimestampProperty().get(), is( 1481218487257L ) );
      assertThat( jeffsProject.expectedBuildTimeProperty().get(), is( 124821L ) );
      assertThat( jeffsProject.failingTestCases(), is( empty() ) );
      assertThat( jeffsProject.getBuildNumber(), is( 560 ) );
      assertThat( jeffsProject.getBuildStatus(), is( BuildResultStatus.SUCCESS ) );
      assertThat( jeffsProject.builtOnProperty().get(), is( database.getJenkinsNode( "Dans Machine" ) ) );
      assertThat( jeffsProject.testFailureCount().get(), is( 12 ) );
      assertThat( jeffsProject.testSkipCount().get(), is( 1 ) );
      assertThat( jeffsProject.testTotalCount().get(), is( 15 ) );
      assertThat( jeffsProject.totalBuildTimeProperty().get(), is( 77521L ) );
      
      JenkinsJob jeffsStuff = database.getJenkinsJob( "Jeffs Stuff" );
      assertThat( jeffsStuff.buildStateProperty().get(), is( BuildState.Building ) );
      assertThat( jeffsStuff.culprits(), is( empty() ) );
      assertThat( jeffsStuff.currentBuildTimeProperty().get(), is( 0L ) );
      assertThat( jeffsStuff.buildTimestampProperty().get(), is( 0L ) );
      assertThat( jeffsStuff.expectedBuildTimeProperty().get(), is( 0L ) );
      assertThat( jeffsStuff.failingTestCases(), is( empty() ) );
      assertThat( jeffsStuff.getBuildNumber(), is( 0 ) );
      assertThat( jeffsStuff.getBuildStatus(), is( BuildResultStatus.NOT_BUILT ) );
      assertThat( jeffsStuff.builtOnProperty().get(), is( nullValue() ) );
      assertThat( jeffsStuff.testFailureCount().get(), is( 0 ) );
      assertThat( jeffsStuff.testSkipCount().get(), is( 0 ) );
      assertThat( jeffsStuff.testTotalCount().get(), is( 0 ) );
      assertThat( jeffsStuff.totalBuildTimeProperty().get(), is( 0L ) );
      
      JenkinsJob newJob = database.getJenkinsJob( "Brand new job" );
      assertThat( newJob, is( notNullValue() ) );
   }//End Method

}//End Class
