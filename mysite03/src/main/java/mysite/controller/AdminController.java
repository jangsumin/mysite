package mysite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import mysite.security.Auth;
import mysite.service.FileUploadService;
import mysite.service.SiteService;
import mysite.vo.SiteVo;

// 클래스 내 모든 메서드는 admin만 접근할 수 있는 메서드다.
@Auth(role="ADMIN")
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private SiteService siteService;
	private FileUploadService fileUploadService;
	
	public AdminController(SiteService siteService, FileUploadService fileUploadService) {
		this.siteService = siteService;
		this.fileUploadService = fileUploadService;
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
		
		SiteVo siteVo = new SiteVo();
		siteVo.setTitle(title);
		siteVo.setWelcome(welcome);
		siteVo.setProfile(profile);
		siteVo.setDescription(description);
		
		System.out.println(title);
		System.out.println(welcome);
		System.out.println(profile);
		System.out.println(description);
		siteService.updateSite(siteVo);
		return "redirect:/";
	}
}
