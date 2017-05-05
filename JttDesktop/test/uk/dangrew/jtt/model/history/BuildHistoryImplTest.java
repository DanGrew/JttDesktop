/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.history;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.model.build.ControllableJenkinsBuild;
import uk.dangrew.jtt.model.build.JenkinsBuildImpl;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;

public class BuildHistoryImplTest {

   private ControllableJenkinsBuild build;
   private JenkinsJob job;
   private ControllableBuildHistory systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      build = new JenkinsBuildImpl();
      build.setBuildNumber( 100 );
      
      job = new JenkinsJobImpl( "Job" );
      systemUnderTest = new BuildHistoryImpl( job );
   }//End Method

   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullJenkinJob() {
      new BuildHistoryImpl( null );
   }//End Method
   
   @Test public void shouldProvideBuildsAsPerAdded(){
      systemUnderTest.addBuildHistory( build );
      assertThat( systemUnderTest.builds(), contains( build ) );
   }//End Method
   
   @Test public void shouldProvideAccessToSpecificBuildsByNumber() {
      assertThat( systemUnderTest.getHistoryFor( build.buildNumber() ), is( nullValue() ) );
      
      systemUnderTest.addBuildHistory( build );
      assertThat( systemUnderTest.getHistoryFor( build.buildNumber() ), is( build ) );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAllowBuildWithSameNumberToBeAdded() {
      systemUnderTest.addBuildHistory( build );
      
      ControllableJenkinsBuild anotherBuild = new JenkinsBuildImpl();
      anotherBuild.setBuildNumber( build.buildNumber() );
      systemUnderTest.addBuildHistory( anotherBuild );
   }//End Method
   
   @Test public void shouldIgnoreAddingBuildThatIsAlreadyPresent() {
      systemUnderTest.addBuildHistory( build );
      systemUnderTest.addBuildHistory( build );
      systemUnderTest.addBuildHistory( build );
      assertThat( systemUnderTest.getHistoryFor( build.buildNumber() ), is( build ) );
   }//End Method
   
   @Test public void shouldProvideOrderedIterationThroughBuilds() {
      systemUnderTest.addBuildHistory( build );
      
      ControllableJenkinsBuild earlierBuild = new JenkinsBuildImpl();
      earlierBuild.setBuildNumber( 20 );
      systemUnderTest.addBuildHistory( earlierBuild );
      
      ControllableJenkinsBuild laterBuild = new JenkinsBuildImpl();
      laterBuild.setBuildNumber( 200 );
      systemUnderTest.addBuildHistory( laterBuild );
      
      assertThat( systemUnderTest.builds(), contains( earlierBuild, build, laterBuild ) );
   }//End Method
   
   @Test public void shouldProvideAssociatedJob(){
      assertThat( systemUnderTest.jenkinsJob(), is( job ) );
   }//End Method
   
   @Test public void shouldProvideObservableBuilds(){
      assertThat( systemUnderTest.builds(), is( notNullValue() ) );
   }//End Method
   
   @Test( expected = UnsupportedOperationException.class ) public void shouldNotAllowModificationsToBuildsDirectly(){
      systemUnderTest.builds().add( new JenkinsBuildImpl() );
   }//End Method

}//End Class
