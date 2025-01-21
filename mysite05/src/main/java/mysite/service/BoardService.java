package mysite.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import mysite.repository.BoardRepository;
import mysite.vo.BoardVo;

@Service
public class BoardService {
	private static final int CONTENTS_PER_PAGE = 5;
	private BoardRepository boardRepository;

	public BoardService(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	public void addContents(BoardVo vo) {
		boardRepository.insert(vo);
	}
	
	public Map<String, Long> getParentContentInfo(long id) {
		return boardRepository.getParentInfo(id);
	}
	
	public void addReply(BoardVo vo) {
		boardRepository.insertReply(vo);
	}
 
	public BoardVo getContents(long id) {
		return boardRepository.findPostById(id);
	}

	public BoardVo getContents(long id, long userId) {
		return boardRepository.findPostById(id, userId);
	}
	
	public void updateViewCount(long id) {
		boardRepository.updatePostViewCount(id);
	}

	public void updateContents(BoardVo vo) {
		boardRepository.modifyPost(vo);
	}

	public void deleteContents(long id, long userId) {
		boardRepository.deletePostById(id, userId);
	}

	public Map<String, Object> getContentsList(int currentPage, String keyword) {
		Map<String, Object> map = new HashMap<>();

		List<BoardVo> list;
		
		if ("".equals(keyword)) {
			list = boardRepository.findAllPerPage(CONTENTS_PER_PAGE, currentPage);
		} else {
			list = boardRepository.findAllPerPage(CONTENTS_PER_PAGE, currentPage, keyword);
		}
		
		map.put("list", list);

		int beginPage = 0;
		if (currentPage % CONTENTS_PER_PAGE == 0) {
			beginPage = currentPage;
		} else {
			beginPage = ((int) (currentPage / CONTENTS_PER_PAGE)) * CONTENTS_PER_PAGE + 1;
		}
		map.put("beginPage", beginPage);

		map.put("currentPage", currentPage);

		int endPage = 0;
		int allContentsCount;
		
		if ("".equals(keyword)) {
			allContentsCount = boardRepository.countAllPosts();
		} else {
			allContentsCount = boardRepository.countAllPosts(keyword);
		}
	
		if (allContentsCount % CONTENTS_PER_PAGE == 0) {
			endPage = allContentsCount / CONTENTS_PER_PAGE;
		} else {
			endPage = allContentsCount / CONTENTS_PER_PAGE + 1;
		}
		map.put("endPage", endPage);

		int maxPage = beginPage + CONTENTS_PER_PAGE - 1;
		if (maxPage > endPage) {
			maxPage = endPage;
		}
		map.put("maxPage", maxPage);
	
		return map;
	}
}
