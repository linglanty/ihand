			var xmlHttp; 
			var fiel=null;
    		function createXmlHttpRequest(){
    			if(window.ActiveXObject){
    				xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
    			}
    			else if(window.XMLHttpRequest){
    				xmlHttp=new XMLHttpRequest();
    			}
    		}
    		
    		function selectfield(vs){
    			fiel=vs;
    			createXmlHttpRequest();
    			var url="servlet/Modify?action=show&param="+vs;
    			xmlHttp.open("GET",url,true);
    			xmlHttp.onreadystatechange=showResult;
    			xmlHttp.send(null);
    		};
    		function showResult(){
	    		if(xmlHttp.readyState==4){
	    			if(xmlHttp.status==200){
	    				 var xmlDoc=xmlHttp.responseText;
	    				 if(xmlDoc=="---+"){
							alert("Don't try any other url");	    				 
	    				 	return;
	    				 }
	    				 if(xmlDoc=="~~~~")
	    				 {
	    				 	alert("Before your operation,you should sign in!!");
	    				 	window.location.href="Manager.jsp";
	    				 	return;
	    				 }
	    				 ///field
	    				 document.getElementById("field").innerHTML=document.getElementById("ts_"+fiel).innerHTML;
			             var data=eval(xmlDoc); 
			             var content = "";
			             for(var i=0;i<data.length;i++){
			                 content+="<tr id=\"p_"+data[i].pid+"\" value=\"12\"><td>"+data[i].abbr+"</td><td>"+data[i].dblpabbr+"</td><td>"+data[i].fullname+"</td><td>";
			                 if(data[i].grade=="3"){
			                 	content+="A</td>";
			                 }
			                 else if(data[i].grade=="2"){
			                 	content+="B</td>";
			                 }
			                 else
			                 {
			                 	content+="C</td>";
			                 }
			                 content+="<td><div class=\"btn-group\"><a class=\"btn\" href=\"#\"><i class=\"icon-wrench\"></i></a>";
			                 content+="<a class=\"btn dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\"><span class=\"caret\"></span></a>";
			            	 content+="<ul class=\"dropdown-menu\"> <li onclick=\"updateperconf("+data[i].pid+");\"><a href=\"#updateperc\" data-toggle=\"modal\"\"><i class=\" icon-edit\"></i> Edit</a></li>";
			            	 content+="<li onclick=\"deleteperconf("+data[i].pid+");\"><a href=\"#\"><i class=\"icon-trash\"></i> Delete</a></li>";
			            	 content+="</ul></div></td></tr>";
			             }
             			document.getElementById("content").innerHTML=content; 
			        } 
			        else{                         
			            alert("AJAX服务器返回错误！");                    
			        }  
	    		}
    		};