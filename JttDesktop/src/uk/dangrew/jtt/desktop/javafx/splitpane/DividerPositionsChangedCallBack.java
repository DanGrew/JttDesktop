/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.splitpane;

/**
 * The {@link DividerPositionsChangedCallBack} provides a bespoke {@link FunctionalInterface}
 * for calling back when the divider position within a {@link javafx.scene.control.SplitPane.Divider} has changed.
 */
@FunctionalInterface public interface DividerPositionsChangedCallBack {
   
   /**
    * Method to be called when the {@link javafx.scene.control.SplitPane.Divider#positionProperty()}
    * changes.
    */
   public void dividerPositionsChanged();

}//End Interface
