/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.history;

import javafx.collections.ObservableList;
import uk.dangrew.jtt.model.build.JenkinsBuild;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link BuildHistory} represents a binding of {@link JenkinsJob} to the 
 * {@link JenkinsBuild}s made from it.
 */
public interface BuildHistory {
   
   /**
    * Access to the associated {@link JenkinsJob}.
    * @return the {@link JenkinsJob}.
    */
   public JenkinsJob jenkinsJob();
   
   /**
    * Access to the unmodifiable, {@link ObservableList} that is also sorted
    * by {@link JenkinsBuild#buildNumber()}.
    * @return the {@link ObservableList}.
    */
   public ObservableList< JenkinsBuild > builds();
   
   /**
    * Getter for the {@link JenkinsBuild} of the given build number.
    * @param buildNumber the build number to get for.
    * @return the {@link JenkinsBuild}, can be null.
    */
   public JenkinsBuild getHistoryFor( int buildNumber );

}//End Interface
