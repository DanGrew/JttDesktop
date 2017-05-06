/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.data.json.tests;

import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * Interface defining the api for importing test results from Jenkins.
 */
public interface JsonTestResultsImporter {

   /**
    * Method to parse the input into {@link TestClass}es and {@link TestCase}s.
    * @param job the {@link JenkinsJob} to import for.
    * @param response the input data to parse.
    */
   public void updateTestResults( JenkinsJob job, String response );

}//End Interface
