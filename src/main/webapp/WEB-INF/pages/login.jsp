<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<%--        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">--%>
		<meta charset="utf-8" />
		<title>文章权限系统-登陆</title>

		<meta name="description" content="User login page" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
		<script type="text/javascript">
			window.jQuery || document.write("<script src='${ctx}/assets/js/jquery.js'>"+"<"+"/script>");
		</script>
		<script src="${ctx}/assets/js/jquery.cookie.js"></script>
		<script src="${ctx}/assets/js/util/md5.js"></script>

		<style>
       *{box-sizing:border-box;-webkit-tap-highlight-color:transparent;-webkit-touch-callout:none;margin:0;padding:0}
        a,button,input,select,textarea{outline:0}
        *,a{-webkit-tap-highlight-color:transparent}
        a{color:#333;text-decoration:none}
        img{margin:0;padding:0;border:none;vertical-align:middle}
        html{font-family:"Helvetica Neue",Helvetica,STHeiTi,Arial,sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%}
        /*body{color:#333;font-size:14px;width:100%;height:100%;}*/
       @font-face{
           font-family: hanyi;
           src: url("${ctx}/assets/fonts/glyphicons-halflings-regular.ttf");
       }
        body{
            /*background-color: #3d4e66;*/
            background: url("${ctx}/assets/images/login/0.png") no-repeat 0 0;
            -moz-background-size:100% 100%; background-size:100% 100%
        }

        /* 清除浮动 */
        .z-clearfix:after,
        .z-clearfix:before {
          content: ".";
          visibility: hidden;
          display: block;
          font-size: 0;
          clear: both;
          height: 0; }
        .z-login-wrap{
            position: relative;
            width:640px;
            margin:0 auto;
            font-family: "微软雅黑";
        }
        .z-login-box{
            width: 489px;
            height: 562px;
            margin:50px  auto 0 auto;
            background: url(${ctx}/assets/images/login/log01.png) no-repeat;
        }
        .z-login-box .z-content{
            
        }
        .z-content .z-title{
            margin:95px auto 0 auto;
            text-align: center;
        }
        .z-content .z-title h1{
            font-size: 28px;
            color: #fff;
            font-weight: normal !important;
        }
        .z-content strong{
            color: #EF8F05;
            font-weight: normal !important;
        }
        .z-content .z-title p{
            margin-top: 5px;
            color: #fff;
        }
        /*.log{*/
        /*    !*background-color: #9d5889;*!*/
        /*    color: #9d5889;*/
        /*    font-family: "hanyi";*/
        /*    */
        /*}*/

        .z-content .z-form{
            text-align: center;
            width: 337px;
            margin: 0 auto;
            padding-top: 30px;
        }

        .form-item{
            display: block;
            overflow: hidden;
            height: auto;
            background: #f5f5f5;
            border-radius:3px;
            margin-bottom: 18px;
        }
        .form-item >input{
            width: 100%;
            padding:12px;
            border:0;
            color: #88A4C8;
            font-size: 16px;
        }
        input::-webkit-input-placeholder{
            font-family: "微软雅黑";
            color: #88A4C8;
        }
        input::-moz-placeholder{
            font-family: "微软雅黑";
        }

        .z-code-box{
            background-color:#495E7A !important; 
        }
        .z-code-box >input{
            width: 240px;
            float: left;
            border-radius: 3px;
        }
        .z-code-box .z-code{
           float: right;
           width: 80px;
           height: 42px;
           border-radius: 3px;
           overflow: hidden;
           
        }

        .z-form .z-inline{
            display: block;
            margin-top: 15px;
            font-size: 14px;
            color: #fff;
        }
        .z-form .z-inline span{
            display: inline-block;
            margin-left: 5px;
            vertical-align: middle;
        }


        .z-login-btn{
            width: 100%;
            padding:12px;
            background-color: #19b394;
            border-radius: 3px;
            border:0; 
            font-family: "微软雅黑";
            font-size: 16px;
            color: #fff;
        }

        .z-login-btn:hover,
        .z-login-btn:active,
        .z-login-btn:focus:active,
        .z-login-btn:focus:hover,
        .z-code:hover,
        .z-switch:hover{
            cursor: pointer;
        }

        .z-err{
			font-size: 14px;
			color: #EF6C6B;
			margin: 5px auto;
			
        }

        .z-shadow{
            position: absolute;
            top: 145px;
            left: 0;
            z-index: -1;
            width: 866px;
            height: 686px;
            background-repeat: no-repeat;
            background: url(${ctx}/assets/images/login/log02.png) no-repeat;
            background-position: 75px 0px;
        }

        /*开关*/
        .z-switch {
            -webkit-appearance: none;
            -moz-appearance: none;
            appearance: none;
            position: relative;
            width: 50px;
            height: 26px;
            border: 1px solid #dfdfdf;
            outline: 0;
            border-radius: 16px;
            box-sizing: border-box;
            background: #dfdfdf;
            vertical-align: middle;
        }

        .z-switch:after {
            width: 24px;
            background-color: #fff;
            box-shadow: 0 1px 3px rgba(0,0,0,.4);
        }
        .z-switch:after, .z-switch:before {
            content: " ";
            position: absolute;
            top: 0;
            left: 0;
            height: 24px;
            border-radius:12px;
            -webkit-transition: -webkit-transform .3s;
            -moz-transition: -webkit-transform .3s;
            transition: -webkit-transform .3s;
            transition: transform .3s;
            transition: transform .3s,
            -webkit-transform .3s;
            -moz-transform .3s;
        }

        .z-switch:checked {
            border-color: #19b394;
            background-color: #19b394;
        }
        .z-switch:checked:after {
            -webkit-transform: translateX(24px);
            -moz-transform: translateX(24px);
            transform: translateX(24px);
        }

        .z-switch:checked:before {
            -webkit-transform: scale(0);
            -moz-transform: scale(0);
            transform: scale(0);
        }


        .z-loading-wrap{
            overflow: hidden;
            position: fixed;
            top: 0;
            left: 0;
            z-index: 1000;
            width: 100%;
            height: 100%;
            background-color: rgba(0,0,0,.5);
            display:none;
        }

        .z-msg{
          width: 100%;
          height: 100px;
          position: relative;
          text-align: center;
          top: 360px;
          font-size: 14px;
          color: #fff;
          font-family: "微软雅黑";
        }

        .spinner {
          width: 100px;
          height: 100px;
          position: relative;
          top: 350px;
          margin:0 auto;
        }
         
         .double-bounce2 {
            background-color: #19b394 !important;
         }
        .double-bounce1, .double-bounce2 {
          width: 100%;
          height: 100%;
          border-radius: 50%;
          background-color: #67CF22;
          opacity: 0.6;
          position: absolute;
          top: 0;
          left: 0;
           
          -webkit-animation: bounce 2.0s infinite ease-in-out;
          animation: bounce 2.0s infinite ease-in-out;
        }
         
        .double-bounce2 {
          -webkit-animation-delay: -1.0s;
          animation-delay: -1.0s;
        }
         
        @-webkit-keyframes bounce {
          0%, 100% { -webkit-transform: scale(0.0) }
          50% { -webkit-transform: scale(1.0) }
        }
         
        @keyframes bounce {
          0%, 100% {
            transform: scale(0.0);
            -webkit-transform: scale(0.0);
          } 50% {
            transform: scale(1.0);
            -webkit-transform: scale(1.0);
          }
        }
        .z-switch:checked + #remenber{
            border-color: #19b394;
            background-color: #19b394;
        }
        .z-switch:checked + #remenber:after{
             -webkit-transform: translateX(24px);
             -moz-transform: translateX(24px);
             transform: translateX(24px);
         } 
         .z-switch:checked + #remenber:before{
            -webkit-transform: scale(0);
            transform: scale(0)
         }

    </style>

    <style>
        /*最外层容器样式*/
        .wrap {
            z-index:9999;/*置于最上层*/
            position: absolute;
            right: 200px;
            top: 100px;
            width: 100px;
            height: 100px;

        }

        /*包裹所有容器样式*/
        .cube {
            width: 50px;
            height: 50px;
            margin: 0 auto;
            transform-style: preserve-3d;
            transform: rotateX(-30deg) rotateY(-80deg);
            animation: rotate linear 20s infinite;
        }

        @-webkit-keyframes rotate {
            from {
                transform: rotateX(0deg) rotateY(0deg);
            }
            to {
                transform: rotateX(360deg) rotateY(360deg);
            }
        }

        .cube div {
            position: absolute;
            width: 200px;
            height: 200px;
            opacity: 0.8;
            transition: all .4s;
        }

        /*定义所有图片样式*/
        .pic {
            width: 200px;
            height: 200px;
        }

        .cube .out_front {
            transform: rotateY(0deg) translateZ(100px);
        }

        .cube .out_back {
            transform: translateZ(-100px) rotateY(180deg);
        }

        .cube .out_left {
            transform: rotateY(-90deg) translateZ(100px);
        }

        .cube .out_right {
            transform: rotateY(90deg) translateZ(100px);
        }

        .cube .out_top {
            transform: rotateX(90deg) translateZ(100px);
        }

        .cube .out_bottom {
            transform: rotateX(-90deg) translateZ(100px);
        }

        /*定义小正方体样式*/
        .cube span {
            display: block;
            width: 100px;
            height: 100px;
            position: absolute;
            top: 50px;
            left: 50px;
        }

        .cube .in_pic {
            width: 100px;
            height: 100px;
        }

        .cube .in_front {
            transform: rotateY(0deg) translateZ(50px);
        }

        .cube .in_back {
            transform: translateZ(-50px) rotateY(180deg);
        }

        .cube .in_left {
            transform: rotateY(-90deg) translateZ(50px);
        }

        .cube .in_right {
            transform: rotateY(90deg) translateZ(50px);
        }

        .cube .in_top {
            transform: rotateX(90deg) translateZ(50px);
        }

        .cube .in_bottom {
            transform: rotateX(-90deg) translateZ(50px);
        }

        /*鼠标移入后样式*/
        .cube:hover .out_front {
            transform: rotateY(0deg) translateZ(200px);
        }

        .cube:hover .out_back {
            transform: translateZ(-200px) rotateY(180deg);
        }

        .cube:hover .out_left {
            transform: rotateY(-90deg) translateZ(200px);
        }

        .cube:hover .out_right {
            transform: rotateY(90deg) translateZ(200px);
        }

        .cube:hover .out_top {
            transform: rotateX(90deg) translateZ(200px);
        }

        .cube:hover .out_bottom {
            transform: rotateX(-90deg) translateZ(200px);
        }
    </style>

    <style>
        .log{
            display: block;
            font-family: "hanyi";
            /*渐变背景*/
            background-image: -webkit-linear-gradient(left, #00dbc3, #f4a100 10%, #b400cf 20%, #f700d4 30%,
            #0dff00 40%, #00dbca 50%, #f4ac00 60%, #c600cd 70%, #f7acbc 80%, #ffd400 90%, #3498db);
            color: transparent; /*文字填充色为透明*/
            -webkit-text-fill-color: transparent;
            -webkit-background-clip: text;          /*背景剪裁为文字，只将文字显示为背景*/
            background-size: 200% 100%;            /*背景图片向水平方向扩大一倍，这样background-position才有移动与变化的空间*/
            /* 动画 */
            animation: masked-animation 8s infinite linear;
        }
        @keyframes masked-animation {
            0% {
                background-position: 0 0;   /*background-position 属性设置背景图像的起始位置。*/
            }
            100% {
                background-position: -100% 0;
            }
        }
    </style>
	</head>

	<body>


    <div class="z-login-wrap">
        <div class="z-login-box">
            <div class="z-content z-clearfix">
                <div class="z-title">
                    <h1>
                        <img src="${ctx}/assets/images/login/log03.png">
                        <span class="log">文章权限系统</span>
                    </h1>

                </div>

                <div class="z-form">
                    <form id="loginForm">
                        <label class="form-item z-clearfix">
                            <input type="text" name="username" id="username" class="form-control" placeholder="请输入用户">
                            <i class="ace-icon fa fa-user"></i>
                        </label>
                        <label class="form-item z-clearfix">
                            <input  type="password" id="password" class="form-control" placeholder="请输入密码" />
                            <i class="ace-icon fa fa-user"></i>
                        </label>

                        <label class="form-item z-clearfix z-code-box" style="margin-bottom: 0px;" id="showVcode"></label>
                        
                        <p id="err" class="z-err">${err}</p>

                        <button type="button" class="z-login-btn" onclick="javascript:login();" >登录</button>

                        <label class="inline z-inline">
                            <input type="checkbox" id="setcheck" name="remenber" class="z-switch" style="position: absolute;opacity: 0;" checked="checked">
                            <span class="z-switch" id="remenber"></span>
                            <span>记住登录</span>
                        </label>
                    </form>
                </div>
            </div>
        </div>
        <div class="z-shadow"></div>
    </div>
    <div class="z-loading-wrap">
        <div class="spinner">
          <div class="double-bounce1"></div>
          <div class="double-bounce2"></div>
        </div>
        <div class="z-msg">拼命加速，正在登录中...</div>
    </div>

    <div class="wrap">
        <!--包裹所有元素的容器-->
        <div class="cube">
            <!--前面图片 -->
            <div class="out_front">
                <img src="${ctx}/assets/images/image2/1.jpg" class="pic">
            </div>
            <!--后面图片 -->
            <div class="out_back">
                <img src="${ctx}/assets/images/image2/2.jpg" class="pic">
            </div>
            <!--左面图片 -->
            <div class="out_left">
                <img src="${ctx}/assets/images/image2/3.jpg" class="pic">
            </div>
            <!--右面图片 -->
            <div class="out_right">
                <img src="${ctx}/assets/images/image2/4.jpg" class="pic">
            </div>
            <!--上面图片 -->
            <div class="out_top">
                <img src="${ctx}/assets/images/image2/5.jpg" class="pic">
            </div>
            <!--下面图片 -->
            <div class="out_bottom">
                <img src="${ctx}/assets/images/image2/6.jpg" class="pic">
            </div>

            <!--小正方体 -->
            <span class="in_front">
                <img src="${ctx}/assets/images/image2/7.jpg" class="in_pic">
            </span>
            <span class="in_back">
                 <img src="${ctx}/assets/images/image2/8.jpg" class="in_pic">
            </span>
            <span class="in_left">
                <img src="${ctx}/assets/images/image2/9.jpg" class="in_pic">
            </span>
            <span class="in_right">
                <img src="${ctx}/assets/images/image2/10.jpg" class="in_pic">
            </span>
            <span class="in_top">
                <img src="${ctx}/assets/images/image2/11.jpg" class="in_pic">
            </span>
            <span class="in_bottom">
                <img src="${ctx}/assets/images/image2/12.jpg" class="in_pic">
            </span>
        </div>
    </div>
    <script type="text/javascript">
		//点击图片刷新验证码
		function vcodeclick() {
			$("#vcode").select();
			$("#vimg").attr("src", "${ctx}/getVcode?random=" + Math.random());
		}
		//获取cookie值设置到文本框
		function setValue(){
			var namevalue = $("#username").val();
			if("" != namevalue){
				var cookieName = namevalue + "userinfo";
				var userinfo = $.cookie(cookieName);
				if(undefined != userinfo && "" != userinfo){
					var infos = userinfo.split("&");
					for(var i in infos){
						if(0 == i){
							$("#username").val(infos[i]);
						}else if(1 == i){
							$("#password").val(infos[i]);
						}
					}
				}
			}else{
				var username = $.cookie("username");
				if(undefined != username && "" != username){
					$("#username").val(username);
				}
				var password = $.cookie("password");
				if(undefined != password && "" != password){
					$("#password").val(password);
				}
			}
		}
		setValue();
		
		$("#username").blur(function(){
			setValue();
		});
		function login(){
			$("#errDiv").hide();
			$("#err").html("");
			var username = $("#username").val();
			if("" == username){
				$("#errDiv").show();
				$("#err").html("用户名不能为空");
				$("#username").focus();
				return;
			}
			var password = $("#password").val();
			if("" == password){
				$("#errDiv").show();
				$("#err").html("密码不能为空");
				$("#password").focus();
				return;
			}
			if(password.length <= 20){
				password = hex_md5(password);
			}
			var data = "username=" + username + "&password=" + password;
			var vcode = $("#vcode").val();
			if(undefined != vcode){
				if("" == vcode){
					$("#errDiv").show();
					$("#err").html("验证码不能为空");
					$("#vcode").focus();
					return;
				}
				data += "&vcode=" + vcode;
			}
			$(".z-loading-wrap").show();
			$.ajax({
				url: "${ctx}/admin/login",
				data: data,
				type: "post",
				dataType: "json",
				beforeSend: function(xhr){
					xhr.setRequestHeader("vcode","1");
				},
				success: function(req){
					if(req.retcode == 1){
						var remenber = $("#setcheck").is(":checked");
						var cookieName = username + ":userinfo";
						if(remenber){
							var cookieValue = username + "&" + password;
							//设置cookie值有效期30天
							$.cookie(cookieName, cookieValue, {expires:30});
							$.cookie("username", username, {expires:30});
							$.cookie("password", password, {expires:30});
						}else{
							$.cookie(cookieName, "");
							$.cookie("username", "");
							$.cookie("password", "");
						}
						window.location.href = "${ctx}/admin/main"
					} else {
						$(".z-loading-wrap").hide();
						$("#vcode").select();
						$("#vimg").attr("src", "${ctx}/getVcode?random=" + Math.random());
						var msg = req.retmsg;
						var end = msg.lastIndexOf("|");
						if(end > 0){
							var errTimes = msg.substring(end + 1, msg.length);
							if(3 <= errTimes && undefined == vcode){
								var html = [];
						        html.push('<input type="text" name="vcode" id="vcode" maxlength="4" class="form-control" placeholder="验证码">');
						        html.push('<img class="z-code" id="vimg" src="${ctx}/getVcode" onclick="vcodeclick()" title="验证码" alt="验证码">');
						        $("#showVcode").append(html.join(''));
							}
							msg = msg.substring(0, end);
						}
						$("#err").html(msg);
					}
				},
				error: function(req){
					$(".z-loading-wrap").hide();
					$("#vcode").select();
					$("#vimg").attr("src", "${ctx}/getVcode?random=" + Math.random());
					$("#errDiv").show();
					$("#err").html("系统异常");
				}
			});
		}
		
	    $(function (){
	       $("#loginForm").keydown(function(e){
	           var e = e || event, keycode = e.which || e.keyCode;
	           if (keycode == 13) {
	               login();
	            }
	       })
	    });
		
		jQuery(function($) {
			$(document).on('click', '.toolbar a[data-target]', function(e) {
				e.preventDefault();
				var target = $(this).data('target');
				$('.widget-box.visible').removeClass('visible');//hide others
				$(target).addClass('visible');//show target
			});
		});

		//you don't need this, just used for changing background
		jQuery(function($) {
			$('#btn-login-dark').on('click', function(e) {
				$('body').attr('class', 'login-layout');
				$('#id-text2').attr('class', 'white');
				$('#id-company-text').attr('class', 'blue');

				e.preventDefault();
			});
			$('#btn-login-light').on('click', function(e) {
				$('body').attr('class', 'login-layout light-login');
				$('#id-text2').attr('class', 'grey');
				$('#id-company-text').attr('class', 'blue');

				e.preventDefault();
			});
			$('#btn-login-blur').on('click', function(e) {
				$('body').attr('class', 'login-layout blur-login');
				$('#id-text2').attr('class', 'white');
				$('#id-company-text').attr('class', 'light-blue');

				e.preventDefault();
			});

		});
		
	</script>
</body>
</html>
