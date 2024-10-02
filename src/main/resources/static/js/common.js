/**
 * 数字かどうかチェックします。
 */
function numCheck() {
	const target = document.getElementById("room_size").value
	if (isNaN(target) && !target == "") {
		alert("数値を入力してください。");
		document.getElementById("room_size").value = "";
	}
}