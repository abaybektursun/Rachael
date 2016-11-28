package client;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.*;

public class callListener extends Task {
    Session session;
    ScheduledExecutorService executor;
    Stage videoStage;
    VideoController videoControl;
    static ScheduledFuture<?> future;
    ExecutorService executionThreadPool;
    public volatile boolean listening = false;


    public callListener(Session session, Stage videoStage, VideoController videoControl){
        this.session = session;
        this.videoStage = videoStage;
        this.videoControl = videoControl;
        executionThreadPool = Executors.newCachedThreadPool();
    }

    @Override
    public Void call() {

        try {
            ServerSocket serverSocket = new ServerSocket(session.getDefaultPort());

            Task startListen = new Task<Void>() {
                @Override
                protected Void call() {
                    listening = false;
                    while (listening) {

                        try {

                            Socket socket = serverSocket.accept();
                            System.out.println("Accept");
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    videoStage.show();
                                }
                            });
                            videoControl.startCallReceiver(socket);
                            listening=false;

                            //future.cancel(false);
                        }
                        //catch(SocketException ex){/*System.out.println("Socket is closed");*/ex.printStackTrace();}
                        catch (Exception ioex) {
                            ioex.printStackTrace();
                        }
                    }
                    return null;
                }
            };
            executionThreadPool.submit(startListen);
            //executor = Executors.newSingleThreadScheduledExecutor();
            //executor.scheduleAtFixedRate(listen, 0, 1, TimeUnit.NANOSECONDS);
        }
        catch (Exception e) {
            System.err.println("Could not start the server on port: " + session.getDefaultPort());
            e.printStackTrace();
        }
        return null;

    }
}
