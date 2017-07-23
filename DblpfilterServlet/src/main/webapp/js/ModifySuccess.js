		    		
	var r_pid;
	var r_abbr;
	var r_dblpabbr;
	var r_fullname;
	var r_grade;
	function modifyperconf(){
		createXmlHttpRequest();
		r_pid=document.getElementById("pearid");
		r_abbr=document.getElementById("abbr");
		r_dblpabbr=document.getElementById("dblpabbr");
		r_fullname=document.getElementById("fullname");
		r_grade=document.getElementById("grade");
		url="servlet/Modify?action=update&pid="+r_pid.value+"&abbr="+r_abbr.value+"&dblpabbr="+r_dblpabbr.value+"&fullname="+r_fullname.value+"&grade="+r_grade.value;
		xmlHttp.open("GET",url,true);
		xmlHttp.onreadystatechange=Modifyresult;
		xmlHttp.send(null);
	};
	
	function Modifyresult(){
		if(xmlHttp.readyState==4){
			if(xmlHttp.status==200){
				 var xmlDoc=xmlHttp.responseText;     
				 if(xmlDoc=="YES"){
				 	alert("----SUCCESS!!----");
			 		var childtd=document.getElementById("p_"+r_pid.value).childNodes;
			  		childtd[0].innerHTML=r_abbr.value;
			  		childtd[1].innerHTML=r_dblpabbr.value;
			  		childtd[2].innerHTML=r_fullname.value;
			  		if(r_grade.value==3)
			  			childtd[3].innerHTML="A";
			  		if(r_grade.value==2)
			  			childtd[3].innerHTML="B";
			  		if(r_grade.value==1)
			  			childtd[3].innerHTML="C";
				 	document.getElementById("closeDiv").click();
				 	
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