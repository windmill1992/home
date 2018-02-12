<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<title>发布协议</title>
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/protocol_release.css?v=20171123">
</head>
<body>
		<div class="pro">
			<h3>协议说明</h3>
			<p class="tel">宁波市善园公益基金会：</p>
			<p class="protocol">本人已知悉贵会的宗旨，并详细了解了贵会关于申请求助的规定，本人认同贵会的宗旨且完全接受贵会关于求助和捐赠等方面的各项规定。</p>
			<p class="tel">现本人就所发布的求助事项，特承诺如下：</p>
			<p class="protocol">2.在申请本次资助前，本人/受助人确认本人/受助人及其近亲属已无力承担相关费用，且已穷尽现有的救济手段。本人确保本人/受助人求助的慈善款系在本人可以获得的所有救济途径（包括但不限于社保理赔、商业保险理赔、政府救济、其他社会救济等）以外尚必需的款项。</p>
			<p class="protocol">3.本人/受助人保证本人/受助人一旦向贵会提交求助申请，在贵会募集慈善款至本人/受助人接受贵会最后一笔捐赠款的期限内，本人/受助人不再通过其他渠道就同一事项进行公开求助。</p>
			<p class="protocol">4.本人/受助人同意贵会工作人员对本人/周助人进行家访，并协助贵会向提供相关证明的单位或个人进行核实。</p>
			<p class="protocol">5.本人/受助人授权贵会对受助人及/或其监护人在接受救助期间的文字、照片和影像等资料为公益事业所需进行无偿使用。</p>
			<p class="protocol">6.本人/受助人在接受贵会慈善款时，若本人/受助人无法签署有关收条或凭据的，本人/受助人的配偶、子女、父母、监护人、近亲属、政府有关部门负责人中的任何一人均有权代为签署，该等人在有关收条或凭据上的签字与本人/受助人的签字具有相同的法律效力。</p>
			<p class="protocol">7.本人/受助人违反上述承诺事项的，愿承担一切法律责任（包括但不限于返还已接受的捐赠、赔偿贵会所遭受的损失和通过公开渠道发布致歉申明等）；因本人/受助人及其近亲属的行为致使本人无法遵守上述承诺的，本人/受助人亦同意按前款约定承担法律责任。</p>
			<p class="protocol">8.本人/受助人向贵会提供的有效联系方式（包括手机号、座机号、电子邮件等）均是真实有效的，可以联系到本人/受助人，若有变更，本人/受助人将立即通知贵会，否则若贵会无法联系到本人/受助人导致的有关责任和后果由本人/受助人承担。</p>
			<div class="foot">
				<a href="javascript:;" class="check">
					<i class="foot_i"></i>
					<span class="foot_sp">我已阅读并同意相关协议</span>
				</a>
				<a href="javascript:;">
					<div class="btn disable" id="btnNext">下一步</div>
				</a>
			</div>
		</div>
		
		<script src="/res/js/jquery-1.8.2.min.js"></script>
		<script>
			$(function() {
				var flag = true;
				$(".foot_i,.foot_sp").click(function() {
					if(flag) {
						$(".foot_i").addClass("footImg");
						$("#btnNext").removeClass('disable');
					} else {
						$(".foot_i").removeClass("footImg");
						$("#btnNext").addClass('disable');
					}
					flag = !flag;
				});
				$("#btnNext").click(function() {
					if(!$(this).hasClass('disable')) {
						location.href = '/project/releaseH5Project.do';
					} else {
						return false;
					}
				});
			});
		</script>
</body>

</html>