/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc;

import uk.dangrew.jtt.desktop.mc.notifiers.jobs.BuildResultStatusNotifier;
import uk.dangrew.jtt.desktop.mc.view.tree.NotificationEvent;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;

/**
 * The {@link NotificationCenter} is responsible for initialising the notifications for 
 * the system.
 */
public class NotificationCenter {

   private final BuildResultStatusNotifier notifier;
   
   /**
    * Constructs a new {@link NotificationCenter}.
    * @param database the {@link JenkinsDatabase} associated.
    */
   public NotificationCenter( JenkinsDatabase database ) {
      this.notifier = new BuildResultStatusNotifier();
      new NotificationEvent().register( event -> event.getValue().showDesktopNotification() );
   }//End Constructor
   
   BuildResultStatusNotifier buildResultStatusNotifier(){
      return notifier;
   }//End Method

}//End Class
