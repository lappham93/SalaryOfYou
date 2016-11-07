(function($) {
	
	$.NOTIFY_TIMEOUT = 3000;
	
	$.log = function(o, level){
		if(!level) level = 'dir';
		level = level.toLocaleLowerCase();
		if(window.console && console[level] && $.isFunction(console[level])) {
			console[level](o);
		}
	};
	$.appContext = new function() {
		var context_path = $('meta[name=context-path]').prop('content');
		this.paths = {
			base : context_path,
			image : context_path + 'img',
			js : context_path + 'js',
			css : context_path + 'css',
			fonts : context_path + 'fonts'
		};
		this.csrf = {
			name : $('meta[name=csrf-param]').prop('content'),
			value : $('meta[name=csrf-token]').prop('content')
		};
		this.customer = {
			loggedIn: $('meta[name=logged-in]').prop('content') == 'true'
		}
	};
	var notify_container = $('<div class="alert-fixed-top">');
	var notify_templates = {
		success : '<div class="alert alert-success" style="background-color:#dff0d8;border-color:#d6e9c6;color:#3c763d"><div class="container"><i class="fa fa-check-circle"></i>&nbsp;&nbsp;&nbsp;<span></span><a class="close" data-dismiss="alert">&times;</a></div></div>',
		info : '<div class="alert alert-info"><div class="container"><i class="fa fa-exclamation-triangle"></i>&nbsp;&nbsp;&nbsp;<span></span><a class="close" data-dismiss="alert">&times;</a></div></div>',
		warning : '<div class="alert alert-warning"><div class="container"><i class="fa fa-exclamation-triangle"></i>&nbsp;&nbsp;&nbsp;<span></span><a class="close" data-dismiss="alert">&times;</a></div></div>',
		danger : '<div class="alert alert-danger"><div class="container"><i class="fa fa-exclamation-triangle"></i>&nbsp;&nbsp;&nbsp;<span></span><a class="close" data-dismiss="alert">&times;</a></div></div>'
	}
	$.notify = function(type, message, callback) {
		!notify_container.parent().length && notify_container.appendTo('body');
		var msgBox = $(notify_templates[type]).css('margin', 0).hide().prependTo(notify_container).fadeIn(500);
		msgBox.find('span').text(message);
		
		if(type != 'danger') {
			setTimeout(function() {
				msgBox.fadeOut(1000, function() {
					$(this).remove();
					if($.isFunction(callback)) {
						callback();
					}
				})
			}, $.NOTIFY_TIMEOUT);
		}
		msgBox.on('closed.bs.alert', function () {
			if($.isFunction(callback)) {
				callback();
			}
		});
		return msgBox;
	}
	
	$.cart = new function() {
		this.updateQuantity = function(productId, orderItemId, quantity) {
			var params = {
				productId : productId,
				quantity : Number(quantity || 1),
				orderItemId : orderItemId
			};
			params[$.appContext.csrf.name] = $.appContext.csrf.value;
			$.ajax({
				url : $.appContext.paths.base + 'cart/updateQuantity',
				type : 'get',
				data : params,
				dataType : 'html',
				beforeSend : function() {
				},
				success : function(resp) {
					$('#shopping_cart_content').replaceWith(resp);
					$.ajax({
						url : $.appContext.paths.base + 'cart/get',
						type : 'get',
						dataType : 'json',
						success : function(resp) {
							$('.cartItemCount').text(resp.itemCount);
						}
					});
				}
			});
		}
	}
})(jQuery);


/** form tabindex **/
$.fn.form = function() {
	if(!this.is('form')) return;
	var $fields = this.data('fields');
	if(!$fields) {
		$fields = this.find('[tabindex]');
		this.data('fields', $fields);
		if($fields.length <= 0) return;
		$fields.sort(function(field1, field2){
			var $field1 = $(field1);
			var $field2 = $(field2);
			var tabindex1 = Number($field1.attr('tabindex')||0);
			var tabindex2 = Number($field2.attr('tabindex')||0);
			return tabindex1 - tabindex2;
		});
		var next = function($field) {
			var index = $field.data('index');
			var next = index + 1;
			if(next >= $fields.length) {
				next = 0;
			}
			var $field = $($fields[next]);
			$field.focus();
		}
		var prev = function($field) {
			var index = $field.data('index');
			var next = index - 1;
			if(next < 0) {
				next = $fields.length - 1;
			}
			var $field = $($fields[next]);
			$field.focus();
		}
		var keydown = function(e){
			var $field = $(this), w = e.which;
			if(w == 13 && $field.is(':submit')) {
				return;
			}
			switch (w) {
			case 9:
			case 13:
			//case 39:
			case 40:
				e.preventDefault();
				next($field);
				return false;
			//case 37:
			case 38:
				e.preventDefault();
				prev($field);
				return false;
			}
		};
		$.each($fields, function(index, field){
			var $field = $(field); 
			$field.data('index', index);
			$field.keydown(keydown);
		});
	}
};
	

$(document).ready(function() {
	
	var $body = $('body');
	var $win = $(window);

	/**** alert ****/
	setTimeout(function() {
		var $alert = $('.alert.alert-auto-hide');
		$alert.fadeOut(1000, function() {
			$alert.remove();
		})
	}, 3000);
	
	/**** swipe menu ****/
	
	$(document).click(function(e) {
		var $elem = $(e.target); 
		if($elem.closest('.swipe').length || $elem.closest('.navbar-wap').length) {
			return;
		}
		if ($body.hasClass('ind')) {
			$body.removeClass('ind');
			return false
		}
	})

	var swipe_control = $('.swipe-control');
	swipe_control.click(function(e) {
		$body.toggleClass('ind');
		e.stopPropagation();
	})
	
	$('.navbar-wap .lang-box').on('show.bs.dropdown', function(){
		swipe_control.addClass('hidden-xs');
	}).on('hidden.bs.dropdown', function(){
		swipe_control.removeClass('hidden-xs');
	});

	$('.swipe').height($win.height() - 50);

	$win.resize(function() {
		$('.swipe').height($win.height() - 50);
	});

	/**** quickview ****/
	$('.quickview').fancybox({
		maxWidth : 0.9 * $win.width(),
		maxHeight : 0.9 * $win.height(),
		fitToView : false,
		width : '65%',
		height : '90%',
		autoSize : false,
		closeClick : false,
		openEffect : 'elastic',
		closeEffect : 'elastic'
	});

	/**** rating ****/
	$("[data-rating]").each(function() {
		var $this = $(this);
		$this.jRate({
			width : 24,
			height : 24,
			readOnly: $this.data('readonly'),
			backgroundColor : '#f1f1f1',
			rating : $this.data('rating') || 0,
			onSet : function(rating) {
				console.log('Your rating: ' + rating);
			}
		});
	});

	/**** sub cart ****/
	$('#sub_cart_btn').click(function(e) {
		var $this = $(this), count = Number($this.find('.cartItemCount').text());
		var $sub_cart = $('#sub_cart');
		if(count > 0) {
			$sub_cart.html('<img src="' + $(this).data('loading-url')+ '"/>');
			$sub_cart.load($.appContext.paths.base + 'cart/getsub');
			$sub_cart.css('display', '');
		} else {
			$sub_cart.hide();
		}
	});
	
	$.doAjax = function(url, method, data) {
		if(url) {
			$.ajax({
				url: url,
				dataType: 'json',
				method: method,
				data: data
			}).done(function(resp) {
				if(resp.status && resp.message) {
					$.notify(resp.status, resp.message, function(){
						if(resp.redirect) window.location = resp.redirect;
					});
				} else if(resp.redirect) window.location = resp.redirect;
				var map = resp.map;
				if(map) {
					$.each(map, function(k,v){
						$(k).html(v);
					})
				}
			});
		}
	};
	
	/**** data-action ****/
	$(document).on('click', '[data-action]', function(e){
		e.preventDefault();
		e.stopPropagation();
		var $this = $(this), 
			action = $this.data('action'), 
			$form = $this.closest('form'),
			method = $form.attr('method')||'GET',
			url = $this.attr('href')||$this.data('url')||$form.attr('action'),
			data = $form.serialize(),
			auth = $this.data('auth');

		if(auth && !$.appContext.customer.loggedIn) {
			$.notify('warning', 'You must logged in to perform this action');
			return;
		}
		
		$.doAjax(url, method, data);
	})
	
	if($.appContext.customer.loggedIn) {
		$.doAjax($.appContext.paths.base + 'account/wishlist/updateView');
	}
	
	$('form').form();
	
	var $alert = $('#alert-box'), $alert_handler = $('#alert-handler');
	
	$('[data-alert]').click(function(e){
		e.preventDefault();
		e.stopPropagation();
		
		var $handler = $(this);

		var msg = $handler.data('alert'), title = $handler.data('alert-title');
		if(!msg) {
			return false;	
		}
		
		$alert.find('.modal-body').text(msg);
		if(!title) {
			$alert.find('.modal-header').hide();
		} else {
			$alert.find('.modal-header').show();
		}
		$alert.find('.continue-btn').unbind('click').on('click', function(e){
			e.preventDefault();
			e.stopPropagation();
			if($handler.is('A') && $handler.attr('href')) {
				return window.location.href = $handler.attr('href');
			}
			$form = $handler.closest('form');
			if($form.length) {
				var name = $handler.attr('name');
				var value = $handler.val();
				if(name && value) {
					var $input = $('<input type="hidden">')
					$input.attr('name', name);
					$input.val(value);
					$form.append($input);
				}
				return $form.submit();
			}
		});
		var $modal = $alert.children('.modal-dialog.modal-sm');
		var count = 0;
		var timer = setInterval(function(){
			var a = $modal.outerHeight();
			if(a > 0 || ++count > 20) {
				clearInterval(timer);
			}
			var b = $win.height();
			var c = (b-a)/2;
			$modal.css('marginTop', c);
		}, 100);
		
		$alert_handler.trigger('click');
	});
	
	$('.product-thumb.thumbnail .image img').matchHeight();
    
    $("#paymentConform").on("change keyup paste", function(){
        var value = $(this).val();
        if(value == 'cyogel@cyogel'){
            $('#paymentMethod_cod').click();
        } else{
            $('#paymentMethod_cc').click();
        }
    });
});