/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.jobs;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

   private StringProperty name;
   private IntegerProperty currentBuildNumber;
   private ObjectProperty< Pair< Integer, BuildResultStatus > > lastBuildStatus;
   private ObjectProperty< BuildState > buildState;
   private LongProperty expectedBuildTime;
   private LongProperty currentBuildTime;
   private LongProperty currentBuildTimestamp;
   private ObjectProperty< JenkinsNode > lastBuiltOn;
   private ObservableList< JenkinsUser > culprits;
   private ObservableList< TestCase > failingTestCases;
   
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
      currentBuildNumber = new SimpleIntegerProperty( DEFAULT_CURRENT_BUILD_NUMBER );
      lastBuildStatus = new SimpleObjectProperty<>( new Pair<>( DEFAULT_LAST_BUILD_NUMBER, DEFAULT_LAST_BUILD_STATUS ) );
      buildState = new SimpleObjectProperty<>( DEFAULT_BUILD_STATE );
      expectedBuildTime = new SimpleLongProperty( DEFAULT_EXPECTED_BUILD_TIME );
      currentBuildTime = new SimpleLongProperty( DEFAULT_CURRENT_BUILD_TIME );
      currentBuildTimestamp = new SimpleLongProperty( DEFAULT_BUILD_TIMESTAMP );
      lastBuiltOn = new SimpleObjectProperty<>( DEFAULT_LAST_BUILT_ON );
      culprits = FXCollections.observableArrayList();
      failingTestCases = FXCollections.observableArrayList();
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
   @Override public IntegerProperty currentBuildNumberProperty() {
      return currentBuildNumber;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Pair< Integer, BuildResultStatus > > lastBuildProperty() {
      return lastBuildStatus;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public Integer getLastBuildNumber() {
      return lastBuildProperty().get().getKey();
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void setLastBuildNumber( Integer number ) {
      lastBuildProperty().set( new Pair<>( number, lastBuildProperty().get().getValue() ) );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public BuildResultStatus getLastBuildStatus() {
      return lastBuildProperty().get().getValue();
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void setLastBuildStatus( BuildResultStatus status ) {
      lastBuildProperty().set( new Pair<>( lastBuildProperty().get().getKey(), status ) );
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
   @Override public LongProperty currentBuildTimestampProperty() {
      return currentBuildTimestamp;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< JenkinsNode > lastBuiltOnProperty() {
      return lastBuiltOn;
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

}//End Class
