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
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;
import uk.dangrew.jtt.utility.TestCommon;

public class JobDetailsParserTest {

   @Test public void shouldParseSampleDataIntoDatabase() {
      String parsed = TestCommon.readFileIntoString( getClass(), "job-details-sample.txt" );
      JSONObject object = new JSONObject( parsed );
      
      JenkinsDatabase database = new JenkinsDatabaseImpl();
      new JobDetailsParser( new JobDetailsModel( database ) ).parse( object );
      
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
      
      JenkinsJob lizsProject = database.getJenkinsJob( "Jeffs Project" );
      assertThat( lizsProject.buildStateProperty().get(), is( BuildState.Built ) );
      assertThat( lizsProject.culprits().get( 0 ), is( database.getJenkinsUser( "Dan Grew" ) ) );
      assertThat( lizsProject.currentBuildTimeProperty().get(), is( 0L ) );
      assertThat( lizsProject.buildTimestampProperty().get(), is( 1481218487257L ) );
      assertThat( lizsProject.expectedBuildTimeProperty().get(), is( 124821L ) );
      assertThat( lizsProject.failingTestCases(), is( empty() ) );
      assertThat( lizsProject.getBuildNumber(), is( 560 ) );
      assertThat( lizsProject.getBuildStatus(), is( BuildResultStatus.SUCCESS ) );
      assertThat( lizsProject.builtOnProperty().get(), is( database.getJenkinsNode( "Dans Machine" ) ) );
      assertThat( lizsProject.testFailureCount().get(), is( 12 ) );
      assertThat( lizsProject.testSkipCount().get(), is( 1 ) );
      assertThat( lizsProject.testTotalCount().get(), is( 15 ) );
      assertThat( lizsProject.totalBuildTimeProperty().get(), is( 77521L ) );
      
      JenkinsJob jeffsProject = database.getJenkinsJob( "Jeffs Stuff" );
      assertThat( jeffsProject.buildStateProperty().get(), is( BuildState.Building ) );
      assertThat( jeffsProject.culprits(), is( empty() ) );
      assertThat( jeffsProject.currentBuildTimeProperty().get(), is( 0L ) );
      assertThat( jeffsProject.buildTimestampProperty().get(), is( 0L ) );
      assertThat( jeffsProject.expectedBuildTimeProperty().get(), is( 0L ) );
      assertThat( jeffsProject.failingTestCases(), is( empty() ) );
      assertThat( jeffsProject.getBuildNumber(), is( 0 ) );
      assertThat( jeffsProject.getBuildStatus(), is( BuildResultStatus.NOT_BUILT ) );
      assertThat( jeffsProject.builtOnProperty().get(), is( nullValue() ) );
      assertThat( jeffsProject.testFailureCount().get(), is( 0 ) );
      assertThat( jeffsProject.testSkipCount().get(), is( 0 ) );
      assertThat( jeffsProject.testTotalCount().get(), is( 0 ) );
      assertThat( jeffsProject.totalBuildTimeProperty().get(), is( 0L ) );
   }//End Method

}//End Class
