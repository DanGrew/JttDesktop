/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package data.json.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import data.JsonTestResultsImporter;
import data.json.tests.JsonTestResultsImporterImpl;
import model.tests.TestCase;
import model.tests.TestClass;
import model.tests.TestResultStatus;
import storage.database.JenkinsDatabase;
import storage.database.JenkinsDatabaseImpl;
import storage.database.TestClassKeyImpl;
import utility.TestCommon;

/**
 * {@link JsonTestResultsImporterImpl} test.
 */
public class JsonTestResultImporterTest {

   private static final String CLASS_LOCATION = "architecture.data";
   private static final String CLASS_NAME = "DataManagerTest";

   /** Convenient way of representing the items being asserted.**/
   private enum AssertableProperty {
      ClassDuration,
      TestCases,
      CaseName,
      CaseDuration,
      Status,
      Skipped,
      Age,
      TestClass
   }//End Enum
   
   private JenkinsDatabase database;
   private JsonTestResultsImporter systemUnderTest;
   
   /**
    * Method to initialise the {@link JsonTestResultsImporterImpl} being tested.
    */
   @Before public void initialiseSystemUnderTest() throws IOException{
      database = new JenkinsDatabaseImpl();
      systemUnderTest = new JsonTestResultsImporterImpl( database );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectNullDatabase(){
      systemUnderTest = new JsonTestResultsImporterImpl( null );
   }//End Method
   
   @Test public void shouldImportSingleTestCase() {
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case.json" );
      systemUnderTest.updateTestResults( input );
      assertTestClassAndTestCasePresent( new ArrayList<>() );
   }//End Method
   
   @Test public void shouldNotDuplicateImportSingleTestCase() {
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case.json" );
      systemUnderTest.updateTestResults( input );
      assertTestClassAndTestCasePresent( new ArrayList<>() );
      
      TestClass firstImported = database.testClasses().get( 0 );
      Assert.assertNotNull( firstImported );
      Assert.assertEquals( 1, database.testClasses().size() );
      
      systemUnderTest.updateTestResults( input );
      
      Assert.assertEquals( 1, database.testClasses().size() );
      TestClass secondImported = database.testClasses().get( 0 );
      Assert.assertNotNull( secondImported );
      Assert.assertEquals( firstImported, secondImported );
   }//End Method
   
   @Test public void shouldSafelyAvoidNullInput(){
      systemUnderTest.updateTestResults( null );
   }//End Method
   
   @Test public void shouldSafelyAvoidInvalidFormat(){
      systemUnderTest.updateTestResults( "anything" );
   }//End Method
   
   @Test public void shouldNotImportSingleTestClassWithMissingCase() {
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-case-missing.json" );
      systemUnderTest.updateTestResults( input );
      assertTestClassAndTestCasePresent( Arrays.asList( AssertableProperty.TestCases ) );
   }//End Method
   
   @Test public void shouldNotImportSingleTestClassWithMissingClass() {
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-class-missing.json" );
      systemUnderTest.updateTestResults( input );
      Assert.assertTrue( database.hasNoTestClasses() );
   }//End Method
   
   @Test public void shouldHandleMissingChildReports(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-child-reports-missing.json" );
      systemUnderTest.updateTestResults( input );
      Assert.assertTrue( database.hasNoTestClasses() );
   }//End Method
   
   @Test public void shouldHandleMissingResult(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-result-missing.json" );
      systemUnderTest.updateTestResults( input );
      Assert.assertTrue( database.hasNoTestClasses() );
   }//End Method
   
   @Test public void shouldHandleMissingSuites(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-suites-missing.json" );
      systemUnderTest.updateTestResults( input );
      Assert.assertTrue( database.hasNoTestClasses() );
   }//End Method
   
   @Test public void shouldHandleMissingClassDuration(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-class-duration-missing.json" );
      systemUnderTest.updateTestResults( input );
      assertTestClassAndTestCasePresent( Arrays.asList( AssertableProperty.ClassDuration ) );
      
      TestClass testClass = database.getTestClass( new TestClassKeyImpl( CLASS_NAME, CLASS_LOCATION ) );
      Assert.assertEquals( TestClass.DEFAULT_DURATION, testClass.durationProperty().get(), TestCommon.precision() );
   }//End Method
   
   @Test public void shouldHandleMissingCaseDuration(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-case-duration-missing.json" );
      systemUnderTest.updateTestResults( input );
      assertTestClassAndTestCasePresent( Arrays.asList( AssertableProperty.CaseDuration ) );
      
      TestClass testClass = database.getTestClass( new TestClassKeyImpl( CLASS_NAME, CLASS_LOCATION ) );
      TestCase testCase = testClass.testCasesList().get( 0 );
      Assert.assertEquals( TestCase.DEFAULT_DURATION, testCase.durationProperty().get(), TestCommon.precision() );
   }//End Method
   
   @Test public void shouldHandleMissingClassName(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-class-name-missing.json" );
      systemUnderTest.updateTestResults( input );
      Assert.assertTrue( database.hasNoTestClasses() );
   }//End Method
   
   @Test public void shouldHandleMissingCaseName(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-name-missing.json" );
      systemUnderTest.updateTestResults( input );
      Assert.assertTrue( database.hasNoTestClasses() );
   }//End Method
   
   @Test public void shouldHandleMissingTestClass(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-class-name-missing.json" );
      systemUnderTest.updateTestResults( input );
      Assert.assertTrue( database.hasNoTestClasses() );
   }//End Method
   
   @Test public void shouldHandleMissingAge(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-age-missing.json" );
      systemUnderTest.updateTestResults( input );
      assertTestClassAndTestCasePresent( Arrays.asList( AssertableProperty.Age ) );
      
      TestClass testClass = database.getTestClass( new TestClassKeyImpl( CLASS_NAME, CLASS_LOCATION ) );
      TestCase testCase = testClass.testCasesList().get( 0 );
      Assert.assertEquals( TestCase.DEFAULT_AGE, testCase.ageProperty().get() );
   }//End Method
   
   @Test public void shouldHandleMissingSkipped(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-skipped-missing.json" );
      systemUnderTest.updateTestResults( input );
      assertTestClassAndTestCasePresent( Arrays.asList( AssertableProperty.Skipped ) );
      
      TestClass testClass = database.getTestClass( new TestClassKeyImpl( CLASS_NAME, CLASS_LOCATION ) );
      TestCase testCase = testClass.testCasesList().get( 0 );
      Assert.assertEquals( TestCase.DEFAULT_SKIPPED, testCase.skippedProperty().get() );
   }//End Method
   
   @Test public void shouldHandleMissingStatus(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-status-missing.json" );
      systemUnderTest.updateTestResults( input );
      assertTestClassAndTestCasePresent( Arrays.asList(  AssertableProperty.Status ) );
      
      TestClass testClass = database.getTestClass( new TestClassKeyImpl( CLASS_NAME, CLASS_LOCATION ) );
      TestCase testCase = testClass.testCasesList().get( 0 );
      Assert.assertEquals( TestCase.DEFAULT_STATUS, testCase.statusProperty().get() );
   }//End Method
   
   @Test public void shouldHandleInvalidClassDuration(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-class-duration-invalid.json" );
      systemUnderTest.updateTestResults( input );
      assertTestClassAndTestCasePresent( Arrays.asList( AssertableProperty.ClassDuration ) );
      
      TestClass testClass = database.getTestClass( new TestClassKeyImpl( CLASS_NAME, CLASS_LOCATION ) );
      Assert.assertEquals( TestClass.DEFAULT_DURATION, testClass.durationProperty().get(), TestCommon.precision() );
   }//End Method
   
   @Test public void shouldHandleInvalidCaseDuration(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-case-duration-invalid.json" );
      systemUnderTest.updateTestResults( input );
      assertTestClassAndTestCasePresent( Arrays.asList( AssertableProperty.CaseDuration ) );
      
      TestClass testClass = database.getTestClass( new TestClassKeyImpl( CLASS_NAME, CLASS_LOCATION ) );
      TestCase testCase = testClass.testCasesList().get( 0 );
      Assert.assertEquals( TestCase.DEFAULT_DURATION, testCase.durationProperty().get(), TestCommon.precision() );
   }//End Method
   
   @Test public void shouldHandleInvalidClassName(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-class-name-invalid.json" );
      systemUnderTest.updateTestResults( input );
      Assert.assertTrue( database.hasNoTestClasses() );
   }//End Method
   
   @Test public void shouldHandleInvalidCaseName(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-name-invalid.json" );
      systemUnderTest.updateTestResults( input );
      Assert.assertTrue( database.hasNoTestClasses() );
   }//End Method
   
   @Test public void shouldHandleInvalidTestClass(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-class-name-invalid.json" );
      systemUnderTest.updateTestResults( input );
      Assert.assertTrue( database.hasNoTestClasses() );
   }//End Method
   
   @Test public void shouldHandleInvalidAge(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-age-invalid.json" );
      systemUnderTest.updateTestResults( input );
      assertTestClassAndTestCasePresent( Arrays.asList( AssertableProperty.Age ) );
      
      TestClass testClass = database.getTestClass( new TestClassKeyImpl( CLASS_NAME, CLASS_LOCATION ) );
      TestCase testCase = testClass.testCasesList().get( 0 );
      Assert.assertEquals( TestCase.DEFAULT_AGE, testCase.ageProperty().get() );
   }//End Method
   
   @Test public void shouldHandleInvalidSkipped(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-skipped-invalid.json" );
      systemUnderTest.updateTestResults( input );
      assertTestClassAndTestCasePresent( Arrays.asList( AssertableProperty.Skipped ) );
      
      TestClass testClass = database.getTestClass( new TestClassKeyImpl( CLASS_NAME, CLASS_LOCATION ) );
      TestCase testCase = testClass.testCasesList().get( 0 );
      Assert.assertEquals( TestCase.DEFAULT_SKIPPED, testCase.skippedProperty().get() );
   }//End Method
   
   @Test public void shouldHandleInvalidStatus(){
      String input = TestCommon.readFileIntoString( getClass(), "single-test-case-status-invalid.json" );
      systemUnderTest.updateTestResults( input );
      assertTestClassAndTestCasePresent( Arrays.asList(  AssertableProperty.Status ) );
      
      TestClass testClass = database.getTestClass( new TestClassKeyImpl( CLASS_NAME, CLASS_LOCATION ) );
      TestCase testCase = testClass.testCasesList().get( 0 );
      Assert.assertEquals( TestCase.DEFAULT_STATUS, testCase.statusProperty().get() );
   }//End Method
   
   @Test public void shouldHandleMultipleTestCases(){
      String input = TestCommon.readFileIntoString( getClass(), "multiple-test-case-single-test-class.json" );
      systemUnderTest.updateTestResults( input );
      
      Assert.assertEquals( 1, database.testClasses().size() );
      TestClass testClass = database.testClasses().get( 0 );
      Assert.assertEquals( 5, testClass.testCasesList().size() );
   }//End Method
   
   @Test public void shouldAddNewTestCasesAndNotDuplicate(){
      String input = TestCommon.readFileIntoString( getClass(), "multiple-test-case-single-test-class.json" );
      systemUnderTest.updateTestResults( input );
      
      Assert.assertEquals( 1, database.testClasses().size() );
      TestClass testClass = database.testClasses().get( 0 );
      Assert.assertEquals( 5, testClass.testCasesList().size() );
      
      testClass.removeTestCase( testClass.testCasesList().get( 4 ) );
      Assert.assertEquals( 4, testClass.testCasesList().size() );
      
      systemUnderTest.updateTestResults( input );
      
      Assert.assertEquals( 1, database.testClasses().size() );
      Assert.assertEquals( 5, testClass.testCasesList().size() );
   }//End Method
   
   @Test public void shouldHandleMultipleTestCasesAndMultipleClasses(){
      String input = TestCommon.readFileIntoString( getClass(), "multiple-test-case-multiple-test-class.json" );
      systemUnderTest.updateTestResults( input );
      
      Assert.assertEquals( 36, database.testClasses().size() );
      assertTestClassHasTestCases( 0, 5 );
      assertTestClassHasTestCases( 1, 1 );
      assertTestClassHasTestCases( 2, 2 );
      assertTestClassHasTestCases( 3, 4 );
      assertTestClassHasTestCases( 4, 2 );
      assertTestClassHasTestCases( 5, 5 );
      assertTestClassHasTestCases( 6, 1 );
      assertTestClassHasTestCases( 7, 3 );
      assertTestClassHasTestCases( 8, 2 );
      assertTestClassHasTestCases( 9, 1 );
      assertTestClassHasTestCases( 10, 2 );
      assertTestClassHasTestCases( 11, 2 );
      assertTestClassHasTestCases( 12, 3 );
      assertTestClassHasTestCases( 13, 2 );
      assertTestClassHasTestCases( 14, 3 );
      assertTestClassHasTestCases( 15, 10 );
      assertTestClassHasTestCases( 16, 6 );
      assertTestClassHasTestCases( 17, 3 );
      assertTestClassHasTestCases( 18, 1 );
      assertTestClassHasTestCases( 19, 1 );
      assertTestClassHasTestCases( 20, 2 );
      assertTestClassHasTestCases( 21, 2 );
      assertTestClassHasTestCases( 22, 2 );
      assertTestClassHasTestCases( 23, 2 );
      assertTestClassHasTestCases( 24, 8 );
      assertTestClassHasTestCases( 25, 1 );
      assertTestClassHasTestCases( 26, 6 );
      assertTestClassHasTestCases( 27, 3 );
      assertTestClassHasTestCases( 28, 8 );
      assertTestClassHasTestCases( 29, 3 );
      assertTestClassHasTestCases( 30, 3 );
      assertTestClassHasTestCases( 31, 1 );
      assertTestClassHasTestCases( 32, 1 );
      assertTestClassHasTestCases( 33, 7 );
      assertTestClassHasTestCases( 34, 4 );
      assertTestClassHasTestCases( 35, 1 );
   }//End Method
   
   /**
    * Method to assert that a {@link TestClass} has the given {@link TestCase} count.
    * @param testClassIndex the index of the {@link TestClass}.
    * @param testCaseCount the {@link TestCase} count.
    */
   private void assertTestClassHasTestCases( int testClassIndex, int testCaseCount ) {
      TestClass testClass = database.testClasses().get( testClassIndex );
      Assert.assertEquals( testCaseCount, testClass.testCasesList().size() );
   }//End Method
   
   /**
    * Method to assert that the common {@link TestClass} and {@link TestCase} have been created.
    * @param propertiesToIgnore {@link AssertableProperty}s that should not be asserted allowing 
    * some values to vary but performing common assertions.
    */
   private void assertTestClassAndTestCasePresent( List< AssertableProperty > propertiesToIgnore ){
      Assert.assertEquals( 1, database.testClasses().size() );
      TestClass testClass = database.getTestClass( new TestClassKeyImpl( CLASS_NAME, CLASS_LOCATION ) );
      Assert.assertNotNull( testClass );
      
      Assert.assertEquals( CLASS_NAME, testClass.nameProperty().get() );
      Assert.assertEquals( CLASS_LOCATION, testClass.locationProperty().get() );
      
      if ( !propertiesToIgnore.contains( AssertableProperty.ClassDuration ) ) {
         Assert.assertEquals( 0.071, testClass.durationProperty().get(), TestCommon.precision() );
      }
      if ( propertiesToIgnore.contains( AssertableProperty.TestCases ) ) {
         Assert.assertEquals( 0, testClass.testCasesList().size() );
         return;
      }
      
      Assert.assertEquals( 1, testClass.testCasesList().size() );
      
      TestCase testCase = testClass.testCasesList().get( 0 );
      if ( !propertiesToIgnore.contains( AssertableProperty.CaseName ) ) {
         Assert.assertEquals( "shouldStoreObjectsAndRemoveAllMatches", testCase.nameProperty().get() );
      }
      if ( !propertiesToIgnore.contains( AssertableProperty.Status ) ) {
         Assert.assertEquals( TestResultStatus.PASSED, testCase.statusProperty().get() );
      }
      if ( !propertiesToIgnore.contains( AssertableProperty.Skipped ) ) {
         Assert.assertEquals( false, testCase.skippedProperty().get() );
      }
      if ( !propertiesToIgnore.contains( AssertableProperty.CaseDuration ) ) {
         Assert.assertEquals( 0.053, testCase.durationProperty().get(), TestCommon.precision() );
      }
      if ( !propertiesToIgnore.contains( AssertableProperty.Age ) ) {
         Assert.assertEquals( 1, testCase.ageProperty().get() );
      }
      if ( !propertiesToIgnore.contains( AssertableProperty.TestClass ) ) {
         Assert.assertEquals( testClass, testCase.testClassProperty().get() );
      }
   }//End Method

}//End Class
