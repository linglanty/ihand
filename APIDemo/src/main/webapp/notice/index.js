define(function(require){
	require("cloud/base/cloud"); 
	require("cloud/lib/plugin/jquery.layout"); 
	var ContentView = require("./content"); 
	var Service = require("./service");
	var Index = Class.create(cloud.Component, {
        initialize: function($super, options){  
//        	 $.ajax({ url :  "http://c2.inhandnetworks.com/oauth2/access_token?grant_type=password&username=zhangyf@inhand.com.cn&password=YxZkTBtF&client_id=000017953450251798098136&client_secret=08E9EC6793345759456CB8BAE52615F3&password_type=1",
//                 type:"POST",
//                 contentType:"application/x-www-form-urlencoded",
//                 success:function(data) { 
//                           console.log("data .....",data);
//                    }
//           });
        	Service.getToken(this,function(returndata){
        		cloud.Ajax._setAccessToken(returndata.access_token);
        	});
            this._render();
        },
		 _render:function(){
			 this.renderLayout();
			 this.renderContent();
	     },
	     renderLayout: function() { 
	            $("body").layout({
	                defaults: {
	                    paneClass: "pane",
	                    togglerClass: "cloud-layout-toggler",
	                    resizerClass: "cloud-layout-resizer",
	                    "spacing_open": 1,
	                    "spacing_closed": 1,
	                    "togglerLength_closed": 50,
						togglerTip_open:locale.get({lang:"close"}),  
	                    togglerTip_closed:locale.get({lang:"open"}),  
	                    resizable: false,
	                    slidable: false
	                }, 
	                center: {
	                    paneSelector: "#user-content"
	                }, 
					onresize: function(){
							$(window).resize();  
					}
	            });
	        },
			renderContent:function(){ 
				console.log(" Service ",Service);
				this.contentView = new ContentView({
					selector:"#user-content",
					service:Service
				});
			},
		    destroy:function(){
				if (this.layout && (!this.layout.destroyed)) {
	            	this.layout.destroy();
	            } 
			}
	});
	
	return Index;
});