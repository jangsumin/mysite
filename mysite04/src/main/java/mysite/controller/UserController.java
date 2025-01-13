package mysite.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.validation.Valid;
import mysite.dto.JsonResult;
import mysite.security.Auth;
import mysite.security.AuthUser;
import mysite.service.UserService;
import mysite.vo.UserVo;

@Controller
@RequestMapping("/user")
public class UserController {

	private UserService userService;
	
	// 생성자 주입
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	// user, admin 상관없이 모두가 접할 수 있어야 함.
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String join(@ModelAttribute UserVo userVo) {
		return "user/join";
	}
	
	// user, admin 상관없이 모두가 접할 수 있어야 함.
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String join(@ModelAttribute @Valid UserVo userVo, BindingResult result, Model model) {
		if (result.hasErrors()) {
			Map<String, Object> map = result.getModel();
			model.addAllAttributes(map);
			return "user/join";
		}
		
		userService.join(userVo);
		return "redirect:/user/joinsuccess";
	}
	
	// user, admin 상관없이 모두가 접할 수 있어야 함.
	@RequestMapping("/joinsuccess")
	public String joinSuccess() {
		return "user/joinsuccess";
	}
	
	// user, admin 상관없이 모두가 접할 수 있어야 함.
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "user/login";
	}
	
	@Auth
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(@AuthUser UserVo authUser, Model model) {
		// Access Control 부분은 @Auth를 통해 처리할 수 있음.
		UserVo userVo = userService.getUser(authUser.getId());
		
		model.addAttribute("vo", userVo);
		return "user/update";
	}
	
	@Auth
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@AuthUser UserVo authUser, UserVo userVo) {
		userVo.setId(authUser.getId());
		userService.update(userVo);
		
		authUser.setName(userVo.getName());
		return "redirect:/";
	}
}
