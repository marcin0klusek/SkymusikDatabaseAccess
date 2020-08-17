import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class DatabaseConnection {

	private static Connection myCon;
	private static Statement myStmt;
	private String ip = "";
	private String db = "";
	private String un = "";
	private String pass = "";

	public void establishConnection() {

		String connectionUrl = "jdbc:sqlserver://" + ip + ":" + 1433 + ";databaseName=" + db + ";user=" + un
				+ ";password=" + pass;
		String komunikat;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			myCon = DriverManager.getConnection(connectionUrl);
			myStmt = myCon.createStatement();

			komunikat = "Próba połączenia z serwerem powiodła się.";

		} catch (Exception e) {
			komunikat = "Wystąpił problem z połączeniem do bazy danych. \nTreść błędy: " + e.getMessage();
		}
		JOptionPane.showMessageDialog(null, komunikat);
	}

	public ArrayList<Event> takeEvents() {
		if (!isConnected())
			this.establishConnection();

		ResultSet myRs;
		ArrayList<Event> events = new ArrayList<Event>();

		try {
			myRs = myStmt.executeQuery("SELECT * FROM Wydarzenia");

			while (myRs.next()) {
				Event ev = new Event();
				ev.setName(myRs.getString("Nazwa"));
				ev.setCity(myRs.getString("Miasto"));
				ev.setAddress(myRs.getString("Adres"));
				ev.setStartDate(myRs.getString("DataOd"));
				ev.setEndDate(myRs.getString("DataDo"));
				ev.setDesc(myRs.getString("Opis"));
				ev.setTickets("Bilet");
				ev.setSocial(myRs.getString("SocialMedia"));
				ev.setImg("Logo");
				events.add(ev);
			}
			closeCon();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showInternalMessageDialog(null, "Wystąpił błąd przy próbie pobrania wydarzeń");
		}

		return events;
	}

	public boolean isConnected() {
		if (myCon != null)
			return true;
		return false;
	}

	public void closeCon() throws SQLException {
		if (isConnected())
			myCon.close();
		JOptionPane.showInternalMessageDialog(null, "Połączenie zostało zamknięte!");
	}

	public Connection getMyCon() {
		return myCon;
	}

	public void insertEvents(ArrayList<Event> events) {
		String wynik = "Udało wprowadzić się wydarzenia!";
		Event error = new Event();
		error.setName("Nieznany błąd w db.insertEvents(");
		try {
			myStmt.executeUpdate("DELETE FROM Wydarzenia");

			for (Event ev : events) {
				error = ev;
				myStmt.executeUpdate("INSERT INTO Wydarzenia " + "VALUES ('" + ev.getName() + "', '" + ev.getOrg()
						+ "', '" + ev.getCity() + "', '" + ev.getAddress() + "', '" + ev.getStartDate() + "', '"
						+ ev.getEndDate() + "', '" + ev.getDesc() + "', '" + ev.getTickets() + "', '" + ev.getSocial()
						+ "', '" + ev.getImg() + "')");
			}

		} catch (Exception e) {
			wynik = "Wprowadzanie wydarzeń nie powiodło się! Błąd dla wydarzenia: " + error.getName();
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, wynik);
	}
}
