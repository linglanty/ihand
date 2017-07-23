	var d_id;
 		
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