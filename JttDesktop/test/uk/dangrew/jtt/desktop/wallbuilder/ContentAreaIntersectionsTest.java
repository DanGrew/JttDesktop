/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import org.junit.Before;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class ContentAreaIntersectionsTest {

   private static final double PARENT_WIDTH = 1000;
   private static final double PARENT_HEIGHT = 1000;
   
   private ContentArea area1;
   private ContentAreaAsserter area1Asserter;
   private ContentArea area2;
   private ContentAreaAsserter area2Asserter;
   private ContentArea area3;
   private ContentAreaAsserter area3Asserter;
   
   private ObservableList< Node > areas;
   private ContentAreaIntersections systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      areas = FXCollections.observableArrayList();
      
      //test non content areas are ignored
      areas.add( new GridPane() );
      
      area1 = new ContentArea( 
               PARENT_WIDTH, PARENT_HEIGHT, 
               0, 0, 
               50, 50 
      );
      area1Asserter = new ContentAreaAsserter( area1 )
         .withDimensionPercentages( 50, 50 )
         .withDimensions( 500, 500 )
         .withPositionPercentages( 0, 0 )
         .withTranslation( 0, 0 );
      
      area2 = new ContentArea( 
               PARENT_WIDTH, PARENT_HEIGHT, 
               90, 90, 
               10, 10 
      );
      area2Asserter = new ContentAreaAsserter( area2 )
         .withDimensionPercentages( 10, 10 )
         .withDimensions( 100, 100 )
         .withPositionPercentages( 90, 90 )
         .withTranslation( 900, 900 );
      
      area3 = new ContentArea( 
               PARENT_WIDTH, PARENT_HEIGHT, 
               50, 50, 
               40, 40 
      );
      area3Asserter = new ContentAreaAsserter( area3 )
               .withDimensionPercentages( 40, 40 )
               .withDimensions( 400, 400 )
               .withPositionPercentages( 50, 50 )
               .withTranslation( 500, 500 );
      
      systemUnderTest = new ContentAreaIntersections();
      systemUnderTest.setNodes( areas );
   }//End Method
   
   @Test public void shouldIgnoreAreasWithNoIntersections(){
      areas.add( area1 );
      area1Asserter.assertArea();
      
      areas.add( area2 );
      area1Asserter.assertArea();
      area2Asserter.assertArea();
      
      areas.add( area3 );
      area1Asserter.assertArea();
      area2Asserter.assertArea();
      area3Asserter.assertArea();
   }//End Method

   @Test public void shouldChangeTranslationXForDetectedIntersection() {
      areas.addAll( area1, area3 );
      
      area3.changeXPositionPercentageBy( -15 );
      systemUnderTest.checkTranslationXIntersection( area3 );
      
      new ContentAreaAsserter( area1 )
         .withDimensionPercentages( 35, 50 )
         .withDimensions( 350, 500 )
         .withPositionPercentages( 0, 0 )
         .withTranslation( 0, 0 )
         .assertArea();
      new ContentAreaAsserter( area3 )
         .withDimensionPercentages( 55, 40 )
         .withDimensions( 550, 400 )
         .withPositionPercentages( 35, 50 )
         .withTranslation( 350, 500 )
         .assertArea();
   }//End Method
   
   @Test public void shouldChangeTranslationYForDetectedIntersection() {
      areas.addAll( area1, area3 );
      
      area3.changeYPositionPercentageBy( -15 );
      systemUnderTest.checkTranslationYIntersection( area3 );
      
      new ContentAreaAsserter( area1 )
         .withDimensionPercentages( 50, 35 )
         .withDimensions( 500, 350 )
         .withPositionPercentages( 0, 0 )
         .withTranslation( 0, 0 )
         .assertArea();
      new ContentAreaAsserter( area3 )
         .withDimensionPercentages( 40, 55 )
         .withDimensions( 400, 550 )
         .withPositionPercentages( 50, 35 )
         .withTranslation( 500, 350 )
         .assertArea();
   }//End Method
   
   @Test public void shouldChangeWidthForDetectedIntersection() {
      areas.addAll( area1, area3 );
      
      area1.changeWidthPercentageBy( 15 );
      systemUnderTest.checkWidthIntersection( area1 );
      
      new ContentAreaAsserter( area1 )
         .withDimensionPercentages( 65, 50 )
         .withDimensions( 650, 500 )
         .withPositionPercentages( 0, 0 )
         .withTranslation( 0, 0 )
         .assertArea();
      new ContentAreaAsserter( area3 )
         .withDimensionPercentages( 25, 40 )
         .withDimensions( 250, 400 )
         .withPositionPercentages( 65, 50 )
         .withTranslation( 650, 500 )
         .assertArea();
   }//End Method
   
   @Test public void shouldChangeHeightForDetectedIntersection() {
      areas.addAll( area1, area3 );
      
      area1.changeHeightPercentageBy( 15 );
      systemUnderTest.checkHeightIntersection( area1 );
      
      new ContentAreaAsserter( area1 )
         .withDimensionPercentages( 50, 65 )
         .withDimensions( 500, 650 )
         .withPositionPercentages( 0, 0 )
         .withTranslation( 0, 0 )
         .assertArea();
      new ContentAreaAsserter( area3 )
         .withDimensionPercentages( 40, 25 )
         .withDimensions( 400, 250 )
         .withPositionPercentages( 50, 65 )
         .withTranslation( 500, 650 )
         .assertArea();
   }//End Method
   
}//End Class
