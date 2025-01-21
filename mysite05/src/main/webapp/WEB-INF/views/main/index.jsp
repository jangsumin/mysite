<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath}/assets/css/main.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="wrapper">
			<div id="content">
				<div id="site-introduction">
					<img id="profile"
						src="${pageContext.request.contextPath}${siteVo.profile}" width="540px">
					
					<c:choose>
						<c:when test="${empty authUser}">
							<h2>안녕하세요. mysite에 오신 것을 환영합니다.</h2>
						</c:when>						
						<c:otherwise>
							<h2>${siteVo.welcome}</h2>						
						</c:otherwise>
					</c:choose>
					<p>
						${siteVo.description}
						<!-- guestbook 서블릿 만들기 -->
						<br> <a href="${pageContext.request.contextPath}/guestbook">방명록</a>에 글 남기기<br>
					</p>
				</div>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>