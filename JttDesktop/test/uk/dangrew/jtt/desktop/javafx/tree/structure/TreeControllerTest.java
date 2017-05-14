/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.tree.structure;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * {@link TreeController} test.
 */
public class TreeControllerTest {
   
   @Mock private TestTreeValueItem item;
   @Mock private TreeLayout< TestTreeValueItem, TestTreeValueItem > layout;
   private TreeController< 
      TestTreeValueItem, 
      TestTreeValueItem, 
      TreeLayout< TestTreeValueItem, TestTreeValueItem > 
   > systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new TreeController<>( layout );
   }//End Method

   @Test public void shouldAddToLayout() {
      systemUnderTest.add( item );
      verify( layout ).add( item );
   }//End Method
   
   @Test public void shouldRemoveFromLayout() {
      systemUnderTest.remove( item );
      verify( layout ).remove( item );
   }//End Method
   
   @Test public void shouldUpdateLayout() {
      systemUnderTest.update( item );
      verify( layout ).update( item );
   }//End Method
   
   @Test public void shouldProvideLayoutManager(){
      assertThat( systemUnderTest.getLayoutManager(), is( layout ) );
   }//End Method

}//End Class
