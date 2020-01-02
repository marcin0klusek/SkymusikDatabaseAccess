import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EventDetails extends JFrame {
	private static final long serialVersionUID = 1L;
	private Event ev;

	public EventDetails(Event ev) {
		this.ev = ev;
		create();
	}

	private void create() {
		setSize(new Dimension(450, 600));
		setLocation(750, 200);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Szczegóły wydarzenia");
		addButtons();
	}

	private void addButtons() {
		Container pane = getContentPane();

		JPanel values = new JPanel();
		values.setLayout(new BoxLayout(values, BoxLayout.PAGE_AXIS));

		// nazwa wydarzenia
		JPanel name = new JPanel();
		name.add(new JLabel("Nazwa wydarzenia: "));
		JTextField nameText = new JTextField(ev.getName());
		nameText.setEditable(false);
		name.add(nameText);
		values.add(name);

		// organizator wydarzenia
		JPanel org = new JPanel();
		org.add(new JLabel("Organizator: "));
		JTextField orgText = new JTextField(ev.getOrg());
		orgText.setEditable(false);
		org.add(orgText);
		values.add(org);

		// miasto
		JPanel city = new JPanel();
		city.add(new JLabel("Miasto: "));
		JTextField cityText = new JTextField(ev.getCity());
		cityText.setEditable(false);
		city.add(cityText);
		values.add(city);

		// ulica wydarzenia
		JPanel address = new JPanel();
		address.add(new JLabel("Ulica: "));
		JTextField adrText = new JTextField(ev.getAddress());
		adrText.setEditable(false);
		address.add(adrText);
		values.add(address);

		// data startowa
		JPanel start = new JPanel();
		start.add(new JLabel("Start wydarzenia: "));
		JTextField startText = new JTextField(ev.getStartDate());
		startText.setEditable(false);
		start.add(startText);
		values.add(start);

		// data koncowa
		JPanel end = new JPanel();
		end.add(new JLabel("Koniec wydarzenia: "));
		JTextField endText = new JTextField(ev.getEndDate());
		endText.setEditable(false);
		end.add(endText);
		values.add(end);

		// opis wydarzenia
		JPanel desc = new JPanel();
		desc.add(new JLabel("Opis: "));
		JTextField descText = new JTextField(ev.getDesc());
		descText.setEditable(false);
		desc.add(descText);
		values.add(desc);

		// przyciski stron
		JPanel buttons = new JPanel();

		JButton openTickets = new JButton("Bilety	");
		JButton openSocial = new JButton("Strona wydarzenia");
		JButton open = new JButton("Obraz");
		buttons.add(openTickets);
		buttons.add(openSocial);
		buttons.add(open);

		values.add(buttons);

		// *****************
		// funkcje przyciskow
		// *****************

		// otwarcie obrazu
		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					java.awt.Desktop.getDesktop().browse(java.net.URI.create(ev.getImg()));
				} catch (IOException e1) {
					System.out.println("Nieudana próba otworzenia: " + ev.getImg());
					e1.printStackTrace();
				}
			}
		});
		// otwarcie strony wydarzenia
		openSocial.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					java.awt.Desktop.getDesktop().browse(java.net.URI.create(ev.getSocial()));
				} catch (IOException e1) {
					e1.printStackTrace();
					System.out.println("Nieudana próba otworzenia: " + ev.getSocial());
				}
			}
		});
		// otwarcie strony z biletami
		openTickets.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					java.awt.Desktop.getDesktop().browse(java.net.URI.create(ev.getTickets()));
				} catch (IOException e1) {
					e1.printStackTrace();
					System.out.println("Nieudana próba otworzenia: " + ev.getTickets());
				}
			}
		});

		// dodanie panelu
		pane.add(values);

		// przycisk zamkniecia okna ze sczegolami wydarzenia
		JButton exitBtn = new JButton("OK");
		JPanel down = new JPanel();
		down.add(exitBtn, BorderLayout.LINE_END);
		pane.add(down, BorderLayout.PAGE_END);

		exitBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

	}
}
