var app = new Vue({
	el : "#test",
	data : {
		files : [],
		src : "https://www.abc.net.au/cm/rimage/9417180-16x9-xlarge.jpg?v=2"
	},
	methods : {
		click : function() {
			if (this.files.length > 0) {
				var reader = new FileReader();
				reader.readAsDataURL(this.files[0]);
				reader.onload = 
					() => { axios({
		                method: 'POST',
		                url: "http://localhost:8081/WebApartmani/rest/image/base64", 
		                data: JSON.stringify(reader.result), 
		                headers:{'Content-Type': 'application/json; charset=utf-8'}
		            })    
		            .then(() => {            
		                axios.get("http://localhost:8081/WebApartmani/rest/image/base64").then(
		                		(response) => this.src = response.data)
		            }) }
			}
		},
		change : function(event) {
			this.files = event.target.files;
		}
	}
});