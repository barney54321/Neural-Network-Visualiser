public class Hidden extends Layer {
    
    /**
     * Class constructor for Hidden Layer.
     * Returns a Hidden Layer object by calling the Layer constructor with
     * arguments prev and (size + 1). This is set as the next layer for
     * the previous layer and the weightMatrix is set as a randomly
     * generated double[][] with the function from Layer.
     * @param prev the previous layer (going from input to output)
     * @param size the number of non-constant nodes (i.e. without bias)
     */
    public Hidden(Layer prev, int size) {
        super(prev, size + 1);
        this.prev.setNext(this);
        this.prevSize = this.prev.getSize();
        this.weightMatrix = generateMatrix(this.size, this.prevSize);
    }

    /**
     * Computes the error within the layer based on the target value. This is
     * calculated with the equation: error = T(next.weightMatrix) . (next.error)
     * @param target the target value for the layer
     */
    public void computeError(double[][] target) {

        // error = T(next.weightMatrix) . (next.error)
        double[][] nextMatrix = this.next.getWeightMatrix();
        double[][] transposed = new double[nextMatrix[0].length][nextMatrix.length];
        
        for (int i = 0; i < this.next.getWeightMatrix().length; i++) {
            for (int j = 0; j < this.next.getWeightMatrix()[0].length; j++) {
                transposed[j][i] = this.next.getWeightMatrix()[i][j];
            }
        }

        this.error = multiplyMatrices(transposed, this.next.getError());
        this.prev.computeError(null);
    }
}