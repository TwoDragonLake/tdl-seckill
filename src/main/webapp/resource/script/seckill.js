//存放主要交互逻辑的js代码
// javascript 模块化(package.类.方法)
var timerFlag;
var goods = {

    //封装秒杀相关ajax的url
    URL: {
        now: function () {
            return '/miaosha/i/time/now';
        },
        exposer: function (goodsId) {
            return '/miaosha/i/' + goodsId + '/getMiaoshaGoodsLink';
        },
        execution: function () {
            return '/miaosha/i/' +'miaosha';
        },
        queryResult: function () { //查询秒杀结果
            return '/miaosha/i/miaoshaResult';
        },
        order: function () { //下单
            return '/miaosha/i/order';
        },
    },

    //验证手机号
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;//直接判断对象会看对象是否为空,空就是undefine就是false; isNaN 非数字返回true
        } else {
            return false;
        }
    },
    
    //轮训查询秒杀结果
    queryResult: function (md5,goodsId,node){    	
		$.ajax({
			type: "post",
			url: goods.URL.queryResult(),
			dataType: "json", // 返回数据类型json
			data: {
            	mobile:$.cookie('killPhone'),
            	goodsRandomName:md5
            },
			success: function(data, status) {
				if(status == "success" && data.code == 0 && data.data) {
					//停止查询定时任务
					window.clearInterval(timerFlag); 
					
					// 下单token存起来，用于下单
					$.cookie('token', data.data, {expires: 1, path: '/miaosha'});
					
					//var goodsBox = $('#goods-box');
					node.before('您已经获得秒杀资格，有效期：3分钟，请立即下单');
					node.hide().html('<button class="btn btn-primary btn-lg" id="orderBtn">立即下单</button>');
					
                    //绑定下单点击事件
                    $('#orderBtn').one('click', function () {
                        //执行秒杀请求
                        //1.先禁用按钮
                        $(this).addClass('disabled');//,<-$(this)===('#killBtn')->
                        //2.发送秒杀请求执行秒杀
                		$.ajax({
                			type: "post",
                			url: goods.URL.order(),
                			dataType: "json", // 返回数据类型json
                			data: {
	                        	mobile:$.cookie('killPhone'),
	                        	goodsId:goodsId,
	                        	token:$.cookie('token')
	                        },
                			success: function(data, status) {
                				if(status == "success" && data.code == 0) {
	                                node.html('<span class="label label-success">' + "下单成功，订单编号："+ data.data + '</span>');
                				} else {
                					node.html('<span class="label label-success">' + "下单失败" + '</span>'); 
                					console.log('token不对: ' + data.data);
                				}
                			},
                			error: function() {
                				/* return false;、*/
                			},
                			complete: function() {}
                		});
                    });
                    
                    node.show();
				}
			},
			error: function() {
				/* return false;、*/
			},
			complete: function() {}
		});
    },

    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        init: function (params) {
            //手机验证和登录,计时交互
            //规划我们的交互流程
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            //验证手机号
            if (!goods.validatePhone(killPhone)) {
                //绑定手机 控制输出
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true,//显示弹出层
                    backdrop: 'static',//禁止位置关闭
                    keyboard: false//关闭键盘事件
                });

                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    console.log("inputPhone: " + inputPhone);
                    if (goods.validatePhone(inputPhone)) {
                        //电话写入cookie(7天过期)
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/miaosha'});
                        //验证通过　　刷新页面
                        window.location.reload();
                    } else {
                        //todo 错误文案信息抽取到前端字典里
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                    }
                });
            }

            //已经登录
            //计时交互
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var goodsId = params['goodsId'];
            
            //设置已经登陆用户
            $("#user").html($.cookie('killPhone'));
            //绑定登出事件
            $('#logout').click(function () {
            	//删除cookie
            	$.cookie('killPhone', null, {expires: 0, path: '/miaosha'});
            	 //刷新页面
                window.location.reload();
            });
            
    		$.ajax({
    			type: "get",
    			url: goods.URL.now(),
    			dataType: "json", // 返回数据类型json
    			success: function(data, status) {
    				if(status == "success" && data.code == 0) {
						var nowTime = data.data;
						goods.countDown(goodsId, nowTime, startTime, endTime);
    				} else {
    					 console.log('result: ' + result);
    	                 alert('result: ' + result);
    				}
    			},
    			error: function() {
    				/* return false;、*/
    			},
    			complete: function() {}
    		});
        }
    },

    handlergoods: function (goodsId, node, nowTime, startTime, endTime) {
        //获取秒杀地址,控制显示器,执行秒杀
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
		$.ajax({
			type: "post",
			url: goods.URL.exposer(goodsId),
			dataType: "json", // 返回数据类型json
			success: function(data, status) {
				if(status == "success" && data.code == 0) {
	                var exposer = data.data;
	                if (exposer) {
	                    //开启秒杀
	                    //获取秒杀地址
	                    var md5 = exposer;
	                    var killUrl = goods.URL.execution(goodsId, md5);
	                    console.log("killUrl: " + killUrl);
	                    //绑定一次点击事件
	                    $('#killBtn').one('click', function () {
	                        //执行秒杀请求
	                        //1.先禁用按钮
	                        $(this).addClass('disabled');//,<-$(this)===('#killBtn')->
	                        //2.发送秒杀请求执行秒杀
	                		$.ajax({
	                			type: "post",
	                			url: killUrl,
	                			dataType: "json", // 返回数据类型json
	                			data: {
		                        	mobile:$.cookie('killPhone'),
		                        	goodsRandomName:md5
		                        },
	                			success: function(data, status) {
	                				if(status == "success" && data.code == 0) {
		                                node.html('<span class="label label-success">' + "排队中，请稍等..." + '</span>');
		                                timerFlag = window.setInterval(goods.queryResult(md5,goodsId,node),3000);
	                				} else if(status == "success" && data.code == 1){
	                					 console.log('result: ' + data.message);
	                	                 alert('result: ' + data.message);
	                	                 $(this).removeClass('disabled');
	                				}
	                			},
	                			error: function() {
	                				/* return false;、*/
	                			},
	                			complete: function() {}
	                		});
	                    });
	                    node.show();
	                } else {
	                    //未开启秒杀(浏览器计时偏差)
	                    var now = nowTime;
	                    var start = startTime;
	                    var end = endTime;
	                    goods.countDown(goodsId, now, start, end);
	                }
				} else {
					 console.log('result: ' + result);
	                 alert('result: ' + result);
				}
				
			},
			error: function() {
				/* return false;、*/
			},
			complete: function() {}
		});

    },

    countDown: function (goodsId, nowTime, startTime, endTime) {
        console.log(goodsId + '_' + nowTime + '_' + startTime + '_' + endTime);
        var goodsBox = $('#goods-box');
        if (nowTime > endTime) {
            //秒杀结束
            goodsBox.html('秒杀结束!');
        } else if (nowTime < startTime) {
            //秒杀未开始,计时事件绑定
            var killTime = new Date(startTime + 1000);//todo 防止时间偏移
            goodsBox.countdown(killTime, function (event) {
                //时间格式
                var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒 ');
                goodsBox.html(format);
            }).on('finish.countdown', function () {
                //时间完成后回调事件
                //获取秒杀地址,控制现实逻辑,执行秒杀
                console.log('______fininsh.countdown');
                goods.handlergoods(goodsId, goodsBox, nowTime, startTime, endTime);
            });
        } else {
            //秒杀开始
            goods.handlergoods(goodsId, goodsBox, nowTime, startTime, endTime);
        }
    }

}