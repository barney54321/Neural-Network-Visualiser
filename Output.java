public class Output extends Layer {
    
    /**
     * Class constructor for the Output layer.
     * Returns a Output Layer object by calling the Layer constructor with
     * arguments prev and (size + 1). This is set as the next layer for
     * the previous layer and the weightMatrix is set as a randomly
     * generated double[][] with the function from Layer.
     * @param prev the previous Layer (will always be Hidden)
     * @param size the number of neurons in Layer (excluding cosntant bias)
     */
    public Output(Layer prev, int size) {
        super(prev, size);
        this.prev.setNext(this);
        this.prevSize = this.prev.getSize();
        this.weightMatrix = generateMatrix(this.size, this.prevSize);
    }

    /*
        computeError
        OUTPUT: error = target - actual
        HIDDEN: error = (next.weightMatrix) . (next.error)
        INPUT:  error = NONE
    */
    /**
     * Computes the error in the output of the layer.
     * Error = target - actual for this layer.
     * @param target the target matrix
     */
    public void computeError(double[][] target) {

        this.error = new double[target.length][1]; 
        for (int i = 0; i < target.length; i++) {
            this.error[i][0] = target[i][0] - this.result[i][0];
        }

        this.prev.computeError(null);
    }
}