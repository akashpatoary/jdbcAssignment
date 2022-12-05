package assigment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdbcWithPreparedStatement {

	public static void main(String[] args) throws SQLException {
		Scanner scanner = new Scanner(System.in);

		String studentName = null;
		Integer courseId = null;
		Integer studentId = null;

		int operationNo = getOperationNo(scanner);
		// if user want to perform insert operation
		if (operationNo == 1) {
			studentName = getStudentName(scanner);
			courseId = getCourseId(scanner);
		} else if (operationNo == 2) {

		}
		// if user want to perform update operation
		else if (operationNo == 3) {
			studentId = getStudentId(scanner, operationNo);
			studentName = getStudentName(scanner);
			courseId = getCourseId(scanner);
		}
		// if user want to perform delete operation
		else if (operationNo == 4) {
			studentId = getStudentId(scanner, operationNo);
		}
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = getJdbcConnection();
			if (connection != null) {
				if (operationNo == 1) {
					String sqlQuery = "insert into student (s_name,c_id) values(?,?)";
					statement = connection.prepareStatement(sqlQuery);
					if (statement != null) {
						statement.setString(1, studentName);
						statement.setInt(2, courseId);
						statement.executeUpdate();
						System.out.println("insert is successful!");
					}
				}

				// read
				else if (operationNo == 2) {
					String sqlQuery = "select * from student;";
					statement = connection.prepareStatement(sqlQuery);
					if (statement != null) {
						// if we want to fetch data s_id we need to set it here
						resultSet = statement.executeQuery();
					}

				}

				// update
				else if (operationNo == 3) {
					String sqlQuery = "update student set s_name=?, c_id =? where s_id =?";
					statement = connection.prepareStatement(sqlQuery);
					if (statement != null) {
						statement.setString(1, studentName);
						statement.setInt(2, courseId);
						statement.setInt(3, studentId);
						statement.executeUpdate();
						System.out.println("update is successful!");
					}
				}
				// delete
				else if (operationNo == 4) {
					String sqlQuery = "delete from student where s_id=?";
					statement = connection.prepareStatement(sqlQuery);
					if (statement != null) {
						statement.setInt(1, studentId);
						statement.executeUpdate();
						System.out.println("delete is successful!");
					}

				}

			}
			if (resultSet != null) {
				System.out.println("s_id\ts_name\t c_id ");
				while (resultSet.next()) {
					int sId = resultSet.getInt("s_id");
					String sName = resultSet.getString("s_name");
					int cId = resultSet.getInt("c_id");
					System.out.println(sId + "\t" + sName + "\t " + cId);
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(resultSet, connection, statement);
			if (scanner != null) {
				scanner.close();
			}
		}

	}

	public static int getOperationNo(Scanner scanner) {
		int operationNo;
		do {
			System.out.println(" 1. Create 2. Read 3. Update 4. Delete");
			operationNo = scanner.nextInt();
			scanner.nextLine();
		} while (!(operationNo <= 4 && operationNo != 0));

		return operationNo;
	}

	public static String getStudentName(Scanner scanner) {
		String userInputName = null;
		Pattern pattern = Pattern.compile("[a-zA-Z]+");
		Matcher matcher = null;

		do {
			System.out.println("Please enter student name ! [only letters are allowed]");
			userInputName = scanner.nextLine();
			matcher = pattern.matcher(userInputName);
		} while (!matcher.matches());
		return userInputName;

	}

	public static int getCourseId(Scanner scanner) {
		int courseId;
		while (true) {
			System.out.println("Please enter the course Id! [only integer is allowed]");
			String input = scanner.nextLine();
			try {
				courseId = Integer.parseInt(input);
				break;
			} catch (NumberFormatException ne) {
				System.out.println("Input is not a number, continue");
			}
		}
		return courseId;

	}

	public static int getStudentId(Scanner scanner, int operationNo) {
		Integer studentId = 0;
		while (true) {

			if (operationNo == 3) {
				System.out.println(
						"Please enter the studentId to change studentName and courseId! [only integer is allowed]");

			} else if (operationNo == 4) {
				System.out.println("Please enter the studentId to delete the record! [only integer is allowed]");

			}
			String userInput = scanner.nextLine();
			try {
				studentId = Integer.parseInt(userInput);
				break;
			} catch (Exception e) {
				System.out.println("Input is not a number, continue");
			}
		}
		return studentId;
	}

	public static Connection getJdbcConnection() throws SQLException {
		Connection connection = null;
		String url = "jdbc:postgresql://localhost:5432/SpringBootEnterPrise";
		String user = "postgres";
		String password = "postgres";

		connection = DriverManager.getConnection(url, user, password);
		if (connection != null) {
			return connection;
		}

		return connection;
	}

	public static void closeConnection(ResultSet resultSet, Connection connection, Statement statement)
			throws SQLException {

		if (resultSet != null) {
			resultSet.close();
		}

		if (statement != null) {
			statement.close();
		}
		if (connection != null) {
			connection.close();
		}

	}

}
