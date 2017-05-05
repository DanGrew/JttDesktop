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
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.model.tests.TestCase;
import uk.dangrew.jtt.model.users.JenkinsUser;

/**
 * {@link JenkinsJobImpl} provides an implementation of the {@link JenkinsJob}.
 */
public class JenkinsJobImpl implements JenkinsJob {

   private final StringProperty name;
   private final ObjectProperty< Pair< Integer, BuildResultStatus > > buildStatus;
   private final ObjectProperty< BuildState > buildState;
   private final LongProperty expectedBuildTime;
   private final LongProperty currentBuildTime;
   private final ObjectProperty< Long > buildTimestamp;
   private final ObjectProperty< JenkinsNode > builtOn;
   private final ObservableList< JenkinsUser > culprits;
   private final ObservableList< TestCase > failingTestCases;
   private final ObjectProperty< Long > totalBuildTime;
   private final ObjectProperty< Integer > testFailureCount;
   private final ObjectProperty< Integer > testSkipCount;
   private final ObjectProperty< Integer > testTotalCount;
   
   /**
    * Constructs a new {@link JenkinsJobImpl}.
    * @param name the name of the {@link JenkinsJob}.
    */
   public JenkinsJobImpl( String name ) {
      if ( name == null ) {
         throw new IllegalArgumentException( "Null name provided for Jenkins Job." );
      }
      if ( name.trim().length() == 0 ) {
         throw new IllegalArgumentException( "Invalid name provided for Jenkins Job." );
      }
      
      this.name = new SimpleStringProperty( name );
      this.buildStatus = new SimpleObjectProperty<>( new Pair<>( DEFAULT_LAST_BUILD_NUMBER, DEFAULT_LAST_BUILD_STATUS ) );
      this.buildState = new SimpleObjectProperty<>( DEFAULT_BUILD_STATE );
      this.expectedBuildTime = new SimpleLongProperty( DEFAULT_EXPECTED_BUILD_TIME );
      this.currentBuildTime = new SimpleLongProperty( DEFAULT_CURRENT_BUILD_TIME );
      this.buildTimestamp = new SimpleObjectProperty<>( DEFAULT_BUILD_TIMESTAMP );
      this.builtOn = new SimpleObjectProperty<>( DEFAULT_LAST_BUILT_ON );
      this.culprits = FXCollections.observableArrayList();
      this.failingTestCases = FXCollections.observableArrayList();
      this.totalBuildTime = new SimpleObjectProperty<>( DEFAULT_TOTAL_BUILD_TIME );
      this.testFailureCount = new SimpleObjectProperty<>( DEFAULT_FAILURE_COUNT );
      this.testSkipCount = new SimpleObjectProperty<>( DEFAULT_SKIP_COUNT );
      this.testTotalCount = new SimpleObjectProperty<>( DEFAULT_TOTAL_TEST_COUNT );
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
   @Override public ObjectProperty< Pair< Integer, BuildResultStatus > > buildProperty() {
      return buildStatus;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public Integer getBuildNumber() {
      return buildProperty().get().getKey();
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void setBuildNumber( Integer number ) {
      buildProperty().set( new Pair<>( number, buildProperty().get().getValue() ) );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public BuildResultStatus getBuildStatus() {
      return buildProperty().get().getValue();
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void setBuildStatus( BuildResultStatus status ) {
      buildProperty().set( new Pair<>( buildProperty().get().getKey(), status ) );
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
   @Override public ObjectProperty< Long > buildTimestampProperty() {
      return buildTimestamp;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< JenkinsNode > builtOnProperty() {
      return builtOn;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObservableList< JenkinsUser > culprits() {
      return culprits;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObservableList< TestCase > failingTestCases() {
      return failingTestCases;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Long > totalBuildTimeProperty() {
      return totalBuildTime;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Integer > testFailureCount() {
      return testFailureCount;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Integer > testSkipCount() {
      return testSkipCount;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Integer > testTotalCount() {
      return testTotalCount;
   }//End Method

}//End Class
