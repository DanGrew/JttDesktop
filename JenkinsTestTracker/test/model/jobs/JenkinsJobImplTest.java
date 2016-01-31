/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package model.jobs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import api.handling.BuildState;

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
   
   @Ignore
   @Test public void shouldHaveBuildTimeUpdated(){
      //expected and current
      Assert.fail();
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

}//End Class
