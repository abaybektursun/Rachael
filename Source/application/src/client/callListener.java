package client;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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

        try
        {
            ServerSocket serverSocket = new ServerSocket(session.getDefaultPort());

            Runnable listen = new Runnable() {
                @Override
                public void run() {

                    try {

                        Socket socket = serverSocket.accept();
                        System.out.println("Accept");
                        Platform.runLater(new Runnable() {
                            @Override public void run() { videoStage.show(); }
                        });
                        videoControl.startCallReceiver(socket);

                        //future.cancel(false);
                    }
                    //catch(SocketException ex){/*System.out.println("Socket is closed");*/ex.printStackTrace();}
                    catch(Exception ioex){ioex.printStackTrace();}
                }
            };
            executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleAtFixedRate(listen, 0, 1, TimeUnit.SECONDS);
        }
        catch (Exception e) {
            System.err.println("Could not start the server on port: " + session.getDefaultPort());
            e.printStackTrace();
        }


        return null;
    }
}
