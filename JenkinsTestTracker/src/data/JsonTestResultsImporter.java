/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package data;

/**
 * Interface defining the api for importing test results from Jenkins.
 */
public interface JsonTestResultsImporter {

   /**
    * Method to parse the input into {@link TestClass}es and {@link TestCase}s.
    * @param response the input data to parse.
    */
   public void updateTestResults( String response );

}//End Interface
