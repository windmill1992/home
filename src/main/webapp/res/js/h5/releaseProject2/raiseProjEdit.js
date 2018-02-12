
var base = 'http://www.17xs.org/';
var dataUrl = {
	imageUpload : base + 'file/wximage.do',											//微信上传图片
	releaseProject : base + 'newReleaseProject/releaseProject.do',					//发布医疗救助项目
	addAidedPerson : base + 'newReleaseProject/addAidedPerson.do',					//添加和完善受助人信息
	addHospitalProve : base + 'newReleaseProject/addHospitalProve.do',				//完善诊断证明信息
	getHostipalInfo : base + 'newReleaseProject/getHostipalInfo.do',				//获取推荐人信息
	getAidedPersonState : base + 'newReleaseProject/getAidedPersonState.do',		//获取信息状态
	getAidedPersonInfo : base + 'newReleaseProject/getAidedPersonInfo.do',			//获取受助人信息
	getHospitalProveInfo : base + 'newReleaseProject/getHospitalProveInfo.do',		//获取诊断证明和其他信息
	delImg : base + 'newReleaseProject/delImg.do',									//删除图片
	boundbankcard : base +'newReleaseProject/boundbankcard.do',						//添加项目的收款账号
	getBoundbankcard : base + 'newReleaseProject/getBoundbankcard.do',				//查询收款账号信息
	getDonateProjectDetail : base + 'newReleaseProject/faqiDonateProjectDetail.do',	//编辑时获取项目详情
	getneedPersonInfo : base + 'newReleaseProject/needPersonInfo.do'				//编辑时获取受助人信息
};

var vm = new Vue({
	el:'#pageContainer',
	data:{
		projectId:'',
		hospitalName:'收款单位、收款账号',
		exp:false,
		personal:false,
		perfectDataUrl:'perfectData.html?projectId=',
		raiseProject:{
			title:'',
			money:'',
			content:'',
			coverImageUrl:'',
			coverImageId:'',
			contentImg:[],
			edit:false
		},
		aidedPerson:{
			name:'',
			contactor:'',
			tel:''
		},
		perfectData:{
			aidedUrl:'aidedPersonInfo.html?projectId=',
			sickUrl:'diagnosticCertificate.html?projectId=',
			otherUrl:'otherCertificate.html?projectId=',
			payeeUrl:'payee.html?projectId=',
			selfUrl:'perfectData.html?projectId=',
			aidedState:false,
			sickState:false,
			otherState:false,
			payeeState:false
		},
		payee:{
			funds:'宁波市善园公益基金会',
			account:'',
			bank:'',
			openName:'',
			bankName:'',
			cardNo:''
		},
		pubSuc:{
			perfectDataUrl:'perfectData.html?projectId=',
			projectDetailUrl:base + 'newReleaseProject/project_detail.do?projectId='
		},
		aidedInfo:{
			aidedPName:'',
			IDCard:'',
			address:'',
			imgList3:[],
			imgIdList3:[],
			hasImg:false
		},
		sickInfo:{
			sicknessName:'',
			departments:'',
			imgList4:[],
			imgIdList4:[],
			hasImg:false
		},
		otherInfo:{
			income:'',
			house:'',
			cars:'',
			insurance:'',
			imgList5:[],
			imgIdList5:[],
			hasImg:false
		}
	}
});

var raiseProject = {
	init:function(){
		var recommendedPerson = wxlogin.getcookie('recommendedPerson');
		if(!recommendedPerson || recommendedPerson=='null'){
			recommendedPerson = wxlogin.GetQueryString('recommendedPerson');
			if(recommendedPerson == '' || recommendedPerson == 'null'){
				recommendedPerson = null;
			}else{}
		}else{}
		var charityId = wxlogin.GetQueryString('charityId');
		if(charityId !=null && recommendedPerson == null){
			vm.exp = true;
		}else{
			vm.exp = false;
		}
		if($('#releaseProjPg').length<=0){
			if(!vm.exp && recommendedPerson == null){
				vm.personal = true;
			}else{
				vm.personal = false;
			}
			vm.projectId = wxlogin.GetQueryString('projectId');
			var arg = vm.exp?('&charityId='+charityId):'';
			vm.perfectDataUrl += vm.projectId+arg;
			vm.perfectData.aidedUrl += vm.projectId+arg;
			vm.perfectData.sickUrl += vm.projectId+arg;
			vm.perfectData.otherUrl += vm.projectId+arg;
			vm.perfectData.payeeUrl += vm.projectId+arg;
			vm.perfectData.selfUrl += vm.projectId+arg;
			$.ajax({
				type:"GET",
				url:dataUrl.getAidedPersonState,
				data:{projectId:vm.projectId,timestamp:new Date().getTime()},
				async:false,
				cache:false,
				success:function(res){
					if(res.code == 1){
						var res2 = res.result;
						vm.perfectData.aidedState = res2.aidedPersonState==1?true:false;
						vm.perfectData.sickState = res2.hospitalProveState==1?true:false;
						vm.perfectData.otherState = res2.otherState==1?true:false;
						vm.perfectData.payeeState = res2.receivableState==1?true:false;
					}else{
						alert(res.msg);return;
					}
				}
			});
			var sendData = {};
			sendData['projectId'] = vm.projectId;
			sendData['timestamp'] = new Date().getTime();
			if(vm.exp){
				sendData['charityId'] = charityId;
			}else if(recommendedPerson != null && charityId == null){
				sendData['recommendedPerson'] = recommendedPerson;
			}else{}
			if(vm.exp || recommendedPerson != null){
				$.ajax({
					type:"GET",
					url:dataUrl.getHostipalInfo,
					data:sendData,
					cache:false,
					success:function(res){
						if(res.code == 1){
							var res2 = res.result;
							vm.perfectData.payeeState = true;
							vm.hospitalName = res2.hospital;
							vm.payee.account = res2.card;
							vm.payee.bank = res2.bankName;
						}else{
							alert(res.msg);return;
						}
					}
				});	
			}else{}
		}else{
			var pId = wxlogin.GetQueryString('projectId');
			if(pId!=null && pId!='' && pId!='null'){
				vm.raiseProject.edit = true;
				vm.raiseProject.pId = pId;
				$.ajax({
					type:"GET",
					url:dataUrl.getDonateProjectDetail,
					data:{projectId:pId},
					success:function(res){
						if(res.code == 1){
							var r = res.result;
							vm.raiseProject.title = r.title;
							vm.raiseProject.money = r.cryMoney;
							vm.raiseProject.content = r.content;
							vm.raiseProject.coverImageId = r.coverImageId;
							vm.raiseProject.coverImageUrl = r.coverImagerUrl;
							vm.raiseProject.contentImg = r.contentImg;
							$('#imgList .add').hide();
							if($('#imgList2 .item:not(.add)').length>8){
								$('#imgList2 .add').hide();
							}else{}
							if(r.state==240 || r.state==260){
								$('#raiseMoney').attr('readonly','readonly');
							}
						}else{
							alert(res.msg);return;
						}
					}
				});
			}
		}
		if($('#aidedEditPg').length>0){
			
			$.ajax({
				type:"GET",
				url:dataUrl.getneedPersonInfo,
				data:{projectId:vm.projectId},
				success:function(res){
					if(res.code == 1){
						var r = res.result;
						vm.aidedPerson.name = r.needName;
						vm.aidedPerson.contactor = r.linkMan;
						vm.aidedPerson.tel = r.linkMobile;
					}else{
						return;
					}
				}
			});
		}else if($('#perfectDataPg').length>0){
			var type = wxlogin.GetQueryString('type');
			if(type!==null){
				vm.perfectDataUrl += '&type='+type;
				vm.perfectData.aidedUrl += '&type='+type;
				vm.perfectData.sickUrl += '&type='+type;
				vm.perfectData.otherUrl += '&type='+type;
				vm.perfectData.payeeUrl += '&type='+type;
			}else{}
		}else if($('#pubSucPg').length>0){
			if(vm.exp){
				vm.pubSuc.perfectDataUrl += (vm.projectId + '&charityId=' + charityId);
				vm.pubSuc.projectDetailUrl = base + 'project/view_h5.do?projectId=' + vm.projectId;
			}else{
				vm.pubSuc.perfectDataUrl += vm.projectId;
				vm.pubSuc.projectDetailUrl += vm.projectId;
			}
		}else if($('#aidedInfoPg').length>0 && vm.perfectData.aidedState){
			$.ajax({
				type:"GET",
				url:dataUrl.getAidedPersonInfo,
				data:{projectId:vm.projectId,timestamp:new Date().getTime()},
				cache:false,
				success:function(res){
					if(res.code == 1){
						var res2 = res.result;
						vm.aidedInfo.aidedPName = res2.name;
						vm.aidedInfo.IDCard = res2.idCrad;
						vm.aidedInfo.address = res2.address;
						var tempArr = res2.contentImgUrl;
						if(tempArr.length>0){
							vm.aidedInfo.imgList3 = tempArr;
							vm.aidedInfo.imgIdList3 = res2.contentImgId;
							vm.aidedInfo.hasImg = true;
						}else{
							vm.aidedInfo.hasImg = false;
						}
					}else{
						alert(res.msg);return;
					}
				}
			});
		}else if($('#diagnosticPg').length>0 && vm.perfectData.sickState){
			$.ajax({
				type:"GET",
				url:dataUrl.getHospitalProveInfo,
				data:{projectId:vm.projectId,type:0,timestamp:new Date().getTime()},
				cache:false,
				success:function(res){
					if(res.code == 1){
						var res2 = res.result;
						vm.sickInfo.sicknessName = res2.diseaseName;
						vm.sickInfo.departments = res2.hospitalBedNumber;
						var tempArr = res2.contentImgUrl;
						if(tempArr.length>0){
							vm.sickInfo.imgList4 = tempArr;
							vm.sickInfo.imgIdList4 = res2.contentImgId;
							vm.sickInfo.hasImg = true;
						}else{
							vm.sickInfo.hasImg = false;
						}
					}else{
						alert(res.msg);return;
					}
				}
			});
		}else if($('#otherPg').length>0 && vm.perfectData.otherState){
			$.ajax({
				type:"GET",
				url:dataUrl.getHospitalProveInfo,
				data:{projectId:vm.projectId,type:1,timestamp:new Date().getTime()},
				cache:false,
				success:function(res){
					if(res.code == 1){
						var res2 = res.result;
						vm.otherInfo.income = res2.houseHoldIncome;
						vm.otherInfo.house = res2.houseDetail;
						vm.otherInfo.cars = res2.carDetail;
						vm.otherInfo.insurance = res2.medicalInsurance;
						var tempArr = res2.contentImgUrl;
						if(tempArr.length>0){
							vm.otherInfo.imgList5 = tempArr;
							vm.otherInfo.imgIdList5 = res2.contentImgId;
							vm.otherInfo.hasImg = true;
						}else{
							vm.otherInfo.hasImg = false;
						}
					}else{
						alert(res.msg);return;
					}
				}
			});
		}else if($('#payeePg').length > 0 && vm.personal && vm.perfectData.payeeState){
			$.ajax({
				type:"get",
				url:dataUrl.getBoundbankcard,
				data:{projectId:vm.projectId,timestamp:new Date().getTime()},
				cache:false,
				success:function(res){
					if(res.code == 1){
						var r = res.result;
						vm.payee.openName = r.name;
						vm.payee.bankName = r.bankName;
						vm.payee.cardNo = r.cardNo;
					}else{
						alert(res.msg);return;
					}
				}
			});
		}
		
		this.bindEvent();
	},
	bindEvent:function(){
		var that = this;
		var type = wxlogin.GetQueryString('type');
		var recommendedPerson = wxlogin.GetQueryString('recommendedPerson');
		var charityId = wxlogin.GetQueryString('charityId');
		var recommendedId = wxlogin.GetQueryString('recommendedId');
		if(charityId !=null && !isNaN(charityId) && recommendedPerson == null && recommendedId == null){
			vm.exp = true;
		}else{
			vm.exp = false;
		}
		var arg = vm.exp?('&charityId='+charityId):'';
		$('body').on('click','#next',function(){
			var projectTitle = $('#projTitle').val();
			var raiseMoney = $('#raiseMoney').val();
			var projectContent = $('#projCon').val();
			var $item1 = $('#imgList .item').not('.add');
			var $item2 = $('#imgList2 .item').not('.add');
			var coverImageId = $item1.attr('id');
			var contentImageId = '';
			
			if(!projectTitle){
				that.showTips('项目标题不能为空!');
				$('#projTitle').focus();
				return;
			}else if(!raiseMoney){
				that.showTips('筹款金额不能为空!');
				$('#raiseMoney').focus();
				return;
			}else if(!projectContent){
				that.showTips('筹款内容不能为空!');
				$('#projectContent').focus();
				return;
			}else if(!$item1.length){
				that.showTips('请上传封面图!');
				return;
			}else if($item2.length<3){
				that.showTips('受助人相关图片不能少于3张!');
				return;
			}else{
				$item2.each(function(){
					contentImageId += $(this).attr('id')+',';
				});
				var send = {
					title:projectTitle,
					content:projectContent,
					coverImageId:coverImageId,
					contentImageId:contentImageId,
					cryMoney:raiseMoney
				};
				if(recommendedPerson != null && !isNaN(recommendedPerson) && charityId == null){
					send['recommendedPerson'] = recommendedPerson;
				}else if(vm.exp){
					send['charityId'] = charityId;
				}else if(recommendedId!=null && !isNaN(recommendedId)){
					send['recommendedId'] = recommendedId;
				}
				if(vm.raiseProject.pId){
					send['id'] = vm.raiseProject.pId;
				}else{}
				$.ajax({
					type:"POST",
					url:dataUrl.releaseProject,
					data:send,
					dataType:'JSON',
					success:function(res){
						if(res.code == 1){
							if(vm.raiseProject.pId){
								vm.projectId = vm.raiseProject.pId;
							}else{
								vm.projectId = res.result.projectId;
							}
							that.showTips('保存中...');
							setTimeout(function(){
								location.href = 'aidedPersonEdit.html?projectId='+vm.projectId+arg;
							},1000);
						}else{
							alert(res.msg);return;
						}
					}
				});
			}
			
		}).on('click','#publish',function(){
			var aidedPName = $('#aidedPName').val();
			var contactor = $('#contactor').val();
			var mobile = $('#mobile').val();
			var check1 = $('#chk1')[0].checked;
			var check2 = $('#chk2')[0].checked;
			var charityId = wxlogin.GetQueryString('charityId');
			if(!aidedPName){
				that.showTips('受助人姓名不能为空!');
				$('#aidedPName').focus();
				return;
			}else if(!contactor){
				that.showTips('联系人不能为空!');
				$('#contactor').focus();
				return;
			}else if(!mobile){
				that.showTips('手机号不能为空!');
				$('#mobile').focus();
				return;
			}else if(!that.telValidate(mobile)){
				that.showTips('手机号格式不正确!');
				$('#mobile').focus();
				return;
			}else if(!check1 || !check2){
				that.showTips('请先同意协议书或承诺!');
				return;
			}else{
				$.ajax({
					type:"POST",
					url:dataUrl.addAidedPerson,
					data:{realName:aidedPName,linkMan:contactor,linkMobile:mobile,projectId:vm.projectId,charityId:charityId},
					success:function(res){
						if(res.code == 1){
							that.showTips('提交中...');
							setTimeout(function(){
								location.href = 'pubSuccess.html?projectId='+vm.projectId+arg;
							},1000);
						}else{
							alert(res.msg);return;
						}
					}
				});
			}
			
		}).on('click','#saveAided',function(){
			if(vm.perfectData.aidedState){
				if(type!==null){
					location.href = base + 'newReleaseProject/project_detail.do?projectId='+vm.projectId+arg;
				}else{
					location.href = vm.perfectData.selfUrl;
				}
			}else{
				var aidedPName = $('#aidedPName_2').val();
				var IDCard = $('#IDCard').val();
				var address = $('#address').val();
				var $item3 = $('#imgList3 .item').not('.add');
				var contentImageId = '';
				
				if(!aidedPName){
					that.showTips('受助人姓名不能为空!');
					$('#aidedPName_2').focus();
					return;
				}else if(!IDCard){
					that.showTips('身份证号不能为空!');
					$('#IDCard').focus();
					return;
				}else if(!checkCard(IDCard)){
					that.showTips('身份证号格式错误!');
					$('#IDCard').focus();
					return;
				}else if(!address){
					that.showTips('家庭地址不能为空!');
					$('#address').focus();
					return;
				}else if($item3.length<3){
					that.showTips('请上传身份证相关照片3张以上!');
					return;
				}else{
					$item3.each(function(){
						contentImageId += $(this).attr('id')+',';
					});
					$.ajax({
						type:"POST",
						url:dataUrl.addAidedPerson,
						data:{realName:aidedPName,indetity:IDCard,familyAddress:address,contentImageId:contentImageId,projectId:vm.projectId},
						success:function(res){
							if(res.code == 1){
								that.showTips('保存成功');
								setTimeout(function(){
									if(type!==null){
										location.href = base + 'newReleaseProject/project_detail.do?projectId='+vm.projectId+arg;
									}else{
										location.href = vm.perfectData.selfUrl;
									}
								},1000);
							}else{
								alert(res.msg);return;
							}
						}
					});
				}	
			}
			
		}).on('click','#saveCertificate',function(){
			if(vm.perfectData.sickState){
				if(type!==null){
					location.href = base + 'newReleaseProject/project_detail.do?projectId='+vm.projectId;
				}else{
					location.href = vm.perfectData.selfUrl;
				}
			}else{
				var diseaseName = $('#sicknessName').val();
				var hospitalBedNumber = $('#departments').val();
				var $item4 = $('#imgList4 .item').not('.add');
				var diseaseImageId = '';
				
				if(!diseaseName){
					that.showTips('疾病名称不能为空!');
					$('#sicknessName').focus();
					return;
				}else if(!hospitalBedNumber){
					that.showTips('科室和病床号不能为空!');
					$('#departments').focus();
					return;
				}else if($item4.length<2){
					that.showTips('请上传诊断证明相关照片2张以上!');
					return;
				}else{
					$item4.each(function(){
						diseaseImageId += $(this).attr('id')+',';
					});	
					$.ajax({
						type:"POST",
						url:dataUrl.addHospitalProve,
						data:{diseaseName:diseaseName,hospitalBedNumber:hospitalBedNumber,diseaseImageId:diseaseImageId,projectId:vm.projectId},
						success:function(res){
							if(res.code == 1){
								that.showTips('保存成功');
								setTimeout(function(){
									if(type!==null){
										location.href = base + 'newReleaseProject/project_detail.do?projectId='+vm.projectId;
									}else{
										location.href = vm.perfectData.selfUrl;
									}
								},1000);
							}else{
								alert(res.msg);return;
							}
						}
					});
				}
			}
			
		}).on('click','#saveOther',function(){
			if(vm.perfectData.otherState){
				if(type!==null){
					location.href = base + 'newReleaseProject/project_detail.do?projectId='+vm.projectId+arg;
				}else{
					location.href = vm.perfectData.selfUrl;
				}
			}else{
				var houseHoldIncome = $('#income').val();
				var houseDetail = $('#house').val();
				var carDetail = $('#cars').val();
				var medicalInsurance = $('#insurance').val();
				var $item5 = $('#imgList5 .item').not('.add');
				var houseDetailImageId = '';
				if($item5.length>0){
					$item5.each(function(){
						houseDetailImageId += $(this).attr('id')+',';
					});
				}else{}
				
				$.ajax({
					type:"POST",
					url:dataUrl.addHospitalProve,
					data:{
						houseHoldIncome:houseHoldIncome,
						houseDetail:houseDetail,
						carDetail:carDetail,
						medicalInsurance:medicalInsurance,
						houseDetailImageId:houseDetailImageId,
						projectId:vm.projectId
					},
					success:function(res){
						if(res.code == 1){
							that.showTips('保存成功');
							setTimeout(function(){
								if(type!==null){
									location.href = base + 'newReleaseProject/project_detail.do?projectId='+vm.projectId+arg;
								}else{
									location.href = vm.perfectData.selfUrl;
								}
							},1000);
						}else{
							alert(res.msg);return;
						}
					}
				});	
			}
			
		}).on('click','#savePayee',function(){
			if(vm.perfectData.payeeState){
				if(type!==null){
					location.href = base + 'newReleaseProject/project_detail.do?projectId='+vm.projectId+arg;
				}else{
					location.href = vm.perfectData.selfUrl;
				}	
			}else{
				var acceptHost = $('#hospital_3').val();
				var account = $('#account').val();
				var bank = $('#bank').val();
				if(!acceptHost){
					that.showTips('收款单位不能为空');
					$('#hospital_3').focus();return;
				}else if(!account){
					that.showTips('收款账号不能为空');
					$('#account').focus();return;
				}else if(!bank){
					that.showTips('收款银行不能为空');
					$('#bank').focus();return;
				}else{
					$.ajax({
						type:"POST",
						url:dataUrl.boundbankcard,
						data:{
							name:acceptHost,
							cardNo:account,
							accountType:0,
							bankName:bank,
							bankType:2,
							pid:vm.projectId
						},
						success:function(res){
							if(res.code == 1){
								that.showTips('保存成功');
								setTimeout(function(){
									if(type!==null){
										location.href = base + 'newReleaseProject/project_detail.do?projectId='+vm.projectId+arg;
									}else{
										location.href = vm.perfectData.selfUrl;
									}
								},1000);
							}else{
								alert(res.msg);return;
							}
						}
					});
				}
			}
			
		}).on('input propertychange','#aidedPName_2,#IDCard,#address',function(){
			vm.perfectData.aidedState = false;
		}).on('input propertychange','#sicknessName,#departments',function(){
			vm.perfectData.sickState = false;
		}).on('input propertychange','#income,#house,#cars,#insurance',function(){
			vm.perfectData.otherState = false;
		}).on('input propertychange','#hospital_3,#account,#bank',function(){
			vm.perfectData.payeeState = false;
		}).on('input propertychange','#projCon',function(){
			var h = $(this).height();
			var t = this.scrollTop;
			$(this).height(t+h);
		});
		
		//  拍照、本地选图
		var images = {localId: [],serverId: []};
		var inn = 0;
		$("body").on("click",".add-btn",function(){
			inn = 0;
			var $addList = $(this).closest('.addList');
			var id = $addList.attr('id');
			var counts = 9;
			if(id=='imgList'){
				counts = 1;
			}else{
				counts = 9;
			}
			var len =$addList.find('.item').not('.add').length;
		    wx.chooseImage({
			    count: counts-len, // 默认9
			    sizeType: ['original','compressed'], // 可以指定是原图还是压缩图，默认二者都有
			    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
			    success: function (res) {
		    		//销毁数据
		            images.localId = images.serverId  = [];
		          	images.localId = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
		          	if(images.localId.length>=counts){
		          		$addList.find(".add").hide();
		          	}else{}
		          	
					syncUpload(images.localId[0],$addList);
		          	vm.perfectData.aidedState = false;
		          	vm.perfectData.sickState = false;
			    }
		    });
		}).on("click",".rpe-uploadPic .item .del",function(e){
			e.stopPropagation();
			var _this = $(this);
			var id = Number(_this.closest('.item').attr('id'));
			$.ajax({
				type:"POST",
				url:dataUrl.delImg,
				data:{imgId:id},
				success:function(res){
					if(res.code == 1){
						that.showTips('删除成功');
						vm.perfectData.aidedState = false;
						vm.perfectData.sickState = false;
						var $add = _this.closest('.addList');
						var len = $add.find('.item').not('.add').length;
						for(var i=0;i<images.serverId.length;i++){
							if(id == images.serverId[i]){
								images.serverId.splice(i,1);
								i--;
							}else{}
						}
						var img1 = (len<=1 && $add.attr('id')=='imgList');
						var img2 = (len<=3 && $add.attr('id')=='imgList2');
						var img3 = (len<=3 && $add.attr('id')=='imgList3');
						var img4 = (len<=2 && $add.attr('id')=='imgList4');
						var img5 = (len<=1 && $add.attr('id')=='imgList5');
						var needImgNum = img1 || img2 || img3 || img4 || img5;
						if(needImgNum){
							$add.find('.add').show();
						}else{}
						_this.closest(".item").remove();
					}else{
						alert(res.msg);return;
					}
				}
			});
			
		}).on('click','.addList .item:not(.add)',function(){
			var $img = $(this).find('img');
			var currentSrc = $img.attr('src');
			var urlsSrc = [];
			$('.addList .item .preview img').each(function(){
				urlsSrc.push($(this).attr('src'));
			});
			wx.previewImage({
				current:currentSrc,
				urls:urlsSrc
			});
		});
		
		//异步上传图片到微信服务器
		var syncUpload = function(localId,o){
          	if(localId.indexOf('wxlocalresource') != -1){
          		localId = localId.replace('wxlocalresource','wxLocalResouce');
          	}else{}
		    wx.uploadImage({
	            localId: localId,
	            isShowProgressTips: 1,
	            success: function (res) {
	                images.serverId.push(res.serverId);// 返回图片的服务器端ID
	                syncDownload(res.serverId,o);
	            },
	            error: function(){
	           		alert("上传图片出现问题，请联系客服");
	            }
		    });
		};
		  
		//异步下载图片到本地
		var syncDownload = function(serverids,o){
		    $.ajax({
				url:dataUrl.imageUpload,
				data:{mId:serverids,typeName:'commonForm',t:new Date()},
				async:false,
				success: function(res){
					if(res.flag==1){
						var html = [];
						var imgUrls = res.obj,imgIds=res.obj1;
						imgUrls = imgUrls.substr(0,imgUrls.length-1);
						imgIds = imgIds.substr(0,imgIds.length-1);
						html.push('<div class="item" id="'+imgIds+'"><a href="javascript:;" class="preview"><img src="'+imgUrls+'"/></a>');
						html.push('<i class="del"><img src="/res/images/h5/images/close.png"/></i></div>')
	            		o.find('.add').before(html.join(''));
	            		inn++;
	            		if(inn<images.localId.length){
	            			syncUpload(images.localId[inn],o);
	            		}else{}
//						that.showTips('图片上传成功');
					}else{
						$('.addList').each(function(t,i){
							var t = $(t);
							var len =t.find('.item').not('.add').length;
							var img1 = (len<9 && t.attr('id')!='imgList');
							var img2 = (len<1 && t.attr('id')=='imgList');
							var showAdd = img1 || img2;
							if(showAdd){
								t.find('.add').show();
							}else {
								t.find('.add').hide();
							}
							that.showTips('图片上传成功');
						});
					}
				}
			}); 
		};
	},
	//手机号格式验证
	telValidate:function(tel){
		var reg = /^(13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\d{8}$/;
		if(!reg.test(tel)){
	        return false;
	    }else{
	    	return true;
	    }
	},
	//提示信息
	showTips:function(txt){
		var $tips = $('.tips');
		if(!$tips.is(':hidden')){
			$tips.hide();
		}
		$tips.text(txt).fadeIn();
		setTimeout(function(){
			$tips.css({'transform':'scale(0.1)'}).fadeOut(function(){
				$(this).text('').css({'transform':'scale(1)'});
			});
		},2000);
	}
};

$(function(){
	wxlogin.login();
	raiseProject.init();
});

		