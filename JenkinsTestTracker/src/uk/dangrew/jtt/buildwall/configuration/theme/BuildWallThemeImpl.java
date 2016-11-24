/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.theme;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableMap;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.utility.synchronisation.SynchronizedObservableMap;

/**
 * Basic implementation of the {@link BuildWallTheme}.
 */
public class BuildWallThemeImpl implements BuildWallTheme {

   private final ObjectProperty< String > nameProperty;
   private final ObservableMap< BuildResultStatus, Color > barColoursMap;
   private final ObservableMap< BuildResultStatus, Color > trackColoursMap;
   private final ObservableMap< BuildResultStatus, Color > jobNameColoursMap;
   private final ObservableMap< BuildResultStatus, Color > buildNumberColoursMap;
   private final ObservableMap< BuildResultStatus, Color > completionEstimateColoursMap;
   private final ObservableMap< BuildResultStatus, Color > detailColoursMap;
   
   /**
    * Constructs a new {@link BuildWallThemeImpl}.
    * @param name the name of the theme, must not be null or empty.
    */
   public BuildWallThemeImpl( String name ) {
      if ( name == null || name.trim().isEmpty() ) {
         throw new IllegalArgumentException( "Invalid theme name provided " + name );
      }
      
      this.nameProperty = new SimpleObjectProperty<>( name );
      this.barColoursMap = new SynchronizedObservableMap<>();
      this.trackColoursMap = new SynchronizedObservableMap<>();
      this.jobNameColoursMap = new SynchronizedObservableMap<>();
      this.buildNumberColoursMap = new SynchronizedObservableMap<>();
      this.completionEstimateColoursMap = new SynchronizedObservableMap<>();
      this.detailColoursMap = new SynchronizedObservableMap<>();
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public ReadOnlyObjectProperty< String > nameProperty() {
      return nameProperty;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObservableMap< BuildResultStatus, Color > barColoursMap() {
      return barColoursMap;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObservableMap< BuildResultStatus, Color > trackColoursMap() {
      return trackColoursMap;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObservableMap< BuildResultStatus, Color > jobNameColoursMap() {
      return jobNameColoursMap;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObservableMap< BuildResultStatus, Color > buildNumberColoursMap() {
      return buildNumberColoursMap;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObservableMap< BuildResultStatus, Color > completionEstimateColoursMap() {
      return completionEstimateColoursMap;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObservableMap< BuildResultStatus, Color > detailColoursMap() {
      return detailColoursMap;
   }//End Method

}//End Class
