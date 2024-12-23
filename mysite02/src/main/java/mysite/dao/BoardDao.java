package mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mysite.vo.BoardVo;

public class BoardDao {
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

	// insert board
	public void insert(BoardVo vo) {
		String sql = "insert into board values(null, ?, ?, 0, now(), ?, ?, ?, ?)";

		getMaxGroupNo(vo);

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setLong(3, vo.getGroupNo());
			pstmt.setLong(4, vo.getOrderNo());
			pstmt.setLong(5, vo.getDepth());
			pstmt.setLong(6, vo.getUserId());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		}
	}

	public void getMaxGroupNo(BoardVo vo) {
		String sql = "select ifnull(max(g_no), 0) + 1 from board";

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();) {
			if (rs.next()) {
				long groupNo = rs.getLong(1);
				vo.setGroupNo(groupNo);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// find all posts
	public List<BoardVo> findAll() {
		List<BoardVo> result = new ArrayList<>();
		String sql = "select b.id, b.title, b.hit, date_format(b.reg_date, '%Y-%m-%d %H:%i:%s'), b.user_id, u.name from board b join user u on b.user_id = u.id order by b.g_no desc, o_no asc";

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();) {
			while (rs.next()) {
				long id = rs.getLong(1);
				String title = rs.getString(2);
				long hit = rs.getLong(3);
				String regDate = rs.getString(4);
				long userId = rs.getLong(5);
				String name = rs.getString(6);

				BoardVo vo = new BoardVo();
				vo.setId(id);
				vo.setTitle(title);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setUserId(userId);
				vo.setUserName(name);

				result.add(vo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return result;
	}

	// find a post
	public BoardVo findPostById(long id) {
		BoardVo vo = null;
		String sql = "select title, contents, user_id from board where id = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				String title = rs.getString(1);
				String contents = rs.getString(2);
				long userId = rs.getLong(3);

				vo = new BoardVo();
				vo.setId(id);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setUserId(userId);
			}

			rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return vo;
	}

	// delete a post
	public void deletePostById(long id) {
		String sql = "delete from board where id = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setLong(1, id);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// modify a post
	public void modifyPost(BoardVo vo) {
		String sql = "update board set title = ?, contents = ? where id = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setLong(3, vo.getId());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		}
	}

	// get groupNo, orderNo, depth of the parent post
	public void getParentPostInfo(BoardVo vo) {
		String sql = "select g_no, o_no, depth from board where id = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setLong(1, vo.getId());

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				long groupNo = rs.getLong(1);
				long orderNo = rs.getLong(2);
				long depth = rs.getLong(3);

				vo.setGroupNo(groupNo);
				vo.setOrderNo(orderNo);
				vo.setDepth(depth);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}
	
	public void insertReply(BoardVo vo) {
		String sql = "insert into board values(null, ?, ?, 0, now(), ?, ?, ?, ?)";
		
		updateOrderNo(vo.getGroupNo(), vo.getOrderNo());

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setLong(3, vo.getGroupNo());
			pstmt.setLong(4, vo.getOrderNo());
			pstmt.setLong(5, vo.getDepth());
			pstmt.setLong(6, vo.getUserId());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		}
	}
	
	public void updateOrderNo(long groupNo, long orderNo) {
		String sql = "update board set o_no = o_no + 1 where g_no = ? and o_no >= ?";
		
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setLong(1, groupNo);
			pstmt.setLong(2, orderNo);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		}
	}
}
