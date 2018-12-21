package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    public TextField pretraga;
    public Button traziBtn;
    public Button prekiniBtn;
    public ListView<String> lista;
    public File root = new File(System.getProperty("user.home"));
    private Thread thread;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        traziBtn.setDisable(false);
        prekiniBtn.setDisable(true);
    }


    private class Pretraga implements Runnable {
        private File korijen;

        public Pretraga(String home) {
            this.korijen = new File(home);
        }

        @Override
        public void run() {
            pretrazi(korijen);
        }

        public void pretrazi(File trenutni) {
            if (!traziBtn.isDisabled())
                return;
            if (trenutni.isDirectory()) {
                File[] listFiles = trenutni.listFiles();
                if (listFiles == null)
                    return;
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        pretrazi(file);
                    }
                    if (file.isFile()) {
                        if (file.getName().contains(pretraga.getText()))
                            Platform.runLater(() -> lista.getItems().add(file.getAbsolutePath()));
                    }
                }
            }
        }
    }

    public void traziBtn(MouseEvent actionEvent) {
        traziBtn.setDisable(true);
        prekiniBtn.setDisable(false);
        lista.getSelectionModel().clearSelection();
        lista.getItems().clear();
        String f = System.getProperty("user.home");
        Pretraga pretraga = new Pretraga(f);
        Thread thread = new Thread(pretraga);
        thread.start();
    }

    public void prekiniBtn(MouseEvent actionEvent) {
        prekiniBtn.setDisable(true);
        traziBtn.setDisable(false);
    }

}

