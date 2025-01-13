package mysite.controller.api;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mysite.dto.JsonResult;
import mysite.service.UserService;
import mysite.vo.UserVo;

@RestController("userApiController")
@RequestMapping("/api/user")
public class UserController {
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	// "{ exist: false or true }"
	// @ResponseBody - @RestController 사용 시에 생략 가능
	@GetMapping("/checkemail")
	public JsonResult checkEmail(@RequestParam(value="email", required=true, defaultValue="") String email) {
		UserVo userVo = userService.getUser(email);
		return JsonResult.success(Map.of("exist", userVo != null));
	}

}
