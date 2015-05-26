import java.util.Date;

public class Message {
	private String id;
	private String text;
	private String author;
	private Date date;
	private String deleted;
	private String request;
	
	public Message(){
		
		this.id = "-1";
		this.author = "";
		this.text = "";
		this.date = null;
		this.deleted = "false";
		this.request = "";
		
	}
	
	public Message(String id, String text, String author, Date date, String deleted, String request) {
		this.id = id;
		this.text = text;
		this.author = author;
		this.date = date;
		this.deleted = deleted;
		this.request = request;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	
	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String toString() {
		return "{\"id\":\"" + this.id + "\",\"author\":\"" + this.author
				+ "\",\"text\":\"" + this.text + "\",\"date\":\"" + this.date + "\",\"deleted\":\"" + this.deleted + 
				"\",\"request\":\"" + this.request + "\"}";
	}
}
