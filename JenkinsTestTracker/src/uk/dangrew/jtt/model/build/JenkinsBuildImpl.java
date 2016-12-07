/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.build;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.model.users.JenkinsUser;

/**
 * Implementation of the {@link JenkinsBuild}.
 */
public class JenkinsBuildImpl implements JenkinsBuild, ControllableJenkinsBuild {

   private int buildNumber;
   private TestResults testResults;
   private long duration;
   private long estimate;
   private BuildResultStatus result;
   private long timestamp;
   private JenkinsNode builtOn;
   private List< JenkinsUser > culprits;
   
   /**
    * Constructs a new {@link JenkinsBuildImpl}.
    */
   public JenkinsBuildImpl() {
      this.culprits = new ArrayList<>();
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public int buildNumber() {
      return buildNumber;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public TestResults testResults() {
      return testResults;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public long duration() {
      return duration;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public long estimate() {
      return estimate;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public BuildResultStatus result() {
      return result;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public long timestamp() {
      return timestamp;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public JenkinsNode builtOn() {
      return builtOn;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public List< JenkinsUser > culprits() {
      return Collections.unmodifiableList( culprits );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void setBuildNumber( int buildNumber ) {
      this.buildNumber = buildNumber;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void setTestResults( TestResults results ) {
      this.testResults = results;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void setDuration( long duration ) {
      this.duration = duration;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void setEstimate( long estimate ) {
      this.estimate = estimate;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void setResult( BuildResultStatus result ) {
      this.result = result;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void setTimestamp( long timestamp ) {
      this.timestamp = timestamp;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void setBuiltOn( JenkinsNode builtOn ) {
      this.builtOn = builtOn;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void setCulprits( Collection< JenkinsUser > culprits ) {
      this.culprits.clear();
      this.culprits.addAll( culprits );
   }//End Method

}//End Class
