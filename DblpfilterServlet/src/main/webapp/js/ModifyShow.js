		
   			function updateperconf(id){
    			createXmlHttpRequest();
    			var url="servlet/Modify?action=showPer&pid="+id;
    			xmlHttp.open("GET",url,true);
    			xmlHttp.onreadystatechange=ShowDiv;
    			xmlHttp.send(null);
    		};
    		
    		function ShowDiv(){
	    		if(xmlHttp.readyState==4){
	    			if(xmlHttp.status==200){
	    				 var xmlDoc=xmlHttp.responseText;     
	    				 
	    				 if(xmlDoc=="---+"){
	    				 	alert("Don't try any other url");	    				 
	    				 	return;
	    				 }
			             var data=eval(xmlDoc);
			             document.getElementById("pearid").value=data[0].pid;
			             document.getElementById("abbr").value=data[0].abbr;
			             document.getElementById("dblpabbr").value=data[0].dblpabbr;
			             document.getElementById("fullname").value=data[0].fullname;
			             if(data[0].grade=="3"){
			            	 document.getElementById("grade").options[0].selected=true;
			             }
			             else if(data[0].grade=="2"){
			             	document.getElementById("grade").options[1].selected=true;
			             }
			             else
			             {
			             	document.getElementById("grade").options[2].selected=true;
			             }
		        } 
		        else{                         
		            alert("AJAX服务器返回错误！");                    
		        }  
	    		}
    		};