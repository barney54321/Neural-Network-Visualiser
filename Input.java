public class Input extends Layer {

    /**
     * Class constructor for Input Layer.
     * Returns a Input Layer object by calling the Layer constructor with
     * the arguments null and (size + 1). The weight matrix is set to null
     * because initial values are whatever is passed from the input array
     * @param size the number of non-constant nodes (i.e. without bias)
     */
    public Input(int size) {
        super(null, size + 1);
        this.weightMatrix = null;
        this.result = new double[this.size][1];
    }

    /**
     * Computes the result for this layer given an input matrix. Since
     * there is no weightMatrix, the result is the original input.
     * @param input the input matrix
     */
    public void compute(double[][] input) {

        // Sets the result to be the input
        for (int i = 0; i < this.size - 1; i++) {
            this.result[i][0] = input[i][0];
        }

        // Sets the constant bias node
        this.result[this.size - 1][0] = 1.0;
        this.next.compute(this.result);

    }

    /**
     * Does nothing as there is no error for the input layer.
     * This is here to stop the iteration process.
     * @param target always will be null
     */
    public void computeError(double[][] target) {
        return;
    }

    /**
     * Does nothing as there is no input matrix to fix.
     * This is here to stop the iteration process.
     * @param learningRate the learning rate of the network.
     */
    public void fix(double learningRate) {
        return;
    }
}