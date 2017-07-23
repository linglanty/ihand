			function update(){
    		var username=document.getElementById("username");
    		if(username=="")
    		{
    			alert("You can't leave the name empty!!");
    			return false;
    		}
    		return true;
    		};
			
    		var xmlHttp;
    		function createXmlHttpRequest(){
    			if(window.ActiveXObject){
    				xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
    			}
    			else if(window.XMLHttpRequest){
    				xmlHttp=new XMLHttpRequest();
    			}
    		}
    		function trigger(){
    			createXmlHttpRequest();
    			var pwd=document.getElementById("oldpas");
    			var email=document.getElementById("email1");
    			var url="servlet/LoginServlet?email="+email.value+"&pwd="+pwd.value;
    			xmlHttp.open("GET",url,true);
    			xmlHttp.onreadystatechange=showResult;
    			xmlHttp.send(null);
    		};
    		
    		function showResult(){
	    		if(xmlHttp.readyState==4){
	    			if(xmlHttp.status==200){
	    				var result=xmlHttp.responseText;
	    				if(result=="Yes"){
	    					document.getElementById("newpass").readOnly=false;
	    					document.getElementById("warning1").style.display="none";
	    				}
	    				else
	    				{
	    					document.getElementById("newpass").readOnly=true;
	    					document.getElementById("warning1").style.display="";
	    				}
	    			}
	    		}
    		};
    		function changewpd(){
    			var newpass=document.getElementById("newpass");
    			if(newpass.value=="")
    			{
    				alert("You can't leave the new password empty!!");
    				return false;
    			}
    			createXmlHttpRequest();
    			var userid=document.getElementById("userid");
    			var url="servlet/ShowUser?action=changepwd&userid="+userid.value+"&pwd="+newpass.value;
    			xmlHttp.open("GET",url,true);
    			xmlHttp.onreadystatechange=Changes;
    			xmlHttp.send(null);
    		};
    		function Changes(){
    			if(xmlHttp.readyState==4){
	    			if(xmlHttp.status==200){
	    				var result=xmlHttp.responseText;
	    				if(result=="Yes"){
	    					alert("Success!!");
	    					document.getElementById("changepas").style.display="none";
	    					document.getElementById("closeDiv").click();
	    				}
	    				else if(result=="No")
	    				{
	    					alert("Failure!!Try again");
	    					document.getElementById("changepas").style.display="";
	    				}
	    				else
	    				{
	    					alert("Don't try any other userid!!");
	    				}
	    			}
	    		}
    		};

   