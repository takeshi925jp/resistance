/**
 * チャット送信時の非同期処理の実装
 *
 */

$(function() {

	$('#send_chat').click( function() {

		// デフォルトのイベントを停止
		e.preventDefault();

		// POSTするurlを取得
		var url = $(this).attr('action')

		// フォームデータを取得
		var formData = new FormData(this);

		alert(url);
		alert(formData);

		$.ajax({
			type : "POST",
			url : url,
			data : formData,
			dataType:'json',
			processData:false,
			contentType:false
		})
	})
})
