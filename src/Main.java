import java.text.DecimalFormat;

public class Main {

    public static void main(String[] args) {
        long[] m = new long[11];
        for (int i = 1, j = 0; i <= 21; i++) {
            if (i % 2 != 0) {
                http://google.com
                m[j++] = i;
            } else {
                continue;
            }
        }

        float[] x = new float[14];
        for (int i = 0; i < 14; i++) {
            x[i] = (float) Math.random()*12 - 10;
        }

        double[][] f = new double[11][14];
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 14; j++) {
                f[i][j] = calculateF(x[j], m, i, j);
            }
        }

        DecimalFormat df = new DecimalFormat("#0.0000");
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 14; j++) {
                System.out.print(df.format(f[i][j]) + " ");
            }
            System.out.println();
        }
    }

    private static double calculateF(float x, long[] m, int i, int j) {
        if (m[i] == 15) {
            return Math.pow((Math.tan(Math.cos(x)) / Math.PI), 2);
        } else if (m[i] == 1  ||
                   m[i] == 7  ||
                   m[i] == 9  ||
                   m[i] == 11 ||
                   m[i] == 13) {
            return Math.tan(Math.exp(Math.cos(x)));
        } else {
            return Math.pow(Math.log(Math.acos(Math.exp(- Math.abs(x)))) / 3.0, 3);
        }
    }
}
