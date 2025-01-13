package mysite.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.service.SiteService;
import mysite.vo.SiteVo;

public class SiteInterceptor implements HandlerInterceptor {
	@Autowired
	private SiteService siteService;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			SiteVo siteVo = siteService.getSite();
			modelAndView.addObject("siteVo", siteVo);
		}
	}
	
	
}
