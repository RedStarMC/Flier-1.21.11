package pl.betoncraft.flier.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class Database {

	private Connection con;
	private final Saver saver;
	private final Map<String, PreparedStatement> statements = new HashMap<>();

	public Database() {
		this.saver = new Saver();
	}

	public void disconnect() throws SQLException {
		// stop the saver thread and wait for it to flush queued records
		saver.end();
		try {
			saver.join(5000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		if (con != null && !con.isClosed()) {
			con.close();
		}
	}

	public void registerStatement(String name, String sql) throws SQLException {
		statements.put(name, getConnection().prepareStatement(sql));
	}

	public void execute(String name, Object[] args) throws SQLException {
		PreparedStatement stmt = statements.get(name);
		for (int i = 0; i < args.length; i++) {
			stmt.setObject(i + 1, args[i]);
		}
		stmt.executeUpdate();
	}

	public void execute(String query) throws SQLException {
		getConnection().createStatement().execute(query);
	}

	public void update(String name, Object[] args) {
		saver.add(statements.get(name), args);
	}

	public void update(String query) throws SQLException {
		getConnection().createStatement().executeUpdate(query);
	}

	public ResultSet query(String name, Object[] args) throws SQLException {
		PreparedStatement stmt = statements.get(name);
		for (int i = 0; i < args.length; i++) {
			stmt.setObject(i + 1, args[i]);
		}
		return stmt.executeQuery();
	}

	public ResultSet query(String query) throws SQLException {
		return getConnection().createStatement().executeQuery(query);
	}

	Connection getConnection() throws SQLException {
		if (con == null || con.isClosed()) {
			con = openConnection();
		}
		return con;
	}

	protected abstract Connection openConnection() throws SQLException;
}
