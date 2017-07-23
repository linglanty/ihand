<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
//String isauto=request.getAttribute("isauto").toString();
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
	<meta http-equiv="description" content="D">
	  <link href="css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 40px;
        padding-bottom: 40px;
        background-color: #f5f5f5;
      }
      .form-signin {
        max-width: 300px;
        padding: 19px 29px 29px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
      }
      .form-signin .form-signin-heading,
      .form-signin .checkbox {
        margin-bottom: 10px;
      }
      .form-signin input[type="text"],
      .form-signin input[type="password"] {
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
      }
      .showinfo {
		max-width: 600px;
		padding: 19px 29px 29px;
		margin: 0 auto 20px;
		background-color: #fff;
		border: 1px solid #e5e5e5;
		-webkit-border-radius: 5px;
		-moz-border-radius: 5px;
		border-radius: 5px;
		-webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
		-moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
		box-shadow: 0 1px 2px rgba(0,0,0,.05);
	}
    </style>
    
   <link href="css/bootstrap-responsive.css" rel="stylesheet">
 	<script type="text/javascript">
 	
 		function showuser(){
    		if(document.cookie!="")
    		{
    			var myCookie=document.cookie.split(";");
    			for(var i=0;i<myCookie.length;i++){
	    			var dblpuser=myCookie[i];
	    			var resfield=dblpuser.split("=");
	    			if(resfield[0].trim()=='dblpArea'){
	    				if(resfield[1].trim()!="nothing"){
		    				var field=resfield[1].split("#");
		    				for(var j=0;j<field.length;j++){
				    			if(field[j]=="A"){
				    				var check=document.getElementById("checkA");
				    				check.checked==true;
				    				var checks=document.getElementById("checksA");
				    				checks.checked==true;
				    			}
				    			else{
				    				
				    				var check=document.getElementById("check"+field[j]);
				    				check.checked=true;
				    				var checks=document.getElementById("checks"+field[j]);
				    				checks.checked=true;
				    			}
				    		}
				    	}
	    			}
	    			
	    			if(resfield[0].trim()=='areaOther'){
	    				var show=document.getElementById("areaShow");
	    				var showinfo=document.getElementById("otherchange");
	    				if(resfield[1].trim()!="nothing"){
		    				var field=resfield[1].split("###");
		    				var str=" ";
		    				var str2=" ";
		    				for(var k=0;k<field.length;k++){
				    			///str+="<i class=\"icon-ok\"></i>"+field[k]+" ";
				    			str+="<span id=\""+"p_"+field[k]+"\"><i class=\"icon-ok\" \" ></i>"+field[k]+"</span> ";
				    			str2+="<span id=\""+"t_"+field[k]+"\">"+field[k]+"<i class=\"icon-remove\" onclick=\"RemoveI("+"t_"+field[k]+")\" ></i>"+"<input type=\"hidden\" id=\""+"p_"+field[k]+"\" name=\"areaOther\" value=\""+field[k]+"\"></span>"+" ";
				    		}
				    		show.innerHTML=str;
				    		showinfo.innerHTML=str2;
				    	}
	    			}
    			}
    		}
    		};
 	
 	</script>
 	
 
  <script src="js/Showuser.js"></script>
  <script src="js/modifyinfo.js"></script></head>
  
 <body onload="return showuser();">
	    <div class="navbar navbar-fixed-top">
        <div class="navbar-inner">
          <div class="container">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
            <a class="brand" href="#">DBLP&nbsp; FILTER</a>
            <div class="nav-collapse collapse">
               <p class="navbar-text pull-right" style="margin-bottom: 5px;">
			<a href="#changepas" role="button" data-toggle="modal">Change your password</a>
            
            </p>
            </div><!--/.nav-collapse -->
          </div>
        </div>
      </div>
	
    <div class="container">
	<div class="hero-unit" width="80%">
      <div class="showinfo" >
      <table class="table table-bordered" width="100%">
     		<tr>
	     		<td style="width:50%" class="form-inline">
	     			<label>Name:</label><span id="tagname">${name}</span>
	     		</td>
	     		<td style="width:50%" class="form-inline">
	     			<label>email:</label>${email}
	     		</td>
     		</tr>
     		<tr>
	     		<td colspan="2" height="100px">
	     			<label>The areas you care about:</label> 
	   <table width="100%" class="table table-bordered">
	   <tr><td width="60%">
		<label class="checkbox inline">
	  <input type="checkbox" name="checkggroup" id="checks6" value="6" disabled> Artificial Intelligence & Pattern Recognition
	</label>
		</td><td width="40%">
	<label class="checkbox inline">
	  <input type="checkbox" name="checkggroup" id="checks5" value="5" disabled> Computer Graphics & Multimedia
	</label>
	</td>
	</tr>
	<tr>
	<td width="60%">
		<label class="checkbox inline">
	  <input type="checkbox" name="checkggroup" id="checks4" value="4" disabled> Databases, Data Mining and Content Retrieval
	</label>
		
	</td>
	<td width="40%">
	<label class="checkbox inline">
	  <input type="checkbox" name="checkggroup" id="checks9" value="9" disabled> Theoretical Computer Science
	</label>
	</td>
	</tr>
	<tr>
	<td width="60%">
	<label class="checkbox inline">
	  <input type="checkbox" name="checkggroup" id="checks7" value="7" disabled> Human-computer Interaction & Ubiquitous Computing
	</label>
	</td>
	<td height="40%">
	<label class="checkbox inline">
	  <input type="checkbox" name="checkggroup" id="checks2" value="2" disabled> Network & Information Security
	</label>
	</td>
	</tr>
	<tr>
	<td width="60%">
	<label class="checkbox inline"><input type="checkbox" name="checkggroup" id="checks1" value="1" disabled> Computer Systems & High-performance Computing</label>
	</td>
	<td width="40%">
	<label class="checkbox inline">
	  <input type="checkbox" id="checksA" name="checkggroup" value="A" disabled> Computer Networks
	</label>
	</td>
	</tr>
	<tr>
	<td colspan="2">
	<label class="checkbox inline">
	  <input type="checkbox" id="checks3" name="checkggroup" value="3" disabled> Software Engineering, System Software and Programming Languages
	</label>
	</td>
	</tr>
	<tr><td colspan="2">
	<label class="checkbox inline">
	  <input type="checkbox" id="checks8" name="checkggroup" value="8" disabled> Frontier,Crossing & Comprehensive
	</label>
	</td>
	</tr>
	</table>
	</td>
     		</tr>
     		<tr>
	     		<td colspan="2" class="form-inline">
	     			<label> AUTO PUSH:</label><span id="tagpush">
	     			<c:choose>
	     			<c:when test="${isauto=='0'}">
	     			NO
	     			</c:when>
	     			<c:otherwise>
	     			YES
	     			</c:otherwise>
	     			</c:choose></span>
	     		</td>
     		</tr>
     			<tr>
	     		<td colspan="2" class="form-inline">
	     			<label>Other Area:</label>
	     			<label id="areaShow"></label>
	     			
	     		</td>
     		</tr>
     		<tr>
     			<td colspan="2">
     			<a href="#myModal" role="button" class="btn" data-toggle="modal"><i class=" icon-pencil"></i>Edit</a>
     			</td>
     		</tr>
     	</table>
      </div>
	</div>
    </div> <!-- /container -->
	<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  	
	  	<div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	    <h3 id="myModalLabel">Update your information</h3>
	 	 </div>
	 <!--  	<form action="servlet/ShowUser" method="POST" onsubmit="return update(this);">-->
	  <div class="modal-body">
	       <table class="table table-bordered" width="100%">
	       <tr>
	     		<td style="width:50%">
	     			<label>Name:</label><input type="text" id="username" class="input-large" value="${name}">
	     			 <input type="hidden" id="id" value="${id}">
	     		</td>
	     		<td style="width:50%">
	     			<label>email:</label><input type="text" class="input-large" value="${email}" readonly>
	     		</td>
     		</tr>
     		<tr>
	     		<td colspan="2">
	     			<label>The areas you care about:</label>
	    <div align="left">
		<table width="100%" class="table table-hover"><tr><td width="60%">
		<label class="checkbox inline">
	  <input type="checkbox"  id="check6" value="6"> Artificial Intelligence & Pattern Recognition
	</label>
		</td><td width="40%">
	<label class="checkbox inline">
	  <input type="checkbox"  id="check5" value="5"> Computer Graphics & Multimedia
	</label>
	</td>
	</tr>
	<tr>
	<td width="60%">
		<label class="checkbox inline">
	  <input type="checkbox" id="check4" value="4"> Databases, Data Mining and Content Retrieval
	</label>
		
	</td>
	<td width="40%">
	<label class="checkbox inline">
	  <input type="checkbox"  id="check9" value="9"> Theoretical Computer Science
	</label>
	</td>
	</tr>
	<tr>
	<td width="60%">
	<label class="checkbox inline">
	  <input type="checkbox" id="check7" value="7"> Human-computer Interaction & Ubiquitous Computing
	</label>
	</td>
	<td height="40%">
	<label class="checkbox inline">
	  <input type="checkbox"  id="check2" value="2"> Network & Information Security
	</label>
	</td>
	</tr>
	<tr>
	<td width="60%">
	<label class="checkbox inline"><input type="checkbox" id="check1" value="1"> Computer Systems & High-performance Computing</label>
	</td>
	<td width="40%">
	<label class="checkbox inline">
	  <input type="checkbox" id="checkA" value="A"> Computer Networks
	</label>
	</td>
	</tr>
	<tr>
	<td colspan="2">
	<label class="checkbox inline">
	  <input type="checkbox" id="check3" value="3"> Software Engineering, System Software and Programming Languages
	</label>
	</td>
	</tr>
	<tr><td colspan="2">
	<label class="checkbox inline">
	  <input type="checkbox" id="check8"  value="8"> Frontier,Crossing & Comprehensive
	</label>
	</td>
	</tr>
	</table>
	</div>
	     		</td>
     		</tr>
     		<tr>
     		<c:choose>
     		<c:when test="${isauto=='1'}">
	     		<td colspan="2" class="form-inline">
	     		<label> AUTO PUSH:</label>&nbsp;&nbsp;<label style="font-size:15px"><input type="radio" name="IsPush" value="yes" id="radioyes" checked="checked" class="radio">YES</label>&nbsp;&nbsp;&nbsp;&nbsp;<label style="font-size:15px"><input type="radio" id="radiono" name="IsPush" value="no" class="radio">NO</label>
	     		</td>
	     	</c:when>
     		<c:otherwise>
     			<td colspan="2" class="form-inline">
	     		<label> AUTO PUSH:</label>&nbsp;&nbsp;<label style="font-size:15px"><input type="radio" name="IsPush" value="yes" id="radioyes" class="radio">YES</label>&nbsp;&nbsp;&nbsp;&nbsp;<label style="font-size:15px"><input type="radio" id="radiono" value="no" name="IsPush"  checked="checked" class="radio">NO</label>
	     		</td>
     		</c:otherwise>
     		</c:choose>
     		</tr>
     		<tr><td  colspan="2">
     		Other Area:
     		<input type="text" value="" id="Otherarea" onfocus="InputIn();" onkeypress="return EnterPress(event);"  placeholder="input the area you care about"> <span id="enterone" style="display:none">One at a time,Enter to submit</span>
     			
     			&nbsp;&nbsp;&nbsp;&nbsp;<div id="otherchange">
     			</div>
     		
     		</td></tr>
     	</table>
     <script type="text/javascript">
               
               function InputIn(){
              	 document.getElementById("enterone").style.display="";
               };
               
               function EnterPress(e){
               var e = e || window.event;
			   if(e.keyCode == 13){
			   		
					
					var areaOther=document.getElementById("otherchange");
					var textvalue=document.getElementById("Otherarea");
					
					var show=document.getElementById("areaShow");
					show.innerHTML=show.innerHTML.concat("<span id=\""+"p_"+textvalue.value.replace(/ /g,"_")+"\"><i class=\"icon-ok\" \" ></i>"+textvalue.value+"</span> ");
					areaOther.innerHTML=areaOther.innerHTML.concat("<span id=\""+"t_"+textvalue.value.replace(/ /g,"_")+"\">"+textvalue.value+"<i class=\"icon-remove\" onclick=\"RemoveI("+"t_"+textvalue.value.replace(/ /g,"_")+")\" ></i>"+"<input type=\"hidden\" id=\""+"p_"+textvalue.value+"\" value=\""+textvalue.value+"\"></span>"+" ");
					textvalue.value="";
					return false;
				}
				
				///alert("+++");
               };
               
               function RemoveI(e){
               
               		if(e!=null){
               			var p= document.getElementById("p_"+e.getAttribute("id").substr(2));
               			p.parentNode.removeChild(p);
               			e.parentNode.removeChild(e);
               		}
               };
               
      
               
               </script>
	  </div>
	  <div class="modal-footer">
	    <button class="btn" data-dismiss="modal" id="clickDone" aria-hidden="true">Close</button>
	    <button onclick="return Showss();" class="btn btn-primary">Submit</button>
	  </div>
	
	<!--  </form> --> 
	</div>
	
  	<div id="changepas" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    	
	  	<div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	    <h3 id="myModalLabel">Change your Password</h3>
	 	 </div>
	 	
	  <div class="modal-body">
	       <table class="table table-bordered" width="100%">
	       <tr>
	       		<td style="width:30%;font-size:18px" ><label>Name:</label></td>
	     		<td style="width:70% ">
	     			<input type="text" id="username" class="input-large" value="${name}">
	     			 <input type="hidden" id="email1" value="${email}">
	     			 <input type="hidden" id="userid" value="${id}">
	     		</td>
	     		</tr>
	     		<tr>
	     		<td style="width:30%;font-size:18px"> <label>Old password:</label></td>
	     		<td style="width:70%" class="form-inline">
	     			<input type="password" id="oldpas" onblur="trigger()" class="input-large">&nbsp;&nbsp;<font color="red" id="warning1" style="display:none">The old password is error!!</font>
	     		</td>
     		</tr>
     		<tr>
	     		<td style="width:30%;font-size:18px"> <label>New password:</label></td>
	     		<td style="width:70%" class="form-inline">
	     			<input type="password" id="newpass" name="newpass" class="input-large">&nbsp;&nbsp;<font color="red">*</font>
	     		</td>
     		</tr>
     </table>
    	</div>
	  <div class="modal-footer">
	    <button class="btn" data-dismiss="modal" id="closeDiv" aria-hidden="true">Close</button>
	    <button type="submit" class="btn btn-primary" onclick="changewpd()">Submit</button>
	  </div>
	
	</div>
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
