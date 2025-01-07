package mysite.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;
import mysite.security.Auth;
import mysite.service.FileUploadService;
import mysite.service.SiteService;
import mysite.vo.SiteVo;

// 클래스 내 모든 메서드는 admin만 접근할 수 있는 메서드다.
@Auth(role="ADMIN")
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private final SiteService siteService;
	private final FileUploadService fileUploadService;
	private final ServletContext servletContext;
	private final ApplicationContext applicationContext;
	
	public AdminController(SiteService siteService, FileUploadService fileUploadService, ServletContext servletContext, ApplicationContext applicationContext) {
		this.siteService = siteService;
		this.fileUploadService = fileUploadService;
		this.servletContext = servletContext;
		this.applicationContext = applicationContext;
	}
	
	@RequestMapping({"", "/main"})
	public String main(Model model) {
		SiteVo siteVo = siteService.getSite();
		model.addAttribute("siteVo", siteVo);
		return "admin/main";
	}
	
	@RequestMapping("/guestbook")
	public String guestbook() {
		return "admin/guestbook";
	}
	
	@RequestMapping("/board")
	public String board() {
		return "admin/board";
	}
	
	@RequestMapping("/user")
	public String user() {
		return "admin/user";
	}
	
	@RequestMapping("/main/update")
	public String updateSite(@RequestParam("title") String title, @RequestParam("welcome") String welcome, @RequestParam("file") MultipartFile file, @RequestParam("description") String description) {
		String profile = fileUploadService.restore(file);
		SiteVo siteVo = (SiteVo) servletContext.getAttribute("siteVo");
		
		if (profile != null) {
			siteVo.setProfile(profile);			
		}
		
		siteVo.setTitle(title);
		siteVo.setWelcome(welcome);
		siteVo.setDescription(description);
		
		siteService.updateSite(siteVo);
		
		// update servlet context bean
		servletContext.setAttribute("siteVo", siteVo);
		
		// update application context bean
		SiteVo site = applicationContext.getBean(SiteVo.class);
		BeanUtils.copyProperties(siteVo, site);
		
		return "redirect:/";
	}
}
