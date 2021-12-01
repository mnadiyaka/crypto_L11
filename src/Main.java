import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {
        Point p = new Point(new BigInteger("4"), new BigInteger("5"));
        Point m = new Point(new BigInteger("12"), new BigInteger("6"));
        BigInteger gf = new BigInteger("23");
        BigInteger aA = new BigInteger("23");
        BigInteger aB = new BigInteger("3");
        ElipC c = new ElipC(new BigInteger(9 + ""), new BigInteger(17 + ""));

        Point qB = multN(p, aB, gf, c);

        BigInteger k = new BigInteger(5 + "");
        Point[] crypt = incrypt(k, p, qB, m, gf, c);
        System.out.println("incrypted: " + crypt[0] + " " + crypt[1]);
        System.out.println("decrypted: " + decrypt(aB, crypt, gf, c));
    }

    public static Point add(Point a, Point b, BigInteger p) {
        BigInteger[] point = new BigInteger[2];
        BigInteger v = ((a.getY().subtract(b.getY())).multiply((a.getX().subtract(b.getX())).modPow(new BigInteger(String.valueOf(-1)), p))).mod(p);
        point[0] = v.pow(2).subtract(a.getX()).subtract(b.getX()).mod(p);
        point[1] = v.multiply(a.getX().subtract(point[0])).subtract(a.getY()).mod(p);
        return new Point(point[0], point[1]);
    }

    public static Point mult2(Point point, BigInteger p, ElipC c) {
        BigInteger v = ((((point.getX().pow(2).multiply(new BigInteger(String.valueOf(3))))
                .add(c.getA())))
                .multiply(point.getY().multiply(new BigInteger(String.valueOf(2)))
                        .modPow(new BigInteger(String.valueOf(-1)), p))).mod(p);
        BigInteger tempPx = (v.pow(2).subtract(point.getX().multiply(new BigInteger(String.valueOf(2))))).mod(p);
        BigInteger tempPy = (v.multiply(point.getX().subtract(tempPx)).subtract(point.getY())).mod(p);
        return new Point(tempPx, tempPy);
    }

    public static Point multN(Point point, BigInteger n, BigInteger p, ElipC c) {
        int d = 2;
        Point newPoint = mult2(point, p, c);
        BigInteger v, tempPx, tempPy;
        do {
            v = ((newPoint.getY().subtract(point.getY())).multiply((newPoint.getX().subtract(point.getX()))
                    .modPow(new BigInteger(String.valueOf(-1)), p))).mod(p);
            tempPx = v.pow(2).subtract(point.getX()).subtract(newPoint.getX()).mod(p);
            tempPy = v.multiply(point.getX().subtract(tempPx)).subtract(point.getY()).mod(p);
            newPoint = new Point(tempPx, tempPy);
            d++;
        } while (n.compareTo(new BigInteger(d + "")) == 1);
        return newPoint;
    }

    public static Point[] incrypt(BigInteger k, Point point, Point qB, Point m, BigInteger p, ElipC c) {
        Point kP = multN(point, k, p, c);
        Point kqB = multN(qB, k, p, c);
        Point r = add(m, kqB, p);

        return new Point[]{kP, r};
    }

    public static Point decrypt(BigInteger aB, Point[] incrypt, BigInteger p, ElipC c) {
        Point aBkP = multN(incrypt[0], aB, p, c);
        return add(incrypt[1], new Point(aBkP.getX(), aBkP.getY().multiply(new BigInteger("-1"))), p);
    }
}

class Point {
    private BigInteger x;
    private BigInteger y;

    public Point(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + "; " + y + ')';
    }
}

class ElipC {
    BigInteger a;
    BigInteger b;

    public ElipC(BigInteger a, BigInteger b) {
        this.a = a;
        this.b = b;
    }

    public BigInteger getA() {
        return a;
    }

    public BigInteger getB() {
        return b;
    }
}
