/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.jobs;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javafx.beans.value.ChangeListener;
import javafx.util.Pair;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.model.nodes.JenkinsNodeImpl;
import uk.dangrew.jtt.model.tests.TestCase;
import uk.dangrew.jtt.model.tests.TestCaseImpl;
import uk.dangrew.jtt.model.tests.TestClass;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;

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
   
   @Test public void shouldProvideLastBuildProperty() {
      Assert.assertEquals( 
               new Pair<>( JenkinsJob.DEFAULT_LAST_BUILD_NUMBER, JenkinsJob.DEFAULT_LAST_BUILD_STATUS ), 
               systemUnderTest.buildProperty().get() 
      );
      
      assertThat( systemUnderTest.getBuildNumber(), is( JenkinsJob.DEFAULT_LAST_BUILD_NUMBER ) );
      assertThat( systemUnderTest.getBuildStatus(), is( JenkinsJob.DEFAULT_LAST_BUILD_STATUS ) );
   }//End Method
   
   @Test public void shouldUpdateLastBuildNumberProperty() {
      shouldProvideLastBuildProperty();
      
      final int newBuild = 100;
      systemUnderTest.buildProperty().set( new Pair<>( newBuild, JenkinsJob.DEFAULT_LAST_BUILD_STATUS ) );
      Assert.assertEquals( newBuild, systemUnderTest.buildProperty().get().getKey().intValue() );
      assertThat( systemUnderTest.getBuildNumber(), is( newBuild ) );
      
      final BuildResultStatus newStatus = BuildResultStatus.SUCCESS;
      systemUnderTest.buildProperty().set( new Pair<>( newBuild, newStatus ) );
      Assert.assertEquals( newStatus, systemUnderTest.buildProperty().get().getValue() );
      assertThat( systemUnderTest.getBuildStatus(), is( newStatus ) );
   }//End Method
   
   @Test public void shouldUpdateBuildNumberAndStatus(){
      systemUnderTest.setBuildNumber( 2000 );
      assertThat( systemUnderTest.buildProperty().get().getKey(), is( 2000 ) );
      assertThat( systemUnderTest.buildProperty().get().getValue(), is( JenkinsJob.DEFAULT_LAST_BUILD_STATUS ) );
      
      systemUnderTest.setBuildStatus( BuildResultStatus.UNSTABLE );
      assertThat( systemUnderTest.buildProperty().get().getKey(), is( 2000 ) );
      assertThat( systemUnderTest.buildProperty().get().getValue(), is( BuildResultStatus.UNSTABLE ) );
   }//End Method
   
   @Test public void shouldNotifyLastBuildListenersWhenEitherValueChanges(){
      @SuppressWarnings("unchecked") //generic mocking 
      ChangeListener< Pair< Integer, BuildResultStatus > > listener = mock( ChangeListener.class );
      
      systemUnderTest.buildProperty().addListener( listener );
      systemUnderTest.setBuildNumber( 10 );
      verify( listener ).changed( 
               systemUnderTest.buildProperty(), 
               new Pair<>( JenkinsJob.DEFAULT_LAST_BUILD_NUMBER, JenkinsJob.DEFAULT_LAST_BUILD_STATUS ), 
               new Pair<>( 10, JenkinsJob.DEFAULT_LAST_BUILD_STATUS ) 
      );
      systemUnderTest.setBuildStatus( BuildResultStatus.SUCCESS );
      verify( listener ).changed( 
               systemUnderTest.buildProperty(), 
               new Pair<>( 10, JenkinsJob.DEFAULT_LAST_BUILD_STATUS ), 
               new Pair<>( 10, BuildResultStatus.SUCCESS ) 
      );
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
   
   @Test public void shouldProvideCurrentBuildTimestampProperty() {
      assertThat( systemUnderTest.buildTimestampProperty().get(), is( JenkinsJob.DEFAULT_BUILD_TIMESTAMP ) );
   }//End Method
   
   @Test public void shouldUpdateCurrentBuildTimestampProperty() {
      shouldProvideCurrentBuildTimestampProperty();
      
      final long value = 1000;
      systemUnderTest.buildTimestampProperty().set( value );
      assertThat( systemUnderTest.buildTimestampProperty().get() , is( value ) );
   }//End Method
   
   @Test public void shouldProvideLastBuiltOnProperty() {
      Assert.assertEquals( JenkinsJob.DEFAULT_LAST_BUILT_ON, systemUnderTest.builtOnProperty().get() );
   }//End Method
   
   @Test public void shouldUpdateLastBuiltOnProperty() {
      shouldProvideLastBuiltOnProperty();
      
      final JenkinsNode value = new JenkinsNodeImpl( "anything" );
      systemUnderTest.builtOnProperty().set( value );
      Assert.assertEquals( value, systemUnderTest.builtOnProperty().get() );
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
   
   @Test public void shouldProvideFailingTestCases(){
      assertThat( systemUnderTest.failingTestCases(), hasSize( 0 ) );
   }//End Method
   
   @Test public void shouldUpdateTestCases(){
      TestCase firstTestCase = new TestCaseImpl( "first", mock( TestClass.class ) );
      TestCase secondTestCase = new TestCaseImpl( "second", mock( TestClass.class ) );
      
      systemUnderTest.failingTestCases().addAll( firstTestCase, secondTestCase );
      assertThat( systemUnderTest.failingTestCases(), contains( firstTestCase, secondTestCase ) );
   }//End Method
   
   @Test public void shouldProvideTotalBuildTime(){
      assertThat( systemUnderTest.totalBuildTimeProperty().get(), is( JenkinsJob.DEFAULT_TOTAL_BUILD_TIME ) );
      systemUnderTest.totalBuildTimeProperty().set( 1234L );
      assertThat( systemUnderTest.totalBuildTimeProperty().get(), is( 1234L ) );
   }//End Method
   
   @Test public void shouldProvideTestFailureCount(){
      assertThat( systemUnderTest.testFailureCount().get(), is( JenkinsJob.DEFAULT_FAILURE_COUNT ) );
      systemUnderTest.testFailureCount().set( 1234 );
      assertThat( systemUnderTest.testFailureCount().get(), is( 1234 ) );
   }//End Method
   
   @Test public void shouldProvideTestSkipCount(){
      assertThat( systemUnderTest.testSkipCount().get(), is( JenkinsJob.DEFAULT_SKIP_COUNT ) );
      systemUnderTest.testSkipCount().set( 1234 );
      assertThat( systemUnderTest.testSkipCount().get(), is( 1234 ) );
   }//End Method
   
   @Test public void shouldProvideTotalTestCount(){
      assertThat( systemUnderTest.testTotalCount().get(), is( JenkinsJob.DEFAULT_TOTAL_TEST_COUNT ) );
      systemUnderTest.testTotalCount().set( 1234 );
      assertThat( systemUnderTest.testTotalCount().get(), is( 1234 ) );
   }//End Method

}//End Class
