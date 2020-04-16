package com.hanshin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

public class PracticeJDBC {

	static void printTable(Statement stmt, String sql)
	{
		try {
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				@SuppressWarnings("unused")
				
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String tel = rs.getString("tel");
				String email = rs.getString("email");
				String address = rs.getString("address");
				System.out.println("id : "+id+", name : "+name+", tel : "+tel+", email : "+email
						+", address : "+address);
			}
			System.out.println("\n\n\n");
			rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String jdbc_driver = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://127.0.0.1:3306/databasetest?serverTimezone=UTC";
		
		try {
			Class.forName(jdbc_driver).newInstance(); 
			Connection con = DriverManager.getConnection(url, "root", "123123"); 
			Statement stmt = null;
			PreparedStatement pstmt = null;
			
			//1) Java ������Ʈ���� Statement�� �̿��Ͽ� addressbook �̶�� table ����� 
			String create_sql = "create table databasetest.addressbook ( id INT PRIMARY KEY, name VARCHAR(45), tel VARCHAR(45), "
					            	+ "email VARCHAR(60), address VARCHAR(60))";
			stmt = con.createStatement();
			stmt.executeUpdate(create_sql);
			
			
			//2) addressbook table�� PreparedStatement�� �̿��Ͽ� 5���� ���� ä��� (���� ���Ƿ� ������ ����)
			int[] ids = {1,2,3,4,5};
			String[] names = {"Kim","Hong","Choi","Lee","Kang"};
			String[] tels = {"010-4856-4811","010-7411-1124","010-9914-8711","010-1339-1999","010-6608-4212"};
			String[] emails = {"Kim@gmail.com","Hong@gmail.com","Choi@gmail.com","Lee@gmail.com","Kang@gmail.com"};
			String[] addresses = {"����� ������","��⵵ �����ν�","��⵵ �����","����� ���빮��","������ ��õ��"};
			
			
			String insert_sql = "insert into databasetest.addressbook(id,name,tel,email,address) values(?,?,?,?,?)";
			pstmt = con.prepareStatement(insert_sql);
			for(int i=0;i<5;i++) {
				pstmt.setInt(1,ids[i]);	
				pstmt.setString(2,names[i]);
				pstmt.setString(3,tels[i]);
				pstmt.setString(4,emails[i]);
				pstmt.setString(5,addresses[i]);
				pstmt.executeUpdate();
			}
			
			String select_sql = "select * from databasetest.addressbook";
			printTable(stmt, select_sql);
			
			
			//3) PreparedStatement �̿��Ͽ� 5���� ���� email�� �������� @naver.com���� UPDATE ����
			String update_sql = "update databasetest.addressbook set email=Replace(email,'@gmail.com','@naver.com')";
			pstmt = con.prepareStatement(update_sql);
			pstmt.executeUpdate();
			
			printTable(stmt, select_sql);
			
			
			//4) Statement�� �̿��Ͽ� ���� 2���� ���� ����� �ڵ� ����
			String delete_sql = "delete from databasetest.addressbook order by id DESC limit 2";
			stmt.executeUpdate(delete_sql);
			
			printTable(stmt, select_sql);
			
			
			pstmt.close();
			stmt.close(); 
			con.close(); 
		} 
		catch (Exception e) { 
			e.printStackTrace(); 
		}
	}
}