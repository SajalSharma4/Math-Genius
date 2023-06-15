import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

public class ScoreBoardDatabase extends Exception {
	private static final String DB_URL = "jdbc:mysql://localhost/scoreboard";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "Mysql123";
	private JFrame frame;

	public void addScoreToDb(String p_name, int p_score, String p_level) throws SQLException, ClassNotFoundException {
		try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement stmt = conn
						.prepareStatement("INSERT INTO scores(p_name, p_score, p_level) values (?, ?, ?)")) {
			stmt.setString(1, p_name);
			stmt.setInt(2, p_score);
			stmt.setString(3, p_level);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public JPanel createResult(String p_level) throws SQLException, ClassNotFoundException {
		JPanel m = new JPanel();
		String[][] scores = new String[5][2];
		String[] headings = { "Name", "Score" };
		try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement stmt = conn
						.prepareStatement("SELECT * FROM scores WHERE p_level = ? ORDER BY p_score DESC LIMIT 5")) {
			stmt.setString(1, p_level);
			try (ResultSet rs = stmt.executeQuery()) {
				int z = 0;
				while (rs.next() && z < 5) {
					scores[z][0] = rs.getString("p_name");
					scores[z][1] = rs.getString("p_score");
					z++;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JTable jt = new JTable(scores, headings);
		JScrollPane sp = new JScrollPane(jt);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		jt.setDefaultRenderer(Object.class, centerRenderer);
		jt.setRowHeight(30);
		sp.setPreferredSize(new Dimension(350, 173));
		m.add(sp);
		return m;
	}

	public JPanel createButtons() {
		JPanel m = new JPanel();
		final JButton okButton = new JButton("OK");
		okButton.setPreferredSize(new Dimension(100, 40));
		final JButton backButton = new JButton("Start Again");
		backButton.setPreferredSize(new Dimension(100, 40));

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				disposeMainFrame();
			}
		});
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				disposeMainFrame();
				new MathGenius();
			}
		});
		m.add(backButton);
		m.add(okButton);
		return m;
	}

	public void setMainFrame(JFrame frame) {
		this.frame = frame;
	}

	public void disposeMainFrame() {
		if (frame != null) {
			frame.dispose();
		}
	}
}