/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.storage.database;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javafx.util.Pair;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.model.nodes.JenkinsNodeImpl;
import uk.dangrew.jtt.model.tests.TestClass;
import uk.dangrew.jtt.model.tests.TestClassImpl;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;
import uk.dangrew.jtt.storage.database.events.JttChangeListener;

/**
 * {@link JenkinsDatabaseImpl} test.
 */
public class JenkinsDatabaseImplTest {

   private static final String TEST_CLASS_NAME = "anything";
   private static final String TEST_CLASS_LOCATION = "anywhere";
   private static final String JENKINS_JOB_NAME = "job name";
   private static final String JENKINS_USER_NAME = "user name";
   private static final String JENKINS_NODE_NAME = "node name";
   private JenkinsDatabase systemUnderTest;
   private TestClass testClass;
   private JenkinsJob jenkinsJob;
   private JenkinsUser jenkinsUser;
   private JenkinsNode jenkinsNode;
   
   /**
    * Method to initialise the {@link JenkinsDatabase} system under test.
    */
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new JenkinsDatabaseImpl();
      testClass = new TestClassImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION );
      jenkinsJob = new JenkinsJobImpl( JENKINS_JOB_NAME );
      jenkinsUser = new JenkinsUserImpl( JENKINS_USER_NAME );
      jenkinsNode = new JenkinsNodeImpl( JENKINS_NODE_NAME );
   }//End Method
   
   @Test public void shouldBeEmptyInitially(){
      Assert.assertTrue( systemUnderTest.hasNoTestClasses() );
      Assert.assertTrue( systemUnderTest.hasNoJenkinsJobs() );
      Assert.assertTrue( systemUnderTest.hasNoJenkinsNodes() );
      
      Assert.assertFalse( systemUnderTest.hasTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
      Assert.assertFalse( systemUnderTest.hasJenkinsJob( JENKINS_JOB_NAME ) );
      Assert.assertFalse( systemUnderTest.hasJenkinsUser( JENKINS_USER_NAME ) );
      Assert.assertFalse( systemUnderTest.hasJenkinsNode( JENKINS_NODE_NAME ) );
   }//End Method
   
   /**
    * Prove that a {@link TestClass} can be stored.
    */
   @Test public void shouldStoreTestClass() {
      systemUnderTest.store( testClass );
      Assert.assertTrue( systemUnderTest.hasTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
      Assert.assertEquals( 1, systemUnderTest.testClasses().size() );
      Assert.assertTrue( systemUnderTest.testClasses().contains( testClass ) );
   }//End Method
   
   /**
    * Prove that a {@link TestClass} can be retrieved.
    */
   @Test public void shouldRetrieveTestClass() {
      systemUnderTest.store( testClass );
      Assert.assertTrue( systemUnderTest.hasTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
      Assert.assertEquals( testClass, systemUnderTest.getTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
   }//End Method
   
   /**
    * Prove that only matching keys are retrieved.
    */
   @Test public void shouldRetrieveOnlyMatchingTestClass() {
      systemUnderTest.store( new TestClassImpl( "something", "elsewhere" ) );
      Assert.assertFalse( systemUnderTest.hasTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
      Assert.assertNull( systemUnderTest.getTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
   }//End Method
   
   /**
    * Prove that storing {@link TestClass}es which match the same key overwrite.
    */
   @Test public void shouldOverwriteDuplicateTestClass() {
      systemUnderTest.store( testClass );
      Assert.assertTrue( systemUnderTest.hasTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
      Assert.assertEquals( 1, systemUnderTest.testClasses().size() );
      Assert.assertTrue( systemUnderTest.testClasses().contains( testClass ) );
      
      TestClass alternate = new TestClassImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION );
      systemUnderTest.store( alternate );
      Assert.assertEquals( alternate, systemUnderTest.getTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
      Assert.assertEquals( 1, systemUnderTest.testClasses().size() );
      Assert.assertTrue( systemUnderTest.testClasses().contains( alternate ) );
   }//End Method
   
   /**
    * Prove that store does not accept null.
    */
   @Test public void shouldNotStoreNullTestClass(){
      TestClass nullClass = null;
      systemUnderTest.store( nullClass );
      Assert.assertTrue( systemUnderTest.hasNoTestClasses() );
   }//End Method
   
   /**
    * Prove that has does not accept null.
    */
   @Test public void shouldNotHasNullTestClass(){
      Assert.assertFalse( systemUnderTest.hasTestClass( null ) );
   }//End Method

   /**
    * Prove that get does not accept null.
    */
   @Test public void shouldNotGetNullTestClass(){
      Assert.assertNull( systemUnderTest.getTestClass( null ) );
   }//End Method
   
   /**
    * Prove that a {@link TestClass} can be removed.
    */
   @Test public void shouldRemoveTestClassUsingKey(){
      systemUnderTest.store( testClass );
      Assert.assertTrue( systemUnderTest.hasTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
      Assert.assertEquals( testClass, systemUnderTest.removeTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
      Assert.assertFalse( systemUnderTest.hasTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
      Assert.assertEquals( 0, systemUnderTest.testClasses().size() );
   }//End Method
   
   @Test public void shouldRemoveTestClassUsingInstance(){
      systemUnderTest.store( testClass );
      Assert.assertTrue( systemUnderTest.hasTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
      Assert.assertTrue( systemUnderTest.removeTestClass( testClass ) );
      Assert.assertFalse( systemUnderTest.hasTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
      Assert.assertEquals( 0, systemUnderTest.testClasses().size() );
   }//End Method
   
   @Test public void shouldNotRemoveTestClassUsingInstance(){
      Assert.assertFalse( systemUnderTest.hasTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
      Assert.assertFalse( systemUnderTest.removeTestClass( testClass ) );
   }//End Method
   
   /**
    * Prove that nothing happens when there is nothing to remove.
    */
   @Test public void shouldNotRemoveTestClassIfNonePresent(){
      Assert.assertFalse( systemUnderTest.hasTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
      Assert.assertNull( systemUnderTest.removeTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
      Assert.assertFalse( systemUnderTest.hasTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
      Assert.assertEquals( 0, systemUnderTest.testClasses().size() );
   }//End Method
   
   /**
    * Prove that nothing is removed when no mathcing {@link TestClass} is found.
    */
   @Test public void shouldNotRemoveTestClassIfNotPresent(){
      systemUnderTest.store( testClass );
      Assert.assertTrue( systemUnderTest.hasTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
      Assert.assertNull( systemUnderTest.removeTestClass( new TestClassKeyImpl( "something", "else" ) ) );
      Assert.assertTrue( systemUnderTest.hasTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
      Assert.assertEquals( 1, systemUnderTest.testClasses().size() );
      Assert.assertTrue( systemUnderTest.testClasses().contains( testClass ) );
   }//End Method
   
   @Test public void shouldNotRemoveNullTestClassWithKey(){
      TestClassKey testClass = null;
      Assert.assertNull( systemUnderTest.removeTestClass( testClass ) );
   }//End Method
   
   @Test public void shouldNotRemoveNullTestClassWithInstance(){
      TestClass testClass = null;
      Assert.assertFalse( systemUnderTest.removeTestClass( testClass ) );
   }//End Method
   
   @Test public void shouldProvideJenkinsJobs(){
      Assert.assertNotNull( systemUnderTest.jenkinsJobs() );
      Assert.assertTrue( systemUnderTest.jenkinsJobs().isEmpty() );
      JenkinsJob anotherJob = new JenkinsJobImpl( "some random job" );
      systemUnderTest.store( anotherJob );
      Assert.assertFalse( systemUnderTest.jenkinsJobs().isEmpty() );
      Assert.assertTrue( systemUnderTest.jenkinsJobs().contains( anotherJob ) );
   }//End Method
   
   @Test public void shouldStoreJenkinsJob() {
      systemUnderTest.store( jenkinsJob );
      Assert.assertTrue( systemUnderTest.hasJenkinsJob( JENKINS_JOB_NAME ) );
      Assert.assertEquals( 1, systemUnderTest.jenkinsJobs().size() );
      Assert.assertTrue( systemUnderTest.jenkinsJobs().contains( jenkinsJob ) );
   }//End Method
   
   @Test public void shouldRetrieveJenkinsJob() {
      systemUnderTest.store( jenkinsJob );
      Assert.assertTrue( systemUnderTest.hasJenkinsJob( JENKINS_JOB_NAME ) );
      Assert.assertEquals( jenkinsJob, systemUnderTest.getJenkinsJob( JENKINS_JOB_NAME ) );
   }//End Method
   
   @Test public void shouldRetrieveOnlyMatchingJenkinsJob() {
      systemUnderTest.store( new JenkinsJobImpl( "something else" ) );
      Assert.assertFalse( systemUnderTest.hasJenkinsJob( JENKINS_JOB_NAME ) );
      Assert.assertNull( systemUnderTest.getJenkinsJob( JENKINS_JOB_NAME ) );
   }//End Method
   
   @Test public void shouldOverwriteDuplicateJenkinsJob() {
      systemUnderTest.store( jenkinsJob );
      Assert.assertTrue( systemUnderTest.hasJenkinsJob( JENKINS_JOB_NAME ) );
      Assert.assertEquals( 1, systemUnderTest.jenkinsJobs().size() );
      Assert.assertTrue( systemUnderTest.jenkinsJobs().contains( jenkinsJob ) );
      
      JenkinsJob alternate = new JenkinsJobImpl( JENKINS_JOB_NAME );
      systemUnderTest.store( alternate );
      Assert.assertEquals( alternate, systemUnderTest.getJenkinsJob( JENKINS_JOB_NAME ) );
      Assert.assertEquals( 1, systemUnderTest.jenkinsJobs().size() );
      Assert.assertTrue( systemUnderTest.jenkinsJobs().contains( alternate ) );
   }//End Method
   
   @Test public void shouldNotStoreNullJenkinsJob(){
      JenkinsJob nullJob = null;
      systemUnderTest.store( nullJob );
      Assert.assertTrue( systemUnderTest.hasNoJenkinsJobs() );
   }//End Method
   
   @Test public void shouldNotHasNullJenkinsJob(){
      Assert.assertFalse( systemUnderTest.hasJenkinsJob( null ) );
   }//End Method

   @Test public void shouldNotGetNullJenkinsJob(){
      Assert.assertNull( systemUnderTest.getJenkinsJob( null ) );
   }//End Method
   
   @Test public void shouldRemoveJenkinsJobUsingKey(){
      systemUnderTest.store( jenkinsJob );
      Assert.assertTrue( systemUnderTest.hasJenkinsJob( JENKINS_JOB_NAME ) );
      Assert.assertEquals( jenkinsJob, systemUnderTest.removeJenkinsJob( JENKINS_JOB_NAME ) );
      Assert.assertFalse( systemUnderTest.hasJenkinsJob( JENKINS_JOB_NAME ) );
      Assert.assertEquals( 0, systemUnderTest.jenkinsJobs().size() );
   }//End Method
   
   @Test public void shouldRemoveJenkinsJobUsinginstance(){
      systemUnderTest.store( jenkinsJob );
      Assert.assertTrue( systemUnderTest.hasJenkinsJob( JENKINS_JOB_NAME ) );
      Assert.assertTrue( systemUnderTest.removeJenkinsJob( jenkinsJob ) );
      Assert.assertFalse( systemUnderTest.hasJenkinsJob( JENKINS_JOB_NAME ) );
      Assert.assertEquals( 0, systemUnderTest.jenkinsJobs().size() );
   }//End Method
   
   @Test public void shouldNotRemoveJenkinsJobUsinginstance(){
      Assert.assertFalse( systemUnderTest.removeJenkinsJob( jenkinsJob ) );
      Assert.assertFalse( systemUnderTest.hasJenkinsJob( JENKINS_JOB_NAME ) );
   }//End Method
   
   @Test public void shouldNotRemoveJenkinsJobIfNonePresent(){
      Assert.assertFalse( systemUnderTest.hasJenkinsJob( JENKINS_JOB_NAME ) );
      Assert.assertNull( systemUnderTest.removeJenkinsJob( JENKINS_JOB_NAME ) );
      Assert.assertFalse( systemUnderTest.hasJenkinsJob( JENKINS_JOB_NAME ) );
      Assert.assertEquals( 0, systemUnderTest.jenkinsJobs().size() );
   }//End Method
   
   @Test public void shouldNotRemoveJenkinsJobIfNotPresent(){
      systemUnderTest.store( jenkinsJob );
      Assert.assertTrue( systemUnderTest.hasJenkinsJob( JENKINS_JOB_NAME ) );
      Assert.assertNull( systemUnderTest.removeJenkinsJob( "something not defined" ) );
      Assert.assertTrue( systemUnderTest.hasJenkinsJob( JENKINS_JOB_NAME ) );
      Assert.assertEquals( 1, systemUnderTest.jenkinsJobs().size() );
      Assert.assertTrue( systemUnderTest.jenkinsJobs().contains( jenkinsJob ) );
   }//End Method
   
   @Test public void shouldNotRemoveNullJenkinsJobWithKey(){
      String name = null;
      Assert.assertNull( systemUnderTest.removeJenkinsJob( name ) );
   }//End Method
   
   @Test public void shouldNotRemoveNullJenkinsJobWithInstance(){
      JenkinsJob job = null;
      Assert.assertFalse( systemUnderTest.removeJenkinsJob( job ) );
   }//End Method
   
   @Test public void shouldNotStoreJenkinsJobWithNoName(){
      jenkinsJob.nameProperty().set( null );
      systemUnderTest.store( jenkinsJob );
      Assert.assertTrue( systemUnderTest.hasNoJenkinsJobs() );
      Assert.assertTrue( systemUnderTest.jenkinsJobs().isEmpty() );
      Assert.assertFalse( systemUnderTest.hasJenkinsJob( null ) );
      Assert.assertNull( systemUnderTest.getJenkinsJob( null ) );
   }//End Method   
   
   @Test public void shouldContainTestClasses(){
      systemUnderTest.store( testClass );
      Assert.assertTrue( systemUnderTest.containsTestClass( testClass ) );
   }//End Method
   
   @Test public void shouldContainJenkinsJob(){
      systemUnderTest.store( jenkinsJob );
      Assert.assertTrue( systemUnderTest.containsJenkinsJob( jenkinsJob ) );
   }//End Method
   
   @Test public void shouldProvideJenkinsJobPropertyListener(){
      assertThat( systemUnderTest.jenkinsJobProperties(), notNullValue() );
      
      @SuppressWarnings("unchecked") //simply mocking genericized objects. 
      JttChangeListener< JenkinsJob, Pair< Integer, BuildResultStatus > > listener = mock( JttChangeListener.class );
      
      systemUnderTest.store( jenkinsJob );
      systemUnderTest.jenkinsJobProperties().addBuildResultStatusListener( listener );
      jenkinsJob.setBuildStatus( BuildResultStatus.SUCCESS );
      
      verify( listener ).changed( 
               jenkinsJob, 
               new Pair<>( JenkinsJob.DEFAULT_LAST_BUILD_NUMBER, BuildResultStatus.NOT_BUILT ), 
               new Pair<>( JenkinsJob.DEFAULT_LAST_BUILD_NUMBER, BuildResultStatus.SUCCESS )
      );
   }//End Method
   
   @Test public void shouldProvideJenkinsUserPropertyListener(){
      assertThat( systemUnderTest.jenkinsUserProperties(), notNullValue() );
      
      @SuppressWarnings("unchecked") //simply mocking genericized objects. 
      JttChangeListener< JenkinsUser, String > listener = mock( JttChangeListener.class );
      
      systemUnderTest.store( jenkinsUser );
      systemUnderTest.jenkinsUserProperties().addNamePropertyListener( listener );
      jenkinsUser.nameProperty().set( "soemthing else for this test" );
      
      verify( listener ).changed( jenkinsUser, JENKINS_USER_NAME, "soemthing else for this test" );
   }//End Method
   
   @Test public void shouldProvideJenkinsUsers(){
      Assert.assertNotNull( systemUnderTest.jenkinsUsers() );
      Assert.assertTrue( systemUnderTest.jenkinsUsers().isEmpty() );
      JenkinsUser anotherUser = new JenkinsUserImpl( "some random user" );
      systemUnderTest.store( anotherUser );
      Assert.assertFalse( systemUnderTest.jenkinsUsers().isEmpty() );
      Assert.assertTrue( systemUnderTest.jenkinsUsers().contains( anotherUser ) );
   }//End Method
   
   @Test public void shouldStoreJenkinsUser() {
      systemUnderTest.store( jenkinsUser );
      Assert.assertTrue( systemUnderTest.hasJenkinsUser( JENKINS_USER_NAME ) );
      Assert.assertEquals( 1, systemUnderTest.jenkinsUsers().size() );
      Assert.assertTrue( systemUnderTest.jenkinsUsers().contains( jenkinsUser ) );
   }//End Method
   
   @Test public void shouldRetrieveJenkinsUser() {
      systemUnderTest.store( jenkinsUser );
      Assert.assertTrue( systemUnderTest.hasJenkinsUser( JENKINS_USER_NAME ) );
      Assert.assertEquals( jenkinsUser, systemUnderTest.getJenkinsUser( JENKINS_USER_NAME ) );
   }//End Method
   
   @Test public void shouldRetrieveOnlyMatchingJenkinsUser() {
      systemUnderTest.store( new JenkinsUserImpl( "something else" ) );
      Assert.assertFalse( systemUnderTest.hasJenkinsUser( JENKINS_USER_NAME ) );
      Assert.assertNull( systemUnderTest.getJenkinsUser( JENKINS_USER_NAME ) );
   }//End Method
   
   @Test public void shouldOverwriteDuplicateJenkinsUser() {
      systemUnderTest.store( jenkinsUser );
      Assert.assertTrue( systemUnderTest.hasJenkinsUser( JENKINS_USER_NAME ) );
      Assert.assertEquals( 1, systemUnderTest.jenkinsUsers().size() );
      Assert.assertTrue( systemUnderTest.jenkinsUsers().contains( jenkinsUser ) );
      
      JenkinsUser alternate = new JenkinsUserImpl( JENKINS_USER_NAME );
      systemUnderTest.store( alternate );
      Assert.assertEquals( alternate, systemUnderTest.getJenkinsUser( JENKINS_USER_NAME ) );
      Assert.assertEquals( 1, systemUnderTest.jenkinsUsers().size() );
      Assert.assertTrue( systemUnderTest.jenkinsUsers().contains( alternate ) );
   }//End Method
   
   @Test public void shouldNotStoreNullJenkinsUser(){
      JenkinsUser nullUser = null;
      systemUnderTest.store( nullUser );
      Assert.assertTrue( systemUnderTest.hasNoJenkinsUsers() );
   }//End Method
   
   @Test public void shouldNotHasNullJenkinsUser(){
      Assert.assertFalse( systemUnderTest.hasJenkinsUser( null ) );
   }//End Method

   @Test public void shouldNotGetNullJenkinsUser(){
      Assert.assertNull( systemUnderTest.getJenkinsUser( null ) );
   }//End Method
   
   @Test public void shouldRemoveJenkinsUserUsingKey(){
      systemUnderTest.store( jenkinsUser );
      Assert.assertTrue( systemUnderTest.hasJenkinsUser( JENKINS_USER_NAME ) );
      Assert.assertEquals( jenkinsUser, systemUnderTest.removeJenkinsUser( JENKINS_USER_NAME ) );
      Assert.assertFalse( systemUnderTest.hasJenkinsUser( JENKINS_USER_NAME ) );
      Assert.assertEquals( 0, systemUnderTest.jenkinsUsers().size() );
   }//End Method
   
   @Test public void shouldRemoveJenkinsUserUsingInstance(){
      systemUnderTest.store( jenkinsUser );
      Assert.assertTrue( systemUnderTest.hasJenkinsUser( JENKINS_USER_NAME ) );
      Assert.assertTrue( systemUnderTest.removeJenkinsUser( jenkinsUser ) );
      Assert.assertFalse( systemUnderTest.hasJenkinsUser( JENKINS_USER_NAME ) );
      Assert.assertEquals( 0, systemUnderTest.jenkinsUsers().size() );
   }//End Method
   
   @Test public void shouldNotRemoveJenkinsUserUsingInstance(){
      Assert.assertFalse( systemUnderTest.removeJenkinsUser( jenkinsUser ) );
      Assert.assertFalse( systemUnderTest.hasJenkinsUser( JENKINS_USER_NAME ) );
   }//End Method
   
   @Test public void shouldNotRemoveJenkinsUserIfNonePresent(){
      Assert.assertFalse( systemUnderTest.hasJenkinsUser( JENKINS_USER_NAME ) );
      Assert.assertNull( systemUnderTest.removeJenkinsUser( JENKINS_USER_NAME ) );
      Assert.assertFalse( systemUnderTest.hasJenkinsUser( JENKINS_USER_NAME ) );
      Assert.assertEquals( 0, systemUnderTest.jenkinsUsers().size() );
   }//End Method
   
   @Test public void shouldNotRemoveJenkinsUserIfNotPresent(){
      systemUnderTest.store( jenkinsUser );
      Assert.assertTrue( systemUnderTest.hasJenkinsUser( JENKINS_USER_NAME ) );
      Assert.assertNull( systemUnderTest.removeJenkinsUser( "something not defined" ) );
      Assert.assertTrue( systemUnderTest.hasJenkinsUser( JENKINS_USER_NAME ) );
      Assert.assertEquals( 1, systemUnderTest.jenkinsUsers().size() );
      Assert.assertTrue( systemUnderTest.jenkinsUsers().contains( jenkinsUser ) );
   }//End Method
   
   @Test public void shouldNotRemoveNullJenkinsUserWithKey(){
      String name = null;
      Assert.assertNull( systemUnderTest.removeJenkinsUser( name ) );
   }//End Method
   
   @Test public void shouldNotRemoveNullJenkinsUserWithInstance(){
      JenkinsUser user = null;
      Assert.assertFalse( systemUnderTest.removeJenkinsUser( user ) );
   }//End Method
   
   @Test public void shouldNotStoreJenkinsUserWithNoName(){
      jenkinsUser.nameProperty().set( null );
      systemUnderTest.store( jenkinsUser );
      Assert.assertTrue( systemUnderTest.hasNoJenkinsUsers() );
      Assert.assertTrue( systemUnderTest.jenkinsUsers().isEmpty() );
      Assert.assertFalse( systemUnderTest.hasJenkinsUser( null ) );
      Assert.assertNull( systemUnderTest.getJenkinsUser( null ) );
   }//End Method   
   
   @Test public void shouldContainJenkinsUser(){
      systemUnderTest.store( jenkinsUser );
      Assert.assertTrue( systemUnderTest.containsJenkinsUser( jenkinsUser ) );
   }//End Method
   
   @Test public void shouldProvideJenkinsNodes(){
      Assert.assertNotNull( systemUnderTest.jenkinsNodes() );
      Assert.assertTrue( systemUnderTest.jenkinsNodes().isEmpty() );
      JenkinsNode anotherUser = new JenkinsNodeImpl( "some random user" );
      systemUnderTest.store( anotherUser );
      Assert.assertFalse( systemUnderTest.jenkinsNodes().isEmpty() );
      Assert.assertTrue( systemUnderTest.jenkinsNodes().contains( anotherUser ) );
   }//End Method
   
   @Test public void shouldStoreJenkinsNode() {
      systemUnderTest.store( jenkinsNode );
      Assert.assertTrue( systemUnderTest.hasJenkinsNode( JENKINS_NODE_NAME ) );
      Assert.assertEquals( 1, systemUnderTest.jenkinsNodes().size() );
      Assert.assertTrue( systemUnderTest.jenkinsNodes().contains( jenkinsNode ) );
   }//End Method
   
   @Test public void shouldRetrieveJenkinsNode() {
      systemUnderTest.store( jenkinsNode );
      Assert.assertTrue( systemUnderTest.hasJenkinsNode( JENKINS_NODE_NAME ) );
      Assert.assertEquals( jenkinsNode, systemUnderTest.getJenkinsNode( JENKINS_NODE_NAME ) );
   }//End Method
   
   @Test public void shouldRetrieveOnlyMatchingJenkinsNode() {
      systemUnderTest.store( new JenkinsNodeImpl( "something else" ) );
      Assert.assertFalse( systemUnderTest.hasJenkinsNode( JENKINS_NODE_NAME ) );
      Assert.assertNull( systemUnderTest.getJenkinsNode( JENKINS_NODE_NAME ) );
   }//End Method
   
   @Test public void shouldOverwriteDuplicateJenkinsNode() {
      systemUnderTest.store( jenkinsNode );
      Assert.assertTrue( systemUnderTest.hasJenkinsNode( JENKINS_NODE_NAME ) );
      Assert.assertEquals( 1, systemUnderTest.jenkinsNodes().size() );
      Assert.assertTrue( systemUnderTest.jenkinsNodes().contains( jenkinsNode ) );
      
      JenkinsNode alternate = new JenkinsNodeImpl( JENKINS_NODE_NAME );
      systemUnderTest.store( alternate );
      Assert.assertEquals( alternate, systemUnderTest.getJenkinsNode( JENKINS_NODE_NAME ) );
      Assert.assertEquals( 1, systemUnderTest.jenkinsNodes().size() );
      Assert.assertTrue( systemUnderTest.jenkinsNodes().contains( alternate ) );
   }//End Method
   
   @Test public void shouldNotStoreNullJenkinsNode(){
      JenkinsNode nullUser = null;
      systemUnderTest.store( nullUser );
      Assert.assertTrue( systemUnderTest.hasNoJenkinsNodes() );
   }//End Method
   
   @Test public void shouldNotHasNullJenkinsNode(){
      Assert.assertFalse( systemUnderTest.hasJenkinsNode( null ) );
   }//End Method

   @Test public void shouldNotGetNullJenkinsNode(){
      Assert.assertNull( systemUnderTest.getJenkinsNode( null ) );
   }//End Method
   
   @Test public void shouldRemoveJenkinsNodeUsingKey(){
      systemUnderTest.store( jenkinsNode );
      Assert.assertTrue( systemUnderTest.hasJenkinsNode( JENKINS_NODE_NAME ) );
      Assert.assertEquals( jenkinsNode, systemUnderTest.removeJenkinsNode( JENKINS_NODE_NAME ) );
      Assert.assertFalse( systemUnderTest.hasJenkinsNode( JENKINS_NODE_NAME ) );
      Assert.assertEquals( 0, systemUnderTest.jenkinsNodes().size() );
   }//End Method
   
   @Test public void shouldRemoveJenkinsNodeUsingInstance(){
      systemUnderTest.store( jenkinsNode );
      Assert.assertTrue( systemUnderTest.hasJenkinsNode( JENKINS_NODE_NAME ) );
      Assert.assertTrue( systemUnderTest.removeJenkinsNode( jenkinsNode ) );
      Assert.assertFalse( systemUnderTest.hasJenkinsNode( JENKINS_NODE_NAME ) );
      Assert.assertEquals( 0, systemUnderTest.jenkinsNodes().size() );
   }//End Method
   
   @Test public void shouldNotRemoveJenkinsNodeUsingInstance(){
      Assert.assertFalse( systemUnderTest.removeJenkinsNode( jenkinsNode ) );
      Assert.assertFalse( systemUnderTest.hasJenkinsNode( JENKINS_NODE_NAME ) );
   }//End Method
   
   @Test public void shouldNotRemoveJenkinsNodeIfNonePresent(){
      Assert.assertFalse( systemUnderTest.hasJenkinsNode( JENKINS_NODE_NAME ) );
      Assert.assertNull( systemUnderTest.removeJenkinsNode( JENKINS_NODE_NAME ) );
      Assert.assertFalse( systemUnderTest.hasJenkinsNode( JENKINS_NODE_NAME ) );
      Assert.assertEquals( 0, systemUnderTest.jenkinsNodes().size() );
   }//End Method
   
   @Test public void shouldNotRemoveJenkinsNodeIfNotPresent(){
      systemUnderTest.store( jenkinsNode );
      Assert.assertTrue( systemUnderTest.hasJenkinsNode( JENKINS_NODE_NAME ) );
      Assert.assertNull( systemUnderTest.removeJenkinsNode( "something not defined" ) );
      Assert.assertTrue( systemUnderTest.hasJenkinsNode( JENKINS_NODE_NAME ) );
      Assert.assertEquals( 1, systemUnderTest.jenkinsNodes().size() );
      Assert.assertTrue( systemUnderTest.jenkinsNodes().contains( jenkinsNode ) );
   }//End Method
   
   @Test public void shouldNotRemoveNullJenkinsNodeWithKey(){
      String name = null;
      Assert.assertNull( systemUnderTest.removeJenkinsNode( name ) );
   }//End Method
   
   @Test public void shouldNotRemoveNullJenkinsNodeWithInstance(){
      JenkinsNode user = null;
      Assert.assertFalse( systemUnderTest.removeJenkinsNode( user ) );
   }//End Method
   
   @Test public void shouldNotStoreJenkinsNodeWithNoName(){
      jenkinsNode.nameProperty().set( null );
      systemUnderTest.store( jenkinsNode );
      Assert.assertTrue( systemUnderTest.hasNoJenkinsNodes() );
      Assert.assertTrue( systemUnderTest.jenkinsNodes().isEmpty() );
      Assert.assertFalse( systemUnderTest.hasJenkinsNode( null ) );
      Assert.assertNull( systemUnderTest.getJenkinsNode( null ) );
   }//End Method   
   
   @Test public void shouldContainJenkinsNode(){
      systemUnderTest.store( jenkinsNode );
      Assert.assertTrue( systemUnderTest.containsJenkinsNode( jenkinsNode ) );
   }//End Method
}//End Class
