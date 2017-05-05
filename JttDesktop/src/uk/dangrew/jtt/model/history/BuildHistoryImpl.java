/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.history;

import java.util.Map;
import java.util.TreeMap;

import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import uk.dangrew.jtt.model.build.JenkinsBuild;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.sd.utility.synchronization.SynchronizedObservableList;

/**
 * Implementation of the {@link BuildHistory} interface.
 */
public class BuildHistoryImpl implements BuildHistory, ControllableBuildHistory {
   
   private final JenkinsJob associatedJob;
   private final SortedList< JenkinsBuild > sortedBuilds;
   private final ObservableList< JenkinsBuild > modifiableBuilds;
   
   private final Map< Integer, JenkinsBuild > buildMap;
   
   /**
    * Constructs a new {@link BuildHistoryImpl}.
    * @param job the {@link JenkinsJob} associated.
    */
   public BuildHistoryImpl( JenkinsJob job ) {
      if ( job == null ) {
         throw new IllegalArgumentException( "Must supply non null job." );
      }
      
      this.associatedJob = job;
      this.modifiableBuilds = new SynchronizedObservableList<>();
      this.sortedBuilds = new SortedList<>( 
               modifiableBuilds,
               ( a, b ) -> Integer.compare( a.buildNumber(), b.buildNumber()
      ) );
      this.buildMap = new TreeMap<>();
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public JenkinsJob jenkinsJob() {
      return associatedJob;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObservableList< JenkinsBuild > builds() {
      return sortedBuilds;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public JenkinsBuild getHistoryFor( int buildNumber ) {
      return buildMap.get( buildNumber );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void addBuildHistory( JenkinsBuild build ) {
      if ( modifiableBuilds.contains( build ) ) {
         return;
      }
      if ( buildMap.containsKey( build.buildNumber() ) ) {
         throw new IllegalArgumentException( "Build already present for " + build.buildNumber() );
      }
      updateBuilds( build );
   }//End Method
   
   /**
    * Method to update the {@link JenkinsBuild} in the structures held by the {@link BuildHistory}.
    * @param build the {@link JenkinsBuild} to add.
    */
   private void updateBuilds( JenkinsBuild build ) {
      buildMap.put( build.buildNumber(), build );
      modifiableBuilds.add( build );
   }//End Method

}//End Class
