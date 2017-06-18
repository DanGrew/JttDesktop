/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class ContentAreaRemoverTest {

   private static final double DIMENSION = 1000;
   
   private ContentArea area1;
   private ContentArea area2;
   
   private ObservableList< Node > nodes;
   private ContentAreaRemover systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      nodes = FXCollections.observableArrayList();
      
      area1 = new ContentArea( 
               new ContentBoundary( 0 ),
               new ContentBoundary( 40 ),
               new ContentBoundary( 20 ),
               new ContentBoundary( 70 ),
               DIMENSION, DIMENSION 
      );
      
      area2 = new ContentArea( 
               new ContentBoundary( 50 ),
               new ContentBoundary( 80 ),
               new ContentBoundary( 51 ),
               new ContentBoundary( 82 ),
               DIMENSION, DIMENSION 
      );
      
      //throw in something different to make sure it copes
      nodes.add( new GridPane() );
      nodes.add( area1 );
      nodes.add( area2 );
      
      systemUnderTest = new ContentAreaRemover();
      systemUnderTest.setNodes( nodes );
   }//End Method

   @Test public void shouldRemoveNoneIfAllBoundariesPresent(){
      systemUnderTest.verifyBoundaries( Arrays.asList( area1, area2 ) );
      shouldContainAreas( area1, area2 );
   }//End Method
   
   @Test public void shouldOnlyConsiderGivenAreas(){
      area2.setLeftBoundary( null );
      systemUnderTest.verifyBoundaries( Arrays.asList( area1 ) );

      shouldContainAreas( area1, area2 );
   }//End Method
   
   @Test public void shouldRemoveMultipleAreas(){
      area1.setTopBoundary( null );
      area2.setLeftBoundary( null );
      systemUnderTest.verifyBoundaries( Arrays.asList( area1, area2 ) );

      shouldNotContainAreas( area1, area2 );
   }//End Method
   
   @Test public void shouldRemoveAreaIfLeftBoundaryMissing(){
      area1.setLeftBoundary( null );
      systemUnderTest.verifyBoundaries( Arrays.asList( area1 ) );

      shouldContainAreas( area2 );
      shouldNotContainAreas( area1 );
      shouldClearAllBoundaries( area1 );
   }//End Method
   
   @Test public void shouldRemoveAreaIfTopBoundaryMissing(){
      area1.setTopBoundary( null );
      systemUnderTest.verifyBoundaries( Arrays.asList( area1 ) );

      shouldContainAreas( area2 );
      shouldNotContainAreas( area1 );
      shouldClearAllBoundaries( area1 );
   }//End Method
   
   @Test public void shouldRemoveAreaIfRightBoundaryMissing(){
      area1.setRightBoundary( null );
      systemUnderTest.verifyBoundaries( Arrays.asList( area1 ) );

      shouldContainAreas( area2 );
      shouldNotContainAreas( area1 );
      shouldClearAllBoundaries( area1 );
   }//End Method
   
   @Test public void shouldRemoveAreaIfBottomBoundaryMissing(){
      area1.setBottomBoundary( null );
      systemUnderTest.verifyBoundaries( Arrays.asList( area1 ) );

      shouldContainAreas( area2 );
      shouldNotContainAreas( area1 );
      shouldClearAllBoundaries( area1 );
   }//End Method
   
   private void shouldClearAllBoundaries( ContentArea area ){
      assertThat( area.leftBoundary(), is( nullValue() ) );
      assertThat( area.topBoundary(), is( nullValue() ) );
      assertThat( area.rightBoundary(), is( nullValue() ) );
      assertThat( area.bottomBoundary(), is( nullValue() ) );
   }//End Method
   
   private void shouldContainAreas( ContentArea... areas ) {
      for ( ContentArea area : areas ) {
         assertThat( nodes.contains( area ), is( true ) );
      }
   }//End Method
   
   private void shouldNotContainAreas( ContentArea... areas ) {
      for ( ContentArea area : areas ) {
         assertThat( nodes.contains( area ), is( false ) );
      }
   }//End Method
   
}//End Class
