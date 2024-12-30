package mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import mysite.vo.UserVo;

@Repository
public class UserRepository {
	// connect database
	private Connection getConnection() throws SQLException {
		Connection conn = null;

		try {
			Class.forName("org.mariadb.jdbc.Driver");
			String url = "jdbc:mariadb://192.168.64.3:3306/webdb";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		}

		return conn;
	}
	
	// insert user
	public void insert(UserVo vo) {
		String sql = "insert into user values(null, ?, ?, ?, ?, now(), 'USER')";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);){
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getEmail());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getGender());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// find user
	public UserVo findByEmailAndPassword(String email, String password) {
		UserVo userVo = null;
		String sql = "select id, name, role from user where email = ? and password = ?";
		
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Long id = rs.getLong(1);
				String name = rs.getString(2);
				String role = rs.getString(3);
				
				userVo = new UserVo();
				userVo.setId(id);
				userVo.setName(name);
				userVo.setRole(role);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return userVo;
	}

	// find user
	public UserVo findById(long id) {
		UserVo userVo = null;
		String sql = "select name, email, gender from user where id = ?";
		
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setLong(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String name = rs.getString(1);
				String email = rs.getString(2);
				String gender = rs.getString(3);
				
				userVo = new UserVo();
				userVo.setId(id);
				userVo.setName(name);
				userVo.setEmail(email);
				userVo.setGender(gender);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return userVo;
	}

	// update
	public void update(UserVo vo) {
		boolean needToUpdatePassword = !"".equals(vo.getPassword());
		String sql = needToUpdatePassword ? "update user set name = ?, password = ?, gender = ? where id = ?" : "update user set name = ?, gender = ? where id = ?";
		
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			if (needToUpdatePassword) {
				pstmt.setString(1, vo.getName());
				pstmt.setString(2, vo.getPassword());
				pstmt.setString(3, vo.getGender());
				pstmt.setLong(4, vo.getId());					
			} else {
				pstmt.setString(1, vo.getName());
				pstmt.setString(2, vo.getGender());
				pstmt.setLong(3, vo.getId());		
			}
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
	}
}
