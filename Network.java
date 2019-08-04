public class Network {
    
    /**
     * Pointer to the Network's Input Layer.
     */
    public Input input;

    /**
     * Pointer to the Network's Output Layer.
     */
    public Output output;

    /**
     * Class constructor for the Network.
     * Configures the number of layers and their respective sizes
     * based on the array passed in.
     * @param layers the sizes of each layer
     */
    public Network(int[] layers) {

        this.input = new Input(layers[0]);

        Layer prev = this.input;
        for (int i = 1; i < layers.length - 1; i++) {
            prev = new Hidden(prev, layers[i]);
        }

        this.output = new Output(prev, layers[layers.length - 1]);

    }

    /**
     * Runs a query through the network.
     * Sends an input to the Input Layer and waits for the Output
     * layer to compute. The input is transposed to form a vertical
     * array (n * 1 size).
     * @param input the input array
     * @return the output array
     */
    public double[] query(double[] input) {

        double[][] inputMatrix = new double[input.length][1];

        for (int i = 0; i < input.length; i++) {
            inputMatrix[i][0] = input[i];
        }

        this.input.compute(inputMatrix);

        return this.output.getResultArray();
    }

    /**
     * Trains the network by providing input and a target.
     * Calls query on the input and then passes through the expected so that
     * the network can calculate the error and then update their matrices.
     * @param input the input array
     * @param target the target output
     * @param learningRate the rate of learning for the network
     */
    public void train(double[] input, double[] target, double learningRate) {

        double[][] inputMatrix = new double[input.length][1];

        for (int i = 0; i < input.length; i++) {
            inputMatrix[i][0] = input[i];
        }

        this.input.compute(inputMatrix);

        double[][] targetMatrix = new double[target.length][1];

        for (int i = 0; i < target.length; i++) {
            targetMatrix[i][0] = target[i];
        }

        this.output.computeError(targetMatrix);

        this.output.fix(learningRate);
    }
}