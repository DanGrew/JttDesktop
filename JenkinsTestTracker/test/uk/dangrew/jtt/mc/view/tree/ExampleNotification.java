/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.view.tree;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import uk.dangrew.jtt.mc.view.item.NotificationTreeItem;

/**
 * Example {@link NotificationTreeItem} for demonstration purposes.
 */
public class ExampleNotification implements NotificationTreeItem {

   private ObjectProperty< Node > title;
   private ObjectProperty< Node > icon;
   private ObjectProperty< Node > label;
   private ObjectProperty< Node > actionButton;
   private ObjectProperty< Node > cancelButton;
   
   /**
    * Constructs a new {@link ExampleNotification} with example {@link Node}s.
    */
   public ExampleNotification() {
      title = new SimpleObjectProperty<>( new BorderPane( new Label( "Title" ) ) );
      
      Rectangle view = new Rectangle( 50, 50, Color.AQUAMARINE );
      icon = new SimpleObjectProperty<>( new BorderPane( view ) );
      
      Button actualButton = new Button( "+" );
      actualButton.setPrefSize( 20, 20 );
      actualButton.setAlignment( Pos.CENTER );
      actualButton.setOnAction( event -> System.out.println( "Pressed " + actualButton.toString() ) );
      actionButton = new SimpleObjectProperty<>( new BorderPane( actualButton ) );
      
      Button cancel = new Button( "x" );
      cancel.setPrefSize( 20, 20 );
      cancel.setOnAction( event -> System.out.println( "Cancelled " + cancel.toString() ) );
      cancelButton = new SimpleObjectProperty<>( new BorderPane( cancel ) );
      
      Label actualLabel = new Label( "lots of text describing something very insteresting "
               + "but difficult to fit on one line or in the company of other elements" );
      actualLabel.setWrapText( true );
      actualLabel.setPrefHeight( 50 );
      label = new SimpleObjectProperty<>( new BorderPane( actualLabel ) );
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Node > getNotificationType() {
      return title;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Node > getNotificationIcon() {
      return icon;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Node > getContent() {
      return label;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Node > getActionButton() {
      return actionButton;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Node > getCancelButton() {
      return cancelButton;
   }//End Method

}//End Class
