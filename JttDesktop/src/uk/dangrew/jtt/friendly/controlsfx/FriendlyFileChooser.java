/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.friendly.controlsfx;

import java.io.File;

import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

/**
 * The {@link FriendlyFileChooser} provides a friendlier interface to a {@link FileChooser}
 * since the {@link FileChooser} is final and launches graphical elements.
 */
public class FriendlyFileChooser {
   
   private final FileChooser fileChooser;
   
   /**
    * Constructs a new {@link FriendlyFileChooser}.
    */
   public FriendlyFileChooser() {
      fileChooser = new FileChooser();
   }//End Constructor

   /**
    * {@link FileChooser#showOpenDialog(Window)}.
    * @param ownerWindow the owner {@link Window}.
    * @return the {@link File} to open.
    */
   public File showOpenDialog( Window ownerWindow ) {
      return fileChooser.showOpenDialog( ownerWindow );
   }//End Method
   
   /**
    * {@link FileChooser#setTitle(String)}.
    * @param title the title.
    */
   public void setTitle( String title ) {
      fileChooser.setTitle( title );
   }//End Method

   /**
    * {@link FileChooser#setInitialDirectory(File)}.
    * @param file the initial directory.
    */
   public void setInitialDirectory( File file ) {
      fileChooser.setInitialDirectory( file );
   }//End Method

   /**
    * {@link FileChooser#getExtensionFilters()}.
    * @return the {@link uk.dangrew.jtt.javafx.stage.FileChooser.ExtensionFilter} {@link ObservableList}.
    */
   public ObservableList< ExtensionFilter > getExtensionFilters() {
      return fileChooser.getExtensionFilters();
   }//End Method

}//End Class
