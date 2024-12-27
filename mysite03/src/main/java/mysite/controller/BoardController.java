package mysite.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
		Map<String, Object> map = boardService.getContentsList(1, "시도");
		model.addAttribute("map", map);
		return "board/list";
	}
	
	@RequestMapping("/view/{id}")
	public String view(@PathVariable("id") long id, Model model) {
		BoardVo post = boardService.getContents(id);
		model.addAttribute("post", post);
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
		
		boardService.addContents(vo);
		
		return "redirect:/board";
	}
}
