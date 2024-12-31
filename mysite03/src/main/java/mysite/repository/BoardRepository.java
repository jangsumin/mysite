package mysite.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import mysite.vo.BoardVo;

@Repository
public class BoardRepository {
	private SqlSession sqlSession;
	
	public BoardRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;			
	}

	// insert board
	public int insert(BoardVo vo) {
		vo.setGroupNo(getMaxGroupNo());
		return sqlSession.insert("board.insert", vo);
	}

	// get max group no
	public int getMaxGroupNo() {
		return sqlSession.selectOne("board.getMaxGroupNo");
	}

	// find all posts adapting pagination
	public List<BoardVo> findAllPerPage(int postCntPerPage, int currentIndex) {
		return sqlSession.selectList("board.findAllPerPage", Map.of("postCntPerPage", postCntPerPage, "currentIndex", currentIndex));
	}
	
	// find all posts adapting pagination with keyword
	public List<BoardVo> findAllPerPage(int postCntPerPage, int currentIndex, String keyword) {
		return sqlSession.selectList("board.findAllPerPageWithKeyword", Map.of("postCntPerPage", postCntPerPage, "currentIndex", currentIndex, "keyword", keyword));
	}

	// count all posts
	public int countAllPosts() {
		return sqlSession.selectOne("board.countAllPosts");
	}
	
	// count all posts with keyword
	public int countAllPosts(String keyword) {
		return sqlSession.selectOne("board.countAllPostsWithKeyword", keyword);
	}

	// find a post by id
	public BoardVo findPostById(long id) {
		return sqlSession.selectOne("board.findPostById", id);
	}

	// find a post by id and userId
	public BoardVo findPostById(long id, long userId) {
		return sqlSession.selectOne("board.findPostByIdAndUserId", Map.of("id", id, "userId", userId));
	}

	// delete a post
	public int deletePostById(long id, long userId) {
		return sqlSession.delete("board.deletePostById", Map.of("id", id, "userId", userId));
	}

	// modify a post
	public int modifyPost(BoardVo vo) {
		return sqlSession.update("board.modifyPost", vo);
	}

	// get groupNo, orderNo, depth of the parent post
//	public Map<String, Long> getParentInfo(long id) {
//		Map<String, Long> parentPost =  new HashMap<>();
//		String sql = "select g_no, o_no, depth from board where id = ?";
//
//		try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
//			pstmt.setLong(1, id);
//
//			ResultSet rs = pstmt.executeQuery();
//			if (rs.next()) {
//				long groupNo = rs.getLong(1);
//				long orderNo = rs.getLong(2);
//				long depth = rs.getLong(3);
//				
//				parentPost.put("groupNo", groupNo);
//				parentPost.put("orderNo", orderNo);
//				parentPost.put("depth", depth);
//			}
//		} catch (SQLException e) {
//			System.out.println("error:" + e);
//		}
//		
//		return parentPost;
//	}

	// insert reply
//	public void insertReply(BoardVo vo) {
//		String sql = "insert into board values(null, ?, ?, 0, now(), ?, ?, ?, ?)";
//
//		updateOrderNo(vo.getGroupNo(), vo.getOrderNo());
//
//		try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
//			pstmt.setString(1, vo.getTitle());
//			pstmt.setString(2, vo.getContents());
//			pstmt.setLong(3, vo.getGroupNo());
//			pstmt.setLong(4, vo.getOrderNo());
//			pstmt.setLong(5, vo.getDepth());
//			pstmt.setLong(6, vo.getUserId());
//
//			pstmt.executeUpdate();
//		} catch (SQLException e) {
//			System.out.println("드라이버 로딩 실패:" + e);
//		}
//	}

	// update order no for create reply
	public int updateOrderNo(long groupNo, long orderNo) {
		return sqlSession.update("board.updateOrderNo", Map.of("groupNo", groupNo, "orderNo", orderNo));
	}

	// update view count
	public int updatePostViewCount(long id) {
		return sqlSession.update("board.updatePostViewCount", id);
	}
}
