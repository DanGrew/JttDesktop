/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.controls;

/**
 * The {@link DirectionControlListener} provides an interface for listening to {@link DirectionControlType}
 * {@link javafx.scene.control.Button} presses on the {@link DirectionControls}.
 */
@FunctionalInterface
public interface DirectionControlListener {

   /**
    * Method called when a {@link javafx.scene.control.Button} is pressed associated with the 
    * given {@link DirectionControlType}.
    * @param type the {@link DirectionControlType} for the direction pressed. 
    */
   public void action( DirectionControlType type );
   
}//End Interface
