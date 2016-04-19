/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package data.json.tests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.JsonTestResultsImporter;
import model.jobs.JenkinsJob;
import model.tests.TestCase;
import model.tests.TestCaseImpl;
import model.tests.TestClass;
import model.tests.TestClassImpl;
import model.tests.TestResultStatus;
import storage.database.JenkinsDatabase;
import storage.database.TestClassKey;
import storage.database.TestClassKeyImpl;

/**
 * The {@link JsonTestResultsImporterImpl} defines a {@link JsonTestResultsImporter} specifically for JSON data.
 */
public class JsonTestResultsImporterImpl implements JsonTestResultsImporter {

   private static final String CHILD_REPORTS = "childReports";
   private static final String RESULT = "result";
   private static final String SUITES = "suites";
   private static final String DURATION = "duration";
   private static final String CASES = "cases";
   private static final String CLASS_NAME = "className";
   private static final String NAME = "name";
   private static final String AGE = "age";
   private static final String SKIPPED = "skipped";
   private static final String STATUS = "status";
   
   private JenkinsDatabase database;
   
   /**
    * Constructs a new {@link JsonTestResultsImporterImpl}.
    * @param database the {@link JenkinsDatabase} to import to.
    */
   public JsonTestResultsImporterImpl( JenkinsDatabase database ) {
      if ( database == null ) throw new IllegalArgumentException( "Null Jenkins Database provided." );
      
      this.database = database;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void updateTestResults( JenkinsJob job, String input ) {
      job.failingTestCases().clear(); 
      
      if ( input == null ) {
         return;
      }
      
      JSONObject jsonRoot = parseJsonInput( input );
      if ( jsonRoot == null ) {
         return;
      }
      
      JSONArray jsonTestClasses = extractTestClasses( jsonRoot );
      if ( jsonTestClasses == null ) {
         return;
      }
      
      for ( int i = 0; i < jsonTestClasses.length(); i++ ) {
         constructTestCases( job, jsonTestClasses.getJSONObject( i ) );
      }
   }//End Method
   
   /**
    * Method to parse the input into a root {@link JSONObject}.
    * @param input the json data to parse.
    * @return the root {@link JSONObject}, or null if parse failed.
    */
   private JSONObject parseJsonInput( String input ) {
      try { 
         JSONObject jsonRoot = new JSONObject( input );
         return jsonRoot;
      } catch ( JSONException exception ) {
         //Invalid, safely return;
         return null;
      }
   }//End Method
   
   /**
    * Method to extract the {@link JSONArray} or {@link TestClass}es from the given {@link JSONObject} root.
    * @param jsonRoot the {@link JSONObject} root of the data.
    * @return the {@link JSONArray} for the {@link TestClass}es, or null if none found.
    */
   private JSONArray extractTestClasses( JSONObject jsonRoot ) {
      try {
         JSONArray suites = null;
         if ( jsonRoot.has( SUITES ) ) {
            suites = jsonRoot.getJSONArray( SUITES );
         } else {
            JSONArray childReports = jsonRoot.getJSONArray( CHILD_REPORTS );
            JSONObject firstChild = childReports.getJSONObject( 0 );
            JSONObject result = firstChild.getJSONObject( RESULT );
            suites = result.getJSONArray( SUITES );
         }
         return suites;
      } catch ( JSONException exception ) {
         return null;
      }
   }//End Method
   
   /**
    * Method to construct a {@link TestClass} from json data.
    * @param job the {@link JenkinsJob} to import into.
    * @param jsonTestClass the {@link JSONObject} representing the {@link TestClass}.
    */
   private void constructTestCases( JenkinsJob job, JSONObject jsonTestClass ) {
      try {
         if ( !jsonTestClass.has( CASES ) ) return;
         JSONArray jsonTestCases = jsonTestClass.getJSONArray( CASES );
         for ( int i = 0; i < jsonTestCases.length(); i++ ) {
            constructTestCase( job, jsonTestCases.getJSONObject( i ) );
         }
      } catch ( JSONException exception ) {
         System.out.println( exception.getMessage() );
         return;
      }
   }//End Method
   
   /**
    * Method to construct a {@link TestClass} if it doesn't already exist from the name given in the
    * test case.
    * @param jsonTestCase the {@link JSONObject} for the test case.
    * @return the {@link TestClass} to use.
    */
   private TestClass constructTestClass( JSONObject jsonTestCase ){
      if ( !jsonTestCase.has( CLASS_NAME ) ) return null;
      String className = jsonTestCase.getString( CLASS_NAME );
      
      TestClassKey key = new TestClassKeyImpl( 
               TestClassImpl.identifyName( className ), TestClassImpl.identifyLocation( className ) 
      );
      if ( !database.hasTestClass( key ) ){
         database.store( new TestClassImpl( className ) );
      }
      
      TestClass testClass = database.getTestClass( key );
      return testClass;
   }//End Method
   
   /**
    * Method to construct the {@link TestCase} from the given {@link JSONObject} test case.
    * @param job the {@link JenkinsJob} to import into.
    * @param jsonTestCase the {@link JSONObject} representing the {@link TestCase}.
    */
   private void constructTestCase( JenkinsJob job, JSONObject jsonTestCase ) {
      try {
         TestClass testClass = constructTestClass( jsonTestCase );
         if ( testClass == null ) return;
         
         if ( !jsonTestCase.has( NAME ) ) return;
         String name = jsonTestCase.getString( NAME );
         
         TestCase testCase = testClass.getTestCase( name );
         if ( testCase == null ) {
            testCase = new TestCaseImpl( name, testClass );
            testClass.addTestCase( testCase );
         }
         
         if ( jsonTestCase.has( DURATION ) ) {
            Double duration = extractDouble( jsonTestCase, DURATION );
            if ( duration != null ) testCase.durationProperty().set( duration );
         }
         
         if ( jsonTestCase.has( AGE ) ) {
            Double age = extractDouble( jsonTestCase, AGE );
            if ( age != null ) testCase.ageProperty().set( age.intValue() );
         }
         
         if ( jsonTestCase.has( SKIPPED ) ) {
            Boolean skipped = extractBoolean( jsonTestCase, SKIPPED );
            if ( skipped != null ) testCase.skippedProperty().set( skipped );
         }
         
         if ( jsonTestCase.has( STATUS ) ) {
            TestResultStatus status = jsonTestCase.getEnum( TestResultStatus.class, STATUS );
            testCase.statusProperty().set( status );
            
            if ( status == TestResultStatus.FAILED || status == TestResultStatus.REGRESSION ) {
               job.failingTestCases().add( testCase );
            }
         }
      } catch ( JSONException exception ) {
         System.out.println( exception.getMessage() );
         return;
      }
   }//End Method
   
   /**
    * Method to extract a double value given the key and handle potential exceptions throw.
    * @param jsonObject the {@link JSONObject} to extract from.
    * @param key the key to extract.
    * @return the {@link Double} value extracted, or null if none.
    */
   private Double extractDouble( JSONObject jsonObject, String key ) {
      try {
         return jsonObject.getDouble( key );
      } catch ( JSONException exception ) {
         return null;
      }
   }//End Method
   
   /**
    * Method to extract a boolean value given the key and handle potential exceptions throw.
    * @param jsonObject the {@link JSONObject} to extract from.
    * @param key the key to extract.
    * @return the {@link Boolean} value extracted, or null if none.
    */
   private Boolean extractBoolean( JSONObject jsonObject, String key ) {
      try {
         return jsonObject.getBoolean( key );
      } catch ( JSONException exception ) {
         return null;
      }
   }//End Method

}//End Class
