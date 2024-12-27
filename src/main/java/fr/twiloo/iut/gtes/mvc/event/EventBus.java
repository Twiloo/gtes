//package fr.twiloo.iut.gtes.mvc.event;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Consumer;
//
//public class EventBus {
//    private final List<Consumer<Event<?, ?>>> listeners = new ArrayList<>();
//
//    // Method to subscribe a listener
//    public void subscribe(Consumer<Event<?, ?>> listener) {
//        listeners.add(listener);
//    }
//
//    // Method to publish an event
//    public void publish(Event<?, ?> event) {
//        System.out.println("Event triggered: " + event.getName());
//        System.out.println("Content: " + event.getPayload());
//
//        for (Consumer<Event<?, ?>> listener : listeners) {
//            listener.accept(event);
//        }
//    }
//}
