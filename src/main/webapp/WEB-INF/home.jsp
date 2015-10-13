<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@include file="/WEB-INF/include.jsp"%>
<html>
<head>

<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<meta name="format-detection" content="telephone=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<title>动云-面向体育生态的云服务平台</title>
<script type="text/javascript"
	src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<link rel="stylesheet" type="text/css" href="css/main.css">
<div id='wx_pic' style='margin: 0 auto; display: none;'>
	<img src='http://www.arenacloud.com/image/logo-square.jpg' />
</div>
<style type="text/css">
.page-content {
	position: relative;
}

.page-rule {
	position: absolute;
	left: 0;
	bottom: 0;
	width: 96%;
	padding: 0 2% 10px;
	font-size: 12px;
	text-align: center;
}

.page-rule span {
	display: inline-block;
	color: #fff;
	background: #029be6;
	border-radius: 5px;
	font-size: 18px;
	line-height: 30px;
	padding: 2px 6px;
}

.page-rule ul {
	text-align: left;
	margin-top: 26px;
}

.page-header {
	margin: 1vh auto 0;
	height: 30vh;
	padding: 0 10%;
	color: #0079bf;
	line-height: 20px;
	font-size: 16px;
}

.page-header h2 {
	line-height: 80px;
	font-size: 22px;
	text-align: center;
}

.page-header p {
	text-indent: 2em;
}

.page-title {
	margin: 0 auto;
	padding: 0 5%;
	color: #029be6;
	position: relative;
	text-align: center;
}

.page-title h3 {
	margin: 0 auto;
	line-height: 20px;
	font-size: 18px;
	width: 40%;
	display: block;
	padding: 0 10px;
	height: 40px;
	background: #fff;
}

.page-title:after {
	content: "";
	display: block;
	border-bottom: 3px solid #1f95cd;
	position: absolute;
	left: 5%;
	width: 90%;
	top: 20px;
	z-index: -1;
}

.mr {
	margin-right: 1%;
}

.lists {
	margin: 0 auto;
	padding: 2em 5% 0;
	overflow: hidden;
}

.lists li {
	padding: 2%;
	display: block;
	width: 43%;
	margin-bottom: 8px;
	font-size: 12px;
	height: 120px;
	overflow: hidden;
	border: 2px solid #3f8ab1;
	line-height: 18px;
	float: left;
	border-radius: 6px;
	text-indent: 2em;
}

.form {
	height: 100%;
	background-color: #fff;
	display: none;
	position: fixed;
	bottom: 0;
	left: 0;
	z-index: 1000;
}

.form h1 {
	padding: 10px 0 25px;
	text-align: center;
	font-size: 1em;
	color: #000;
	border-bottom: 1px solid #e5e5e5;
}

.form .cell {
	position: relative;
	margin: 0 20px 20px;
	padding-left: 80px;
	height: 40px;
	line-height: 40px;
	background-color: #f2f2f2;
}

.form .cell label {
	position: absolute;
	color: #000;
	left: 15px;
}

.form .cell input[type=text] {
	background-color: #f2f2f2;
	border: 0px;
	height: 33px;
	width: 90%;
	font-size: 1em;
}

.form .cell input[type=radio] {
	height: 32px;
	line-height: 32px;
	font-size: 1em;
	float: left;
	margin-right: 6px;
}

.form .cell span {
	line-height: 32px;
	height: 32px;
	display: inline-block;
	margin-right: 10px;
}

.form .submit,.unSubmit {
	margin: 20px auto;
	display: block;
	width: 128px;
	height: 39px;
	line-height: 39px;
	text-align: center;
	background-color: #1a9bff;
	color: #fff;
}

.form .ercode {
	margin: 0 auto;
	text-align: center;
}

.form .ercode img {
	margin: 0 auto;
}

.mask {
	display: none;
	background-image: url(images/toshare-bg.png);
	background-repeat: no-repeat;
	background-position: right top;
	background-color: rgba(0, 0, 0, 0.69);
	background-size: 103px;
	position: fixed;
	top: 0;
	bottom: 0;
	left: 0;
	right: 0;
	z-index: 200;
}

.mask div {
	z-index: 201;
	line-height: 140px;
	font-size: 0.9em;
	color: #000;
	height: 140px;
	width: 280px;
	position: absolute;
	left: 50%;
	margin-left: -140px;
	background: #fff;
	text-align: center;
	border-radius: 25px;
	margin-top: 140px;
}
</style>
</head>
<body id="m-body">
	<div class="m-wrap">
		<div class="popup-loading">
			<div class="loading-wrap">
				<div class="loading-bar"></div>
				<div class="loading-text">0%</div>
			</div>
		</div>
		<div class="m-page">
			<div class="page-content lazy-img" data-position="top center"
				data-repeat="no-repeat" data-size="100%"
				data-src="images/small_1.jpg">
				<div class="page-rule">
					<span id="share">分享至朋友圈，即可抽大奖</span>
					<ul>
						<li><strong>活动规则：</strong></li>
						<li><strong>活动时间：</strong>2015.10.15-10.30</li>
						<li><strong>参与方式：</strong>分享活动至微信朋友或朋友圈，即可参与抽奖。（提示：100%中奖哦）</li>
						<li><strong>活动奖品：</strong>价值7888元iPhone6s Plus
							64GB(1名)、价值1500元智能篮球(8名)、价值100元虎扑黑话T恤(100名)、200-1000不等动云代金券。</li>
						<li><strong>领奖方式：</strong>请关注动云微信公众号(dongyun_tiyu),我们将通过公众号来通知您</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="m-page fn-hide">
			<div class="page-content lazy-img" data-position="top center"
				data-size="cover" data-src="images/2.jpg"></div>
		</div>
		<div class="m-page fn-hide">
			<div class="page-content lazy-img" data-position="top center"
				data-size="cover" data-src="images/3.jpg"></div>
		</div>
		<div class="m-page fn-hide">
			<div class="page-content">
				<div class="page-header">
					<h2>体育大数据</h2>
					<p>基于体育行业海量的大数据分析与挖掘，做体育方面独特的数据分析与共享，给企业带来多元化、智能化的流量变现方案</p>
				</div>
				<div class="page-title">
					<h3>体育海量大数据分析和挖掘</h3>
				</div>
				<ul class="lists">
					<li class="mr">用于2亿体育爱好者的数据分析，有体育BBS、网页搜索、广告、直播等产品的丰富实践经验。</li>
					<li>专注体育赛事、赛程数据挖掘、分析和管理。以软件即服务(SaaS)的商业模式，帮助企业创造价值。</li>
					<li class="mr">拥有北上广深一线城市的体育场馆数据，对体育O2O公司的预订与运营提供大量的有价值数据。</li>
					<li>积累了体育爱好者的人群特点、喜好、用户行为的数据，进行个性化分析的数据，有助于客户根据用户画像进行精准营销。</li>
				</ul>
			</div>
		</div>
		<div class="m-page fn-hide">
			<div class="page-content">
				<div class="page-header">
					<h2>视频云</h2>
					<p>累积10年的视频技术，极速上传、高速转码，稳定服务亿级用户。在体育大型赛事、健身O2O、体育SNS等垂直细分领域，集拍、传、转、存、发、播六大核心基础功能于一身的体育视频应用平台</p>
				</div>
				<div class="page-title">
					<h3>一站式视频解决方案</h3>
				</div>
				<ul class="lists">
					<li class="mr">互动直播活动内容长期有效，即播即录，即时生成点播，会议信息内容持续传播。</li>
					<li>强大的直播数据统计功能，能实时统计每个直播活动的在线观看人数、流量情况，观众的观看时长、观众的地域分布、观众的观看日志。</li>
					<li class="mr">强大的观看条件限制功能，可限制同时在线观看人数、限制最大流量、播放网站的黑白名单等。</li>
					<li>BGP网络覆盖17+运营商400多个CDN加速节点给用户极速体验</li>
				</ul>
			</div>
		</div>
		<div class="m-page fn-hide">
			<div class="page-content lazy-img" data-position="top center"
				data-size="cover" data-src="images/6.jpg"></div>
		</div>
		<div class="fixed-arrow"></div>
		<div class="m-share-submit" id="form"></div>

		<div class="form" id="page2">
			<div style="padding: 15px;">
				<h1>您好，为方便中奖后与您取得联系， 请您必须要填写联系信息, 然后关注我们的公众号</h1>
			</div>
			<div class="cell">
				<label>姓名：</label> <input type="text" id="name" name="name"
					class="name" maxlength="10">
			</div>
			<div class="cell">
				<label>性别：</label>
				<ul></ul>
				<span><input type="radio" checked="checked" name="sex"
					value="1">男</span> <span><input type="radio" name="sex"
					value="2">女</span>
			</div>
			<div class="cell">
				<label>手机号：</label> <input type="text" name="phone" class="phone"
					type="tel" maxlength="11">
			</div>

			<a class="submit" style="display: none;">提交</a> <span
				class="unSubmit" style="background: #efefef; color: #ccc;">提交</span>
			<div class="ercode">
				<img src="images/ercode.jpg" width="200">
			</div>
		</div>

		<div class="mask" id="suc" style="background-image: none;">
			<div
				style="height: auto; padding: 46px 10px; width: 260px; line-height: 23px; text-align: left;">
				信息提交成功！</div>
		</div>

		<div class="mask" id="dodo">
			<div>亲，马上分享，即可获得抽奖机会哦～</div>
		</div>

		<div class="mask" id="err" style="background-image: none;">
			<div>提交失败，请重试</div>
		</div>

	</div>
	<script src="js/zepto.min.js"></script>
	<script src="js/touchPaging.js"></script>
	<script>
		var timestamp_arg = '<c:out value="${timestamp}" />';
		var nonceStr_arg = '<c:out value="${nonce}" />';
		var signature_arg = '<c:out value="${signature}" />';

		new TouchPaging({});
		$(function() {

			$.ajax({
				type : "get",
				url : "http://events.arenacloud.com/wechat/share",
				dataType : "json",
				data:{
                    "dynamicUrl": encodeURIComponent(location.href.split('#')[0])
				},
				success : function(data) {
					wx.config({
						debug : false,
						appId : "wx63ac9d2096253fc0",
						timestamp : data.timestamp,
						nonceStr : data.nonce,
						signature : data.signature,
						jsApiList : [ 'onMenuShareTimeline', 'onMenuShareAppMessage' ]
					});
				},
				error : function(data) {
					//alert("连接失败！");
				}
			});

/* 			wx.config({
				debug : false,
				appId : 'wx63ac9d2096253fc0',
				timestamp : timestamp_arg,
				nonceStr : nonceStr_arg,
				signature : signature_arg,
				jsApiList : [ 'onMenuShareTimeline', 'onMenuShareAppMessage' ]
			}); */

			wx.ready(function() {
				wx.onMenuShareTimeline({
					title : '动云-最懂体育的云',
					link : window.location.href,
					imgUrl : 'http://www.arenacloud.com/image/logo-square.jpg',
					success : function() {
						$('#dodo').hide();
						$('#page2').show();

					}
				});

				wx.onMenuShareAppMessage({
					title : '动云-最懂体育的云',
					desc : '一站式视频点播处理服务，轻松应对海量数据的存储与访问，1.1万米深度挖掘体育行业大数据',
					link : window.location.href,
					imgUrl : 'http://www.arenacloud.com/image/logo-square.jpg',
					success : function() {
						$('#dodo').hide();
						$('#page2').show();
					}
				});
			});

			/* 			wx.error(function(res) {
			 alert(res);
			 alert("error");
			 }); */

			//test
			//$("#page2").show();
			$('#share').click(function() {
				$('#dodo').show();
			});

			$('.mask').click(function() {
				if ($(this).attr('id') != 'err') {
					$('#page2').hide();
				}
				$(this).hide();
			});

			$('input').bind(
					'keyup',
					function() {
						if ($.trim($('.name').val()).length
								&& $.trim($('.phone').val()).length) {
							$('.submit').show();
							$('.unSubmit').hide();
						} else {
							$('.unSubmit').show();
							$('.submit').hide();
						}
					});

			$('.submit')
					.click(
							function() {
								if ($.trim($('.name').val()).length == 0
										|| $.trim($('.phone').val()).length == 0)
									return;
								var url = "http://events.arenacloud.com/wechat/saveUserInfo";

								$.ajax({
									url : url,
									dataType : "json",
									type : "POST",
									data : {
										name : $("input[name='name']").val(),
										sex : $("input[name='sex']:checked")
												.val(),
										phone : $("input[name='phone']").val()
									},
									success : function(response) {
										if (response.code == '200') {
											$('#suc').show();
											$('#page2').hide();
											$('#dodo').hide();
											setTimeout(function() {
												$('#suc').hide();
												$("#page2").show();
											}, 1000);

										} else {
											document.getElementsByTagName(
													"input").focus();
											$('#err').show();
										}
									}
								}

								);

							});

		});
	</script>
</body>
</html>
