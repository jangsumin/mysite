package mysite.repository;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.util.StopWatch;

import mysite.vo.UserVo;

@Repository
public class UserRepository {
	private SqlSession sqlSession;
	
	public UserRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;			
	}
	
	// insert user
	public int insert(UserVo vo) {
		return sqlSession.insert("user.insert", vo);		
	}

	// find user
	public UserVo findByEmailAndPassword(String email, String password) {
		UserVo userVo = sqlSession.selectOne("user.findByEmailAndPassword", Map.of("email", email, "password", password));
		return userVo;
	}

	// find user
	public UserVo findById(long id) {
		return sqlSession.selectOne("user.findById", id);
	}

	// find user for checking email validity
	public UserVo findByEmail(String email) {
		return sqlSession.selectOne("user.findByEmail", email);
	}

	// update
	public int update(UserVo vo) {
		return sqlSession.update("user.update", vo);
	}

}
