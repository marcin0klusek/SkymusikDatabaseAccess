import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Okno extends JFrame {
	private static final long serialVersionUID = 1L;
	private final String title = "Skymusik database access ";
	private Container pane = this.getContentPane();
	private ArrayList<Event> events = new ArrayList<Event>();
	private static DatabaseConnection db = new DatabaseConnection();
	private JScrollPane eventList;
	private JButton writeEvents = new JButton("Wprowadzanie");
	private JButton readEvents = new JButton("Odczyt");
	private JButton insertEvents = new JButton("Wprowadz wydarzenia do bazy danych");

	public static void main(String[] args) {
		new Okno();
	}

	public Okno() {
		setSize(new Dimension(500, 400));
		setLocation(400, 100);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(title);

		JPanel topPanel = new JPanel();

		giveActions();
		topPanel.add(writeEvents);
		topPanel.add(readEvents);

		pane.add(topPanel, BorderLayout.PAGE_START);

		setGuiWrite();

		db.establishConnection();
		if (db.isConnected()) {
			readEvents.setEnabled(true);
		}

	}

	private void giveActions() { // dodaje akcje do przyciskow w gornym panelu
		readEvents.addActionListener(new ActionListener() { // ODCZYT

			@Override
			public void actionPerformed(ActionEvent e) {
				events = db.takeEvents();
				newEventList();
				setTitle(title + "- odczytywanie...");
				insertEvents.setEnabled(false);

				pack();
				repaint();

				JOptionPane.showMessageDialog(null, "Odczytano " + events.size() + " wydarzeń.");
			}
		});

		writeEvents.addActionListener(new ActionListener() { // WCZYTAJ PLIK

			@Override
			public void actionPerformed(ActionEvent e) {
				chooseFile();
				newEventList();
				setTitle(title + "- wprowadzanie...");
				insertEvents.setEnabled(true);

				pack();
				repaint();
			}

		});

		insertEvents.addActionListener(new ActionListener() { // WCZYTAJ PLIK

			@Override
			public void actionPerformed(ActionEvent e) {

				if (!db.isConnected())
					db.establishConnection();

				db.insertEvents(events);
				insertEvents.setEnabled(false);
				pack();
				repaint();
			}

		});
	}

	@SuppressWarnings("rawtypes")
	private void setGuiWrite() {
		setTitle(getTitle());

		eventList = new JScrollPane(new JList());
		eventList.setPreferredSize(new Dimension(500, 400));
		pane.add(eventList, BorderLayout.CENTER);
		readEvents.setEnabled(false);
		insertEvents.setEnabled(false);

		pane.add(insertEvents, BorderLayout.PAGE_END);

		pack();
		repaint();
	}

	private void newEventList() {
		Object[] eList = events.toArray();

		pane.remove(eventList);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		JList list = new JList(eList);
		eventList = new JScrollPane(list);
		eventList.setPreferredSize(new Dimension(500, 400));
		pane.add(eventList, BorderLayout.CENTER);
		list.updateUI();

		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				@SuppressWarnings("rawtypes")
				JList list = (JList) evt.getSource();
				if (evt.getClickCount() == 2) {
					new EventDetails(events.get(list.locationToIndex(evt.getPoint())));
				}
			}
		});

	}

	private void chooseFile() {
		readFile(new File(""));

		JFileChooser jfc = new JFileChooser();
		jfc.setFileFilter(new FileNameExtensionFilter(".csv files", "csv"));
		jfc.showOpenDialog(null);
		File selectedFile = jfc.getSelectedFile();
		if (selectedFile != null)
			readFile(selectedFile);

	}

	private void readFile(File file) {
		if (file.exists()) {
			try {
				events = new ArrayList<Event>();
				FileReader fileReader = new FileReader(file.getAbsolutePath());
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				String textLine = bufferedReader.readLine();
				// String[] eventsProperties = textLine.split(",");
				textLine = bufferedReader.readLine();

				do {
					String[] eventsData = textLine.split(",");
					Event wydarzenie = new Event();

					for (int i = 0; i < eventsData.length; i++) {

						if (i == 1) // name
							wydarzenie.setName(eventsData[i].replace('"', Character.MIN_VALUE));

						if (i == 3) { // desc
							String desc = eventsData[i].replace('"', Character.MIN_VALUE).trim();
							if (desc.equals("") || desc.equals(null))
								wydarzenie.setDesc("Sprawdz szczegóły na stronie wydarzenia.");
							else
								wydarzenie.setDesc(eventsData[i].replace('"', Character.MIN_VALUE));
						}

						if (eventsData[i].contains("image")) { // img
							String[] imges = eventsData[i].split("/");
							eventsData[i] = "https://static.wixstatic.com/media/"
									+ imges[imges.length - 1].substring(0, imges[imges.length - 1].length() - 1);
							wydarzenie.setImg(eventsData[i].replace('"', Character.MIN_VALUE));
						}

						if (i == 6) { // start
							eventsData[i] = eventsData[i].substring(0, eventsData[i].length() - 10);
							wydarzenie.setStartDate(eventsData[i]);
						}

						if (i == 7) { // end
							eventsData[i] = eventsData[i].substring(0, eventsData[i].length() - 10);
							wydarzenie.setEndDate(eventsData[i]);
						}

						String value = eventsData[i];
						if (value.startsWith("\"") && !value.endsWith("\"") && i > 8) {
							do {
								value += ", " + eventsData[++i];
							} while (!eventsData[i].endsWith("\""));
							eventsData[i] = value;
						}

						if (eventsData[i].indexOf(",") >= 2 && !eventsData[i].contains("2019")
								&& !eventsData[i].contains("2020") && !eventsData[i].contains("2021")) { // adres i cty
							String[] adres = eventsData[i].replace('"', Character.MIN_VALUE).split(","); // ulic,
																											// kod-pocztowy
																											// miasto,
																											// kraj
							wydarzenie.setAddress(adres[0]);
							String[] dataCity = adres[1].trim().split(" ");
							wydarzenie.setCity(dataCity[1]);
						}

						if (eventsData[i].contains("Ustalenia")) {
							wydarzenie.setCity("Lokalizacja Do Ustalenia");
							wydarzenie.setAddress("");
						}

						if (eventsData[i].contains("/event-details/") && eventsData[i].contains("allhem150")) // tickets
							wydarzenie.setTickets(eventsData[i].replace('"', Character.MIN_VALUE));

						if (eventsData[i].contains("/event-details/") && !eventsData[i].contains("allhem150")) { // event's
																													// site
							eventsData[i] = "https://allhem150.wixsite.com/skymusik"
									+ eventsData[i].replace('"', Character.MIN_VALUE).trim();
							wydarzenie.setSocial(eventsData[i].replace('"', Character.MIN_VALUE));
						}
					}
					events.add(wydarzenie);
					textLine = bufferedReader.readLine();
				} while (textLine != null);

				bufferedReader.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
