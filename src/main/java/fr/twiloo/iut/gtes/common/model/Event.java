package fr.twiloo.iut.gtes.common.model;

import fr.twiloo.iut.gtes.common.EventType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class Event<P> implements Serializable {
    private final EventType type;
    private P payload;

    public Event(EventType type, P payload) {
        this.type = type;
        this.payload = payload;
    }

    public EventType type() {
        return type;
    }

    public P payload() {
        return payload;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();  // Write non-transient fields
        if (payload instanceof List<?> list) {
            out.writeBoolean(true); // Indicate that the payload is a List and each element should be deserialized
            out.writeInt(list.size()); // Write the size of the list
            for (Object element : list) {
                out.writeUnshared(element); // Write each element as a unique object
            }
        } else {
            out.writeBoolean(false); // Indicate that the payload is not a List
            out.writeUnshared(payload); // Write the single object as a unique object
        }
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Read non-transient fields

        boolean isList = in.readBoolean(); // Check if the payload is a List
        if (isList) {
            int size = in.readInt();
            List<Object> list = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                Object element = in.readUnshared(); // Read each element as a unique object
                list.add(element);
            }
            payload = (P) list;
        } else {
            payload = (P) in.readUnshared(); // Read the single object as a unique object
        }
    }

     @Override
    public String toString() {
        return type + " " + payload;
     }
}