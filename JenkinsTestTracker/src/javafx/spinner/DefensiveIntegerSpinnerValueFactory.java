/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package javafx.spinner;

import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;

/**
 * Defensive extension of the {@link IntegerSpinnerValueFactory} that does not crash when
 * an unexpected value is found.
 */
public class DefensiveIntegerSpinnerValueFactory extends IntegerSpinnerValueFactory {

   /**
    * Constructs a new {@link DefensiveIntegerSpinnerValueFactory}, setting the {@link DefensiveIntegerStringConverter}
    * as the {@link StringConverter}.
    * @param min see {@link DefensiveIntegerSpinnerValueFactory#getMin()}.
    * @param max see {@link DefensiveIntegerSpinnerValueFactory#getMax()}.
    */
   public DefensiveIntegerSpinnerValueFactory( int min, int max ) {
      super( min, max );
      setConverter( new DefensiveIntegerStringConverter( valueProperty() ) );
   }//End Constructor

}//End Class
