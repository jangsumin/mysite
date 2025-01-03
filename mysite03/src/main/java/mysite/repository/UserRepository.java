package mysite.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

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
		return sqlSession.selectOne("user.findByEmailAndPassword", Map.of("email", email, "password", password));
	}

	// find user
	public UserVo findById(long id) {
		return sqlSession.selectOne("user.findById", id);
	}

	// update
	public int update(UserVo vo) {
		return sqlSession.update("user.update", vo);
	}
}
