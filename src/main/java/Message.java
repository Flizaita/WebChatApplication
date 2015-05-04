public class Message {
	private String id;
	private String text;
	private String author;
	private String date;
	private String deleted;
	

	public Message(String id, String text, String author, String date, String deleted) {
		this.id = id;
		this.text = text;
		this.author = author;
		this.date = date;
		this.deleted = deleted;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String toString() {
		return "{\"id\":\"" + this.id + "\",\"author\":\"" + this.author
				+ "\",\"text\":\"" + this.text + "\",\"date\":\"" + this.date + "\",\"deleted\":\"" + this.deleted
				+ "\"}";
	}
}
