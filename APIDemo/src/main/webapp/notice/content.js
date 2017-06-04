define(function(require){
	var cloud = require("cloud/base/cloud"); 
	var html = require("text!./content.html");
	var Table = require("cloud/components/table");
	var layout = require("cloud/lib/plugin/jquery.layout"); 
	var Toolbar = require("cloud/components/toolbar");
	var Button = require("cloud/components/button");
	var InputSuggest = require("cloud/components/input-suggest");
	var service = require("./service");
	var columns = [
	{
		"title": "状态",
		"dataIndex": "online",
		"lang":"{text:state}",
		"cls": null,
		"width": "5%",
		render:function(data, type, row){
			var display = "";
			if ("display" == type) {
				switch (data) {
					case 1:
						var onlineStr = locale.get({lang:"online"});
						display += new Template(
						"<div  style = \"display : inline-block;\" class = \"cloud-table-online\" title = \"#{status}\"}></div>")
						.evaluate({
							status : onlineStr
						});
						break;
					case 0:
						var offlineStr = locale.get({lang:"offline"});
						display += new Template(
						"<div  style = \"display : inline-block;\" class = \"cloud-table-offline\" title = \"#{status}\"}></div>")
						.evaluate({
							status : offlineStr
						});
						break;
					
					default:
						break;
				}
				return display;
			} else {
				return data;
			}
		}
	} , {
		"title": "现场名称",
		"dataIndex": "name",
		"width": "45%",
		"lang":"{text:site_name}"
	},{
		"title": "安装地址",
		"dataIndex": "address",
		"cls": null,
		"width": "30%",
		"lang":"{text:installation_address}"
	}, {
		"title": "业务状态",
		"dataIndex": "businessState",
		"cls": null,
		"width": "20%",
		"lang":"{text:business_state}",
		render: function(data, type, row) {
			var display = "";
			if ("display" == type) {
				switch (data) {
					case 0:
						display = locale.get({lang:"construction"});
						break;
					case 1:
						display = locale.get({lang:"commissionin"});
						break;
					case 2:
						display = locale.get({lang:"fault"});
						break;
					case 3:
						display = locale.get({lang:"overhaul"});
						break;
					default:
						break;
				}
				return display;
			} else {
				return data;
			}
		}
	}];
	var Content = Class.create(cloud.Component,{
		initialize:function($super,options){
			this.modelName="site-list";
			$super(options);
			this.service = service; 
			this.elements = {
					bar:{
						id:"content-bar",
						"class":null
					},
					table : {
						id:"content-table",
						"class":null
					} 
			};
			this.display = null ;
			this.pageDisplay = 30;
			this.render();
		},
		render:function(){
			this.renderHtml();
			this.renderLayout();
			this.renderToolBar();
			this.addSiteInput();
			this.renderTable(); 
			locale.render({element:this.element});
		},
		
		renderHtml:function(){
			this.element.html(html);
		
		},
		renderLayout:function(){
			this.layout = this.element.layout({
				defaults: {
                    paneClass: "pane",
                    togglerLength_open: 50,
                    togglerClass: "cloud-layout-toggler",
                    resizerClass: "cloud-layout-resizer",
                    spacing_open: 1,
                    spacing_closed: 1,
                    togglerLength_closed: 50,
                    resizable: false,
                    slidable: false,	
                    closable: false
                },
                north: {
                    paneSelector: "#" + this.elements.bar.id,
                    size: 35
                },
                center:{
                	paneSelector:"#"+this.elements.table.id
                }
			});
		},
		
		renderTable:function(){
			var self = this;
			this.table = new Table({
				selector:"#"+this.elements.table.id,
				columns:columns,
				datas:[],
				pageSize:100,
				autoWidth: false,
                pageToolBar: false,
                checkbox : "full"
			});  
			this.initDataTable();
		},
		initDataTable: function(){
			var self = this; 
			var param = { 
					siteName:""
				};
			self.service.getSites(param,function(returnData){ 
						self.table.clearTableData();
						self.table.render(returnData); 
			},this);
		},
		
		//初始化toolbar
		renderToolBar:function(){
			var self = this;  
			var queryBtn = new Button({ 
              text: locale.get("query"),
              events: {
                  click: this.queryBtnClick,
                  scope : this
              }
          }); 
			this.toolbar = new Toolbar({
				selector:"#"+this.elements.bar.id,
				rightItems: [queryBtn]
			}); 
		},
		addSiteInput: function(toolbar) {
			var self = this;
			var $htmls = $("<div id='search-time' style='float: left;margin-top:5px;'>" 
					+ "<label style='margin:auto 10px auto 10px'>" 
					+ locale.get({
						lang : "site"
					})
					+ "</label>"
					+ "<input style='width:125px' type='text'  id='siteName' style='height:10px'/>" 
					+ "</div>");
	       
	            var toolbarLeftElement="."+$(this.toolbar["leftDiv"][0]).attr("class");
				$("#"+this.toolbar.id).find(toolbarLeftElement).before($htmls);
				
        },       
		queryBtnClick:function(){
        	var self = this;
        	var siteName = $("#siteName").val();
        	self.opt= {
        			siteName:siteName 
			};
        	self.service.getSites(self.opt,function(returnData){ 
				self.table.clearTableData();
				self.table.render(returnData); 
				self.unmask();
        	},this);
			
		},
		destory:function(){
			if (this.layout && (!this.layout.destroyed)) {
            	this.layout.destroy();
            }
			this.content.clearTableData();
			this.content.destroy();
			$("#"+this.elements.table.id).empty();
		}
	});
	return Content;
});
