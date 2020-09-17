const ApartmentComponent={template: '<guest-apartments></guest-apartments>'}
const ApartmentDetailsComponent={template: '<apartmentDetails></apartmentDetails>'}
const LoginComponent={template: '<login></login>'}
const RegisterComponent={template: '<register></register>'}

const router=new VueRouter({
    mode: 'hash',
    routes:[
        {path: '/apartments', component:ApartmentComponent},
        {path: '/apartment/:id', component:ApartmentDetailsComponent},
        {path: '/login', component:LoginComponent},
        {path: '/register', component:RegisterComponent},
        {path:'/*',component:ApartmentComponent}       
    ]
});

var app = new Vue({
	router,
	el : "#unregistered",
	mounted(){
		axios.get("/WebApartmani/rest/profile").then(
				response => this.redirect(response.data)).catch(error => 1 + 1);
	}, 
	methods:{
		redirect(user){
			if (user.role === "Administrator")
				window.location.href = "http://localhost:8081/WebApartmani/admin.html";            		
			else if (user.role === "Gost")
				window.location.href = "http://localhost:8081/WebApartmani/guest.html";
			else if (user.role === "Domaćin")
				window.location.href = "http://localhost:8081/WebApartmani/host.html";
		}
	},
    components : { ApartmentComponent, LoginComponent, 
    	ApartmentDetailsComponent, RegisterComponent },
});