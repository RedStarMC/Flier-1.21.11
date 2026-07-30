package pl.betoncraft.flier.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Saver extends Thread {

	private final ConcurrentLinkedQueue<Record> queue = new ConcurrentLinkedQueue<>();
	private volatile boolean run = true;

	public Saver() {
		setDaemon(true);
		setName("Flier-Saver");
		start();
	}

	@Override
	public void run() {
		while (run) {
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
					interrupt();
					return;
				}
			}
			drain();
		}
		// flush records queued before shutdown instead of dropping them
		drain();
	}

	private void drain() {
		Record rec;
		while ((rec = queue.poll()) != null) {
			try {
				for (int i = 0; i < rec.args.length; i++) {
					rec.update.setObject(i + 1, rec.args[i]);
				}
				rec.update.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void add(PreparedStatement update, Object[] args) {
		queue.add(new Record(update, args));
		notify();
	}

	public synchronized void end() {
		run = false;
		notify();
	}

	private static class Record {
		private final PreparedStatement update;
		private final Object[] args;

		private Record(PreparedStatement update, Object[] args) {
			this.update = update;
			this.args = args;
		}
	}
}
