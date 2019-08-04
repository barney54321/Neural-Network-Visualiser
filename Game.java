import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

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
		int widthBracket = (this.WIDTH - widthOffset) / this.layout.length;
		for (int i = 0; i < this.layout.length; i++) {
			int heightBracket = (this.HEIGHT - heightOffset * 2) / this.layout[i];
			int midHeight = heightBracket / 2;
			int upOffset = 40;
			for (int j = 0; j < this.layout[i]; j++) {
				g.fillRect(widthOffset + widthBracket * i, heightOffset + heightBracket * j + midHeight - upOffset, 40, 40);
			}
		}

		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		new Game();
	}
}
