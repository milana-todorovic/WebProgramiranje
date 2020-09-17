const ApartmentComponent = { template: '<admin-apartments></admin-apartments>' }
const ApartmentDetailsComponent = { template: '<admin-apartmentDetails></admin-apartmentDetails>' }
const UsersComponent = { template: '<admin-users></admin-users>' }
const ReservationComponent = { template: '<admin-reservations></admin-reservations>' }
const HostComponent = { template: '<create-host></create-host>' }
const AmenityComponent = { template: '<admin-amenities></admin-amenities>' }
const HolidaysComponent = { template: '<admin-holidays></admin-holidays>' }


const router = new VueRouter({
    mode: 'hash',
    routes: [
        { path: '/apartments', component: ApartmentComponent },
        { path: '/apartmentDetails', component: ApartmentDetailsComponent },
        { path: '/users', component: UsersComponent },
        { path: '/reservations', component: ReservationComponent },
        { path: '/host', component: HostComponent },
        { path: '/amenities', component: AmenityComponent },
        { path: '/holidays', component: HolidaysComponent },
        { path: '/*', component: ApartmentComponent }
    ]
});

var appAdmin = new Vue({
    router,
    el: "#adminApp",
    data: {
        title: "Admin naslov"
    },
	mounted(){
		axios.get("/WebApartmani/rest/profile").then(
				response => this.redirect(response.data)).catch(
						error => window.location.href = "http://localhost:8081/WebApartmani/index.html#/login");
	},
	methods:{
		redirect(user){
			if (!(user.role === "Administrator")){
				window.location.href = "http://localhost:8081/WebApartmani/";
			}
		}
	},
    components: { ApartmentComponent, ApartmentDetailsComponent, UsersComponent, ReservationComponent, 
        HostComponent, AmenityComponent, HolidaysComponent }
});