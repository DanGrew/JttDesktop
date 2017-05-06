/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.components.themebuilder;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableMap;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.utility.observable.FunctionMapChangeListenerImpl;

/**
 * Specific implementation of {@link FunctionMapChangeListenerImpl} for filtering notifications
 * based on the associated {@link BuildResultStatus}.
 */
public class StatusFilterPropertyUpdater extends FunctionMapChangeListenerImpl< BuildResultStatus, Color >{

   /**
    * Constructs a new {@link StatusFilterPropertyUpdater}.
    * @param map the map notifying.
    * @param status the {@link BuildResultStatus} to filter for.
    * @param property the {@link ObjectProperty} to update.
    */
   public StatusFilterPropertyUpdater( 
            ObservableMap< BuildResultStatus, Color > map,
            BuildResultStatus status, 
            ObjectProperty< Color > property
   ) {
      super( 
               map, 
               ( k, v ) -> {
                  if ( k == status ) {
                     property.set( v );
                  }
               }, 
               ( k, v ) -> { /* do nothing if removed, leave previous value */ } 
      );
   }//End Constructor

}//End Class
