package mysite.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mysite.service.GuestbookService;
import mysite.vo.GuestbookVo;

@Controller
@RequestMapping("/guestbook")
public class GuestbookController {
	
	private GuestbookService guestbookService;
	
	public GuestbookController(GuestbookService guestbookService) {
		this.guestbookService = guestbookService;
	}
	
	@RequestMapping("")
	public String index(Model model) {
		List<GuestbookVo> list = guestbookService.getContentsList();
		model.addAttribute("list", list);
		return "guestbook/list";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@RequestParam("name") String name, @RequestParam("password") String password, @RequestParam("contents") String contents) {
		GuestbookVo vo = new GuestbookVo();
		vo.setName(name);
		vo.setPassword(password);
		vo.setContents(contents);
		
		guestbookService.addContents(vo);
		return "redirect:/guestbook";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id) {
		return "guestbook/delete";
	}
	
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id, @RequestParam("password") String password) {
		guestbookService.deleteContents(id, password);
		return "redirect:/guestbook";
	}
}
