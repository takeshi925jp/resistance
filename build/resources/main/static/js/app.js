var stompClient = null;

function setConnected(connected) {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  if (connected) {
    $("#conversation").show();
  } else {
    $("#conversation").hide();
  }
  $("#message").html("");
}

function connect() {
  var socket = new SockJS('/websocket'); // WebSocket通信開始
  //var socket = new WebSocket('wss://localhost:8080'); // WebSocket通信開始
  console.log('--new websocket--');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    setConnected(true);
    console.log('Connected: ' + frame);

    // /receive/chatエンドポイントでメッセージを受信し、表示する
    stompClient.subscribe('/receive/chat', function (response) {
      showChat(JSON.parse(response.body));
    });

    // /receive/paintエンドポイントでメッセージを受信し、表示する
    stompClient.subscribe('/receive/paint', function (response) {

    	var obj = JSON.parse(response.body);

    	//bse64形式を画像に変換★★
    	Base64ToImage(obj.canvasData, function(img) {

    		var roomId = document.getElementById("roomId").value;

    			if (roomId == obj.roomId) {
					recvImage(img);
				}
    	});
    });

  });
}

function disconnect() {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log("Disconnected");
}


function recvImage(img) {

    const canvas = document.getElementById("myCanvas");
    const context = canvas.getContext("2d");

    context.drawImage(img, 0, 0);
}


function sendMessage() {
  // /send/messageエンドポイントにメッセージを送信する
  stompClient.send("/send/message", {}, JSON.stringify(
      {'name': $("#name").val(), 'statement': $("#statement").val()}));
  $("#statement").val('');
}

function sendMessageSecond() {
	  // /send/messageエンドポイントにメッセージを送信する
	  stompClient.send("/send_second/message", {}, JSON.stringify(
	      {'name': $("#name").val(), 'statement': $("#statement").val()}));
	  $("#statement").val('');
	}

function sendChat() {

	stompClient.send("/send/chat", {}, JSON.stringify(
    {'talker': $("#talker").val(), 'text': $("#text").val(), 'memberId': $("#memberId").val(), 'roomId': $("#roomId").val()}
    ));
	  $("#text").val('');
	}

function sendPaint(canvasData, roomId) {

	// canvasの情報を送る
	  stompClient.send("/send/paint", {}, JSON.stringify(
  {'canvasData': canvasData,'roomId': roomId}));
	}

function showMessage(message) {
  // 受信したメッセージを整形して表示
  $("#message").append(
      "<tr><td>" + message.name + ": " + message.statement + "</td></tr>");
}

function showChat(chat) {
	  // 受信したメッセージを整形して表示
	  //$("add_chat").append("<tr><td>" + chat.talker + ": " + chat.text + "</td></tr>");

	var roomId = document.getElementById("roomId").value;
	var talker =  document.getElementById("talker").value;

	if (chat.roomId == roomId) {
		// チャットログを追加表示
		if(chat.talker == talker) {
			$("#add_mychat").append('<div class="mycomment"><span>' + chat.text + '</span><br></div>');
		} else {
			$("#add_chat").append(
					'<div class="balloon6"><div class="faceicon"><span>' + chat.talker + '</span><img th:src="@{/image/icon.PNG}" width="25" height="25"></div>   <div class="chatting"><div class="says">' + chat.text + '<br></div></div></div>'
			);
		}
	}
}

$(function () {
  $("form").on('submit', function (e) {
    e.preventDefault();
  });
  $("#connect").click(function () {
    connect();
  });
  $("#disconnect").click(function () {
    disconnect();
  });
  $("#send").click(function () {
    sendMessage();
  });
  $("#send_second").click(function () {
	  sendMessageSecond();
	  });
  $("#send_chat").click(function () {
	  sendChat();
	  });
});

//=====================================================
// Base64形式の文字列 → <img>要素に変換
//   base64img: Base64形式の文字列
//   callback : 変換後のコールバック。引数は<img>要素
//=====================================================
function Base64ToImage(base64img, callback) {
    var img = new Image();
    img.onload = function () {
        callback(img);
    };
    img.src = base64img;
}

setTimeout("connect()", 3000);