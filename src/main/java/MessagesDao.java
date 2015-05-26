
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

public class MessagesDao implements MessagesDaoInterface {
	private static Logger logger = Logger.getLogger(MessagesDao.class.getName());

	@Override
	public void add(Message msg) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement userStatement = null;
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			connection = ConnectionManager.getConnection();
			userStatement = connection.prepareStatement("SELECT * FROM users WHERE name = ?");
			userStatement.setString(1, msg.getAuthor());
			resultSet = userStatement.executeQuery();
			long id;
			if(resultSet.next()) {
				id = resultSet.getLong("id");
			}
			else {
				 userStatement = connection.prepareStatement("INSERT INTO users (name) VALUES (?)");
			     userStatement.setString(1, msg.getAuthor());
			     userStatement.executeUpdate();
				 ResultSet set = null;
			     statement = connection.createStatement();
			     set = statement.executeQuery("SELECT * FROM users WHERE id = LAST_INSERT_ID()");
			     set.next();
			     id = set.getLong("id");
			}
			preparedStatement = connection.prepareStatement("INSERT INTO messages (id, text, date, user_id) VALUES (?, ?, ?, ?)");
			preparedStatement.setLong(1, Long.parseLong(msg.getId()));
			preparedStatement.setString(2, msg.getText());
			preparedStatement.setTimestamp(3, new Timestamp(msg.getDate().getTime()));
			preparedStatement.setLong(4, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
	}

	@Override
	public void update(Message msg) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement("Update messages SET text = ? WHERE id = ?");
			preparedStatement.setString(1, msg.getText());
			preparedStatement.setLong(2, Long.parseLong(msg.getId()));
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
	}

	@Override
	public Message selectById(Message msg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String selectUserById(int id) {
		Connection connection = null;
		PreparedStatement userStatement = null;
		ResultSet resultSet = null;
		String name = null;
		try {
			connection = ConnectionManager.getConnection();
			userStatement = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
			userStatement.setInt(1,id);
			resultSet = userStatement.executeQuery();
			resultSet.next();
			name = resultSet.getString("name");
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			if (userStatement != null) {
				try {
					userStatement.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
		return name;
	}
	
	@Override
	public List<Message> selectAll() {
		List<Message> messages = new ArrayList<Message>();
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = ConnectionManager.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM messages ORDER BY date");
			while (resultSet.next()) {
				long id = resultSet.getLong("id");
				String text = resultSet.getString("text");
				Date date = resultSet.getTimestamp("date");
				String user = selectUserById(resultSet.getInt("user_id"));
				messages.add(new Message(Long.toString(id),text, user, date, "false","POST"));
			}
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
		return messages;
	}

	@Override
	public void delete(String id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement("DELETE FROM messages WHERE id = ?");
			preparedStatement.setLong(1, Long.parseLong(id));
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}
	}


}
