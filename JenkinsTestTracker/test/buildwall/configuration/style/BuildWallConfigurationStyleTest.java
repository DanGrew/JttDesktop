/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration.style;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import graphics.JavaFxInitializer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.text.FontWeight;
import javafx.spinner.DefensiveDoubleSpinnerValueFactory;
import javafx.spinner.DefensiveIntegerSpinnerValueFactory;
import javafx.spinner.DoublePropertySpinner;
import javafx.spinner.IntegerPropertySpinner;
import utility.TestCommon;

/**
 * {@link BuildWallConfigurationStyle} test.
 */
public class BuildWallConfigurationStyleTest {

   private BuildWallConfigurationStyle systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      JavaFxInitializer.startPlatform();
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new BuildWallConfigurationStyle();
   }//End Method
   
   @Test public void shouldProvideLabelWithBoldFont() {
      final String text = "anything";
      Label label = systemUnderTest.createBoldLabel( text );
      
      assertThat( label.getText(), is( text ) );
      assertThat( FontWeight.findByName( label.getFont().getStyle() ), is( FontWeight.BOLD ) );
   }//End Method
   
   @Test public void shouldProvideLabelWithBoldFontAndSize() {
      final String text = "anything";
      final double size = 34;
      Label label = systemUnderTest.createBoldLabel( text, size );
      
      assertThat( label.getText(), is( text ) );
      assertThat( FontWeight.findByName( label.getFont().getStyle() ), is( FontWeight.BOLD ) );
      assertThat( label.getFont().getSize(), closeTo( size, TestCommon.precision() ) );
   }//End Method
   
   @Test public void shouldConfigureIntegerPropertySpinner(){
      IntegerProperty property = new SimpleIntegerProperty();
      IntegerPropertySpinner spinner = new IntegerPropertySpinner();
      final int min = 20;
      final int max = 40;
      final int step = 5;
      
      systemUnderTest.configureIntegerSpinner( spinner, property, min, max, step );
      
      assertThat( spinner.getValueFactory(), instanceOf( DefensiveIntegerSpinnerValueFactory.class ) );
      
      spinner.getValueFactory().setValue( 50 );
      assertThat( spinner.getValueFactory().getValue(), is( max ) );
      
      spinner.getValueFactory().setValue( 10 );
      assertThat( spinner.getValueFactory().getValue(), is( min ) );
      
      spinner.increment();
      assertThat( spinner.getValueFactory().getValue(), is( min + step ) );
      assertThat( property.intValue(), is( min + step ) );
      
      final int newValue = 35;
      property.setValue( newValue );
      assertThat( spinner.getValueFactory().getValue(), is( newValue ) );
      
      assertThat( spinner.isEditable(), is( true ) );
      assertThat( spinner.getMaxWidth(), is( Double.MAX_VALUE ) );
   }//End Method
   
   @Test public void shouldConfigureDoublePropertySpinner(){
      DoubleProperty property = new SimpleDoubleProperty();
      DoublePropertySpinner spinner = new DoublePropertySpinner();
      final double min = 20.5;
      final double max = 40.1;
      final double step = 5.6;
      
      systemUnderTest.configureDoubleSpinner( spinner, property, min, max, step );
      
      assertThat( spinner.getValueFactory(), instanceOf( DefensiveDoubleSpinnerValueFactory.class ) );
      
      spinner.getValueFactory().setValue( 50.0 );
      assertThat( spinner.getValueFactory().getValue(), is( max ) );
      
      spinner.getValueFactory().setValue( 10.4 );
      assertThat( spinner.getValueFactory().getValue(), is( min ) );
      
      spinner.increment();
      assertThat( spinner.getValueFactory().getValue(), is( min + step ) );
      assertThat( property.doubleValue(), is( min + step ) );
      
      final double newValue = 35.7;
      property.setValue( newValue );
      assertThat( spinner.getValueFactory().getValue(), is( newValue ) );
      
      assertThat( spinner.isEditable(), is( true ) );
      assertThat( spinner.getMaxWidth(), is( Double.MAX_VALUE ) );
   }//End Method

}//End Class
