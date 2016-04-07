/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package model.jobs;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import api.handling.BuildState;
import model.users.JenkinsUser;
import model.users.JenkinsUserImpl;

/**
 * {@link JenkinsJobImpl} test.
 */
public class JenkinsJobImplTest {
   
   private static final String JENKINS_JOB_NAME = "anyName";
   private JenkinsJob systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new JenkinsJobImpl( JENKINS_JOB_NAME );
   }//End Method

   @Test public void shouldProvideNameProperty() {
      Assert.assertEquals( JENKINS_JOB_NAME, systemUnderTest.nameProperty().get() );
   }//End Method
   
   @Test public void shouldUpdateNameProperty() {
      shouldProvideNameProperty();
      
      final String newName = "any other name";
      systemUnderTest.nameProperty().set( newName );
      Assert.assertEquals( newName, systemUnderTest.nameProperty().get() );
   }//End Method
   
   @Test public void shouldProvideLastBuildNumberProperty() {
      Assert.assertEquals( JenkinsJob.DEFAULT_LAST_BUILD_NUMBER, systemUnderTest.lastBuildNumberProperty().get() );
   }//End Method
   
   @Test public void shouldUpdateLastBuildNumberProperty() {
      shouldProvideLastBuildNumberProperty();
      
      final int newBuild = 100;
      systemUnderTest.lastBuildNumberProperty().set( newBuild );
      Assert.assertEquals( newBuild, systemUnderTest.lastBuildNumberProperty().get() );
   }//End Method
   
   @Test public void shouldProvideLastBuildStatusProperty() {
      Assert.assertEquals( JenkinsJob.DEFAULT_LAST_BUILD_STATUS, systemUnderTest.lastBuildStatusProperty().get() );
   }//End Method
   
   @Test public void shouldUpdateLastBuildStatusProperty() {
      shouldProvideLastBuildStatusProperty();
      
      final BuildResultStatus newStatus = BuildResultStatus.SUCCESS;
      systemUnderTest.lastBuildStatusProperty().set( newStatus );
      Assert.assertEquals( newStatus, systemUnderTest.lastBuildStatusProperty().get() );
   }//End Method
   
   @Test public void shouldProvideBuildStateProperty() {
      Assert.assertEquals( JenkinsJob.DEFAULT_BUILD_STATE, systemUnderTest.buildStateProperty().get() );
   }//End Method
   
   @Test public void shouldUpdateBuildStateProperty() {
      shouldProvideBuildStateProperty();
      
      final BuildState newStatus = BuildState.Building;
      systemUnderTest.buildStateProperty().set( newStatus );
      Assert.assertEquals( newStatus, systemUnderTest.buildStateProperty().get() );
   }//End Method
   
   @Test public void shouldProvideExpectedBuildTimeProperty() {
      Assert.assertEquals( JenkinsJob.DEFAULT_EXPECTED_BUILD_TIME, systemUnderTest.expectedBuildTimeProperty().get() );
   }//End Method
   
   @Test public void shouldUpdateExpectedBuildTimeProperty() {
      shouldProvideExpectedBuildTimeProperty();
      
      final int value = 1000;
      systemUnderTest.expectedBuildTimeProperty().set( value );
      Assert.assertEquals( value, systemUnderTest.expectedBuildTimeProperty().get() );
   }//End Method
   
   @Test public void shouldProvideCurrentBuildTimeProperty() {
      Assert.assertEquals( JenkinsJob.DEFAULT_CURRENT_BUILD_TIME, systemUnderTest.currentBuildTimeProperty().get() );
   }//End Method
   
   @Test public void shouldUpdateCurrentBuildTimeProperty() {
      shouldProvideCurrentBuildTimeProperty();
      
      final int value = 1000;
      systemUnderTest.currentBuildTimeProperty().set( value );
      Assert.assertEquals( value, systemUnderTest.currentBuildTimeProperty().get() );
   }//End Method
   
   @Test public void shouldProvideLastBuildTimestampProperty() {
      Assert.assertEquals( JenkinsJob.DEFAULT_BUILD_TIMESTAMP, systemUnderTest.lastBuildTimestampProperty().get() );
   }//End Method
   
   @Test public void shouldUpdateLastBuildTimestampProperty() {
      shouldProvideLastBuildTimestampProperty();
      
      final int value = 1000;
      systemUnderTest.lastBuildTimestampProperty().set( value );
      Assert.assertEquals( value, systemUnderTest.lastBuildTimestampProperty().get() );
   }//End Method
   
   @Test public void shouldProvideTestResultsAreSynchronizedProperty() {
      assertThat( systemUnderTest.testResultsAreSynchronizedProperty().get(), is( JenkinsJob.DEFAULT_TEST_RESULTS_ARE_SYNC ) );
   }//End Method
   
   @Test public void shouldUpdateTestResultsAreSynchronizedProperty() {
      shouldProvideTestResultsAreSynchronizedProperty();
      
      final boolean value = !JenkinsJob.DEFAULT_TEST_RESULTS_ARE_SYNC;
      systemUnderTest.testResultsAreSynchronizedProperty().set( value );
      assertThat( systemUnderTest.testResultsAreSynchronizedProperty().get(), is( value ) );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectNullNameInConstructor(){
      new JenkinsJobImpl( null );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectEmptyNameInConstructor(){
      new JenkinsJobImpl( "" );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectSpacesOnlyNameInConstructor(){
      new JenkinsJobImpl( "    " );
   }//End Method
   
   @Test public void shouldProvideCulprits() {
      assertThat( systemUnderTest.culprits(), hasSize( 0 ) );
   }//End Method
   
   @Test public void shouldUpdateCulprits() {
      shouldProvideCulprits();

      JenkinsUser user1 = new JenkinsUserImpl( "Frist User" );
      JenkinsUser user2 = new JenkinsUserImpl( "Second User" );
      systemUnderTest.culprits().add( user1 );
      systemUnderTest.culprits().add( user2 );
      assertThat( systemUnderTest.culprits(), hasItems( user1, user2 ) );
   }//End Method

}//End Class
