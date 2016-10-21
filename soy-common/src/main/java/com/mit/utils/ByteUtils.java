package com.mit.utils;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public class ByteUtils {
	private static int maxByte = 0x000000ff;

	public static byte[] intToByte(int val) {
		int size = 4;
		int shiftVal = val;
		byte[] bytes = new byte[size];
		for(int i = 1; i <= size; i++) {
			bytes[size - i] = (byte)(maxByte & shiftVal);
			shiftVal = val >>> 8;
		}
		return bytes;
	}

	public static int byteToInt(byte[] bytes) {
		int size = bytes.length > 4 ? 4 : bytes.length;
		int val = 0;
		for(int i = 0; i < size; i++) {
			val = val << 8;
			val = (val | bytes[i]);
		}
		return val;
	}

	public static byte[] longToByte(long val) {
		int size = 8;
		long shiftVal = val;
		byte[] bytes = new byte[size];
		for(int i = 1; i <= size; i++) {
			bytes[size - i] = (byte)(maxByte & shiftVal);
			shiftVal = val >>> 8;
		}
		return bytes;
	}

	public static long byteToLong(byte[] bytes) {
		int size = bytes.length > 8 ? 8 : bytes.length;
		long val = 0;
		for(int i = 0; i < size; i++) {
			val = (val | bytes[i]);
			if(i < size - 1) {
				val = val << 8;
			}
		}
		return val;
	}
    
    public static void releaseByteBuffer(ByteBuffer buf){
        if(buf != null && buf.isDirect()){
            try {
                Method cleaner = buf.getClass().getMethod("cleaner");
                cleaner.setAccessible(true);
                Method clean = Class.forName("sun.misc.Cleaner").getMethod("clean");
                clean.setAccessible(true);
                clean.invoke(cleaner.invoke(buf));
            } catch(Exception ex) { }
            buf = null;
        }
    }
    
}
