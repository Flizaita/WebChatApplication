import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public final class MessageUtil {
	public static final String TOKEN = "token";
	public static final String MESSAGES = "messages";
	private static final String TN = "TN";
	private static final String EN = "EN";
	public static final String ID = "id";
	public static final String TEXT = "text";
	public static final String AUTHOR = "author";
	public static final String DELETED = "deleted";

	private MessageUtil() {
	}

	public static String getToken(int index) {
		Integer number = index * 8 + 11;
		return TN + number + EN;
	}

	public static int getIndex(String token) {
		return (Integer.valueOf(token.substring(2, token.length() - 2)) - 11) / 8;
	}

	public static JSONObject stringToJson(String data) throws ParseException {
		JSONParser parser = new JSONParser();
		return (JSONObject) parser.parse(data.trim());
	}

	public static Message jsonToMessage(JSONObject json, Date date, String request) {
		Object id = json.get(ID);
		Object text = json.get(TEXT);
		Object author = json.get(AUTHOR);
		Object deleted = json.get(DELETED);

		if (id != null && text != null && author != null) {
			return new Message((String) id, (String) text, (String) author,
					date, (String)deleted, request);
		}
		return null;
	}
}
