
		var xmlHttp;
   		function createXmlHttpRequest(){
   			if(window.ActiveXObject){
   				xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
   			}
   			else if(window.XMLHttpRequest){
   				xmlHttp=new XMLHttpRequest();
   			}
   		}
	
	function Showss(){
	
	///alert('---');
	var Ids=document.getElementById("id");
		var username=document.getElementById("username");
		var checks="";
		var ischeck=false;
		if(document.getElementById("checkA").checked){
			checks="A";
			ischeck=true;
		}
		for(var i=1;i<=9;i++){
			if(document.getElementById("check"+i).checked){
				if(ischeck==false){
					checks+=i;
					ischeck=true;
				}
				else
				checks+=","+i;
			}
		}
		var radios;
		if(document.getElementById("radioyes").checked){
			radios="yes";
		}
		else
		{
			radios="no";
		}
		var otherar="";
		var Otherchange=document.getElementById("otherchange");
		var otherCheck=false;
		for(var i=0;i<Otherchange.childNodes.length;i++){
			var OtherChild=Otherchange.childNodes[i];
			if(OtherChild.nodeName=="SPAN"&&OtherChild.innerText!=""){
				if(otherCheck==false){
					otherar+=OtherChild.innerText;
					otherCheck=true;
				}
				else
					otherar+=",,,"+OtherChild.innerText;
			}
		}
 	var url="servlet/ShowUser?action=modify&userid="+Ids.value+"&username="+username.value+"&selectpush="+radios+"&checkggroup="+checks+"&areaOther="+otherar;
	///alert(url);
	createXmlHttpRequest();
	xmlHttp.open("POST",url,true);
	xmlHttp.onreadystatechange=ModifyInf;
	xmlHttp.send(null);
	};
	
	function ModifyInf(){
	
		if(xmlHttp.readyState==4){
    			if(xmlHttp.status==200){
    			
    				var result=xmlHttp.responseText;
    				
    				if(result=="Yes"){
    					///alert("---1---");
    					document.getElementById("tagname").innerText=document.getElementById("username").value;
			 			if(document.getElementById("radioyes").checked){
				 			document.getElementById("tagpush").innerText="YES";
				 		}
				 		else
				 		{
				 			document.getElementById("tagpush").innerText="NO";
				 		}
				 		    
				 		///alert("check"+1);
				 		for(var i=1;i<=9;i++){
				 			document.getElementById("checks"+i).checked=document.getElementById("check"+i).checked;
    						/*if(document.getElementById("check"+i).checked){
				 				document.getElementById("checks"+i).checked=checked;
				 			}
				 			esle
				 			{
				 				document.getElementById("checks"+i).checked="";
				 			}*/
    					}
    					document.getElementById("checksA").checked=document.getElementById("checkA").checked;
    					/*if(document.getElementById("checkA").checked){
    					
    					}
			 				document.getElementById("checksA").checked=checked;
			 			}
			 			else
			 			   document.getElementById("checksA").checked="";*/
    					alert("Success!!");
    					document.getElementById("clickDone").click();
    				}
    				else
    				{
    					alert("Failure!! Try again.");
    				}
    			}
    		}
	}
