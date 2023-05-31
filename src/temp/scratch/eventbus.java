import com.almasb.fxgl.event.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

// FirstController.java
public class FirstController implements Initializable {
    private EventBus eventBus;

    @FXML
    private void onButtonClick(ActionEvent event) {
        eventBus.fireEvent(new MyCustomEvent());
    }

    // Called by FXMLLoader after loading the FXML file
    public void initialize() {
        eventBus = new EventBus();
    }

    // Called by FXMLLoader to provide access to the event bus
    public EventBus getEventBus() {
        return eventBus;
    }
}

// SecondController.java
public class SecondController {
    @FXML
    private Label label;

    // Called by FXMLLoader after loading the FXML file
    public void initialize() {
        FirstController firstController = getFirstController();
        firstController.getEventBus().register(this);

        label.setText("Waiting for event...");
    }

    @Subscribe
    public void handleCustomEvent(MyCustomEvent event) {
        label.setText("Received event!");
    }

    private FirstController getFirstController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("path/to/first/fxml.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader.getController();
    }
}

// MyCustomEvent.java
public class MyCustomEvent {
}
