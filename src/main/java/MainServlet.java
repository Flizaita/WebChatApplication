import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import java.util.Date;
import java.text.SimpleDateFormat;

@WebServlet("/WebChat")
public class MainServlet extends HttpServlet {

	@Override
	public void init() throws ServletException {
		try {
			loadHistory();
		} catch (SAXException e) {
			System.out.print("ERROR");
		} catch (IOException e) {
			System.out.print("ERROR");
		} catch (ParserConfigurationException e) {
			System.out.print("ERROR");
		} catch (TransformerException e) {
			System.out.print("ERROR");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String data = ServletUtil.getMessageBody(request);
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
		try {
			JSONObject json = stringToJson(data);
			Message message = MessageUtil.jsonToMessage(json,
					format.format(date));
			MessageStorage.addMessage(message);
			XMLHistoryUtil.addData(message);
			System.out.println(message.getDate() + " " + message.getText());
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (SAXException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} catch (IOException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} catch (ParserConfigurationException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} catch (TransformerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} catch (ParseException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String token = request.getParameter("token");

		if (token != null && !"".equals(token)) {
			int index = MessageUtil.getIndex(token);
			String messages = formResponse(index);
			response.setContentType(ServletUtil.APPLICATION_JSON);
			PrintWriter out = response.getWriter();
			out.print(messages);
			out.flush();
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"'token' parameter needed");
		}
	}

	@SuppressWarnings("unchecked")
	private String formResponse(int index) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("messages", MessageStorage.getSubMessagesByIndex(index));
		jsonObject.put("token", MessageUtil.getToken(MessageStorage.getSize()));
		return jsonObject.toJSONString();
	}

	private void loadHistory() throws SAXException, IOException,
			ParserConfigurationException, TransformerException {
		if (Boolean.getBoolean(XMLHistoryUtil.doesStorageExist())) {
			MessageStorage.addAll(XMLHistoryUtil.getMessages());
		} else {
			XMLHistoryUtil.createStorage();
		}
	}

	public static JSONObject stringToJson(String data) throws ParseException {
		JSONParser parser = new JSONParser();
		return (JSONObject) parser.parse(data.trim());
	}
}
