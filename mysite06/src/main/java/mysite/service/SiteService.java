package mysite.service;

import org.springframework.stereotype.Service;

import mysite.repository.SiteRepository;
import mysite.vo.SiteVo;

@Service
public class SiteService {
	
	private SiteRepository siteRepository;
	
	public SiteService(SiteRepository siteRepository) {
		this.siteRepository = siteRepository;
	}
	
	public SiteVo getSite() {
		return siteRepository.findSite();
	}
	
	public void updateSite(SiteVo siteVo) {
		siteRepository.updateSite(siteVo);
	}
	
}
