/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.layout;

import javafx.beans.property.ReadOnlyBooleanProperty;

/**
 * Interface defining any variation of a {@link BuildWall} for displaying {@link JenkinsJob}
 * build status.
 */
public interface BuildWall {

   /** 
    * Provides the {@link ReadOnlyBooleanProperty} indicating whether the {@link BuildWall} is empty or not.
    * @return the {@link ReadOnlyBooleanProperty}.
    */
   public ReadOnlyBooleanProperty emptyProperty();
   
}//End Interface
