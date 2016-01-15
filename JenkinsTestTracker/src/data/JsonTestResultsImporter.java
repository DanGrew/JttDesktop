/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package data;

import model.tests.TestCase;
import model.tests.TestClass;

/**
 * Interface defining the api for importing test results from Jenkins.
 */
public interface JsonTestResultsImporter {

   /**
    * Method to parse the input into {@link TestClass}es and {@link TestCase}s.
    * @param input the input data to parse.
    */
   public void parse( String input );

}//End Interface
