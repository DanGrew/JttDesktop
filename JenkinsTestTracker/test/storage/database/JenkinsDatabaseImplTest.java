/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package storage.database;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import model.tests.TestClass;
import model.tests.TestClassImpl;

/**
 * {@link JenkinsDatabaseImpl} test.
 */
public class JenkinsDatabaseImplTest {

   private static final String TEST_CLASS_NAME = "anything";
   private static final String TEST_CLASS_LOCATION = "anywhere";
   private static final String JENKINS_JOB_NAME = "job name";
   private JenkinsDatabase systemUnderTest;
   private TestClass testClass;
   private JenkinsJob jenkinsJob;
   
   /**
    * Method to initialise the {@link JenkinsDatabase} system under test.
    */
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new JenkinsDatabaseImpl();
      Assert.assertTrue( systemUnderTest.hasNoTestClasses() );
      Assert.assertTrue( systemUnderTest.hasNoJenkinsJobs() );
      
      testClass = new TestClassImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION );
      Assert.assertFalse( systemUnderTest.hasTestClass( new TestClassKeyImpl( TEST_CLASS_NAME, TEST_CLASS_LOCATION ) ) );
      jenkinsJob = new JenkinsJobImpl( JENKINS_JOB_NAME );
      Assert.assertFalse( systemUnderTest.hasJenkinsJob( JENKINS_JOB_NAME ) );
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
}//End Class
