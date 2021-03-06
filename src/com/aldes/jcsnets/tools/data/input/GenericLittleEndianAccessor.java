package com.aldes.jcsnets.tools.data.input;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * $File: GenericLittleEndianAccessor.java $
 * $Date: 2017-03-28 00:22:32 $
 * $Revision: $
 * $Creator: Jen-Chieh Shen $
 * $Notice: See LICENSE.txt for modification and distribution information
 *                   Copyright (c) 2017 by Shen, Jen-Chieh $
 */


/**
 * @class GenericLittleEndianAccessor
 * @brief
 */
public class GenericLittleEndianAccessor implements LittleEndianAccessor {

    private ByteInputStream bs;

    /**
     * Class constructor - Wraps the accessor around a stream of bytes.
     *
     * @param bs The byte stream to wrap the accessor around.
     */
    public GenericLittleEndianAccessor(ByteInputStream bs) {
        this.bs = bs;
    }

    /**
     * Read a single byte from the stream.
     *
     * @return The byte read.
     * @see net.sf.odinms.tools.data.input.ByteInputStream#readByte
     */
    @Override
    public byte readByte() {
        return (byte) bs.readByte();
    }
    
    /**
     * Reads a boolean.
     *
     * @return The boolean read.
     */
    @Override
    public boolean readBoolean() {
        return (readByte() != 0);
    }

    /**
     * Reads an integer from the stream.
     *
     * @return The integer read.
     */
    @Override
    public int readInt() {
        int byte1, byte2, byte3, byte4;
        byte1 = bs.readByte();
        byte2 = bs.readByte();
        byte3 = bs.readByte();
        byte4 = bs.readByte();
        return (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }

    /**
     * Reads a short integer from the stream.
     *
     * @return The short read.
     */
    @Override
    public short readShort() {
        int byte1, byte2;
        byte1 = bs.readByte();
        byte2 = bs.readByte();
        return (short) ((byte2 << 8) + byte1);
    }

    /**
     * Reads a single character from the stream.
     *
     * @return The character read.
     */
    @Override
    public char readChar() {
        return (char) readShort();
    }

    /**
     * Reads a long integer from the stream.
     *
     * @return The long integer read.
     */
    @Override
    public long readLong() {
        long byte1 = bs.readByte();
        long byte2 = bs.readByte();
        long byte3 = bs.readByte();
        long byte4 = bs.readByte();
        long byte5 = bs.readByte();
        long byte6 = bs.readByte();
        long byte7 = bs.readByte();
        long byte8 = bs.readByte();
        return (byte8 << 56) + (byte7 << 48) + (byte6 << 40) + (byte5 << 32) + (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }

    /**
     * Reads a floating point integer from the stream.
     *
     * @return The float-type integer read.
     */
    @Override
    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    /**
     * Reads a double-precision integer from the stream.
     *
     * @return The double-type integer read.
     */
    @Override
    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    /**
     * Reads an ASCII string from the stream with length <code>n</code>.
     *
     * @param n Number of characters to read.
     * @return The string read.
     */
    public final String readAsciiString(int n) {
        byte ret[] = new byte[n];
        for (int x = 0; x < n; x++) {
            ret[x] = (byte) readByte();
        }
        try {
            String str= new String(ret,"BIG5");
            //String str= new String(ret,"GBK");
            return str;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    /**
     * Reads a null-terminated string from the stream.
     *
     * @return The string read.
     */
    public final String readNullTerminatedAsciiString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte b = 1;
        while (b != 0) {
            b = readByte();
            baos.write(b);
        }
        byte[] buf = baos.toByteArray();
        char[] chrBuf = new char[buf.length];
        for (int x = 0; x < buf.length; x++) {
            chrBuf[x] = (char) buf[x];
        }
        return String.valueOf(chrBuf);
    }

    /**
     * Gets the number of bytes read from the stream so far.
     *
     * @return A long integer representing the number of bytes read.
     * @see net.sf.odinms.tools.data.input.ByteInputStream#getBytesRead()
     */
    public long getBytesRead() {
        return bs.getBytesRead();
    }

    /**
     * Reads a JCSNetS convention lengthed ASCII string.
     * This consists of a short integer telling the length of the string,
     * then the string itself.
     *
     * @return The string read.
     */
    @Override
    public String readJCSNetSAsciiString() {
        return readAsciiString(readShort());
    }
    
    /**
     * Read char in sequence until terminated character appear.
     * 
     * @return The string read.
     */
    @Override
    public String readNullTerminatedUnicodeString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        byte b = 1;
        while (b != 0) {
            b = readByte();
            baos.write(b);
        }
        
        byte[] buf = baos.toByteArray();
        String strBuf = null;
        try {
            strBuf = new String(buf, "Unicode");
            // get ride of the null terminate character.
            strBuf = strBuf.substring(0, strBuf.length() - 1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return strBuf;
    }
    
    /**
     * Read char in sequence until terminated character appear.
     * 
     * @return The string read.
     */
    @Override
    public String readNullTerminatedUTF8String() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte b = 1;
        while (b != 0) {
            b = readByte();
            baos.write(b);
        }
        
        byte[] buf = baos.toByteArray();
        String strBuf = null;
        try {
            strBuf = new String(buf, "UTF-8");
            // get ride of the null terminate character.
            strBuf = strBuf.substring(0, strBuf.length() - 1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return strBuf;
    }
    
    /**
     * Read char in sequence until terminated character appear.
     * 
     * @return The string read.
     */
    @Override
    public String readNullTerminatedUTF16String() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte b = 1;
        while (b != 0) {
            b = readByte();
            baos.write(b);
        }
        
        byte[] buf = baos.toByteArray();
        String strBuf = null;
        try {
            strBuf = new String(buf, "UTF-16");
            // get ride of the null terminate character.
            strBuf = strBuf.substring(0, strBuf.length() - 1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return strBuf;
    }
    
    /**
     * Read char in sequence until terminated character appear.
     * 
     * @return The string read.
     */
    @Override
    public String readNullTerminatedUTF32String() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte b = 1;
        while (b != 0) {
            b = readByte();
            baos.write(b);
        }
        
        byte[] buf = baos.toByteArray();
        String strBuf = null;
        try {
            strBuf = new String(buf, "UTF-32");
            // get ride of the null terminate character.
            strBuf = strBuf.substring(0, strBuf.length() - 1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return strBuf;
    }


    /**
     * Reads <code>num</code> bytes off the stream.
     *
     * @param num The number of bytes to read.
     * @return An array of bytes with the length of <code>num</code>
     */
    @Override
    public byte[] read(int num) {
        byte[] ret = new byte[num];
        for (int x = 0; x < num; x++) {
            ret[x] = readByte();
        }
        return ret;
    }

    /**
     * Skips the current position of the stream <code>num</code> bytes ahead.
     *
     * @param num Number of bytes to skip.
     */
    @Override
    public void skip(int num) {
        for (int x = 0; x < num; x++) {
            readByte();
        }
    }

    /**
     * @see net.sf.odinms.tools.data.input.ByteInputStream#available
     */
    @Override
    public long available() {
        return bs.available();
    }

    /**
     * @see java.lang.Object#toString
     */
    @Override
    public String toString() {
        return bs.toString();
    }
}
