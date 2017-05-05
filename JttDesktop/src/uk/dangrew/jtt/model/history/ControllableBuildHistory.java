/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.history;

import uk.dangrew.jtt.model.build.JenkinsBuild;

/**
 * The {@link ControllableBuildHistory} provides a method of controlling the data
 * within the {@link BuildHistory}.
 */
public interface ControllableBuildHistory extends BuildHistory {

   /**
    * Method to add an element to the {@link BuildHistory}.
    * @param build the {@link JenkinsBuild} to add.
    */
   public void addBuildHistory( JenkinsBuild build );
   
}//End Interface
