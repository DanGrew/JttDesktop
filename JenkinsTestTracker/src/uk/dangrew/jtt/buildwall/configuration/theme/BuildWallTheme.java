/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.theme;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableMap;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;

/**
 * The {@link BuildWallTheme} provides a interface to objects providing information for imposing
 * a theme on the build wall.
 */
public interface BuildWallTheme {

   /**
    * {@link ReadOnlyObjectProperty} for the name of the {@link BuildWallTheme}.
    * @return the {@link ReadOnlyObjectProperty}.
    */
   public ReadOnlyObjectProperty< String > nameProperty();
   
   /**
    * {@link ObservableMap} for the {@link Color}s associated with {@link uk.dangrew.jtt.buildwall.panel.JobProgressImpl}
    * bars.
    * @return the {@link ObservableMap}.
    */
   public ObservableMap< BuildResultStatus, Color > barColoursMap();
   
   /**
    * {@link ObservableMap} for the {@link Color}s associated with {@link uk.dangrew.jtt.buildwall.panel.JobProgressImpl}
    * track (within the bar).
    * @return the {@link ObservableMap}.
    */
   public ObservableMap< BuildResultStatus, Color > trackColoursMap();
   
   /**
    * {@link ObservableMap} for the {@link Color}s associated with {@link uk.dangrew.jtt.buildwall.panel.JobProgressImpl}
    * job name text.
    * @return the {@link ObservableMap}.
    */
   public ObservableMap< BuildResultStatus, Color > jobNameTextColoursMap();
   
   /**
    * {@link ObservableMap} for the {@link Color}s associated with {@link uk.dangrew.jtt.buildwall.panel.JobProgressImpl}
    * build number text.
    * @return the {@link ObservableMap}.
    */
   public ObservableMap< BuildResultStatus, Color > buildNumberTextColoursMap();
   
   /**
    * {@link ObservableMap} for the {@link Color}s associated with {@link uk.dangrew.jtt.buildwall.panel.JobProgressImpl}
    * completion estimate text.
    * @return the {@link ObservableMap}.
    */
   public ObservableMap< BuildResultStatus, Color > completionEstimateTextColoursMap();
   
   /**
    * {@link ObservableMap} for the {@link Color}s associated with {@link uk.dangrew.jtt.buildwall.panel.JobProgressImpl}
    * detail text.
    * @return the {@link ObservableMap}.
    */
   public ObservableMap< BuildResultStatus, Color > detailTextColoursMap();
   
}//End Method
