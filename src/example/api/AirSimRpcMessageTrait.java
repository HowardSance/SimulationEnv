package api;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import org.msgpack.MessagePackable;
import org.msgpack.packer.Packer;
import org.msgpack.type.ValueType;
import org.msgpack.unpacker.Unpacker;


public class AirSimRpcMessageTrait implements MessagePackable{
	public void writeTo(Packer pk) throws IOException {
		
		Field[] fields = this.getClass().getDeclaredFields();
		 pk.writeMapBegin(fields.length);
		 
		 for (Field f : fields) {
			String key = f.getName();
			
			pk.write(key);
		
			try {
				pk.write(f.get(this));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		 pk.writeMapEnd();
	}
	
	public void readFrom(Unpacker u) throws IOException {
		if(u.getNextType().equals(ValueType.MAP)) {
			u.readMapBegin();
			
			HashMap<String, Field> fields = new HashMap<String, Field>();
			for (Field f : this.getClass().getDeclaredFields()) {
				fields.put(f.getName(), f);
				
			}
			
			while(!u.trySkipNil() && u.getNextType()==ValueType.RAW) {
				String key = u.readString();
				
				Field fie = fields.get(key);
				if(fie==null) {
					System.out.println("Fields not found");
				}
				
			 Object value = u.read(fie.getType());
			 try {
				 this.getClass().getDeclaredField(key).set(this, value);
				
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			}
			
			u.readMapEnd();
			
		}
		
		
	}

}