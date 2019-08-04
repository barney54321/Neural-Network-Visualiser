import java.util.Random;

public class Runner {

    public Network net;
    public double learningRate;
    public int iteration;
    public int[] layout;

    public Runner(int[] layout) {
        this.layout = layout;
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
}