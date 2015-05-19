import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
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
import org.apache.log4j.PropertyConfigurator;

import java.util.Date;
import java.text.SimpleDateFormat;

@WebServlet("/WebChat")
public class MainServlet extends HttpServlet {
    
	private static Logger logger = Logger.getLogger(MainServlet.class.getName());

 
	
	@Override
	public void init() throws ServletException {
		try {
			loadHistory();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("doPost");
		String data = ServletUtil.getMessageBody(request);
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
		try {
			JSONObject json = stringToJson(data);
			Message message = MessageUtil.jsonToMessage(json,
					format.format(date), "POST");
			MessageStorage.addMessage(message);
			XMLHistoryUtil.addData(message);
			logger.info(message.getDate() + " " + message.getText());
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} 

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String token = request.getParameter("token");
		logger.info("doGet");
		logger.info("Token " + token);
		if (token != null && !"".equals(token)) {
			int index = MessageUtil.getIndex(token);
			if(MessageStorage.getSubMessagesByIndex(index).size() != 0){
			String messages = formResponse(index);
			response.setContentType(ServletUtil.APPLICATION_JSON);
			PrintWriter out = response.getWriter();
			out.print(messages);
			out.flush();
			}
			else {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);	
			}
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"'token' parameter needed");
			logger.error("BAD REQUEST");
		}
	}
	
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		    String data = ServletUtil.getMessageBody(request);
		    logger.info("doDelete");
		  try{
			JSONObject json = stringToJson(data);
		    String id = (String)json.get(MessageUtil.ID);
		    Message message = new Message();
		    message.setId(id);
		    message.setRequest("DELETE");
		    message.setDeleted((String)json.get(MessageUtil.DELETED));
		    XMLHistoryUtil.deleteData(id);
			MessageStorage.addMessage(message);
		    logger.info("Delete is done: message id " + id);
		    response.setStatus(HttpServletResponse.SC_OK);
		  }catch(Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				 logger.error(e);
			} 
		    
	}
	
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		    String data = ServletUtil.getMessageBody(request);
		    logger.info("doPut");
		  try{
			JSONObject json = stringToJson(data);
		    String id = (String)json.get(MessageUtil.ID);
		    String text = (String)json.get(MessageUtil.TEXT);
		    Message message = new Message();
		    message.setId(id);
		    message.setRequest("PUT");
		    message.setText(text);
			MessageStorage.addMessage(message);
		    XMLHistoryUtil.updateData(text, id);
		    System.out.println("Put is done: message  id " + id + " new text: " + text);
		    response.setStatus(HttpServletResponse.SC_OK);
		  }catch(Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				 logger.error(e);
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
		if (XMLHistoryUtil.doesStorageExist()) {
			MessageStorage.addAll(XMLHistoryUtil.getMessages());
			System.out.println("Messages from History.xml:");
			MessageStorage.printMessages();
			System.out.println("End of messages from History.xml:");
		} else {
			XMLHistoryUtil.createStorage();
		}
	}

	public static JSONObject stringToJson(String data) throws ParseException {
		JSONParser parser = new JSONParser();
		return (JSONObject) parser.parse(data.trim());
	}
}
