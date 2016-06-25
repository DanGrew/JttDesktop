/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.javafx.splitpane;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.control.SplitPane;
import javafx.scene.control.SplitPane.Divider;

/**
 * The {@link SplitPaneDividerPositionListener} is responsible for listening to direct changes
 * to {@link javafx.scene.control.SplitPane.Divider#positionProperty()}s within a {@link SplitPane}.
 * New {@link javafx.scene.control.SplitPane.Divider}s are accounted for but do not trigger call backs.
 */
public class SplitPaneDividerPositionListener {

   private final SplitPane subject;
   
   private final ChangeListener< Number > commonListener;
   private final Set< Divider > positionDividers;
   
   /**
    * Constructs a new {@link SplitPaneDividerPositionListener}.
    * @param subject the {@link SplitPane} subject to monitor.
    * @param callBack the {@link DividerPositionsChangedCallBack} to trigger.
    */
   public SplitPaneDividerPositionListener( SplitPane subject, DividerPositionsChangedCallBack callBack ) {
      this.subject = subject;
      this.positionDividers = new HashSet<>();
      this.commonListener = ( source, old, updated ) -> callBack.dividerPositionsChanged();
      
      this.subject.getDividers().addListener( ( Change< ? extends Divider > change ) -> updateDividerRegistrations() );
      updateDividerRegistrations();
   }//End Constructor
   
   /**
    * Method to update the divider registrations, accounting for added and removed {@link javafx.scene.control.SplitPane.Divider}s.
    */
   private void updateDividerRegistrations(){
      Iterator< Divider > iterator = positionDividers.iterator();
      while( iterator.hasNext() ) {
         Divider divider = iterator.next();
         if ( !subject.getDividers().contains( divider ) ) {
            divider.positionProperty().removeListener( commonListener );
            iterator.remove();
         }
      }
      
      for ( Divider divider : subject.getDividers() ) {
         if ( !positionDividers.contains( divider ) ) {
            divider.positionProperty().addListener( commonListener );
            positionDividers.add( divider );
         }
      }
   }//End Method

}//End Class
