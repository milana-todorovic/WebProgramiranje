var app = new Vue({
	el : "#test",
	data : {
		title : ""
	},
	mounted(){
		axios.get("rest/test/testMetoda").then(response => (this.title = response.data))
	}
});