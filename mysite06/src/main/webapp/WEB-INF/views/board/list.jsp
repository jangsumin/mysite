<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath}/assets/css/board.css"
    rel="stylesheet" type="text/css">
</head>
<body>
    <div id="container">
        <c:import url="/WEB-INF/views/includes/header.jsp" />
        <div id="content">
            <div id="board">
                <form id="search_form" action="${pageContext.request.contextPath}/board/search" method="post">
                    <input type="text" id="kwd" name="kwd" value="${keyword}"> <input
                        type="submit" value="찾기">
                </form>
                <table class="tbl-ex">
                    <tr>
                        <th>번호</th>
                        <th>제목</th>
                        <th>글쓴이</th>
                        <th>조회수</th>
                        <th>작성일</th>
                        <th>&nbsp;</th>
                    </tr>
                    <c:set var="count" value="${fn:length(list)}" />
                    <c:forEach items='${map.get("list")}' var="vo" varStatus="status">
                        <tr>
                            <td>${map.get("currentPage") * 5 + status.index - 4}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/board/view/${vo.id}">
                                    <c:forEach begin="1" end="${vo.depth}">RE:</c:forEach>
                                    ${vo.title}
                                </a>
                            </td>
                            <td>${vo.userName}</td>
                            <td>${vo.hit}</td>
                            <td>${vo.regDate}</td>
                            <td>
                                <c:if test="${vo.userId == authUser.id}">
                                    <a href="${pageContext.request.contextPath}/board/delete/${vo.id}" class="del">삭제</a>                       
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                
                <!-- pager 추가 -->
                <div class="pager">
                    <ul>
                        <c:set var="hasKeyword" value='${keyword == null || keyword == ""}' />
                        <c:choose>
                            <c:when test='${hasKeyword}'>
                                <c:if test='${map.get("currentPage") != 1}'>
                                    <li><a href='${pageContext.request.contextPath}/board/page/${map.get("currentPage") - 1}'>◀</a></li>                            
                                </c:if>
                
                                <c:forEach var="index" begin='${map.get("beginPage")}' end='${map.get("maxPage")}'>
                                    <c:choose>
                                        <c:when test='${map.get("currentPage") == index}'>
                                            <li class="selected">${index}</li>                              
                                        </c:when>
                                        <c:otherwise>
                                            <li><a href="${pageContext.request.contextPath}/board/page/${index}">${index}</a></li>                              
                                        </c:otherwise>
                                    </c:choose> 
                                </c:forEach>
                                
                                <c:if test='${map.get("endPage") != map.get("currentPage")}'>
                                    <li><a href='${pageContext.request.contextPath}/board/search/page/${map.get("currentPage") + 1}'>▶</a></li>                         
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <c:if test='${map.get("currentPage") != 1}'>
                                    <li><a href='${pageContext.request.contextPath}/board/search/${keyword}/page/${map.get("currentPage") - 1}'>◀</a></li>                          
                                </c:if>
                
                                <c:forEach var="index" begin='${map.get("beginPage")}' end='${map.get("maxPage")}'>
                                    <c:choose>
                                        <c:when test='${map.get("currentPage") == index}'>
                                            <li class="selected">${index}</li>                              
                                        </c:when>
                                        <c:otherwise>
                                            <li><a href="${pageContext.request.contextPath}/board/search/${keyword}/page/${index}">${index}</a></li>                                
                                        </c:otherwise>
                                    </c:choose> 
                                </c:forEach>
                                
                                <c:if test='${map.get("endPage") != map.get("currentPage")}'>
                                    <li><a href='${pageContext.request.contextPath}/board/search/${keyword}/page/${map.get("currentPage") + 1}'>▶</a></li>                          
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>                  
                <!-- pager 추가 -->
                
                <c:if test="${not empty authUser}">
                    <div class="bottom">
                        <a href="${pageContext.request.contextPath}/board/write" id="new-book">글쓰기</a>
                    </div>              
                </c:if>
            </div>
        </div>
        <c:import url="/WEB-INF/views/includes/navigation.jsp" />
        <c:import url="/WEB-INF/views/includes/footer.jsp" />
    </div>
</body>
</html>