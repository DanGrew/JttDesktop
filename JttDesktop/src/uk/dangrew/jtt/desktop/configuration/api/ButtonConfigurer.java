/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.api;

import java.util.function.Function;

import javafx.scene.control.Button;

/**
 * The {@link ButtonConfigurer} provides a {@link FunctionalInterface} for configuring {@link JenkinsConnectionControls}.
 */
@FunctionalInterface
interface ButtonConfigurer {
   
   /**
    * Class to redirect the {@link JenkinsControlButtonStates} to a {@link ButtonConfigurer}.
    */
   public static class StateTranslator implements Function< JenkinsControlButtonStates, ButtonConfigurer > {

      /**
       * {@inheritDoc}
       */
      @Override public ButtonConfigurer apply( JenkinsControlButtonStates t ) {
         return t;
      }//End Method
      
   }//End Class

   /**
    * Method to configure the given {@link Button}s and {@link JenkinsConnectionControls} actions to use.
    * @param first the first {@link Button}.
    * @param second the second {@link Button}.
    * @param third the third {@link Button}.
    * @param controls for directing action.
    */
   public void configure( Button first, Button second, Button third, JenkinsConnectionControls controls );
   
}//End Interface
