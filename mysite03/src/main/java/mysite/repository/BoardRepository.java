package mysite.repository;

import java.util.HashMap;
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
	public long getMaxGroupNo() {
		return sqlSession.selectOne("board.getMaxGroupNo");
	}

	// find all posts adapting pagination
	public List<BoardVo> findAllPerPage(int postCntPerPage, int currentIndex) {
		return sqlSession.selectList("board.findAllPerPage", Map.of("postCntPerPage", postCntPerPage, "currentIndex", currentIndex * postCntPerPage - postCntPerPage));
	}
	
	// find all posts adapting pagination with keyword
	public List<BoardVo> findAllPerPage(int postCntPerPage, int currentIndex, String keyword) {
		return sqlSession.selectList("board.findAllPerPageWithKeyword", Map.of("postCntPerPage", postCntPerPage, "currentIndex", currentIndex * postCntPerPage - postCntPerPage, "keyword", keyword));
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
	public Map<String, Long> getParentInfo(long id) {
		Map<String, Long> parentPost =  new HashMap<>();
		BoardVo vo = sqlSession.selectOne("board.getParentInfo", id);
		parentPost.put("groupNo", vo.getGroupNo());
		parentPost.put("orderNo", vo.getOrderNo());
		parentPost.put("depth", vo.getDepth());
		return parentPost;
	}

	// insert reply
	public int insertReply(BoardVo vo) {
		updateOrderNo(vo.getGroupNo(), vo.getOrderNo());
		return sqlSession.insert("board.insertReply", vo);
	}

	// update order no for create reply
	public int updateOrderNo(long groupNo, long orderNo) {
		return sqlSession.update("board.updateOrderNo", Map.of("groupNo", groupNo, "orderNo", orderNo));
	}

	// update view count
	public int updatePostViewCount(long id) {
		return sqlSession.update("board.updatePostViewCount", id);
	}
}
