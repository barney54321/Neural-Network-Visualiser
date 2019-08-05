import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.lang.Math;
import java.awt.Font;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1550691097823471818L; // This is required
	public static final int WIDTH = 780, HEIGHT = WIDTH / 12 * 9; 
	private Thread thread;
	private boolean running = false; 

	private int[] layout;
	private Runner runner;

	public Game() {

		this.layout = new int[] {2, 4, 4, 1};
		this.runner = new Runner(this.layout);

		new Window(WIDTH, HEIGHT, "Neural Net Visualiser", this); // Game constructor

	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				delta--;
			}
			if (running) {
				render();
			}
			frames++;

			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				//System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}

	private void tick() {

		this.runner.tick();

	}

	private void render() { // Renders the window
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.cyan);

		int widthOffset = 200;
		int heightOffset = 0;
		int length = 40;
		int widthBracket = (this.WIDTH - widthOffset) / this.layout.length;
		double[][][] matrices = this.runner.getMatrices();

		// Calculate the larget weight
		double max = matrices[0][0][0];
		double min = matrices[0][0][0];
		for (int i = 0; i < matrices.length; i++) {
			for (int j = 0; j < matrices[i].length; j++) {
				for (int m = 0; m < matrices[i][j].length; m++) {
					if (matrices[i][j][m] > max) {
						max = matrices[i][j][m];
					} else if (matrices[i][j][m] < min) {
						min = matrices[i][j][m];
					}
				}
			}
		}

		// Calculate what is larger
		double bigger = Math.abs(min) > Math.abs(max) ? Math.abs(min) : Math.abs(max);

		for (int i = 0; i < this.layout.length; i++) {
			int heightBracket = (this.HEIGHT - heightOffset * 2) / this.layout[i];
			int midHeight = heightBracket / 2;
			int upOffset = 40;
			for (int j = 0; j < this.layout[i]; j++) {

				if (i != this.layout.length - 1) {

					// Figure out the locations of the next layer's nodes
					int nextX = widthOffset + widthBracket * (i + 1) + length / 2;
					int nextHeightBracket = (this.HEIGHT - heightOffset * 2) / this.layout[i + 1];
					int nextMidHeight = nextHeightBracket / 2;

					for (int k = 0; k < this.layout[i+1]; k++) {

						double opacityBracket = (255 / 2) / bigger;
						int opacity = (int) ( 255 / 2 + opacityBracket * matrices[i][k][j]);
						if (opacity < 0) {
							opacity = 0;
						} else if (opacity > 255) {
							opacity = 254;
						}
						g.setColor(new Color(0, 255, 0, opacity));

						int startX = widthOffset + widthBracket * i + length / 2;
						int startY = heightOffset + heightBracket * j + midHeight - upOffset + length / 2;
						int nextY = heightOffset + nextHeightBracket * k + nextMidHeight - upOffset + length / 2;
						g.drawLine(startX, startY, nextX, nextY);
					}
				}

				g.setColor(Color.cyan);
				g.fillRect(widthOffset + widthBracket * i, heightOffset + heightBracket * j + midHeight - upOffset, length, length);
			}
		}

		g.setColor(Color.white);
		g.setFont(new Font("Arial", 0, 30)); 
		g.drawString("T T: " + ("" + (100 * this.runner.queryBothTrue())).substring(0, 4) + "%", 20, 200);
		g.drawString("T F: " + ("" + (100 * this.runner.queryLeftTrue())).substring(0, 4) + "%", 20, 240);
		g.drawString("F T: " + ("" + (100 * this.runner.queryRightTrue())).substring(0, 4) + "%", 20, 280);
		g.drawString("F F: " + ("" + (100 * this.runner.queryBothFalse())).substring(0, 4) + "%", 20, 320);

		g.drawString(("" + this.runner.iteration), this.WIDTH - 140, 40);

		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		new Game();
	}
}
