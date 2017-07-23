<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
		        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
		        font-size: 18px;
		        font-weight: 200;
				color: inherit;
		      }
		      #gs_hp_lr{
		      	margin-top: 15px;
				zoom: 1;
				text-align: center;
		      };
    </style>
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
	    				///alert(href.charAt(href.length ¨C 1));
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
	
	<link rel="stylesheet" href="css/bootstrap-responsive.css" type="text/css"></link>
    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
     <!--  <script src="js/html5shiv.js"></script>--> 
  
  <script src="js/login1.js"></script></head>
   <body onload="return showinfo();">
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="#">DBLP&nbsp; FILTER</a>
          <div class="nav-collapse collapse">
          <p class="navbar-text pull-right">
          <a href="#myModal" role="button" id="dblpuser" class="navbar-link" data-toggle="modal">Sign in</a>
          	
          	&nbsp;&nbsp;<a onclick="return Signout();" id="usersignout" style="display:none" class="navbar-link"><small >Sign out</small></a>
            </p>
          </div>
        </div>
      </div>
    </div>


    <div class="container">

      <div class="hero-unit"><div align="center"> 
      <table  class="table" cellPadding="0px" cellSpacing="0px" border="0px">
      <tr>
     
      	<td width="100%" align="center" valign="middle">
      	<div align="center">
      		<img src="img/logo.png" class="img-rounded" align="middle">
      		</div>
      	</td>
      </tr>
         <tr>
        <td width="100%" align="center"nowrap="nowrap">
        <div align="center" >
        <form class="form-search" method="GET" action="servlet/DoSearch" onsubmit="return search();">
  		<input class="span5" type="text" name="searchtext" id="searchid" placeholder="What you want?">
  		<button type="submit" class="btn">Search</button>
  		<table id="gs_hp_lr">
  		<tr>
  		<td>
  		<label style="font-size:18px"><input type="radio" name="sortfuction" checked="checked" value="1" class="radio">&nbsp;Sort by relevance</label>
  		</td>
  		<td>
  		<label style="font-size:18px"><input type="radio" name="sortfuction" value="2"  class="radio">&nbsp;Sort by importance</label>
  		</td>
  		<td>
  		<label style="font-size:18px"><input type="radio" name="sortfuction" value="3"  class="radio">&nbsp;Sort by date</label>
  		</td>
  		</tr>
  		</table>
  		</form>
        </div></td>
      </tr>
      	</table>
      </div>
      <!-- Example row of columns -->
      <div class="row">
    </div>
      <hr>
      <footer style="margin-bottom:1cm">
        <p align="center">&copy; KDE-LAB  &nbsp; &nbsp; &nbsp;<small><a href="Feeback.html" target="_blank">Welcome to feedback your suggestion</a></small></p>
      </footer>
	<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	 <div class="modal-header">
	    <button type="button" class="close" id="closeDiv" data-dismiss="modal" aria-hidden="true">¡Á</button>
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
    </div> <!-- /container -->
    

 
    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="js/jquery.js"></script>
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
</body>

</html>
