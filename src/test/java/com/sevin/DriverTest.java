package com.sevin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DriverTest {

	public static void main(String[] args) {
		String url = "jdbc:mysql://172.23.0.174:3306/search?useUnicode=true&characterEncoding=utf-8";
		
		try {
			Connection con = DriverManager.getConnection(url, "search_dev", "dkkqthmjal");
			PreparedStatement ps = con.prepareStatement("select count(1) from content");
			ResultSet rs = ps.executeQuery();
			int i = 0;
			while (rs.next()) {
				i++;
				System.out.println(rs.getObject(i));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
