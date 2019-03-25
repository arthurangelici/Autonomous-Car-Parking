package parkingCar;

import java.util.EnumSet;
import java.util.Set;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MultiplePressedKeysEventHandler implements EventHandler<KeyEvent>
{
    private final Set<KeyCode> buffer = EnumSet.noneOf(KeyCode.class);

    public void handle(final KeyEvent event)
    {
        final KeyCode code = event.getCode();

        if (KeyEvent.KEY_PRESSED.equals(event.getEventType()))
        {
            buffer.add(code);
        }
        else if (KeyEvent.KEY_RELEASED.equals(event.getEventType()))
        {
            buffer.remove(code);
        }

        event.consume();
    }

    public boolean isPressed(final KeyCode key)
    {
        return buffer.contains(key);
    }
}
