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

public class ListAction implements Action {
	public static final int POSTS_PER_COUNT = 5;

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// List<BoardVo> list = new BoardDao().findAll();
		int countAllPosts = new BoardDao().countAllPosts();

		List<BoardVo> list = new BoardDao().findAllPerPage(POSTS_PER_COUNT, 1);
		request.setAttribute("list", list);

		Map<String, Integer> map = new HashMap<>();
		map.put("minPageIndex", 1);
		map.put("currentPageIndex", 1);

		int maxPageIndex = 0;
		if (countAllPosts % POSTS_PER_COUNT == 0) {
			maxPageIndex = countAllPosts / POSTS_PER_COUNT;
		} else {
			maxPageIndex = countAllPosts / POSTS_PER_COUNT + 1;
		}
		map.put("maxPageIndex", maxPageIndex);

		// 마지막 페이지 인덱스
		int lastPageIndex = 0;
		if (countAllPosts % POSTS_PER_COUNT == 0) {
			lastPageIndex = countAllPosts / POSTS_PER_COUNT;
		} else {
			lastPageIndex = countAllPosts / POSTS_PER_COUNT + 1;
		}
		map.put("lastPageIndex", lastPageIndex);
		request.setAttribute("map", map);

		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/list.jsp");
		rd.forward(request, response);
	}

}
