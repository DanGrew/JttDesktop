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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * {@link JenkinsJobImpl} provides an implementation of the {@link JenkinsJob}.
 */
public class JenkinsJobImpl implements JenkinsJob {

   private StringProperty name;
   private IntegerProperty lastBuildNumber;
   private ObjectProperty< BuildResultStatus > lastBuildStatus;
   private ObjectProperty< BuildState > buildState;
   private LongProperty expectedBuildTime;
   private LongProperty currentBuildTime;
   private LongProperty lastBuildTimestamp;
   private BooleanProperty testResultsAreSynchronized;
   
   /**
    * Constructs a new {@link JenkinsJobImpl}.
    * @param name the name of the {@link JenkinsJob}.
    */
   public JenkinsJobImpl( String name ) {
      if ( name == null ) throw new IllegalArgumentException( "Null name provided for Jenkins Job." );
      if ( name.trim().length() == 0 ) throw new IllegalArgumentException( "Invalid name provided for Jenkins Job." );
      
      this.name = new SimpleStringProperty( name );
      lastBuildNumber = new SimpleIntegerProperty( DEFAULT_LAST_BUILD_NUMBER );
      lastBuildStatus = new SimpleObjectProperty< BuildResultStatus >( DEFAULT_LAST_BUILD_STATUS );
      buildState = new SimpleObjectProperty< BuildState >( DEFAULT_BUILD_STATE );
      expectedBuildTime = new SimpleLongProperty( DEFAULT_EXPECTED_BUILD_TIME );
      currentBuildTime = new SimpleLongProperty( DEFAULT_CURRENT_BUILD_TIME );
      lastBuildTimestamp = new SimpleLongProperty( DEFAULT_BUILD_TIMESTAMP );
      testResultsAreSynchronized = new SimpleBooleanProperty( DEFAULT_TEST_RESULTS_ARE_SYNC );
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public StringProperty nameProperty() {
      return name;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public IntegerProperty lastBuildNumberProperty() {
      return lastBuildNumber;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< BuildResultStatus > lastBuildStatusProperty() {
      return lastBuildStatus;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< BuildState > buildStateProperty() {
      return buildState;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public LongProperty expectedBuildTimeProperty() {
      return expectedBuildTime;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public LongProperty currentBuildTimeProperty() {
      return currentBuildTime;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public LongProperty lastBuildTimestampProperty() {
      return lastBuildTimestamp;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public BooleanProperty testResultsAreSynchronizedProperty() {
      return testResultsAreSynchronized;
   }//End Method

}//End Class
