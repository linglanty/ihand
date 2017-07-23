		var xmlHttp; 
  		function createXmlHttpRequest(){
  			if(window.ActiveXObject){
  				xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
  			}
  			else if(window.XMLHttpRequest){
  				xmlHttp=new XMLHttpRequest();
  			}
  		}
  		
  		function login(){
  			
	   		var email=document.getElementById("email");
	   		var pwd=document.getElementById("pwd");
	   		var remb=document.getElementById("remb");
	   		if(trim(email.value)==""||trim(pwd.value)=="")
	   		{
	   		
	   			alert("You can't keep the meail or password empty!!");
	   			return false;
	   		}
	   		var reg=/^[\\.a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
	   		if(!reg.test(email.value))
	   		{	
	   			alert("Format of the email is error");
	   			return false;
	   		}
  			createXmlHttpRequest();
  			var url="servlet/LoginServlet?email="+email.value+"&pwd="+pwd.value+"&remb="+remb.value;
  			xmlHttp.open("POST",url,true);
  			xmlHttp.onreadystatechange=loginService;
  			xmlHttp.send(null);
	  			
  		};
  		function loginService(){
	   		if(xmlHttp.readyState==4){
	   			if(xmlHttp.status==200){
	   				 var xmlDoc=xmlHttp.responseText;
	   				 if(xmlDoc=="Yes")
	   				 {
	   				 	document.getElementById("closeDiv").click();
	   				 	showinfo();
	   				 }
	   				 else 
	   				 {
	   				 	alert("Failure!!");
	   				 }
	   			}
	   		}
   		};
   		
   		function Signout(){
     		
     		if(confirm('Are you sure?'))
     		{
     			createXmlHttpRequest();
	  			var url="servlet/UserExit?Exit="+"23232";
	  			xmlHttp.open("GET",url,true);
	  			////alert("====");
	  			xmlHttp.onreadystatechange=SignOutService;
	  			xmlHttp.send(null);
     		
     		}
     	};
     	function SignOutService(){
     		if(xmlHttp.readyState==4){
	   			if(xmlHttp.status==200){
	   				 var xmlDoc=xmlHttp.responseText;
	   				 if(xmlDoc=="Yes")
	   				 {
	   				 	var dblp=document.getElementById("dblpuser");
	   				 	dblp.innerHTML="Sign in";
	   				 	dblp.href="signin.html";
	   				 	var usersignout=document.getElementById("usersignout");
     					usersignout.style.display="none";
	   				 	///alert("Success!!");
	   				 	
	   				 }
	   			}
	   		}
     	};
   		
   		
   	