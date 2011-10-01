package ai;

public class Helpers {
    public static byte index(byte x, byte y) {
        return (byte)(x + y * Board.ROWS);
    }

    public static byte index(int x, int y) {
        return index((byte)x, (byte)y);
    }

    public static byte index(byte index, int x, int y) {
        return (byte)(index + (x + y * Board.ROWS));
    }
}
