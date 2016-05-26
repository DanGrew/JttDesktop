/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.jobs;

/**
 * Enum defining the build result of a {@link JenkinsJob}.
 */
public enum BuildResultStatus {

   ABORTED,
   SUCCESS,
   FAILURE,
   NOT_BUILT,
   UNSTABLE,
   UNKNOWN
   
}//End Enum
