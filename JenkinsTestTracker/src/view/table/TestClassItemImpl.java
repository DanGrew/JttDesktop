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
import javafx.scene.control.TreeTableView;
import model.tests.TestClass;

/**
 * The {@link TestClassItemImpl} provides an implementation of {@link TestTableItem}
 * for displaying information relating to {@link TestClass}es in the {@link TreeTableView}.
 */
public class TestClassItemImpl implements TestTableItem {

   private TestClass subject;
   private StringProperty description;
   private StringProperty duration;
   
   /**
    * Constructs a new {@link TestCaseItemImpl}.
    * @param testClass the {@link TestClass} associated.
    */
   public TestClassItemImpl( TestClass testClass ) {
      this.subject = testClass;
      description = new SimpleStringProperty( testClass.getDescription() );
      testClass.nameProperty().addListener( ( source, old, updated ) -> description.set( testClass.getDescription() ) );
      testClass.locationProperty().addListener( ( source, old, updated ) -> description.set( testClass.getDescription() ) );
      duration = new SimpleStringProperty( "" + testClass.durationProperty().get() );
      testClass.durationProperty().addListener( ( source, old, updated ) -> duration.set( "" + testClass.durationProperty().get() ) );
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public StringProperty getColumnProperty( int columnReference ) {
      switch ( columnReference ) {
         case 0:
            return description;
         case 1:
         case 2:
         case 3:
            return null;
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

}//End Class
