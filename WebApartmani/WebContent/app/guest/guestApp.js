const ApartmentComponent={template: '<guest-apartments></guest-apartments>'}
const ReservationComponent={template: '<guest-reservations></guest-reservations>'}
const ApartmentDetailsComponent={template: '<guest-apartmentDetails></guest-apartmentDetails>'}

const router=new VueRouter({
    mode: 'hash',
    routes:[
        {path: '/apartments', component:ApartmentComponent},
        {path: '/reservations', component:ReservationComponent},
        {path: '/apartmentDetails', component:ApartmentDetailsComponent}
    ]
});

var appGuest = new Vue({
    router,
	el : "#guestApp",
	data : {
		title : "Guest naslov"
    },
	
});