/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.tree.structure;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.List;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;
import uk.dangrew.jtt.desktop.javafx.tree.structure.Tree;
import uk.dangrew.jtt.desktop.javafx.tree.structure.TreeController;
import uk.dangrew.jtt.desktop.javafx.tree.structure.TreeLayout;

/**
 * {@link Tree} test.
 */
public class TreeTest {
   
   @Mock private Object insertColumnsKey, constructLayoutKey, constructControllerKey, performInitialLayoutKey;
   @Mock private List< Object > methodExecutions;
   
   @Mock private TestTreeValueItem item;
   @Mock private TreeLayout< TestTreeValueItem, TestTreeValueItem > layout;
   @Mock private TreeController< TestTreeValueItem, TestTreeValueItem, TreeLayout< TestTreeValueItem, TestTreeValueItem > > controller;
   private Tree< 
      TestTreeValueItem, 
      TestTreeValueItem, 
      TreeLayout< TestTreeValueItem, TestTreeValueItem >, 
      TreeController< TestTreeValueItem, TestTreeValueItem, TreeLayout< TestTreeValueItem, TestTreeValueItem > > 
   > systemUnderTest;
   
   /** Test extension.**/
   private class TestTree extends Tree< 
         TestTreeValueItem, 
         TestTreeValueItem, 
         TreeLayout< TestTreeValueItem, TestTreeValueItem >,  
         TreeController< TestTreeValueItem, TestTreeValueItem, TreeLayout< TestTreeValueItem, TestTreeValueItem > > 
   > {
      
      /** {@inheritDoc} */
      @Override protected void insertColumns() {
         methodExecutions.add( insertColumnsKey );
      }//End Method

      /** {@inheritDoc} */
      @Override protected TreeLayout< TestTreeValueItem, TestTreeValueItem > constructLayout() {
         methodExecutions.add( constructLayoutKey );
         return layout;
      }//End Method

      /** {@inheritDoc} */
      @Override protected TreeController< 
            TestTreeValueItem, 
            TestTreeValueItem, 
            TreeLayout< TestTreeValueItem, TestTreeValueItem > 
      > constructController() {
         methodExecutions.add( constructControllerKey );
         return controller;
      }//End Method

      /** {@inheritDoc} */
      @Override protected void performInitialLayout() {
         methodExecutions.add( performInitialLayoutKey );
      }//End Method
      
   }//End Class

   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new TestTree();
   }//End Method
   
   @Test public void shouldProvideLayoutAndController() {
      systemUnderTest.initialise();
      assertThat( systemUnderTest.getLayoutManager(), is( layout ) );
      assertThat( systemUnderTest.getController(), is( controller ) );
   }//End Method
   
   @Test public void shouldConstructRoot(){
      assertThat( systemUnderTest.getRoot(), is( notNullValue() ) );
      assertThat( systemUnderTest.getRoot().isExpanded(), is( true ) );
      assertThat( systemUnderTest.isShowRoot(), is( false ) );
   }//End Method
   
   @Test public void shouldConfigureTree(){
      assertThat( systemUnderTest.getSelectionModel().getSelectionMode(), is( SelectionMode.SINGLE ) );
      assertThat( systemUnderTest.getColumnResizePolicy(), is( TreeTableView.CONSTRAINED_RESIZE_POLICY ) );
   }//End Method
   
   @Test public void shouldNotInitialiseInConstructor(){
      verifyZeroInteractions( methodExecutions );
   }//End Method
   
   @Test public void shouldInitialiseTreeInCorrectSequence(){
      systemUnderTest.initialise();
      InOrder order = inOrder( methodExecutions );
      order.verify( methodExecutions ).add( insertColumnsKey );
      order.verify( methodExecutions ).add( constructLayoutKey );
      order.verify( methodExecutions ).add( performInitialLayoutKey );
      order.verify( methodExecutions ).add( constructControllerKey );
   }//End Method
   
   @Test public void shouldInsertColumnUsingGivenFunction(){
      @SuppressWarnings("unchecked")//mocking generics 
      Function< TestTreeValueItem, ObjectProperty< Node > > retriever = mock( Function.class );
      
      systemUnderTest.insertColumn( retriever );
      assertThat( systemUnderTest.getColumns(), hasSize( 1 ) );
      
      assertThat( systemUnderTest.getColumns().get( 0 ).getCellValueFactory().call( 
               new CellDataFeatures<>( systemUnderTest, null, new TreeItem<>() ) 
      ), is( nullValue() ) );
      verifyZeroInteractions( retriever );
      
      assertThat( systemUnderTest.getColumns().get( 0 ).getCellValueFactory().call( 
               new CellDataFeatures<>( systemUnderTest, null, new TreeItem<>( item ) ) 
      ), is( nullValue() ) );
      verify( retriever ).apply( item );
   }//End Method
   
   @Test public void shouldInsertMultipleColumnInOrder(){
      @SuppressWarnings("unchecked")//mocking generics 
      Function< TestTreeValueItem, ObjectProperty< Node > > retrieverA = mock( Function.class );
      @SuppressWarnings("unchecked")//mocking generics 
      Function< TestTreeValueItem, ObjectProperty< Node > > retrieverB = mock( Function.class );
      @SuppressWarnings("unchecked")//mocking generics 
      Function< TestTreeValueItem, ObjectProperty< Node > > retrieverC = mock( Function.class );
      
      systemUnderTest.insertColumn( retrieverA );
      systemUnderTest.insertColumn( retrieverB );
      systemUnderTest.insertColumn( retrieverC );
      assertThat( systemUnderTest.getColumns(), hasSize( 3 ) );
      
      assertThat( systemUnderTest.getColumns().get( 0 ).getCellValueFactory().call( 
               new CellDataFeatures<>( systemUnderTest, null, new TreeItem<>( item ) ) 
      ), is( nullValue() ) );
      verify( retrieverA ).apply( item );
      
      assertThat( systemUnderTest.getColumns().get( 1 ).getCellValueFactory().call( 
               new CellDataFeatures<>( systemUnderTest, null, new TreeItem<>( item ) ) 
      ), is( nullValue() ) );
      verify( retrieverB ).apply( item );
      
      assertThat( systemUnderTest.getColumns().get( 2 ).getCellValueFactory().call( 
               new CellDataFeatures<>( systemUnderTest, null, new TreeItem<>( item ) ) 
      ), is( nullValue() ) );
      verify( retrieverC ).apply( item );
   }//End Method

}//End Class
