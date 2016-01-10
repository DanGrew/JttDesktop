/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package view.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.TreeTableView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.tests.TestCase;

/**
 * {@link TestCaseItemImpl} is an implementation of {@link TestTableItem} for displaying information
 * relating to a specific {@link TestCase} in the overall {@link TreeTableView}.
 */
public class TestCaseItemImpl implements TestTableItem {

   static final double DEFAULT_STATUS_GRAPHIC_RADIUS = 5;
   static final Color DEFAULT_PASS_COLOUR = Color.GREEN.brighter();
   static final Color DEFAULT_FAIL_COLOUR = Color.RED;
   static final Color DEFAULT_UNKNOWN_COLOUR = Color.GRAY;
   private TestCase subject;
   private StringProperty name;
   private StringProperty status;
   private StringProperty skipped;
   private StringProperty age;
   private StringProperty duration;
   private Circle statusGraphic;
   
   /**
    * Constructs a new {@link TestCaseItemImpl}.
    * @param testCase the associated {@link TestCase}.
    */
   public TestCaseItemImpl( TestCase testCase ) {
      this.subject = testCase;
      
      name = new SimpleStringProperty( testCase.nameProperty().get() );
      testCase.nameProperty().addListener( ( source, old, updated ) -> name.set( testCase.nameProperty().get() ) );
      
      status = new SimpleStringProperty( testCase.statusProperty().get().toString() );
      statusGraphic = new Circle( DEFAULT_STATUS_GRAPHIC_RADIUS );
      updateStatusGraphic();
      testCase.statusProperty().addListener( ( source, old, updated ) -> {
         status.set( testCase.statusProperty().get().toString() );
         updateStatusGraphic();
      } );
      
      skipped = new SimpleStringProperty( "" + testCase.skippedProperty().get() );
      testCase.skippedProperty().addListener( ( source, old, updated ) -> skipped.set( "" + updated ) );
      
      age = new SimpleStringProperty( "" + testCase.ageProperty().get() );
      testCase.ageProperty().addListener( ( source, old, updated ) -> age.set( "" + updated ) );
      
      duration = new SimpleStringProperty( "" + testCase.durationProperty().get() );
      testCase.durationProperty().addListener( ( source, old, updated ) -> duration.set( "" + updated ) );
   }//End Constructor
   
   /**
    * Method to update the {@link Color} of the {@link Circle} status graphic.
    */
   private void updateStatusGraphic() {
      switch ( subject.statusProperty().get() ) {
         case FAILED:
            statusGraphic.strokeProperty().set( DEFAULT_FAIL_COLOUR );
            statusGraphic.fillProperty().set( DEFAULT_FAIL_COLOUR );
            break;
         case PASSED:
            statusGraphic.strokeProperty().set( DEFAULT_PASS_COLOUR );
            statusGraphic.fillProperty().set( DEFAULT_PASS_COLOUR );
            break;
         default:
            statusGraphic.strokeProperty().set( DEFAULT_UNKNOWN_COLOUR );
            statusGraphic.fillProperty().set( DEFAULT_UNKNOWN_COLOUR );
            break;
      }
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public StringProperty getColumnProperty( int columnReference ) {
      switch ( columnReference ) {
         case 0:
            return name;
         case 1:
            return status;
         case 2:
            return skipped;
         case 3:
            return age;
         case 4:
            return duration;
         default:
            return null;   
      }
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public Object getSubject() {
      return subject;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public Node getStatusGraphic() {
      return statusGraphic;
   }//End Method

}//End Class
