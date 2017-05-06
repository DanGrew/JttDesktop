/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.tree.utility;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sun.javafx.application.PlatformImpl;

import javafx.scene.control.TreeTableView;
import javafx.scene.layout.Pane;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;
import uk.dangrew.jtt.desktop.javafx.tree.utility.ColumnHeaderHider;

/**
 * {@link ColumnHeaderHider} test.
 */
public class ColumnHeaderHiderTest {

   @Mock private Pane header;
   private WidthSettableTable table;
   private ColumnHeaderHider systemUnderTest;
   
   /** Extension to expose setWidth.**/
   private class WidthSettableTable extends TreeTableView< Object > {
      @Override protected void setWidth( double value ) {
         super.setWidth( value );
      }//End Method
   }//End Class
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      table = spy( new WidthSettableTable() );
      
      when( table.lookup( ColumnHeaderHider.TABLE_HEADER_ROW ) ).thenReturn( header );
      header.setMaxHeight( 1000 );
      header.setMinHeight( 1000 );
      header.setPrefHeight( 1000 );
      
      systemUnderTest = new ColumnHeaderHider();
   }//End Method
   
   @Test public void shoudlHideHeaderWhenTriggered() {
      systemUnderTest.hideColumnHeaders( table );
      
      table.setWidth( 100 );
      PlatformImpl.runAndWait( () -> {} );
      assertThat( header.getMaxHeight(), is( ColumnHeaderHider.ZERO_DIMENSION ) );
      assertThat( header.getMinHeight(), is( ColumnHeaderHider.ZERO_DIMENSION ) );
      assertThat( header.getPrefHeight(), is( ColumnHeaderHider.ZERO_DIMENSION ) );
      assertThat( header.isVisible(), is( false ) );
      assertThat( header.isManaged(), is( false ) );
   }//End Method
   
   @Test public void shouldIgnoreNullHeader(){
      when( table.lookup( ColumnHeaderHider.TABLE_HEADER_ROW ) ).thenReturn( null );
      systemUnderTest.hideColumnHeaders( table );
   }//End Method
   
   @Test public void shouldIgnoreInvisibleHeader(){
      header.setVisible( true );
      systemUnderTest.hideColumnHeaders( table );
      
      table.setPrefWidth( 100 );
      assertThat( header.getMaxHeight(), is( not( ColumnHeaderHider.ZERO_DIMENSION ) ) );
      assertThat( header.getMinHeight(), is( not( ColumnHeaderHider.ZERO_DIMENSION ) ) );
      assertThat( header.getPrefHeight(), is( not( ColumnHeaderHider.ZERO_DIMENSION ) ) );
      assertThat( header.isManaged(), is( true ) );
   }//End Method
   
}//End Class
