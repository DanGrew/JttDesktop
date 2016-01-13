/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package data.json.jobs;

import api.handling.BuildState;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.jobs.BuildResultStatus;
import model.jobs.JenkinsJob;

/**
 * {@link JenkinsJobImpl} provides an implementation of the {@link JenkinsJob}.
 */
public class JenkinsJobImpl implements JenkinsJob {

   private StringProperty name;
   private IntegerProperty lastBuildNumber;
   private ObjectProperty< BuildResultStatus > lastBuildStatus;
   private ObjectProperty< BuildState > buildState;
   
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

}//End Class
