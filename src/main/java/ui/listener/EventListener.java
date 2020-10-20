package ui.listener;

public interface EventListener {
    void onResponse(Object event);
    void onError(Object message);
}
