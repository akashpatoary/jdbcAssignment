package assigment;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdbcWithPreparedStatementAndDate {

	public static void main(String[] args) throws SQLException {
		Scanner scanner = new Scanner(System.in);

		String alienName = null;
		String alienAddress = null;
		String alienGender = null;
		Date dob = null;
		Date doj = null;
		Date dom = null;

		int operationNo = getOperationNo(scanner);
		// if user want to perform insert operation
		if (operationNo == 1) {
			alienName =getAlienName(scanner);
			alienAddress = getAddress(scanner);
			alienGender = getGender(scanner);
			try {
				dob = getDob(scanner);			
				doj = getDoj(scanner);			
				dom = getDom(scanner);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} 
		// if user want to perform read operation
		else if (operationNo == 2) {

		}
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = getJdbcConnection();
			if (connection != null) {
				if (operationNo == 1) {
					String sqlQuery = "insert into alien (name,address,gender,dob,doj,dom) values(?,?,?,?,?,?)";
					statement = connection.prepareStatement(sqlQuery);
					if (statement != null) {
						statement.setString(1, alienName);
						statement.setString(2, alienAddress);
						statement.setString(3, alienGender);
						statement.setDate(4, dob);
						statement.setDate(5, doj);
						statement.setDate(6, dom);
						statement.executeUpdate();
						System.out.println("insert is successful!");
					}
				}

				// read
				else if (operationNo == 2) {
					String sqlQuery = "select * from alien;";
					statement = connection.prepareStatement(sqlQuery);
					if (statement != null) {
						// if we want to fetch data s_id we need to set it here
						resultSet = statement.executeQuery();
					}

				}


			}
			if (resultSet != null) {
				System.out.println("a_name\t a_address\t a_gender\t a_dob\t\t a_doj\t\t a_dom ");
				SimpleDateFormat dobFormat = new SimpleDateFormat("dd-MM-yyyy");
				SimpleDateFormat dojFormat = new SimpleDateFormat("MM-dd-yyyy");
				SimpleDateFormat domFormat = new SimpleDateFormat("yyyy-MM-dd");
				while (resultSet.next()) {
					String aName = resultSet.getString("name");
					String aAddress = resultSet.getString("address");
					String aGender = resultSet.getString("gender");					
					String ddob = dobFormat.format(resultSet.getDate("dob"));
					String ddoj = dojFormat.format(resultSet.getDate("doj"));
					String ddom = domFormat.format(resultSet.getDate("dom"));					
					System.out.println(aName + "\t" + aAddress + "\t \t" + aGender+ "\t\t" + ddob + "\t " + ddoj+ "\t " + ddom);
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
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
			System.out.println(" 1. Create 2. Read ");
			operationNo = scanner.nextInt();
			scanner.nextLine();
		} while (!(operationNo <= 2 && operationNo != 0));

		return operationNo;
	}

	public static String getAlienName(Scanner scanner) {
		String userInputName = null;
		Pattern pattern = Pattern.compile("[a-zA-Z]+");
		Matcher matcher = null;

		do {
			System.out.println("Please enter Alien name ! [only letters are allowed]");
			userInputName = scanner.nextLine();
			matcher = pattern.matcher(userInputName);
		} while (!matcher.matches());
		return userInputName;

	}
	public static String getAddress(Scanner scanner) {
		String address = null;
		Pattern pattern = Pattern.compile("[a-zA-Z]+");
		Matcher matcher = null;

		do {
			System.out.println("Please enter Alien address ! [only letters are allowed]");
			address = scanner.nextLine();
			matcher = pattern.matcher(address);
		} while (!matcher.matches());
		return address;

	}

	public static String getGender(Scanner scanner) {
		String gender = null;
		Pattern pattern = Pattern.compile("[a-zA-Z]+");
		Matcher matcher = null;

		do {
			System.out.println("Please enter Alien gender ! [only letters are allowed]");
			gender = scanner.nextLine();
			matcher = pattern.matcher(gender);
		} while (!matcher.matches());
		return gender;

	}
	
	public static Date getDob(Scanner scanner) throws ParseException {
		System.out.println("Enter your Dob [YYYY-MM-dd]");
		String sdob = scanner.nextLine();
		/*SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date uDate = sdf.parse(sdob);
		long time = uDate.getTime();*/
		Date dob =Date.valueOf(sdob);
		return dob;
	}

	public static Date getDoj(Scanner scanner) throws ParseException {
		System.out.println("Enter your Doj [YYYY-MM-dd]");
		String sdoj = scanner.nextLine();
		/*SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		java.util.Date uDate = sdf.parse(sdoj);
		long time = uDate.getTime();*/
		Date doj = Date.valueOf(sdoj);
		return doj;
	}

	public static Date getDom(Scanner scanner) throws ParseException {
		System.out.println("Enter your Dom [YYYY-MM-dd]");
		String sdom = scanner.nextLine();
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date uDate = sdf.parse(sdom);
		long time = uDate.getTime();*/
		Date dom = Date.valueOf(sdom);
		return dom;
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
