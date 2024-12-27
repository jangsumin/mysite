package mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import mysite.vo.GuestbookVo;

@Repository
public class GuestbookRepository {
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

	public List<GuestbookVo> findAll() {
		List<GuestbookVo> result = new ArrayList<>();
		String sql = "select id, name, contents, date_format(reg_date, '%Y-%m-%d %h:%i:%s') from guestbook order by reg_date desc";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery();) {
			while (rs.next()) {
				long id = rs.getLong(1);
				String name = rs.getString(2);
				String contents = rs.getString(3);
				String regDate = rs.getString(4);

				GuestbookVo vo = new GuestbookVo();
				vo.setId(id);
				vo.setName(name);
				vo.setContents(contents);
				vo.setRegDate(regDate);

				result.add(vo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return result;
	}

	public void insert(GuestbookVo vo) {
		String sql = "insert into guestbook values(null, ?, ?, now(), ?)";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);){
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getContents());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		}
	}

	public void deleteByIdAndPassword(long id, String password) {
		String sql = "delete from guestbook where id = ? and password = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);){
			pstmt.setLong(1, id);
			pstmt.setString(2, password);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		}
	}

}
