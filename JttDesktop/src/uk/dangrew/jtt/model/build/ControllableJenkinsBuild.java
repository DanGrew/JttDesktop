/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.build;

import java.util.Collection;

import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.model.users.JenkinsUser;

/**
 * The {@link ControllableJenkinsBuild} provides an interface for controlling
 * the data within the {@link JenkinsBuild}.
 */
public interface ControllableJenkinsBuild extends JenkinsBuild {

   /**
    * Setter for the build number from jenkins.
    * @param buildNumber the value.
    */
   public void setBuildNumber( int buildNumber );
   
   /**
    * Setter for the {@link TestResults} of the build, if any.
    * @param results the value.
    */
   public void setTestResults( TestResults results );
   
   /**
    * Setter for the duration of the build in milliseconds.
    * @param duration the value.
    */
   public void setDuration( long duration );
   
   /**
    * Setter for the estimate for completion time of the build, in milliseconds.
    * @param estimate the value.
    */
   public void setEstimate( long estimate );
   
   /**
    * Setter for the {@link BuildResultStatus} of the build.
    * @param result the value.
    */
   public void setResult( BuildResultStatus result );
   
   /**
    * Setter for the timestamp of the start of the build.
    * @param timestamp the value.
    */
   public void setTimestamp( long timestamp );
   
   /**
    * Setter for the {@link JenkinsNode} the build was built on.
    * @param builtOn the value.
    */
   public void setBuiltOn( JenkinsNode builtOn );
   
   /**
    * Setter for the {@link Collection} of {@link JenkinsUser} culprits, if any.
    * @param culprits the value.
    */
   public void setCulprits( Collection< JenkinsUser > culprits );
}//End Interface
