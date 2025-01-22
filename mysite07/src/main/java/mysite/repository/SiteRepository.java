package mysite.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import mysite.vo.SiteVo;

@Repository
public class SiteRepository {
private SqlSession sqlSession;
	
	public SiteRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;			
	}
	
	// get site
	public SiteVo findSite() {
		return sqlSession.selectOne("site.findSite");
	}
	
	// update site
	public int updateSite(SiteVo siteVo) {
		return sqlSession.update("site.updateSite", siteVo);
	}
}
