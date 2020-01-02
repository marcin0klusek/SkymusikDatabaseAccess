public class Event {
	private String name;
	private final String org = "Skymusik";
	private String city;
	private String address;
	private String startDate;
	private String endDate;
	private String desc;
	private String tickets;
	private String social;
	private String img;

	public String getOrg() {
		return org;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city.trim();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address.trim();
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate.trim();
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate.trim();
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc.trim();
	}

	public String getTickets() {
		return tickets;
	}

	public void setTickets(String tickets) {
		this.tickets = tickets.trim();
	}

	public String getSocial() {
		return social;
	}

	public void setSocial(String social) {
		this.social = social.trim();
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img.trim();
	}

	@Override
	public String toString() {
		return this.getName();
	}
}
