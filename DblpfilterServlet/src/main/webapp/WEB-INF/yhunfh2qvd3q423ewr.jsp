<%@ page language="java" import="ChartDirector.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String nums=request.getAttribute("totalnum").toString();
String sec=request.getAttribute("searchtime").toString();
String query=request.getAttribute("queryStr").toString();
int pageindex=Integer.parseInt(request.getAttribute("pageindex").toString());
int maxindex=Integer.parseInt(request.getAttribute("maxindex").toString());
String sortfuc=request.getAttribute("sortfuc").toString();
String trend=request.getAttribute("trend").toString();
String start=request.getAttribute("start").toString();
String end=request.getAttribute("end").toString();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 5.01 Transitional//EN">
<html>
  <head>
     <base href="<%=basePath%>">
    <title>DBLP FILTER</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	   <link href="css/bootstrap.css" rel="stylesheet">
      <style type="text/css">
	      body {
	        padding-top: 60px;
	        padding-bottom: 40px;
	      }
	      .sidebar-nav {
	        padding: 9px 0;
	      }
	
	      @media (max-width: 980px) {
	        /* Enable use of floated navbar text */
	        .navbar-text.pull-right {
	          float: none;
	          padding-left: 5px;
	          padding-right: 5px;
	        }
	        }
    </style>
    
  <link href="css/bootstrap-responsive.css" rel="stylesheet">
   <link rel="stylesheet" href="css/searchpage.css">
   	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>
	<script src="js/jquery.peity.js"></script>
	<script>
	$(function() {
	  // Just the defaults.
	$(".bar-colours-2").peity("bar", {
    colours: function(value) {
      return value > 0 ? "green" : "red"
    }
  	})
 		  // Updating charts.
	})
	
	$.fn.peity.defaults.bar = {
	  colours: ["#4d89f9"],
	  delimiter: ",",
	  height: 45,
	  max: null,
	  min: 0,
	  spacing: devicePixelRatio || 1,
	  width: 120
	}
	</script>

  <script type="text/javascript">
  		function trim(str){
		   return str.replace(/(^\s*)|(\s*$)/g, ""); 
		};
  
    	function search(){
    		var searchtext=document.getElementById("searchid");
    		if(trim(searchtext.value)==""){
    			alert("Please enter a query in the search box!!");
    			return false;
    		}
    		return;
    	};
    	function rangeSubmit(){
    		var rangelow=document.getElementById("lowval");
    		var rangeup=document.getElementById("upval");
    		var reg = /^\d+$/;
    		if(!reg.test(rangelow.value)||!reg.test(rangeup.value))
    		{
    			alert("Enter numbers only!!");
    			return false;
    		}
    		return true;
    	};
    	
    	function showinfo(){
    	
    		if(document.cookie!="")
    		{
    			///alert(document.cookie);
    			var myCookie=document.cookie.split(";");
    			for(var i=0;i<myCookie.length;i++){
	    			var dblpuser=myCookie[i];
	    			var loginuser=dblpuser.split("=");
	    			var dbl=loginuser[0].trim();
	    			if(dbl=='dblpuser')
	    			{
	    				var username=loginuser[1].split("#");
	    				var dblp=document.getElementById("dblpuser");
	    				if(username[0].substring(0,1)=='\"'){
	    					dblp.innerHTML=username[0].substring(1);
	    				}
						else
						{
							dblp.innerHTML=username[0];
						}
	    				var href="servlet/ShowUser?action=showuser&userid="+username[1];
	    				//alert(href.substr(0,href.length-1));
	    				///alert(href.charAt(href.length – 1));
	    				if(href.substr(href.length-1,1)=='\"'){
	    					dblp.href=href.substr(0,href.length-1);
	    				}
	    				else
	    				{
	    					dblp.href=href;
	    				}
	    				//alert(dblp.href);
	    				var usersignout=document.getElementById("usersignout");
	    				usersignout.style.display="";
	    			}
    			}
    		}
    		return true;
    	};
    	
    </script>
  
    
  </head>
  <body onload="return showinfo();">
    <div class="navbar navbar-inverse navbar-fixed-top" style="margin-bottom: 3px;">
      <div class="navbar-inner" style="min-width:1336px">
        <div class="container-fluid"  style="min-width:1336px">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"> 
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="index.jsp">DBLP&nbsp; FILTER</a>
          <div class="nav-collapse collapse">
            <p class="navbar-text pull-right">
          	<a href="signin.html" role="button" id="dblpuser" class="navbar-link" data-toggle="modal">Sign in</a>
            &nbsp;&nbsp;<a onclick="return Signout();" id="usersignout" style="display:none" class="navbar-link"><small >Sign out</small></a>
            
            </p>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    <div class="hero-top" align="left" style="min-width:1336px">
    <form class="form-inline" method="GET" action="servlet/DoSearch" onsubmit="return search();">
     <img src="img/smalllogo.png" class="img-rounded" style="margin-left:40px"/>
     <div class="input-append" style="margin-left:70px">
     <input type="hidden" name="searchcopy" value="<%=query%>">
     <input type="hidden" name="trend" value="<%=trend%>">
  	<input class="span6" style="min-width:420px" type="text" name="searchtext" id="searchid" value="<%=query%>" placeholder="">
  		<button type="submit" class="btn btn-primary" align="left">Search</button></div>&nbsp;&nbsp;<label>Sort by </label>
  		&nbsp;<%if(sortfuc.equals("1")){ %>
  		<label><input type="radio" name="sortfuction" checked="checked" class="radio" value="1">&nbsp;relevance</label>&nbsp;&nbsp;&nbsp;&nbsp;<label><input type="radio" name="sortfuction"  class="radio" value="2">&nbsp;importance</label>&nbsp;&nbsp;&nbsp;&nbsp;<label><input type="radio" name="sortfuction"  class="radio" value="3">&nbsp; date</label>
  		<%}else if(sortfuc.equals("2")){ %>
  		<label><input type="radio" name="sortfuction" class="radio" value="1">&nbsp;relevance</label>&nbsp;&nbsp;&nbsp;&nbsp;<label><input type="radio"  checked="checked" name="sortfuction"  class="radio" value="2">&nbsp;importance</label>&nbsp;&nbsp;&nbsp;&nbsp;<label><input type="radio" name="sortfuction"  class="radio" value="3">&nbsp; date</label>
  		<%}else if(sortfuc.equals("3")) {%>
  		 <label><input type="radio" name="sortfuction" class="radio" value="1">&nbsp;relevance</label>&nbsp;&nbsp;&nbsp;&nbsp;<label><input type="radio"  name="sortfuction"  class="radio" value="2">&nbsp;importance</label>&nbsp;&nbsp;&nbsp;&nbsp;<label><input type="radio" name="sortfuction"  checked="checked"  class="radio" value="3">&nbsp; date</label>
  		<%}else{ %>
  		 <label><input type="radio" name="sortfuction" checked="checked" class="radio" value="1">&nbsp;relevance</label>&nbsp;&nbsp;&nbsp;&nbsp;<label><input type="radio" name="sortfuction"  class="radio" value="2">&nbsp;importance</label>&nbsp;&nbsp;&nbsp;&nbsp;<label><input type="radio" name="sortfuction"  class="radio" value="3">&nbsp; date</label>
  		<%} %>
  		</form>
  		</div>
     </div>
    <div class="hero-top2" style="min-width:1336px">
</div>
<div class="hero-top2" style="min-width:1336px;min-height:52px">
		<c:choose>
		<c:when test="${totalnum!='0'}">
		<div id="showinfo"><label class="hero_lab" >Scholar  </label>
		<label class="hero_labr">About <font color="red"><%=nums%></font> results ( <font color="red"><%=sec%></font>sec)</label> 
		<span class="hero_labr2"><span class="bar-colours-2"><%=trend%></span>(<em>1998~2013</em>)</span>
		
		</div>
		</c:when>
		<c:otherwise>
		<div class="alert" align="center" id="warninfo">
		  <strong>Your search - <b><%=query%></b> - did not match any articles.No Article were found containing "<b><%=query%></b> ".</strong> 
		</div>
		</c:otherwise>
		</c:choose>
		<br/>
		</div>
    <div class="container-fluid" style="min-width:1024px;margin: 0 auto -50px;">
      <div class="row-fluid">
        <div class="span2" style="min-width:190px">
          <div class="well sidebar-nav" style="margin-left:20px">
            <ul class="nav nav-list">
              <li class="nav-header">Since Time</li>
              <li ><a href="servlet/DoTimeFilter?query=<%=query%>&fuc=13&trend=<%=trend%>">Since 2013</a></li><!-- class="active" -->
              <li><a href="servlet/DoTimeFilter?query=<%=query%>&fuc=12&trend=<%=trend%>">Since 2012</a></li>
              <li ><a href="servlet/DoTimeFilter?query=<%=query%>&fuc=09&trend=<%=trend%>">Since 2009</a></li>
              <li><a onclick="return rangeSearch();">Custom range...</a>
              <script type="text/javascript">
              	function rangeSearch(){
              		var traget=document.getElementById("rangeid");
              		if(traget.style.display=="none"){ 
              			traget.style.display="";  
				    }
				    else
				    { 
				    	traget.style.display="none";
				    } 
              	};
              	
            </script>
            <script type="text/javascript">
	            var xmlHttp; 
	    		function createXmlHttpRequest(){
	    			if(window.ActiveXObject){
	    				xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
	    			}
	    			else if(window.XMLHttpRequest){
	    				xmlHttp=new XMLHttpRequest();
	    			}
	    		}
	    		
	    		function Increas(id){
	    			createXmlHttpRequest();
	    			var queryCopy=document.getElementById("queryCopy");
	    			var url="servlet/HitsIncreServlet?Sid="+id+"&queryCopy="+queryCopy.value;
	    			xmlHttp.open("GET",url,true);
	    			xmlHttp.send(null);
	    		};
            </script>
              <div id="rangeid" style="display:none">
              <form method="post" action="servlet/DoTimeFilter" onsubmit="return rangeSubmit();">
             	<input type="hidden" name="query" id="queryCopy" value="<%=query%>">
             	<input type="hidden" name="trend" value="<%=trend%>">
              	<input type="text" name="lowval" id="lowval" class="span4">--<input type="text" id="upwval" class="span4" name="upval">
              	<button type="submit"  class="btn" align="center">&nbsp;<i class="icon-search">&nbsp;</i></button>
              </form>
              </div>
              <li class="nav-header">Sort&nbsp;&nbsp;&nbsp;By</li>
              <li><a href="servlet/DoSearch?change=-1&searchtext=<%=query%>&sortfuction=1&trend=<%=trend%>">Sort By Relevance</a></li>
              <li><a href="servlet/DoSearch?change=-1&searchtext=<%=query%>&sortfuction=2&trend=<%=trend%>">Sort By Importance</a></li>
              <li><a href="servlet/DoSearch?change=-1&searchtext=<%=query%>&sortfuction=3&trend=<%=trend%>">Sort By Date</a></li>
             
            </ul>
          </div><!--/.well -->
        </div><!--/span-->
        <div class="span9"  style="min-width:1060px">
        <c:if test="${totalnum=='0'}">
        <div  style="margin-left:50px">
        <h5><p>Suggestions:</p>
		<p>-Make sure all words are spelled correctly.<br>
		-Try different keywords.<br>
		-Try more general keywords.<br>
		-<a target="_blank" href="http://http://scholar.google.com/scholar?q=<%=query%>">Try your query on the entire web</a><br>
		</p>
		</h5>
		</div>
		</c:if>
	
     	<div class="div_info">
     	<% int i=0; %>
     	<c:if test="${!empty documentlist}">
     		<table border="1px" style="border-spacing: 2px;border-collapse: separate;">
     			<c:forEach items="${documentlist}" var="document">
     			<%if(i%2==0){ %>
     			<tr style="background:rgb(237, 255, 237);">
     			<%}
     			else{ %>
     			<tr  class="warning">
     			<%} %>
     			<td style="width:10px">
     			
     			<c:if test="${document.grade==2}">
				<font color="blue">B</font>
				</c:if>
				<c:if test="${document.grade==3}">
					<font color="red">A</font>
				</c:if>
				<c:if test="${document.grade==1}">
					<font color="#22E41A">C</font>
				</c:if>
				<c:if test="${document.grade==0}">
					--
				</c:if>
     			</td>
     			<c:choose>
				<c:when test="${document.type=='article'}">		<!-- <tr style="background:rgb(237, 255, 237);"> -->
				<td style="background:#ffcccc; vertical-align:middle;">
				</c:when>
				<c:otherwise>
				<c:choose>
				<c:when test="${document.type=='inproceedings'}">
				<td style="background:#ccffcc; vertical-align:middle;">
				</c:when>
				<c:otherwise>
				<td style="background:rgb(166, 166, 197); vertical-align:middle;">			
				</c:otherwise>
				</c:choose>
				</c:otherwise>
				</c:choose>
				
				<c:choose>
				<c:when test="${!empty document.ee}">
				<a href="${document.ee}" target="_blank"  onmousedown="Increas(${document.id })">EE</a></td>
				</c:when>
				<c:otherwise>
				<a>EE</a></td>	
				</c:otherwise>
				</c:choose>
				<c:choose>
				<c:when test="${!empty document.googlelink}">
				<td style="vertical-align:middle;"><a  onmousedown="Increas(${document.id })" href="${document.googlelink}" title="Google Scholar Engine" target="_blank">Scholar</a></td>
				
				</c:when>
				<c:otherwise>
					<td style="vertical-align:middle;"><a>Scholar</a></td>
				</c:otherwise>
			</c:choose>
			<td>
				
				<c:choose>
				<c:when test="${!empty document.url}">		
				<a href="${document.url}"  onmousedown="Increas(${document.id })" target="_blank">${document.title}</a>
				</c:when>
				<c:otherwise>
				<c:choose>
				<c:when test="${!empty document.ee}">
				<a href="${document.ee}" onmousedown="Increas(${document.id })" target="_blank">${document.title}</a>
				</c:when>
				<c:otherwise>
				<a>${document.title}</a>
				</c:otherwise>
				</c:choose>
				</c:otherwise>
				</c:choose>	
				<c:if test="${document.author!='None'}">
				(${document.author})
				</c:if>
			&nbsp;&nbsp;&nbsp;<font color="#009933;" ><em>
				<c:if test="${document.type=='article'}">	${document.jourac}</c:if>
				<c:if test="${document.type=='inproceedings'}">${document.conf}</c:if>
				<c:if test="${document.type!='inproceedings'&&document.type!='article'}">${document.conf}</c:if>
				<c:if test="${!empty document.abbr}">(${document.abbr} )</c:if>	
				[${document.year}]
				</em>
				</font>
				<c:if test="${document.citenum!='0'}"><span class="text-right"> <font  color="blue">Cited by ${document.citenum}</font></span>
			</c:if>
			</td>
			</tr>
			<% i++; %>
			</c:forEach>
		</table>
	
		</c:if>
		<c:if test="${totalnum!='0'}">
		<div class="pagination pagination-large" align="center">
		  <ul>
		    <%
		    	if(pageindex==1)
		    	{
		    		out.print("<li class=\"disabled\"><a>Prev</a></li>");
		    		out.print("<li class=\"disabled\"><a>1</a></li>");
		    	}
		    	else
		    	{
		    		out.print("<li><a href=\"servlet/Page?trend="+trend+"&sortfuc="+sortfuc+"&query="+query+"&pageindex="+(pageindex-1)+"&start="+start+"&end="+end+"\">Prev</a></li>");
		    		out.print("<li><a href=\"servlet/Page?trend="+trend+"&sortfuc="+sortfuc+"&query="+query+"&pageindex=1"+"&start="+start+"&end="+end+"\">1</a></li>");
		    	}
		    	if(pageindex!=2&&maxindex>=2)
		    	{
		    		out.print("<li><a href=\"servlet/Page?trend="+trend+"&sortfuc="+sortfuc+"&query="+query+"&pageindex=2"+"&start="+start+"&end="+end+"\">2</a></li>");
		    	}
		    	else if(pageindex==2)
		    	{
		    		out.print("<li class=\"disabled\"><a>2</a></li>");
		    	}
		    	if(pageindex!=3&&maxindex>=3)
		    	{
		    		out.print("<li><a href=\"servlet/Page?trend="+trend+"&sortfuc="+sortfuc+"&query="+query+"&pageindex=3"+"&start="+start+"&end="+end+"\">3</a></li>");
		    	}
		    	else if(pageindex==3)
		    	{
		    		out.print("<li class=\"disabled\"><a>3</a></li>");
		    	}
		    	
		    	if(pageindex!=4&&maxindex>=4)
		    	{
		    		out.print("<li><a href=\"servlet/Page?trend="+trend+"&sortfuc="+sortfuc+"&query="+query+"&pageindex=4"+"&start="+start+"&end="+end+"\">4</a></li>");
		    	}
		    	else if(pageindex==4)
		    	{
		    		out.print("<li class=\"disabled\"><a>4</a></li>");
		    	}
		    	if(pageindex!=5&&maxindex>=5)
		    	{
		    		out.print("<li><a href=\"servlet/Page?trend="+trend+"&sortfuc="+sortfuc+"&query="+query+"&pageindex=5"+"&start="+start+"&end="+end+"\">5</a></li>");
		    	}
		    	else if(pageindex==5)
		    	{
		    		out.print("<li class=\"disabled\"><a>5</a></li>");
		    	}
		    	
		    	if(pageindex<maxindex)
		    	{
		    		out.print("<li><a href=\"servlet/Page?trend="+trend+"&sortfuc="+sortfuc+"&query="+query+"&pageindex="+(pageindex+1)+"&start="+start+"&end="+end+"\">Next</a></li>");
		    	}
		    	else
		    	{
		    		out.print("<li class=\"disabled\"><a>Next</a></li>");
		    	}
		    	
		     %>
		   
		  </ul>
		</div>
		</c:if>
		
		</div>
		 
        </div><!--/span-->
      </div><!--/row-->

      <hr>

      <footer style="margin-bottom:20px;clear:both;height: 50px;">
        <p align="center">&copy; KDE-LAB&nbsp;&nbsp; <small><a href="Feeback.html" target="_blank">Welcome to feedback your suggestion</a></small></p>
      </footer>
      	<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	 <div class="modal-header">
	    <button type="button" class="close" id="closeDiv" data-dismiss="modal" aria-hidden="true">×</button>
	    <h3 id="myModalLabel">DBLP FILTER</h3>
	  </div>
	  <div class="modal-body">
	  <div class="hero-unit">
        <h2 class="form-signin-heading">Please sign in</h2>
        <input type="text" name="email" id="email" class="input-block-level" placeholder="Email address">
        <input type="password" name="pwd" id="pwd" class="input-block-level" placeholder="Password">
        <label class="checkbox">
          <input type="checkbox" name="remb" id="remb" value="rm"> Remember me
        </label>
        <button class="btn btn-large btn-primary" onclick="login();" type="button">Sign in</button>
      <p></p>
        <div padding-top="5px">
        <a href="#">Can't access your account?</a>
        </div>
        <div padding-top="5px">
        	<a href="signup.html" class="pull-right">Signup</a>
        </div>
	  </div>
	  <div class="modal-footer">
	   <!--  <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
	    <button class="btn btn-primary">Save changes</button> -->
	  </div>
	</div>
</div>
  
  </div><!--/.fluid-container-->

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="js/bootstrap-transition.js"></script>
    <script src="js/bootstrap-alert.js"></script>
    <script src="js/bootstrap-modal.js"></script>
    <script src="js/bootstrap-dropdown.js"></script>
    <script src="js/bootstrap-scrollspy.js"></script>
    <script src="js/bootstrap-tab.js"></script>
    <script src="js/bootstrap-tooltip.js"></script>
    <script src="js/bootstrap-popover.js"></script>
    <script src="js/bootstrap-button.js"></script>
    <script src="js/bootstrap-collapse.js"></script>
    <script src="js/bootstrap-carousel.js"></script>
    <script src="js/bootstrap-typeahead.js"></script>
	<script src="js/login.js"></script>

  </body>

</html>
