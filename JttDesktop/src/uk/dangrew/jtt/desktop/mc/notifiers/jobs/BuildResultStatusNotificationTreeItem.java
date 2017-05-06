/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.notifiers.jobs;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.mc.model.Notification;
import uk.dangrew.jtt.desktop.mc.resources.ManagementConsoleImages;
import uk.dangrew.jtt.desktop.mc.view.ManagementConsoleStyle;
import uk.dangrew.jtt.desktop.mc.view.item.NotificationTreeItem;
import uk.dangrew.jtt.desktop.mc.view.tree.NotificationTreeController;

/**
 * {@link BuildResultStatusNotificationTreeItem} represents a {@link BuildResultStatusNotification}
 * in the {@link uk.dangrew.jtt.mc.view.tree.NotificationTree}.
 */
public class BuildResultStatusNotificationTreeItem implements NotificationTreeItem {

   static final double STATUS_PROPORTION = 10;
   static final double TITLE_PROPORTION = 20;
   static final double DESCRIPTION_PROPORTION = 55;
   static final double PEOPLE_PROPORTION = 7.5;
   static final double CLOSE_PROPORTION = 7.5;
   
   static final double PREFERRED_ROW_HEIGHT = 20;
   static final double PREFERRED_IMAGE_HEIGHT = 20;
   static final double PREFERRED_IMAGE_WIDTH = 20;  
   
   private final NotificationTreeController controller;
   private final ChangeIdentifier changeIdentifier;
   private final JavaFxStyle javaFxStyling;
   private final ManagementConsoleStyle mcStyling;
   private final ManagementConsoleImages images;
   private final BuildResultStatusNotification notification;
   private final ObjectProperty< Node > contentProperty;
   
   private final Node status;
   private final Node title;
   private final Node description;
   private final Node people;
   private final Node close;
   
   /**
    * Constructs a new {@link BuildResultStatusNotificationTreeItem}.
    * @param notification the {@link BuildResultStatusNotification} associated.
    * @param controller the {@link NotificationTreeController} for instructions.
    */
   public BuildResultStatusNotificationTreeItem( BuildResultStatusNotification notification, NotificationTreeController controller ) {
      this( notification, controller, new ChangeIdentifier(), new JavaFxStyle(), new ManagementConsoleStyle(), new ManagementConsoleImages() );
   }//End Constructor
   
   /**
    * Constructs a new {@link BuildResultStatusNotificationTreeItem}.
    * @param notification the {@link BuildResultStatusNotification} associated.
    * @param controller the {@link NotificationTreeController} for instructions.
    * @param changeIdentifier the {@link ChangeIdentifier} for identifying change type.
    * @param stying the {@link JavaFxStyle} to apply.
    * @param mcStyling the {@link ManagementConsoleStyle}.
    * @param images the {@link ManagementConsoleImages} available.
    */
   BuildResultStatusNotificationTreeItem( 
            BuildResultStatusNotification notification, 
            NotificationTreeController controller,
            ChangeIdentifier changeIdentifier, 
            JavaFxStyle stying,
            ManagementConsoleStyle mcStyling,
            ManagementConsoleImages images
   ) {
      if ( notification == null || controller == null ) {
         throw new IllegalArgumentException( "Arguments must not be null." );
      }
      
      this.notification = notification;
      this.controller = controller;
      this.changeIdentifier = changeIdentifier;
      this.javaFxStyling = stying;
      this.mcStyling = mcStyling;
      this.images = images;
      
      this.status = constructStatusImage();
      this.title = constructCenteredTitle();
      this.description = constructDescriptionLabel();
      this.people = constructPeopleImage();
      this.close = constructCloseImage();
      
      GridPane row = new GridPane();
      
      ColumnConstraints statusColumn = new ColumnConstraints();
      statusColumn.setPercentWidth( STATUS_PROPORTION );
      ColumnConstraints titleColumn = new ColumnConstraints();
      titleColumn.setPercentWidth( TITLE_PROPORTION );
      ColumnConstraints descriptionColumn = new ColumnConstraints();
      descriptionColumn.setPercentWidth( DESCRIPTION_PROPORTION );
      ColumnConstraints peopleColumn = new ColumnConstraints();
      peopleColumn.setPercentWidth( PEOPLE_PROPORTION );
      ColumnConstraints closeColumn = new ColumnConstraints();
      closeColumn.setPercentWidth( CLOSE_PROPORTION );
      row.getColumnConstraints().addAll( statusColumn, titleColumn, descriptionColumn, peopleColumn, closeColumn );
      
      row.add( status, 0, 0 );
      row.add( title, 1, 0 );
      row.add( description, 2, 0 );
      row.add( people, 3, 0 );
      row.add( close, 4, 0 );
      this.contentProperty = new SimpleObjectProperty<>( row );
   }//End Constructor
   
   /**
    * Method to construct the status {@link Image}.
    * @return the {@link ImageView}.
    */
   private Node constructStatusImage(){
      ImageView view = changeIdentifier.identifyChangeType( 
               notification.getPreviousBuildResultStatus(), 
               notification.getNewBuildResultStatus() 
      ).constructImage();
      view.setFitWidth( PREFERRED_IMAGE_WIDTH );
      view.setFitHeight( PREFERRED_IMAGE_HEIGHT );
      return view;
   }//End Method
   
   /**
    * Method to construct the title {@link Node}.
    * @return the {@link Node}.
    */
   private Node constructCenteredTitle(){
      return new BorderPane( javaFxStyling.createBoldLabel( notification.getJenkinsJob().nameProperty().get() ) );
   }//End Method
   
   /**
    * Method to construct the description {@link Node}.
    * @return the {@link Node}.
    */
   private Node constructDescriptionLabel(){
      Label actualLabel = javaFxStyling.createWrappedTextLabel( notification.getDescription() );
      actualLabel.setPrefHeight( PREFERRED_ROW_HEIGHT );
      return new BorderPane( actualLabel );
   }//End Method
   
   /**
    * Method to create {@link Button}s in the same style.
    * @param image the {@link Image} to place on the {@link Button}.
    * @return the {@link Button}.
    */
   private Button constructControl( Image image ){
      Button button = new Button();
      mcStyling.styleButtonSize( button, image );
      javaFxStyling.removeBackgroundAndColourOnClick( button, Color.GRAY );
      return button;
   }//End Method
   
   /**
    * Method to construct the people {@link Button}.
    * @return the {@link Node}.
    */
   private Node constructPeopleImage(){
      Button button = constructControl( images.constuctPeopleImage() );
      return new BorderPane( button );
   }//End Method
   
   /**
    * Method to construct the close {@link Button}.
    * @return the {@link Node}.
    */
   private Node constructCloseImage(){
      Button button = constructControl( images.constuctCloseImage() );
      button.setOnAction( event -> controller.remove( this ) );
      return new BorderPane( button );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Node > contentProperty() {
      return contentProperty;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public Notification getNotification() {
      return notification;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean hasController(NotificationTreeController controller) {
      return this.controller == controller;
   }//End Method
   
   Node status(){
      return status;
   }//End Method
   
   Node title(){
      return title;
   }//End Method
   
   Node description(){
      return description;
   }//End Method
   
   Node people(){
      return people;
   }//End Method
   
   Node close(){
      return close;
   }//End Method
   
}//End Class
