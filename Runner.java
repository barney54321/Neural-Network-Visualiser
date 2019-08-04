import java.util.Random;

public class Runner {

    public Network net;
    public double learningRate;
    public int iteration;
    public int[] layout;

    public Runner() {
        this.layout = new int[] {2, 4, 4, 1};
        this.learningRate = 0.3;
        this.net = new Network(this.layout);
        this.iteration = 0;
    }

    public double[] generateOutput(double[] input) {

        double[] res = {0};

        if (input[0] == 1 && input[1] == 1) {
            res[0] = 1;
        }

        return res;

    }

    public void tick() {

        if (this.iteration % 4 == 0) {
            double[] input = {1, 1};
            double[] output = this.generateOutput(input);
            this.net.train(input, output, this.learningRate);
        } else if (this.iteration % 4 == 1) {
            double[] input = {1, 0};
            double[] output = this.generateOutput(input);
            this.net.train(input, output, this.learningRate);
        } else if (this.iteration % 4 == 2) {
            double[] input = {0, 1};
            double[] output = this.generateOutput(input);
            this.net.train(input, output, this.learningRate);
        } else {
            double[] input = {0, 0};
            double[] output = this.generateOutput(input);
            this.net.train(input, output, this.learningRate);
        } 

        this.iteration++;
    }

    public static void printMatrix(double[] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.print(matrix[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {

        int[] layout = {2, 3, 2};
        Network net = new Network(layout);
        double learningRate = 0.3;

        Random r = new Random();
        for (int i = 0; i < 2000000; i++) {
            boolean a = r.nextBoolean();
            boolean b = r.nextBoolean();

            double[] input = {0.0, 0.0};
            double[] output = {0.0, 0.0};

            if (a == true) {
                input[0] = 1.0;
            }
            if (b == true) {
                input[1] = 1.0;
            }

            if (a && b) {
                output[0] = 1.0;
            } else {
                output[1] = 1.0;
            }

            net.train(input, output, learningRate);
            // printMatrix(net.query(new double[] {1.0, 1.0}));
            // printMatrix(net.query(new double[] {1.0, 0.0}));
            // printMatrix(net.query(new double[] {0.0, 1.0}));
            // printMatrix(net.query(new double[] {0.0, 0.0}));
            // System.out.println();
        }

        // printMatrix(net.query(new double[] {0.50, 0.50}));
        printMatrix(net.query(new double[] {1.0, 1.0}));
        printMatrix(net.query(new double[] {1.0, 0.0}));
        printMatrix(net.query(new double[] {0.0, 1.0}));
        printMatrix(net.query(new double[] {0.0, 0.0}));
    }
}