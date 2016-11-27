package client;

import javafx.concurrent.Task;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class callListener extends Task {
    Session session;
    ScheduledExecutorService executor;
    Stage videoStage;
    VideoController videoControl;
    static ScheduledFuture<?> future;


    public callListener(Session session, Stage videoStage, VideoController videoControl){
        this.session = session;
        this.videoStage = videoStage;
        this.videoControl = videoControl;
    }

    @Override
    public Void call() {
        try ( ServerSocket serverSocket = new ServerSocket(session.getDefaultPort()) ) {
            Runnable listen = new Runnable() {
                @Override
                public void run() {
                    try {
                        videoControl.startCallReceiver(serverSocket.accept());
                        videoStage.show();
                        future.cancel(false);
                    } catch(Exception ex){ex.printStackTrace();}


                }
            };
            executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleAtFixedRate(listen, 0, 50, TimeUnit.MILLISECONDS);
        }
        catch (Exception e) {
            System.err.println("Could not start the server on port: " + session.getDefaultPort());
            e.printStackTrace();
        }


        return null;
    }
}
