/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.build;

import java.util.List;

import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.model.users.JenkinsUser;

/**
 * The {@link JenkinsBuild} interface defines what a completed build comprises of once
 * built.
 */
public interface JenkinsBuild {
   
   /**
    * Access to the build number of the build.
    * @return the value.
    */
   public int buildNumber();
   
   /**
    * Access to the {@link TestResults} of the build, if any.
    * @return the value.
    */
   public TestResults testResults();
   
   /**
    * Access to the duration of the build, in milliseconds.
    * @return the value.
    */
   public long duration();
   
   /**
    * Access to the estimate for the completion of the build, in milliseconds.
    * @return the value.
    */
   public long estimate();
   
   /**
    * Access to the {@link BuildResultStatus} of the build.
    * @return the value.
    */
   public BuildResultStatus result();
   
   /**
    * Access to the timestamp of the start of the build.
    * @return the value.
    */
   public long timestamp();
   
   /**
    * Access to the {@link JenkinsNode} the build was built on.
    * @return the value.
    */
   public JenkinsNode builtOn();
   
   /**
    * Access to the {@link JenkinsUser} culprits, if any.
    * @return the value.
    */
   public List< JenkinsUser > culprits();

}//End Interface
