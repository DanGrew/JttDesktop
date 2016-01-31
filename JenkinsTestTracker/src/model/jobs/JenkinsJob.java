/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package model.jobs;

import api.handling.BuildState;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

/**
 * The {@link JenkinsJob} provides a representation of a single job or build on the associated
 * jenkins instance.
 */
public interface JenkinsJob {

   public static final int DEFAULT_LAST_BUILD_NUMBER = 0;
   public static final BuildResultStatus DEFAULT_LAST_BUILD_STATUS = BuildResultStatus.FAILURE;
   public static final BuildState DEFAULT_BUILD_STATE = BuildState.Built;
   public static final long DEFAULT_CURRENT_BUILD_TIME = 0;
   public static final long DEFAULT_EXPECTED_BUILD_TIME = 0;

   /**
    * Provides the name {@link StringProperty} of the {@link JenkinsJob}.
    * @return the {@link StringProperty}.
    */
   public StringProperty nameProperty();

   /**
    * Provides the last build number {@link IntegerProperty} of the {@link JenkinsJob}.
    * @return the {@link IntegerProperty}.
    */
   public IntegerProperty lastBuildNumberProperty();

   /**
    * Provides the last build status of the {@link JenkinsJob}.
    * @return the {@link ObjectProperty} for the {@link BuildResultStatus}.
    */
   public ObjectProperty< BuildResultStatus > lastBuildStatusProperty();

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
   
}//End Interface
