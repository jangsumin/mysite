package mysite.controller.action.board;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.controller.ActionServlet.Action;
import mysite.dao.BoardDao;
import mysite.vo.BoardVo;

public class PageMoveAction implements Action {
	public static final int POSTS_PER_COUNT = 5;
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int currentIndex = Integer.parseInt(request.getParameter("currentIndex"));
		
		int countAllPosts = new BoardDao().countAllPosts();
		
		List<BoardVo> list = new BoardDao().findAllPerPage(POSTS_PER_COUNT, currentIndex);
		request.setAttribute("list", list);
		
		Map<String, Integer> map = new HashMap<>();
		
		// 가장 작은 페이지 인덱스
		int minPageIndex = 0;
		if (currentIndex % POSTS_PER_COUNT == 0) {
			minPageIndex = currentIndex;
		} else {
			minPageIndex = ((int) (currentIndex / POSTS_PER_COUNT)) * POSTS_PER_COUNT + 1;
		}
		map.put("minPageIndex", minPageIndex);
		
		// 현재 페이지 인덱스
		map.put("currentPageIndex", currentIndex);
		
		// 마지막 페이지 인덱스
		int lastPageIndex = 0;
		if (countAllPosts % POSTS_PER_COUNT == 0) {
			lastPageIndex = countAllPosts / POSTS_PER_COUNT;
		} else {
			lastPageIndex = countAllPosts / POSTS_PER_COUNT + 1;
		}
		map.put("lastPageIndex", lastPageIndex);
		
		// 가장 큰 페이지 인덱스
		int maxPageIndex = minPageIndex + POSTS_PER_COUNT - 1;
		if (maxPageIndex > lastPageIndex) {
			maxPageIndex = lastPageIndex;
		}
		map.put("maxPageIndex", maxPageIndex);
		request.setAttribute("map", map);
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/list.jsp");
		rd.forward(request, response);
	}

}
