
import java.util.List;

public interface MessagesDaoInterface {
	void add(Message msg);

	void update(Message msg);

	void delete(String id);

	Message selectById(Message msg);

	List<Message> selectAll();
	
	String selectUserById(int id);
}
