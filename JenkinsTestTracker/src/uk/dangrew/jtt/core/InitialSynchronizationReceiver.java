/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.core;

import uk.dangrew.sd.core.lockdown.DigestProgressReceiver;
import uk.dangrew.sd.core.message.Message;
import uk.dangrew.sd.core.progress.Progress;
import uk.dangrew.sd.core.source.Source;
import uk.dangrew.sd.progressbar.model.DigestProgressBar;

/**
 * The {@link InitialSynchronizationReceiver} is a {@link DigestProgressReceiver} that forwards
 * progress to a {@link DigestProgressReceiver}, instructing the {@link JttInitializer} when the 
 * prgress is complete.
 */
public class InitialSynchronizationReceiver implements DigestProgressReceiver {
      
   private final JttInitializer initializer;
   private final DigestProgressBar progressBar;
   
   /**
    * Constructs a new {@link InitialSynchronizationReceiver}.
    * @param initializer the {@link JttInitializer}.
    * @param progressBar the {@link DigestProgressReceiver}.
    */
   public InitialSynchronizationReceiver( JttInitializer initializer, DigestProgressBar progressBar ) {
      this.initializer = initializer;
      this.progressBar = progressBar;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void progress( Source source, Progress progress, Message message ) {
      progressBar.handleProgress( source, progress, message );
      if ( progress.isComplete() ) {
         initializer.systemReady();
      }
   }//End Method
      
}//End Class
