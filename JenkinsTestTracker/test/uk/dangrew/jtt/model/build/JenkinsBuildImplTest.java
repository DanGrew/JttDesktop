/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.build;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;

public class JenkinsBuildImplTest {

   private ControllableJenkinsBuild systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new JenkinsBuildImpl();
   }//End Method
   
   @Test public void shouldProvideBuildNumber(){
      assertThat( systemUnderTest.duration(), is( 0L ) );
      systemUnderTest.setBuildNumber( 1000 );
      assertThat( systemUnderTest.buildNumber(), is( 1000 ) );
   }//End Method

   @Test public void shouldProvideTestResults(){
      assertThat( systemUnderTest.testResults(), is( nullValue() ) );
      TestResults results = mock( TestResults.class );
      systemUnderTest.setTestResults( results );
      assertThat( systemUnderTest.testResults(), is( results ) );
   }//End Method
   
   @Test public void shouldProvideDuration(){
      assertThat( systemUnderTest.duration(), is( 0L ) );
      systemUnderTest.setDuration( 1000 );
      assertThat( systemUnderTest.duration(), is( 1000L ) );
   }//End Method
   
   @Test public void shouldProvideEstimate(){
      assertThat( systemUnderTest.estimate(), is( 0L ) );
      systemUnderTest.setEstimate( 394875 );
      assertThat( systemUnderTest.estimate(), is( 394875L ) );
   }//End Method
   
   @Test public void shouldProvideResult(){
      assertThat( systemUnderTest.result(), is( nullValue() ) );
      systemUnderTest.setResult( BuildResultStatus.FAILURE );
      assertThat( systemUnderTest.result(), is( BuildResultStatus.FAILURE ) );
   }//End Method
   
   @Test public void shouldProvideTimestamp(){
      assertThat( systemUnderTest.timestamp(), is( 0L ) );
      systemUnderTest.setTimestamp( 83756 );
      assertThat( systemUnderTest.timestamp(), is( 83756L ) );
   }//End Method
   
   @Test public void shouldProvideBuiltOn(){
      assertThat( systemUnderTest.builtOn(), is( nullValue() ) );
      JenkinsNode node = mock( JenkinsNode.class );
      systemUnderTest.setBuiltOn( node );
      assertThat( systemUnderTest.builtOn(), is( node ) );
   }//End Method
   
   @Test public void shouldProvideCulprits(){
      assertThat( systemUnderTest.culprits(), is( empty() ) );
      List< JenkinsUser > culprits = new ArrayList<>();
      culprits.add( new JenkinsUserImpl( "User1" ) );
      culprits.add( new JenkinsUserImpl( "User2" ) );
      culprits.add( new JenkinsUserImpl( "User3" ) );
      systemUnderTest.setCulprits( culprits );
      assertThat( systemUnderTest.culprits().containsAll( culprits ), is( true ) );
      systemUnderTest.setCulprits( new ArrayList<>() );
      assertThat( systemUnderTest.culprits(), is( empty() ) );
   }//End Method
   
   @Test( expected = UnsupportedOperationException.class ) public void shouldProvideNonModifiableCulprits(){
      systemUnderTest.culprits().add( new JenkinsUserImpl( "anything" ) );
   }//End Method

}//End Class
