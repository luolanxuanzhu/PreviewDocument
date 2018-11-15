<%@ page language="java" pageEncoding="utf-8" import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>文档显示</title>
</head>
<body>
	<h2>目录：D:/ftp/${requestScope.dirname}</h2>

	<c:forEach items="${requestScope.fileInfoList}" var="map">
		<div>
			<c:if test="${map.ispreview==true}">
				<a
					href="convert?dirname=${requestScope.dirname}&filename=${map.filename}&role=${requestScope.role}">${map.filename}</a>
			</c:if>
			<c:if test="${map.ispreview==false}">
				<c:if test="${map.isfile==1}">
					<a
						href="convert?dirname=${requestScope.dirname}&filename=${map.filename}&role=${requestScope.role}">${map.filename}</a>
				</c:if>
				<c:if test="${map.isfile==2}">
				${map.filename}
			</c:if>

			</c:if>
			<%-- <a
				href="convert?dirname=${requestScope.dirname}&filename=${map.filename}">${map.filename}</a> --%>
			<%-- <c:if test="${map.isfile==2}">
				<a
					href="download?dirname=${requestScope.dirname}&filename=${map.filename}">下载</a>
			</c:if> --%>

		</div>
	</c:forEach>
</body>
</html>