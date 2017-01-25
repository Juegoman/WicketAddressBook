package juegoman.addressbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

//Uses java serialization methods to serialize or deserialize a serializable Object.
public class SerializationHelper {
    public static byte[] serialize(Object o) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(o);
            return bos.toByteArray();
        }
    }
    
    public static Object deserialize(byte[] b) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);
             ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }
}
