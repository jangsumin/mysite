package mysite.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mysite.service.BoardService;
import mysite.vo.BoardVo;
import mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	private BoardService boardService;
	
	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@RequestMapping("")
	public String index(Model model) {
		Map<String, Object> map = boardService.getContentsList(1, "");
		model.addAttribute("map", map);
		return "board/list";
	}
	
	@RequestMapping("/page/{index}")
	public String movePage(@PathVariable("index") int index, Model model) {
		Map<String, Object> map = boardService.getContentsList(index, "");
		model.addAttribute("map", map);
		return "board/list";
	}
	
	@RequestMapping("/search")
	public String search(@RequestParam("kwd") String keyword, Model model) {
		Map<String, Object> map = boardService.getContentsList(1, keyword);
		model.addAttribute("map", map);
		model.addAttribute("keyword", keyword);
		return "board/list";
	}
	
	@RequestMapping("/search/{keyword}/page/{index}")
	public String search(@PathVariable("keyword") String keyword, @PathVariable("index") int index, Model model) {
		Map<String, Object> map = boardService.getContentsList(index, keyword);
		model.addAttribute("map", map);
		model.addAttribute("keyword", keyword);
		return "board/list";
	}
	
	@RequestMapping("/view/{id}")
	public String view(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") long id, Model model) {
		BoardVo post = boardService.getContents(id);
		model.addAttribute("post", post);
		
		Cookie[] cookies = request.getCookies();
		boolean hasViewed = false;
		
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("viewed_" + id)) {
					hasViewed = true;
					break;
				}
			}
		}
		
		if (!hasViewed) {
			boardService.updateViewCount(id);
			Cookie cookie = new Cookie("viewed_" + id, "true");
			cookie.setMaxAge(60 * 30);
			cookie.setPath(request.getContextPath());
			response.addCookie(cookie);
		}
		
		return "board/view";
	}
	
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable("id") long id, HttpSession session) {
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			return "redirect:/board";
		}
		
		boardService.deletContents(id, authUser.getId());
		return "redirect:/board";
	}
	
	@RequestMapping(value = "/modify/{id}", method = RequestMethod.GET)
	public String modify(@PathVariable("id") long id, Model model, HttpSession session) {
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			return "redirect:/board";
		}
		
		BoardVo post = boardService.getContents(id, authUser.getId());
		model.addAttribute("post", post);
		return "board/modify";
	}
	
	@RequestMapping(value = "/modify/{id}", method = RequestMethod.POST)
	public String modify(@PathVariable("id") long id, @RequestParam("title") String title, @RequestParam("content") String contents, HttpSession session) {
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			return "redirect:/board";
		}
		
		BoardVo vo = new BoardVo();
		vo.setId(id);
		vo.setTitle(title);
		vo.setContents(contents);
		
		boardService.updateContents(vo);
		return "redirect:/board";
	}
	
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public String write() {
		return "board/write";
	}
	
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String write(@RequestParam("title") String title, @RequestParam("content") String contents, HttpSession session) {
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			return "redirect:/board";
		}
		
		BoardVo vo = new BoardVo();
		vo.setUserId(authUser.getId());
		vo.setTitle(title);
		vo.setContents(contents);
		vo.setOrderNo(1);
		vo.setDepth(0);
		
		boardService.addContents(vo);
		
		return "redirect:/board";
	}
	
	@RequestMapping(value = "/reply/{id}", method = RequestMethod.GET)
	public String reply(@PathVariable("id") long id, Model model) {
		
		Map<String, Long> parentPost = boardService.getParentContentInfo(id);
		model.addAttribute("groupNo", parentPost.get("groupNo"));
		model.addAttribute("orderNo", parentPost.get("orderNo"));
		model.addAttribute("depth", parentPost.get("depth"));
		
		return "board/reply";
	}
	
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public String reply(@RequestParam("groupNo") long groupNo, @RequestParam("orderNo") long orderNo, @RequestParam("depth") long depth, @RequestParam("title") String title, @RequestParam("content") String contents, HttpSession session) {
		UserVo userVo = (UserVo) session.getAttribute("authUser");
		long userId = userVo.getId();
		
		BoardVo vo = new BoardVo();
		vo.setTitle(title);
		vo.setContents(contents);
		vo.setGroupNo(groupNo);
		vo.setOrderNo(orderNo + 1);
		vo.setDepth(depth + 1);
		vo.setUserId(userId);
		
		boardService.addReply(vo);
		
		return "redirect:/board";
	}
}
