define(function(require) {
	require("cloud/base/cloud");
	var Service = Class.create({
		 initialize: function() {
        	this.type = "site";
            this.resourceType = 3;
        	this.map = $H(this.map);
        }, 
        getToken:function(context,callback){
        	cloud.Ajax.request({
        		url: "oauth2/access_token",
                type: "POST",
                dataType: "JSON",
                contentType:"application/x-www-form-urlencoded",
                parameters: { 
                	grant_type: "password",
                	username:"test@inhand.com.cn",
                	password:"123456",
                	client_id:"000017953450251798098136",
                	client_secret:"08E9EC6793345759456CB8BAE52615F3",
                	password_type:1 
                },
                success: function(data) {
                	console.log("success ",data);
                	callback.call(context || this, data);
                }
        	});
        	  
        },
        getSites: function(opt,callback, context) {
            if (this.lastGetResroucesRequest) {
                this.lastGetResroucesRequest.abort();
            }
            var self = this; 
            this.lastGetResroucesRequest = cloud.Ajax.request({
            	url: "api/test/list",
                type: "GET",
                dataType: "JSON",
                parameters: { 
                    site_name: opt.siteName,
                    verbose: 100
                },
                success: function(data) { 
                    self.lastGetResroucesRequest = null; 
                    callback.call(context || this, data.result);
                } 
            });
             
        } 
	});
	return new Service();
});
