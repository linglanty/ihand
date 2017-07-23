<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 5.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>EDIT JOURNALS AND CONFERENCE</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link href="css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 20px;
        padding-bottom: 40px;
      }

      /* Custom container */
      .container-narrow {
        margin: 0 auto;
        max-width: 700px;
      }
      .container-narrow > hr {
        margin: 30px 0;
      }

      /* Main marketing message and sign up button */
      .jumbotron {
        margin: 60px 0;
        text-align: center;
      }
      .jumbotron h1 {
        font-size: 72px;
        line-height: 1;
      }
      .jumbotron .btn {
        font-size: 21px;
        padding: 14px 24px;
      }

      /* Supporting marketing content */
      .marketing {
        margin: 60px 0;
      }
      .marketing p + h4 {
        margin-top: 28px;
      }
    </style>
    <link href="css/bootstrap-responsive.css" rel="stylesheet">
  	<script type="text/javascript">
			var xmlHttp; 
			var d_id;
    		function createXmlHttpRequest(){
    			if(window.ActiveXObject){
    				xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
    			}
    			else if(window.XMLHttpRequest){
    				xmlHttp=new XMLHttpRequest();
    			}
    		};
    		
   			function deleteperconf(id){
   				var msg="Are you sure to delete?";
   				if(confirm(msg)==true)
   				{
	   				d_id=id;
	    			createXmlHttpRequest();
	    			var url="servlet/Modify?action=delete&pid="+id;
	    			xmlHttp.open("GET",url,true);
	    			xmlHttp.onreadystatechange=DeleteTr;
	    			xmlHttp.send(null);
    			}
    		};
    		
    		function DeleteTr(){
	    		if(xmlHttp.readyState==4){
	    			if(xmlHttp.status==200){
	    				 var xmlDoc=xmlHttp.responseText;     
	    				 
	    				 if(xmlDoc=="---+"){
	    				 	alert("Don't try any other url!!");	    				 
	    				 	return;
	    				 }
			             else if(xmlDoc=="YES")
			             {
			             	alert("----SUCCESS!!----");
			             	document.getElementById("p_"+d_id).style.display="none";
			             }
			             else
			             {
			             	alert("----FAILURE!!Please try again!!----");
			             }
			        } 
			        else{                         
			            alert("AJAX服务器返回错误！");                    
			        }  
	    		}
    		};
   		
  </script>
  
  </head>
  
   <body>
    <div class="container-narrow">
      <div class="masthead">
        <h3 class="muted">DBLP FILTER</h3>
      </div>
      <div class="jumbotron">
        <h2>Change the sort of Journals and Conference!</h2>
      </div>
      <hr>
      <div class="row-fluid marketing">
      <div class="dropdown">
  	 	<span class="lead">Select the Academic field:
  	 	       <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">Academic  Field<b class="caret"></b></a>
                  <ul class="dropdown-menu">
					 <li onclick="selectfield(3);"><a id="ts_3" href="#" >Software engineering, system software and programming languages</a></li>
                     <li onclick="selectfield(7);"><a id="ts_7" href="#" >Human-computer interaction & ubiquitous computing</a></li>
                     <li onclick="selectfield(1);"><a id="ts_1"  href="#" >Computer systems & high-performance computing</a></li>
                     <li onclick="selectfield(6);"><a id="ts_6" href="#" >Artificial Intelligence & Pattern Recognition</a></li>
                     <li onclick="selectfield(4);"><a id="ts_4" href="#" >Databases, data mining and content retrieval</a></li>
                     <li onclick="selectfield(8);"><a id="ts_8" href="#" >Frontier crossing & comprehensive</a></li>
                     <li onclick="selectfield(2);"><a id="ts_2" href="#" >Network & Information Security</a></li>
                     <li onclick="selectfield(5);"><a id="ts_5" href="#" >Computer Graphics & Multimedia</a></li>
                     <li onclick="selectfield(9);"><a id="ts_9" href="#" >Theoretical Computer Science</a></li>
                     <li onclick="selectfield(10);"><a id="ts_10" href="#" >Computer networks</a></li>
                  </ul>
                   </span>
                  </div>
                 
  				<div style="margin-top:30px">
  				<!--  Id, Abbr, Dblpabbr, Fullname, Area, grade, Itype, Publisher-->
  				<div><h4 align="center" id="field"></h4></div>
  				<table class="table table-bordered">
  				<thead><tr><th width="30px">Abbr</th> <th width="296px">Dblp Abbr</th> <th width="396px">Fullname</th> <th>Grade</th> <th>Edit</th></tr></thead>
  				<tbody id="content">
  				
  				</tbody>
  				</table>
  				</div>
     		 <hr>

      <div class="footer" align="center">
        <p>&copy; Company 2013</p>
      </div>

    </div> <!-- /container -->
    
	  		 <div id="updateperc" class="modal hide fade"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    	
		  	<div class="modal-header">
		    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		    <h3 id="myModalLabel">Modify the Journals and Conference</h3>
		 	 </div>
	 	
	 		 <div class="modal-body">
	       <table class="table table-bordered" width="100%">
	       <tr>
	       		<td style="width:30%;font-size:18px" ><label>Abbr:</label></td><!--  Id, Abbr, Dblpabbr, Fullname, Area, grade, Itype, Publisher-->
	     		<td style="width:70%">
	     			<input type="text" name="abbr" id="abbr" class="input-xlarge">
	     			<!-- <input type="hidden" id="email1" value="${email}">-->
	     			 <input type="hidden" id="pearid"> 
	     		</td>
	     		</tr>
	     		<tr>
	     		<td style="width:30%;font-size:18px"> <label>Dblpabbr:</label></td>
	     		<td style="width:70%" class="form-inline">
	     			<input type="text" id="dblpabbr" class="input-xlarge">&nbsp;&nbsp;<font color="red" id="warning1" style="display:none">The old password is error!!</font>
	     		</td>
     		</tr>
     		<tr>
	     		<td style="width:30%;font-size:18px"> <label>Fullname:</label></td>
	     		<td style="width:70%" class="form-inline">
	     			<input type="text" id="fullname" name="fullname" class="input-xlarge">&nbsp;&nbsp;<font color="red" style="display:none">*</font>
	     		</td>
     		</tr>
     		<tr>
	     		<td style="width:30%;font-size:18px"> <label>Grade:</label></td>
	     		<td style="width:70%" class="form-inline">
					<select class="span2" id="grade">
					  <option value="3">A</option>
					  <option value="2">B</option>
					  <option value="1">C</option>
					</select>
	     		</td>
	     		
     		</tr>
     </table>
    	</div>
	  <div class="modal-footer">
	    <button class="btn" data-dismiss="modal" id="closeDiv" aria-hidden="true">Close</button>
	    <button type="submit" class="btn btn-primary" id="submitbtn" onclick="modifyperconf();">Submit</button>
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
	<script src="js/ShowPerconf.js"></script>
 	<script src="js/ModifyShow.js"></script>
  	<script src="js/ModifySuccess.js"></script>
  </body>

</html>
