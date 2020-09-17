const ApartmentComponent={template: '<host-apartments></host-apartments>'}
const ReservationComponent={template: '<host-reservations></host-reservations>'}
const ApartmentDetailsComponent={template: '<host-apartmentDetails></host-apartmentDetails>'}
const UsersComponent={template:'<host-users></host-users>'}
const AddApartmentComponent={template:'<host-addApartment></host-addApartment'}


const router=new VueRouter({
    mode: 'hash',
    routes:[
        {path: '/apartments', component:ApartmentComponent},
        {path: '/reservations', component:ReservationComponent},
        {path: '/apartmentDetails/:id', name: 'apartmentDetails', component:ApartmentDetailsComponent},
        {path:'/users',component:UsersComponent},
        {path:'/addApartment',component:AddApartmentComponent},
        { path: '/*', component:ApartmentComponent }
    ]
});

var appHost = new Vue({
    router,
	el : "#hostApp",
	data : {
		title : "Host naslov"
    },
	mounted(){
    	axios.get("/WebApartmani/rest/profile").then(
				response => this.redirect(response.data)).catch(
						error => window.location.href = "http://localhost:8081/WebApartmani/index.html#/login");
	},
	methods:{
		redirect(user){
			if (!(user.role === "Doma\u0107in")){
				window.location.href = "http://localhost:8081/WebApartmani/";
			}
		}
	},
    components : { ApartmentComponent, ReservationComponent, 
    	ApartmentDetailsComponent, UsersComponent, AddApartmentComponent },
});