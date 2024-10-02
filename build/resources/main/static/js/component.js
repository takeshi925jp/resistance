/**
 * コンポーネント群を定義
 */



/*ヘッダタイトル*/
Vue.component('hedder-title', {
	template: `
    <div class="spin-deco">
		<slot></slot>
    </div>
    `});


/*説明文のコーディング*/
Vue.component('description', {
    template: `
    <div>
        <span class="icon">
            <slot name="icon"></slot>
        </span>
        <span class="room_name">
            <slot name="room_name"></slot>
        </span>

        <div class="description_textblock">
            <span class="description_text">
                <slot name="description_text"></slot>
            </span>
        </div>
    </div>`
});

/*ヘッダタイトル*/
Vue.component('header-component', {
    template: `
    <div class="header-deco">
        <div class="spin-deco">
            <slot></slot>
        </div>
    </div>
    `});

/*フッター*/
Vue.component('footer-component', {
    template: `
    <div class="footer-deco">
            <slot></slot>
    </div>
    `});





    var app = new Vue({
    	el:"#app"
    });