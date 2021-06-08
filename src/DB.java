import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

public class DB {
    private static Connection con;
    private static PreparedStatement ps;
    private static ResultSet rs;
    private static String port;
    private static String databaseName;
    private static String userName;
    private static String password;
    public static final String NOMOREDATA = "|ND|";
    private static int numberOfColumns;
    private static int currentColumnNumber = 1;
    private static boolean moreData = false;
    private static boolean pendingData = false;
    private static boolean terminated = false;

    private DB() {
    }

    /**
     * Tommy's do while loop put in a method so we can use it through out the code and easily clear
     * the DB.Select statements
     */
    public static void clearData() {
        do {
            String data = DB.getDisplayData();
            if (data.equals(DB.NOMOREDATA)) {
                break;
            } else {
                System.out.print(data);
            }
        } while (true);
    }

    private static void connect() {
        try {
            con = DriverManager.getConnection("jdbc:sqlserver://localhost:" + port + ";databaseName=" + databaseName, userName, password);
        } catch (SQLException var1) {
            System.err.println(var1.getMessage());
        }

    }

    private static void disconnect() {
        try {
            con.close();
        } catch (SQLException var1) {
            System.err.println(var1.getMessage());
        }

    }

    public static void selectSQL(String sql) {
        if (terminated) {
            System.exit(0);
        }

        try {
            if (ps != null) {
                ps.close();
            }

            if (rs != null) {
                rs.close();
            }

            connect();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            pendingData = true;
            moreData = rs.next();
            ResultSetMetaData rsmd = rs.getMetaData();
            numberOfColumns = rsmd.getColumnCount();
        } catch (Exception var2) {
            System.err.println("Error in the sql parameter, please test this in SQLServer first");
            System.err.println(var2.getMessage());
        }

    }

    public static String getDisplayData() {
        if (terminated) {
            System.exit(0);
        }

        if (!pendingData) {
            terminated = true;
            throw new RuntimeException("ERROR! No previous select, communication with the database is lost!");
        } else if (!moreData) {
            disconnect();
            pendingData = false;
            return "|ND|";
        } else {
            return getNextValue(true);
        }
    }

    public static int getNumberOfColumns() {
        return numberOfColumns;
    }

    public static String getData() {
        if (terminated) {
            System.exit(0);
        }

        if (!pendingData) {
            terminated = true;
            throw new RuntimeException("ERROR! No previous select, communication with the database is lost!");
        } else if (!moreData) {
            disconnect();
            pendingData = false;
            return "|ND|";
        } else {
            return getNextValue(false).trim();
        }
    }

    private static String getNextValue(boolean view) {
        StringBuilder value = new StringBuilder();

        try {
            value.append(rs.getString(currentColumnNumber));
            if (currentColumnNumber >= numberOfColumns) {
                currentColumnNumber = 1;
                if (view) {
                    value.append("\n");
                }

                moreData = rs.next();
            } else {
                if (view) {
                    value.append(" ");
                }

                ++currentColumnNumber;
            }
        } catch (SQLException var3) {
            System.err.println(var3.getMessage());
        }

        return value.toString();
    }

    public static boolean insertSQL(String sql) {
        return executeUpdate(sql);
    }

    public static boolean updateSQL(String sql) {
        return executeUpdate(sql);
    }

    public static boolean deleteSQL(String sql) {
        return executeUpdate(sql);
    }

    private static boolean executeUpdate(String sql) {
        if (terminated) {
            System.exit(0);
        }

        if (pendingData) {
            terminated = true;
            throw new RuntimeException("ERROR! There were pending data from previous select, communication with the database is lost! ");
        } else {
            try {
                if (ps != null) {
                    ps.close();
                }

                connect();
                ps = con.prepareStatement(sql);
                int rows = ps.executeUpdate();
                ps.close();
                if (rows > 0) {
                    return true;
                }
            } catch (SQLException | RuntimeException var6) {
                System.err.println(var6.getMessage());
            } finally {
                disconnect();
            }

            return false;
        }
    }

    static {
        Properties props = new Properties();
        String fileName = "./src/db.properties";

        try {
            InputStream input = new FileInputStream(fileName);
            props.load(input);
            port = props.getProperty("port", "1433");
            databaseName = props.getProperty("databaseName");
            userName = props.getProperty("userName", "sa");
            password = props.getProperty("password");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("Database Ready");
        } catch (ClassNotFoundException | IOException var4) {
            System.err.println(var4.getMessage());
        }

    }
}

