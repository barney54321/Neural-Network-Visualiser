import java.util.Random;

public abstract class Layer {

    /**
     * The previous Layer going from input to output.
     */
    public Layer prev;

    /**
     * The next Layer going from input to output.
     */
    public Layer next;

    /**
     * The result matrix.
     */
    public double[][] result;

    /**
     * The input matrix from prev to this.
     */
    public double[][] weightMatrix;

    /**
     * The error matrix calculated at computeError().
     */
    public double[][] error;

    /**
     * The size of the previous layer.
     */
    public int prevSize;

    /**
     * The number of neurons in the layer.
     */
    public int size;

    /**
     * Class constructor for Layer.
     * Initialised prev, next and size.
     * @param prev the previous Layer.
     * @param size the number of neurons in the layer.
     */
    public Layer(Layer prev, int size) {
        this.prev = prev;
        this.next = null;
        this.size = size;
    }

    /**
     * Computes the result for this layer given an input matrix.
     * To calculate, the weightMatrix is matrix multiplied with
     * the input array (with bias added in). The resulting size x 1
     * matrix is then put through the sigmoid function to return
     * the resulting matrix. Note that this method is overloaded
     * in the Input class.
     * @param input the input matrix to be passed through.
     */
    public void compute(double[][] input) {

        double[][] inputWithBias = new double[input.length + 1][1];
        for (int i = 0; i < input.length; i++) {
            inputWithBias[i][0] = input[i][0];
        }
        inputWithBias[input.length][0] = 1;

        this.result = multiplyMatrices(this.weightMatrix, inputWithBias);

        applySigmoid(this.result);

        // This isn't called if it's an Output Layer
        if (this.next != null) {
            this.next.compute(this.result);
        }
    }

    /**
     * Computes the error for the layer. 
     * Since Hidden, Input and Output Layers all have 
     * different calculations for the Layer, the method 
     * is abstract so they can define their own.
     * @param target the target matrix for the Layer
     */
    public abstract void computeError(double[][] target);

    /**
     * Adjusts the weightMatrix haven calculated the error.
     * Uses the equation ▲w_jk = lr * E_k * sigmoid(O_k) * (1 - sigmoid(O_k)) . T(O_j)
     * to create the matrix for adjustment, and this is then added to the weightMatrix.
     * Note that . means matrix multiplication and * means elements multiply their corresponding.
     * Since this is overloaded in the Input layer, this calls fix on the previous layer once done.
     * @param learningRate the learning rate of the network.
     */
    public void fix(double learningRate) {

        double[][] combinedMatrix = new double[this.error.length][1];
        for (int i = 0; i < this.error.length; i++) {
            combinedMatrix[i][0] = this.error[i][0] * this.result[i][0] * (1 - this.result[i][0]);
        }

        double[][] transposedMatrix = new double[1][this.prev.result.length];
        for (int i = 0; i < this.prev.result.length; i++) {
            transposedMatrix[0][i] = this.prev.result[i][0];
        }

        double[][] magicMatrix = multiplyMatrices(combinedMatrix, transposedMatrix);

        for (int i = 0; i < magicMatrix.length; i++) {
            for (int j = 0; j < magicMatrix[0].length; j++) {
                this.weightMatrix[i][j] += learningRate * magicMatrix[i][j];
            }
        }

        this.prev.fix(learningRate);
    }

    /**
     * Sets the next Layer.
     * @param next the next Layer
     */
    public void setNext(Layer next) {
        this.next = next;
    }

    /**
     * Returns the size of the Layer.
     * @return the size of the Layer.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Returns the error matrix.
     * @return the error matrix.
     */
    public double[][] getError() {
        return this.error;
    }

    /**
     * Returns the result array.
     * Converts the matrix into a horizontal array.
     * @return the result array.
     */
    public double[] getResultArray() {

        double[] res = new double[this.result.length];
        for (int i = 0; i < this.result.length; i++) {
            res[i] = this.result[i][0];
        }

        return res;
    }

    /**
     * Returns the weightMatrix.
     * @return the weightMatrix.
     */
    public double[][] getWeightMatrix() {
        return this.weightMatrix;
    }

    /**
     * The sigmoid function.
     * Runs the equation y = (1 / 1 + e^(-x)).
     * @param x the x value of the equation.
     * @return the resulting y value of the equation.
     */
    public static double sigmoid(double x) {
        return (1 / (1 + Math.pow(Math.E, (-1) * x)));
    }

    /**
     * Applies the sigmoid function to an entire vertical array.
     * @param matrix the n*1 matrix to be modified.
     */
    public static void applySigmoid(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            matrix[i][0] = sigmoid(matrix[i][0]);
        }
    }

    /**
     * Returns a randomised weightMatrix.
     * The range of weights is -0.5 to 0.5. No two weights are
     * the exact same in the matrix.
     * @param height the height of the matrix.
     * @param width the width of the matrix.
     * @return the randomised weightMatrix.
     */
    public static double[][] generateMatrix(int height, int width) {

        double output[][] = new double[height][width];
        double array[] = new double[height * width];
        Random r = new Random();

        int i = 0; 
        while (i < array.length) {

            boolean isNew = true;
            double next = r.nextDouble() - 0.5;
            for (int j = 0; j < i; j++) {
                if (array[j] == next || next > 0.5) {
                    isNew = false;
                }
            }

            if (isNew == true) {
                array[i] = next;
                i++;
            }
        }

        for (int a = 0; a < height; a++) {
            for (int b = 0; b < width; b++) {
                output[a][b] = array[a * width + b];
            }
        }

        return output;
    }

    /**
     * Returns the matrix multiplication of two matrices.
     * Assumes that the matrices are compatable, otherwise will throw index error
     * @param firstMatrix the first matrix to be multiplied
     * @param secondMatrix the other matrix to be multiplied
     * @return the matrix multiplication of the two matrices
     */
    public static double[][] multiplyMatrices(double[][] firstMatrix, double[][] secondMatrix) {

        int r1 = firstMatrix.length;
        int c1 = firstMatrix[0].length;
        int c2 = secondMatrix[0].length;

        double[][] product = new double[r1][c2];
        for(int i = 0; i < r1; i++) {
            for (int j = 0; j < c2; j++) {
                for (int k = 0; k < c1; k++) {
                    product[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }
        return product;
        
    }
}