import java.io.*;
import java.sql.*;

public class DatabaseJAVA {

    static final String jdbcURL 
	= "jdbc:oracle:thin:@127.0.0.1:1521:orcl";

    public static void main(String[] args) {
        try {

            // Load the driver. This creates an instance of the driver
	        // and calls the registerDriver method to make Oracle Thin
	        // driver available to clients.

            Class.forName("oracle.jdbc.OracleDriver");

	        String user = "asaha1";	// For example, "jsmith"
	        String passwd = "";	// Your 9 digit student ID number


            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {

		// Get a connection from the first driver in the
		// DriverManager list that recognizes the URL jdbcURL

		conn = DriverManager.getConnection(jdbcURL, user, passwd);

		// Create a statement object that will be sending your
		// SQL statements to the DBMS

		stmt = conn.createStatement();
	/*
		String sid = "S1";
        System.out.println("Reserved Resources");
        //reserved book
		rs = stmt.executeQuery("SELECT * from sBookWL,Books where sBookWL.sid ='"+sid+"' and sBookWL.sb_bid = Books.bid");
        System.out.println("-------------------Reserved Books--------------------");
		while (rs.next()) {
            String sb_bid = rs.getString("sb_bid");
            String show_bisbn = rs.getString("bisbn");
			String show_btitle = rs.getString("btitle");
			int show_bedition = rs.getInt("bedition");
			String show_bauthor = rs.getString("bauthor");
			int  show_byear = rs.getInt("bpubyear");
			String show_bpublisher = rs.getString("bpub");
			String show_blocation = rs.getString("blocation");
            System.out.println(sid+" "+sb_bid+" "+show_bisbn+" "+show_btitle+" "+show_bedition+" "+show_bauthor+" "+show_bpublisher+" "+show_byear+" "+show_blocation);

		}
	*/

            } finally {
                close(rs);
                close(stmt);
                close(conn);
            }
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    }

    static void close(Connection conn) {
        if(conn != null) {
            try { conn.close(); } catch(Throwable whatever) {}
        }
    }

    static void close(Statement st) {
        if(st != null) {
            try { st.close(); } catch(Throwable whatever) {}
        }
    }

    static void close(ResultSet rs) {
        if(rs != null) {
            try { rs.close(); } catch(Throwable whatever) {}
        }
    }
}

