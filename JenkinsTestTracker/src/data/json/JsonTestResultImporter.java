/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package data.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.TestResultsImporter;
import model.tests.TestCase;
import model.tests.TestCaseImpl;
import model.tests.TestClass;
import model.tests.TestClassImpl;
import model.tests.TestResultStatus;
import storage.database.JenkinsDatabase;

/**
 * The {@link JsonTestResultImporter} defines a {@link TestResultsImporter} specifically for JSON data.
 */
public class JsonTestResultImporter implements TestResultsImporter {

   private static final String CHILD_REPORTS = "childReports";
   private static final String RESULT = "result";
   private static final String SUITES = "suites";
   private static final String DURATION = "duration";
   private static final String CASES = "cases";
   private static final String NAME = "name";
   private static final String AGE = "age";
   private static final String SKIPPED = "skipped";
   private static final String STATUS = "status";
   
   private JenkinsDatabase database;
   
   /**
    * Constructs a new {@link JsonTestResultImporter}.
    * @param database the {@link JenkinsDatabase} to import to.
    */
   public JsonTestResultImporter( JenkinsDatabase database ) {
      if ( database == null ) throw new IllegalArgumentException( "Null Jenkins Database provided." );
      
      this.database = database;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void parse( String input ) {
      if ( input == null || database == null ) {
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
         constructTestClass( jsonTestClasses.getJSONObject( i ) );
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
         JSONArray childReports = jsonRoot.getJSONArray( CHILD_REPORTS );
         JSONObject firstChild = childReports.getJSONObject( 0 );
         JSONObject result = firstChild.getJSONObject( RESULT );
         JSONArray suites = result.getJSONArray( SUITES );
         return suites;
      } catch ( JSONException exception ) {
         return null;
      }
   }//End Method
   
   /**
    * Method to consruct a {@link TestClass} from json data.
    * @param jsonTestClass the {@link JSONObject} representing the {@link TestClass}.
    */
   private void constructTestClass( JSONObject jsonTestClass ) {
      try {
         if ( !jsonTestClass.has( NAME ) ) return;
         String fullName = jsonTestClass.getString( NAME );
         TestClass testClass = new TestClassImpl( fullName );
         database.store( testClass );
         
         if ( jsonTestClass.has( DURATION ) ) {
            Double duration = extractDouble( jsonTestClass, DURATION );
            if ( duration != null ) testClass.durationProperty().set( duration );
         }
         
         if ( !jsonTestClass.has( CASES ) ) return;
         JSONArray jsonTestCases = jsonTestClass.getJSONArray( CASES );
         for ( int i = 0; i < jsonTestCases.length(); i++ ) {
            constructTestCase( testClass, jsonTestCases.getJSONObject( i ) );
         }
      } catch ( JSONException exception ) {
         System.out.println( exception.getMessage() );
         return;
      }
   }//End Method
   
   /**
    * Method to construct the {@link TestCase} from the given {@link JSONObject} test case, in the given
    * {@link TestClass}.
    * @param testClass the {@link TestClass} the {@link TestCase} is for.
    * @param jsonTestCase the {@link JSONObject} representing the {@link TestCase}.
    */
   private void constructTestCase( TestClass testClass, JSONObject jsonTestCase ) {
      try {
         if ( !jsonTestCase.has( NAME ) ) return;
         String name = jsonTestCase.getString( NAME );
         
         TestCase testCase = new TestCaseImpl( name, testClass );
         testClass.testCasesList().add( testCase );
         
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
