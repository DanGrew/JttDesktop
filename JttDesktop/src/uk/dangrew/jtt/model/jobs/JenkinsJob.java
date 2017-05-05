/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.jobs;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.model.tests.TestCase;
import uk.dangrew.jtt.model.users.JenkinsUser;

/**
 * The {@link JenkinsJob} provides a representation of a single job or build on the associated
 * jenkins instance.
 */
public interface JenkinsJob {

   public static final int DEFAULT_LAST_BUILD_NUMBER = 0;
   public static final BuildResultStatus DEFAULT_LAST_BUILD_STATUS = BuildResultStatus.NOT_BUILT;
   public static final BuildState DEFAULT_BUILD_STATE = BuildState.Built;
   public static final long DEFAULT_CURRENT_BUILD_TIME = 0;
   public static final long DEFAULT_EXPECTED_BUILD_TIME = 0;
   public static final long DEFAULT_BUILD_TIMESTAMP = 0;
   public static final boolean DEFAULT_TEST_RESULTS_ARE_SYNC = false;
   public static final JenkinsNode DEFAULT_LAST_BUILT_ON = null;
   public static final long DEFAULT_TOTAL_BUILD_TIME = 0;
   public static final int DEFAULT_FAILURE_COUNT = 0;
   public static final int DEFAULT_SKIP_COUNT = 0;
   public static final int DEFAULT_TOTAL_TEST_COUNT = 0;

   /**
    * Provides the name {@link StringProperty} of the {@link JenkinsJob}.
    * @return the {@link StringProperty}.
    */
   public StringProperty nameProperty();

   /**
    * Provides the build number and build status of the {@link JenkinsJob}.
    * @return the {@link ObjectProperty} for the build number and {@link BuildResultStatus}.
    */
   public ObjectProperty< Pair< Integer, BuildResultStatus > > buildProperty();
   
   /**
    * Method to get the build number on the {@link #buildProperty()}.
    * @return the {@link Integer} build number.
    */
   public Integer getBuildNumber();
   
   /**
    * Method to set the build number on the {@link #buildProperty()}.
    * @param number the {@link Integer} build number.
    */
   public void setBuildNumber( Integer number );

   /**
    * Method to get the {@link BuildResultStatus} on the {@link #buildProperty()}.
    * @return the {@link BuildResultStatus}.
    */
   public BuildResultStatus getBuildStatus();
   
   /**
    * Method to set the {@link BuildResultStatus} on the {@link #buildProperty()}.
    * @param status the {@link BuildResultStatus}.
    */
   public void setBuildStatus( BuildResultStatus status );

   /**
    * Provides the current build state of the {@link JenkinsJob}.
    * @return the {@link ObjectProperty} for the {@link BuildState}.
    */
   public ObjectProperty< BuildState > buildStateProperty();

   /**
    * Provides the expected completion time of the current build.
    * @return the {@link LongProperty}.
    */
   public LongProperty expectedBuildTimeProperty();

   /**
    * Provides the current build time of the current build.
    * @return the {@link LongProperty}.
    */
   public LongProperty currentBuildTimeProperty();
   
   /**
    * Provides the most current build's timestamp. This is defined as the point at which
    * the build was started.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Long > buildTimestampProperty();
   
   /**
    * Access to the time taken to complete the last build.
    * @return the property.
    */
   public ObjectProperty< Long > totalBuildTimeProperty();
   
   /**
    * Provides the {@link JenkinsNode} the job was last built on.
    * @return the {@link ObjectProperty}. Note that the node can be null.
    */
   public ObjectProperty< JenkinsNode > builtOnProperty();
   
   /**
    * Provides an {@link ObservableList} of {@link JenkinsUser} culprits for the failure of the
    * last build.
    * @return the {@link ObservableList}.
    */
   public ObservableList< JenkinsUser > culprits();

   /**
    * Provides an {@link ObservableList} of {TestCase}s for the failure of the
    * last build.
    * @return the {@link ObservableList}.
    */
   public ObservableList< TestCase > failingTestCases();

   /**
    * Access to the number of test failures property.
    * @return the property.
    */
   public ObjectProperty< Integer > testFailureCount();

   /**
    * Access to the number of tests skipped property.
    * @return the property.
    */
   public ObjectProperty< Integer > testSkipCount();

   /**
    * Access to the number of tests in total property.
    * @return the property.
    */
   public ObjectProperty< Integer > testTotalCount();

}//End Interface
