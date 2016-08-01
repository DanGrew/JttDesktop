/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.view.tree;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.mc.view.item.NotificationTreeItem;

/**
 * {@link NotificationTreeController} test.
 */
public class NotificationTreeControllerTest {

   @Mock private NotificationTreeItem item;
   @Mock private NotificationTreeLayoutManager layoutManager;
   private NotificationTreeController systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new NotificationTreeController( layoutManager );
   }//End Method
   
   @Test public void shouldAddNewItem() {
      systemUnderTest.addItem( item );
      verify( layoutManager ).add( item );
   }//End Method
   
   @Test public void shouldRemoveItem(){
      systemUnderTest.removeItem( item );
      verify( layoutManager ).remove( item );
   }//End Method

}//End Class
