package mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import mysite.vo.BoardVo;

@Repository
public class BoardRepository {
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

	// find all posts adapting pagination
	public List<BoardVo> findAllPerPage(int postCntPerPage, int currentIndex) {
		List<BoardVo> result = new ArrayList<>();
		String sql = "select b.id, b.title, b.hit, date_format(b.reg_date, '%Y-%m-%d %H:%i:%s'), b.depth, b.user_id, u.name from board b join user u on b.user_id = u.id order by b.g_no desc, o_no asc limit ? offset ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, postCntPerPage);
			pstmt.setInt(2, postCntPerPage * currentIndex - postCntPerPage);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				long id = rs.getLong(1);
				String title = rs.getString(2);
				long hit = rs.getLong(3);
				String regDate = rs.getString(4);
				long depth = rs.getLong(5);
				long userId = rs.getLong(6);
				String name = rs.getString(7);

				BoardVo vo = new BoardVo();
				vo.setId(id);
				vo.setTitle(title);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setDepth(depth);
				vo.setUserId(userId);
				vo.setUserName(name);

				result.add(vo);
			}

			rs.close();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return result;
	}
	
	public List<BoardVo> findAllPerPage(int postCntPerPage, int currentIndex, String keyword) {
		List<BoardVo> result = new ArrayList<>();
		String sql = "select b.id, b.title, b.hit, date_format(b.reg_date, '%Y-%m-%d %H:%i:%s'), b.depth, b.user_id, u.name from board b join user u on b.user_id = u.id where b.title like ? order by b.g_no desc, o_no asc limit ? offset ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, "%" + keyword + "%");
			pstmt.setInt(2, postCntPerPage);
			pstmt.setInt(3, postCntPerPage * currentIndex - postCntPerPage);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				long id = rs.getLong(1);
				String title = rs.getString(2);
				long hit = rs.getLong(3);
				String regDate = rs.getString(4);
				long depth = rs.getLong(5);
				long userId = rs.getLong(6);
				String name = rs.getString(7);

				BoardVo vo = new BoardVo();
				vo.setId(id);
				vo.setTitle(title);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setDepth(depth);
				vo.setUserId(userId);
				vo.setUserName(name);

				result.add(vo);
			}

			rs.close();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return result;
	}

	// count all posts
	public int countAllPosts() {
		int result = 0;
		String sql = "select count(*) from board";

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();) {
			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return result;
	}
	
	public int countAllPosts(String keyword) {
		int result = 0;
		String sql = "select count(*) from board where title like ?";

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setString(1, "%" + keyword + "%");
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
			rs.close();

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

	// method overload
	public BoardVo findPostById(long id, long userId) {
		BoardVo vo = null;
		String sql = "select title, contents from board where id = ? and user_id = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setLong(1, id);
			pstmt.setLong(2, userId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				String title = rs.getString(1);
				String contents = rs.getString(2);

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
	public void deletePostById(long id, long userId) {
		String sql = "delete from board where id = ? and user_id = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setLong(1, id);
			pstmt.setLong(2, userId);

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
	public Map<String, Long> getParentInfo(long id) {
		Map<String, Long> parentPost =  new HashMap<>();
		String sql = "select g_no, o_no, depth from board where id = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				long groupNo = rs.getLong(1);
				long orderNo = rs.getLong(2);
				long depth = rs.getLong(3);
				
				parentPost.put("groupNo", groupNo);
				parentPost.put("orderNo", orderNo);
				parentPost.put("depth", depth);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return parentPost;
	}

	// insert reply
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

	// search by title
//	public List<BoardVo> searchAllByTitle(String str) {
//		List<BoardVo> result = new ArrayList<>();
//		String sql = "select b.id, b.title, b.hit, date_format(b.reg_date, '%Y-%m-%d %H:%i:%s'), b.depth, b.user_id, u.name from board b join user u on b.user_id = u.id where b.title like ? order by b.g_no desc, o_no asc";
//
//		try (Connection conn = getConnection();
//				PreparedStatement pstmt = conn.prepareStatement(sql);) {
//			pstmt.setString(1, "%" + str + "%");
//			ResultSet rs = pstmt.executeQuery();
//			
//			while (rs.next()) {
//				long id = rs.getLong(1);
//				String title = rs.getString(2);
//				long hit = rs.getLong(3);
//				String regDate = rs.getString(4);
//				long depth = rs.getLong(5);
//				long userId = rs.getLong(6);
//				String name = rs.getString(7);
//
//				BoardVo vo = new BoardVo();
//				vo.setId(id);
//				vo.setTitle(title);
//				vo.setHit(hit);
//				vo.setRegDate(regDate);
//				vo.setDepth(depth);
//				vo.setUserId(userId);
//				vo.setUserName(name);
//
//				result.add(vo);
//			}
//			
//			rs.close();
//
//		} catch (SQLException e) {
//			System.out.println("error:" + e);
//		}
//
//		return result;
//	}

	// update view count
	public void updatePostViewCount(long id) {
		String sql = "update board set hit = hit + 1 where id = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setLong(1, id);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		}
	}
}
