const ApartmentComponent={template: '<guest-apartments></guest-apartments>'}
const ReservationComponent={template: '<guest-reservations></guest-reservations>'}
const ApartmentDetailsComponent={template: '<guest-apartmentDetails></guest-apartmentDetails>'}
const ProfileComponent={template: '<guest-profile></guest-profile>'}


const router=new VueRouter({
    mode: 'hash',
    routes:[
        {path: '/apartments', component:ApartmentComponent},
        {path: '/reservations', component:ReservationComponent},
        {path: '/apartmentDetails/:id', name: 'apartmentDetails', component:ApartmentDetailsComponent},
        {path: '/profile', component:ProfileComponent},
        { path: '/*', component:ApartmentComponent }
    ]
});

var appGuest = new Vue({
    router,
	el : "#guestApp",
	data : {
		title : "Guest naslov"
    },
	mounted(){
		axios.get("/WebApartmani/rest/profile").then(
				response => this.redirect(response.data)).catch(
						error => window.location.href = "http://localhost:8081/WebApartmani/index.html#/login");
	},
	methods:{
		redirect(user){
			if (!(user.role === "Gost")){
				window.location.href = "http://localhost:8081/WebApartmani/";
			}
		}
	},
	components: {ApartmentComponent, ReservationComponent, ApartmentDetailsComponent, ProfileComponent}
});